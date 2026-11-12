package com.curated.app.data.repo

import com.curated.app.data.ai.AiException
import com.curated.app.data.ai.AiLook
import com.curated.app.data.ai.AiLookStore
import com.curated.app.data.ai.CloudflareAi
import com.curated.app.data.ai.LookImageGenerator
import com.curated.app.data.ai.LookSubject
import com.curated.app.data.ai.PieceType
import com.curated.app.data.ai.ProductTags
import com.curated.app.data.ai.StylistTags
import com.curated.app.data.mock.MockCatalog
import com.curated.app.data.model.Outfit
import com.curated.app.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * The AI Stylist. A request goes through four steps:
 *
 * 1. the text model reads the request (English, Urdu or Roman Urdu) into a [Brief];
 * 2. the app shortlists catalogue pieces that fit the brief, using the product tags;
 * 3. the text model picks one outfit from that shortlist;
 * 4. the app checks the pick: only real ids, one piece per slot, total within budget.
 *
 * The model is never trusted with facts: it cannot add products, and the budget is
 * enforced here because it does not add prices up reliably. The look is saved under
 * My Creations and its picture is made in the background.
 */
@Singleton
class StylistRepository @Inject constructor(
    private val catalog: CatalogRepository,
    private val outfits: OutfitRepository,
    private val ai: CloudflareAi,
    private val tags: StylistTags,
    private val looks: AiLookStore,
    private val images: LookImageGenerator,
    private val prefs: PrefsStore
) {
    val vibeChips: List<String> = MockCatalog.vibeChips

    /** Who the look image is drawn on; a mannequin until the user chooses otherwise. */
    val lookSubject: Flow<LookSubject> = prefs.aiLookSubject.map { key ->
        LookSubject.entries.firstOrNull { it.key == key } ?: LookSubject.Mannequin
    }

    suspend fun setLookSubject(subject: LookSubject) = prefs.setAiLookSubject(subject.key)

    /** Builds a look for the request. Throws [AiException] with a message for the user. */
    suspend fun generate(prompt: String, vibes: Set<String>, subject: LookSubject): Outfit =
        withContext(Dispatchers.IO) {
            val request = (listOf(prompt.trim()) + vibes).filter { it.isNotBlank() }.joinToString(". ")
            if (request.isBlank()) {
                throw AiException("Tell the stylist what you are in the mood for first.")
            }

            val brief = readBrief(request)
            val pool = eligiblePieces(brief)
            val shortlist = shortlist(pool)
            if (shortlist.none { it.tags.type in MAIN_TYPES }) {
                throw AiException(NOTHING_FOUND)
            }
            val pick = pickOutfit(request, brief, shortlist)
            val settled = settle(pick.ids, shortlist, pool, brief)
            val total = settled.pieces.sumOf { it.product.pricePkr }

            val look = AiLook(
                id = "ai_" + System.currentTimeMillis(),
                title = pick.title.ifBlank { "Your Curated Look" },
                tags = pick.tags.ifEmpty { fallbackTags(brief) },
                productIds = settled.pieces.map { it.product.id },
                note = when {
                    brief.budget > 0 && total > brief.budget ->
                        "The closest the catalogue gets to your budget of ${MockCatalog.formatPkr(brief.budget)}."
                    settled.changedForBudget ->
                        "Chosen to stay within your budget of ${MockCatalog.formatPkr(brief.budget)}."
                    else -> pick.reason
                },
                request = request,
                subject = subject,
                createdAt = System.currentTimeMillis()
            )
            looks.add(look)
            outfits.addCreation(look.id)
            images.start(look.id)
            looks.outfit(look.id) ?: throw AiException("The look could not be saved. Please try again.")
        }

    // ---- Step 1: read the request ------------------------------------------------

    private data class Brief(
        val occasion: String,
        val style: String,
        val season: String,
        val colours: List<String>,
        val budget: Int,
        val audience: String
    )

    private suspend fun readBrief(request: String): Brief {
        val schema = objectSchema(
            "occasion" to enumSchema("casual", "work", "party", "wedding", "festive", ANY),
            "style" to enumSchema("desi", "western", "fusion", ANY),
            "season" to enumSchema("summer", "winter", ANY),
            "colours" to JSONObject().put("type", "array").put("items", JSONObject().put("type", "string")),
            "budget_pkr" to JSONObject().put("type", "integer"),
            "audience" to enumSchema("women", "men", "kids")
        )
        val answer = ai.chatJson(BRIEF_INSTRUCTIONS, request, schema, maxTokens = 150)
        val colours = answer.optJSONArray("colours")
        return Brief(
            occasion = answer.optString("occasion", ANY).lowercase(),
            style = answer.optString("style", ANY).lowercase(),
            season = answer.optString("season", ANY).lowercase(),
            colours = buildList {
                if (colours != null) {
                    for (i in 0 until colours.length()) {
                        colours.optString(i).trim().lowercase().takeIf { it.isNotEmpty() }?.let(::add)
                    }
                }
            },
            budget = answer.optInt("budget_pkr", 0).coerceAtLeast(0),
            audience = answer.optString("audience", "women").lowercase()
                .takeIf { it in setOf("women", "men", "kids") } ?: "women"
        )
    }

    // ---- Step 2: shortlist catalogue pieces ----------------------------------------

    private class Candidate(val product: Product, val tags: ProductTags, val score: Float)

    /**
     * Every piece that could be in the look, scored against the brief. With a budget, a
     * piece that would eat too much of it for its kind is left out, so the model is not
     * shown a dress that leaves nothing for shoes.
     */
    private fun eligiblePieces(brief: Brief): List<Candidate> =
        catalog.products().mapNotNull { product ->
            val productTags = tags.of(product.id) ?: return@mapNotNull null
            if (productTags.audience != brief.audience) return@mapNotNull null
            val share = BUDGET_SHARES[productTags.type] ?: return@mapNotNull null
            if (brief.budget > 0 && product.pricePkr > brief.budget * share) return@mapNotNull null
            Candidate(product, productTags, score(productTags, brief))
        }

    /** The best few of each kind: what the model chooses from. */
    private fun shortlist(pool: List<Candidate>): List<Candidate> =
        SHORTLIST_SIZES.flatMap { (type, size) ->
            pool.filter { it.tags.type == type }.sortedByDescending { it.score }.take(size)
        }

    private fun score(piece: ProductTags, brief: Brief): Float {
        var points = Random.nextFloat() // breaks ties differently each time, so looks vary
        if (brief.occasion != ANY) points += if (brief.occasion in piece.occasions) 3f else -2f
        if (brief.style != ANY) {
            points += when {
                piece.style == brief.style -> 2f
                piece.style == "fusion" || brief.style == "fusion" -> 1f
                else -> -1f
            }
        }
        if (brief.season != ANY) points += if (piece.season == brief.season || piece.season == "all") 1f else -2f
        if (brief.colours.any { it in piece.colour || piece.description.contains(it, ignoreCase = true) }) points += 3f
        // A garment the image model cannot see properly makes a wrong look picture.
        if (piece.type in MAIN_TYPES) points += if (piece.goodReference) 2f else -2f
        return points
    }

    // ---- Step 3: let the model pick ---------------------------------------------------

    private class Pick(val title: String, val tags: List<String>, val ids: List<String>, val reason: String)

    private suspend fun pickOutfit(request: String, brief: Brief, shortlist: List<Candidate>): Pick {
        val listing = shortlist.joinToString("\n") { candidate ->
            with(candidate) {
                "${product.id} | ${tags.type.key} | ${tags.colour} | ${tags.description} | ${product.pricePkr}"
            }
        }
        val budgetRule = if (brief.budget > 0) {
            " The prices of everything you pick must add up to no more than ${brief.budget} rupees."
        } else {
            ""
        }
        val schema = objectSchema(
            "title" to JSONObject().put("type", "string"),
            "tags" to JSONObject().put("type", "array").put("items", JSONObject().put("type", "string")),
            "product_ids" to JSONObject().put("type", "array").put("items", JSONObject().put("type", "string")),
            "reason" to JSONObject().put("type", "string")
        )
        val answer = ai.chatJson(
            system = PICK_INSTRUCTIONS + budgetRule,
            user = "Request: $request\n\nCandidates (id | type | colour | description | price in rupees):\n$listing",
            schema = schema,
            maxTokens = 350
        )
        return Pick(
            title = answer.optString("title").trim().take(MAX_TITLE_LENGTH),
            tags = answer.optJSONArray("tags").strings()
                .map { it.trim().replaceFirstChar(Char::uppercase) }
                .filter { it.isNotEmpty() }
                .take(3),
            ids = answer.optJSONArray("product_ids").strings(),
            reason = answer.optString("reason").trim().take(MAX_NOTE_LENGTH)
        )
    }

    // ---- Step 4: check the pick ---------------------------------------------------------

    private class Settled(val pieces: List<Candidate>, val changedForBudget: Boolean)

    private fun settle(
        ids: List<String>,
        shortlist: List<Candidate>,
        pool: List<Candidate>,
        brief: Brief
    ): Settled {
        val byId = shortlist.associateBy { it.product.id }
        fun best(type: PieceType) = shortlist.filter { it.tags.type == type }.maxByOrNull { it.score }

        // One piece per slot; ids the model made up simply drop out here.
        val slots = LinkedHashMap<PieceType, Candidate>()
        ids.mapNotNull { byId[it] }.forEach { slots.putIfAbsent(it.tags.type, it) }

        // The body has to be covered: a complete outfit, or a top with a bottom.
        if (PieceType.Outfit in slots) {
            slots.remove(PieceType.Top)
            slots.remove(PieceType.Bottom)
        } else {
            val top = slots[PieceType.Top] ?: best(PieceType.Top)
            val bottom = slots[PieceType.Bottom] ?: best(PieceType.Bottom)
            if (top != null && bottom != null) {
                slots[PieceType.Top] = top
                slots[PieceType.Bottom] = bottom
            } else {
                slots.remove(PieceType.Top)
                slots.remove(PieceType.Bottom)
                slots[PieceType.Outfit] = best(PieceType.Outfit) ?: throw AiException(NOTHING_FOUND)
            }
        }
        if (PieceType.Shoes !in slots) best(PieceType.Shoes)?.let { slots[PieceType.Shoes] = it }

        var changed = false
        if (brief.budget > 0) {
            fun total() = slots.values.sumOf { it.product.pricePkr }
            // Extras go first, least important first.
            for (extra in listOf(PieceType.Jewellery, PieceType.Scarf, PieceType.Bag, PieceType.Outerwear)) {
                if (total() > brief.budget && slots.remove(extra) != null) changed = true
            }
            // Still over: the clothes and shoes are chosen again together, so a cheaper
            // dress can make room for shoes that suit it instead of any pair that is cheap.
            if (total() > brief.budget) {
                bestWithin(brief.budget, slots, pool).forEach { slots[it.tags.type] = it }
                changed = true
            }
        }

        return Settled(DISPLAY_ORDER.mapNotNull { slots[it] }, changed)
    }

    /**
     * One piece for each slot in [current], taken from [pool], with the highest total
     * score that costs no more than [budget]; the model's own choices get a head start.
     * When nothing fits, the cheapest piece of each kind is the closest there is.
     */
    private fun bestWithin(
        budget: Int,
        current: Map<PieceType, Candidate>,
        pool: List<Candidate>
    ): List<Candidate> {
        val options = current.map { (type, chosen) ->
            pool.filter { it.tags.type == type }
                .sortedByDescending { it.score }
                .take(BUDGET_SEARCH_WIDTH)
                .map { it to it.score + if (it.product.id == chosen.product.id) KEEP_BONUS else 0f }
        }
        var best: List<Candidate>? = null
        var bestScore = Float.NEGATIVE_INFINITY
        fun search(slot: Int, picked: List<Candidate>, price: Int, score: Float) {
            if (price > budget) return
            if (slot == options.size) {
                if (score > bestScore) {
                    best = picked
                    bestScore = score
                }
                return
            }
            options[slot].forEach { (candidate, points) ->
                search(slot + 1, picked + candidate, price + candidate.product.pricePkr, score + points)
            }
        }
        search(0, emptyList(), 0, 0f)
        return best ?: current.keys.mapNotNull { type ->
            pool.filter { it.tags.type == type }.minByOrNull { it.product.pricePkr }
        }
    }

    private fun fallbackTags(brief: Brief): List<String> =
        listOf(brief.occasion, brief.style, brief.season)
            .filter { it != ANY }
            .map { it.replaceFirstChar(Char::uppercase) }
            .ifEmpty { listOf("Curated") }

    private fun JSONArray?.strings(): List<String> =
        if (this == null) emptyList() else (0 until length()).map { optString(it) }

    private fun objectSchema(vararg properties: Pair<String, JSONObject>): JSONObject {
        val fields = JSONObject()
        properties.forEach { (name, schema) -> fields.put(name, schema) }
        return JSONObject()
            .put("type", "object")
            .put("properties", fields)
            .put("required", JSONArray(properties.map { it.first }))
    }

    private fun enumSchema(vararg values: String): JSONObject =
        JSONObject().put("type", "string").put("enum", JSONArray(values.toList()))

    private companion object {
        const val ANY = "any"
        const val MAX_TITLE_LENGTH = 40
        const val MAX_NOTE_LENGTH = 260
        const val NOTHING_FOUND =
            "The stylist found nothing in the catalogue for that. Try describing it differently."

        /** How many pieces of each kind the budget search looks at. */
        const val BUDGET_SEARCH_WIDTH = 30

        /** Score head start for a piece the model picked itself, when re-choosing for the budget. */
        const val KEEP_BONUS = 1.5f

        val MAIN_TYPES = setOf(PieceType.Outfit, PieceType.Top, PieceType.Bottom, PieceType.Outerwear)

        /**
         * The largest part of a budget one piece of each kind may take. Kinds that are
         * not listed (PieceType.Other) never go into a look.
         */
        val BUDGET_SHARES = mapOf(
            PieceType.Outfit to 0.75f,
            PieceType.Top to 0.45f,
            PieceType.Bottom to 0.4f,
            PieceType.Outerwear to 0.4f,
            PieceType.Shoes to 0.3f,
            PieceType.Bag to 0.25f,
            PieceType.Jewellery to 0.25f,
            PieceType.Scarf to 0.2f
        )

        /** How many pieces of each kind the model gets to choose from. */
        val SHORTLIST_SIZES = listOf(
            PieceType.Outfit to 16,
            PieceType.Top to 6,
            PieceType.Bottom to 6,
            PieceType.Outerwear to 4,
            PieceType.Shoes to 10,
            PieceType.Bag to 6,
            PieceType.Jewellery to 5,
            PieceType.Scarf to 4
        )

        val DISPLAY_ORDER = listOf(
            PieceType.Outfit,
            PieceType.Top,
            PieceType.Bottom,
            PieceType.Outerwear,
            PieceType.Scarf,
            PieceType.Shoes,
            PieceType.Bag,
            PieceType.Jewellery
        )

        const val BRIEF_INSTRUCTIONS =
            "You read a shopping request for a Pakistani fashion app. The customer may write in English, " +
                "Urdu or Roman Urdu. Reply as JSON with what they want. " +
                "occasion: mehndi, eid and similar celebrations are festive; shaadi, barat and walima are wedding; " +
                "dinner, date and birthday are party; office, university and interview are work; " +
                "outings, shopping and everyday wear are casual. " +
                "style: desi for eastern wear such as shalwar kameez, kurta or saree; western for jeans, dresses, " +
                "coats and the like; fusion for a mix. " +
                "season: winter for sardi, winter or cold weather; summer for garmi, summer or lawn. " +
                "colours: the colours they ask for, as English colour names in lower case; an empty list if none. " +
                "budget_pkr: their total budget in rupees as a plain number (\"25 hazar\" is 25000, " +
                "\"1 lakh\" is 100000); 0 if they give none. " +
                "audience: who will wear it; women unless the request clearly says men or kids. " +
                "Use \"any\" for occasion, style or season when the request does not say."

        const val PICK_INSTRUCTIONS =
            "You are a fashion stylist for a Pakistani shopping app. The customer may write in English, Urdu " +
                "or Roman Urdu. Build ONE outfit using only product ids from the candidate list. " +
                "Pick either one item of type outfit, or one top together with one bottom. Add exactly one " +
                "pair of shoes. You may add one bag, one jewellery item, one scarf and one outerwear piece " +
                "when they suit the look. The colours and styles of all pieces must go together and fit " +
                "the request. Never invent ids. Reply as JSON: title is a name for the look in English, at " +
                "most four words; tags are three single English words that describe it; product_ids are " +
                "the ids you picked; reason is one or two short English sentences for the customer on why " +
                "the pieces work together, without ids or prices."
    }
}
