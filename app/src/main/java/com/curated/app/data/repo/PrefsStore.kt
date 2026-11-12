package com.curated.app.data.repo

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "curated_prefs")

@Singleton
class PrefsStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val wishlistProducts = stringSetPreferencesKey("wishlist_products")
    private val wishlistOutfits = stringSetPreferencesKey("wishlist_outfits")
    private val aiLookSubjectKey = stringPreferencesKey("ai_look_subject")

    val wishlistProductIds: Flow<Set<String>> = context.dataStore.data.map {
        it[wishlistProducts] ?: setOf("p_shirt", "p_dupatta")
    }

    val wishlistOutfitIds: Flow<Set<String>> = context.dataStore.data.map {
        it[wishlistOutfits] ?: setOf("o_graceful", "o_dinner")
    }

    suspend fun setWishlistProducts(ids: Set<String>) {
        context.dataStore.edit { it[wishlistProducts] = ids }
    }

    suspend fun setWishlistOutfits(ids: Set<String>) {
        context.dataStore.edit { it[wishlistOutfits] = ids }
    }

    /** Key of the subject the AI Stylist draws looks on; null until the user picks one. */
    val aiLookSubject: Flow<String?> = context.dataStore.data.map { it[aiLookSubjectKey] }

    suspend fun setAiLookSubject(key: String) {
        context.dataStore.edit { it[aiLookSubjectKey] = key }
    }
}
