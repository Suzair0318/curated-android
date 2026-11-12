package com.curated.app.feature.brand

import androidx.lifecycle.ViewModel
import com.curated.app.data.model.Brand
import com.curated.app.data.repo.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class BrandsUiState(
    val brands: List<Brand> = emptyList()
)

@HiltViewModel
class BrandsViewModel @Inject constructor(
    catalog: CatalogRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(BrandsUiState(brands = catalog.allBrands()))
    val ui: StateFlow<BrandsUiState> = _ui.asStateFlow()
}
