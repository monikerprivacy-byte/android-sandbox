package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.OptionChainSummary
import com.example.data.repository.BinanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class NavigationTab(val label: String, val iconName: String) {
    OPTION_CHAIN("Option Chain", "rebase_edit"),
    ORDER_FLOW("Order Flow & rVol", "analytics"),
    MARKET_MECHANICS("Mechanics & S/R", "monitoring"),
    AI_LEARNING("AI Flow Engine", "psychology")
}

class OptionChainViewModel(
    private val repository: BinanceRepository = BinanceRepository()
) : ViewModel() {

    val chainState: StateFlow<OptionChainSummary> = repository.optionChainState

    private val _selectedTab = MutableStateFlow(NavigationTab.OPTION_CHAIN)
    val selectedTab: StateFlow<NavigationTab> = _selectedTab.asStateFlow()

    private val _showOnlyAtm5 = MutableStateFlow(true)
    val showOnlyAtm5: StateFlow<Boolean> = _showOnlyAtm5.asStateFlow()

    private val _selectedStrikeForDetail = MutableStateFlow<Double?>(null)
    val selectedStrikeForDetail: StateFlow<Double?> = _selectedStrikeForDetail.asStateFlow()

    val availableSymbols = listOf("BTCUSDT", "ETHUSDT", "SOLUSDT", "BNBUSDT")
    val availableExpiries = listOf("26JUL24", "02AUG24", "30AUG24", "27SEP24")

    fun selectTab(tab: NavigationTab) {
        _selectedTab.value = tab
    }

    fun selectSymbol(symbol: String) {
        repository.selectSymbol(symbol)
        _selectedStrikeForDetail.value = null
    }

    fun selectExpiry(expiry: String) {
        repository.selectExpiry(expiry)
    }

    fun toggleAtm5Filter() {
        _showOnlyAtm5.update { !it }
    }

    fun selectStrikeForDetail(strikePrice: Double?) {
        _selectedStrikeForDetail.value = strikePrice
    }
}
