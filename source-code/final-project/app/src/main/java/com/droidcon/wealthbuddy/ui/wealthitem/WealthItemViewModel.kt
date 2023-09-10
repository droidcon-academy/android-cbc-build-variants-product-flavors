package com.droidcon.wealthbuddy.ui.wealthitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.wealthbuddy.BuildConfig
import com.droidcon.wealthbuddy.domain.CalculateAndSaveUseCase
import com.droidcon.wealthbuddy.domain.GetWealthDataItemsUseCase
import com.droidcon.wealthbuddy.domain.WealthDomainItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WealthItemViewModel @Inject constructor(
    private val calculateAndSaveUseCase: CalculateAndSaveUseCase,
    getWealthDataItemsUseCase: GetWealthDataItemsUseCase
) : ViewModel() {

    val uiState: StateFlow<WealthItemUiState> =
        getWealthDataItemsUseCase()
            .map<List<WealthDomainItem>, WealthItemUiState> {
                WealthItemUiState.Success(
                    it.map { domainItem ->
                        WealthUIItem.from(domainItem)
                    }
                )
            }
            .catch { emit(WealthItemUiState.Error(it)) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                WealthItemUiState.Loading
            )

    fun addWealthItem(amount: Double, roi: Double, tenureInYears: Int) {
        viewModelScope.launch {
            calculateAndSaveUseCase(amount, roi, tenureInYears, BuildConfig.isInvestment)
        }
    }
}

sealed interface WealthItemUiState {
    object Loading : WealthItemUiState
    data class Error(val throwable: Throwable) : WealthItemUiState
    data class Success(val data: List<WealthUIItem>) : WealthItemUiState
}
