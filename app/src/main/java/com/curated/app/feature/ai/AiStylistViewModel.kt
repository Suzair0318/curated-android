package com.curated.app.feature.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curated.app.data.ai.AiException
import com.curated.app.data.ai.LookSubject
import com.curated.app.data.repo.StylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AiUiState(
    val prompt: String = "",
    val selectedVibes: Set<String> = emptySet(),
    val vibeChips: List<String> = emptyList(),
    /** Who the look image will be drawn on. */
    val subject: LookSubject = LookSubject.Mannequin,
    val loading: Boolean = false,
    /** Why the last request produced no look, in words for the user. */
    val error: String? = null,
    val generatedOutfitId: String? = null
)

@HiltViewModel
class AiStylistViewModel @Inject constructor(
    private val stylist: StylistRepository,
    private val promptHolder: AiPromptHolder
) : ViewModel() {

    private val _ui = MutableStateFlow(
        AiUiState(
            prompt = promptHolder.pendingPrompt,
            vibeChips = stylist.vibeChips
        )
    )
    val ui: StateFlow<AiUiState> = _ui.asStateFlow()

    init {
        if (promptHolder.pendingPrompt.isNotBlank()) {
            _ui.value = _ui.value.copy(prompt = promptHolder.pendingPrompt)
            promptHolder.pendingPrompt = ""
        }
        viewModelScope.launch {
            stylist.lookSubject.collect { subject -> _ui.update { it.copy(subject = subject) } }
        }
    }

    fun onPromptChange(value: String) {
        _ui.update { it.copy(prompt = value, error = null) }
    }

    fun toggleVibe(vibe: String) {
        _ui.update {
            val current = it.selectedVibes
            it.copy(selectedVibes = if (vibe in current) current - vibe else current + vibe, error = null)
        }
    }

    fun selectSubject(subject: LookSubject) {
        _ui.update { it.copy(subject = subject) }
        viewModelScope.launch { stylist.setLookSubject(subject) }
    }

    fun generate(onDone: (String) -> Unit) {
        if (_ui.value.loading) return
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null, generatedOutfitId = null) }
            val request = _ui.value
            try {
                val outfit = stylist.generate(request.prompt, request.selectedVibes, request.subject)
                _ui.update { it.copy(loading = false, generatedOutfitId = outfit.id) }
                onDone(outfit.id)
            } catch (error: CancellationException) {
                throw error
            } catch (error: AiException) {
                _ui.update { it.copy(loading = false, error = error.userMessage) }
            } catch (error: Exception) {
                _ui.update { it.copy(loading = false, error = "Something went wrong. Please try again.") }
            }
        }
    }
}
