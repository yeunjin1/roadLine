package com.lgcns.crossdev.onboarding1.presentation.ui.money

import androidx.lifecycle.viewModelScope
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.domain.usecase.DeleteMoneyUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.GetAllCurrenciesUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.GetCurrenciesByCodeUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.GetMoneysByDateUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.GetTravelUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.InsertMoneyUseCase
import com.lgcns.crossdev.onboarding1.presentation.base.BaseViewModel
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.AddPlanFragment
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.PlanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MoneyViewModel @Inject constructor(
    private val getTravelUseCase: GetTravelUseCase,
    private val getMoneysUseCase: GetMoneysByDateUseCase,
    private val deleteMoneysUseCase: DeleteMoneyUseCase,
    private val getCurrenciesByCodeUseCase: GetCurrenciesByCodeUseCase,
): BaseViewModel() {
    private var _travelId = -1L
    val travelId: Long
        get() = _travelId

    private lateinit var _travel: StateFlow<Travel?>
    val travel: StateFlow<Travel?>
        get() = _travel

    private lateinit var _moneyList: StateFlow<List<Money>>
    val moneyList: StateFlow<List<Money>>
        get() = _moneyList

    private var _selectedMoneyList = MutableStateFlow<List<Pair<LocalDate, List<Money>>>>(
        mutableListOf()
    )
    val selectedMoneyList: StateFlow<List<Pair<LocalDate, List<Money>>>>
        get() = _selectedMoneyList.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?>
        get() = _selectedDate.asStateFlow()

    private lateinit var _currencyList: StateFlow<List<Currency>>
    val currencyList: StateFlow<List<Currency>>
        get() = _currencyList

    fun setTravelId(id: Long) {
        _travelId = id
    }

    fun getTravelByTravelId() {
        _travel = getTravelUseCase.invoke(_travelId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
        viewModelScope.launch {
            _travel.collectLatest {
                it?.let {
                    getMoneys(it)
                    getCurrencies(it)
                }
            }
        }
    }

    private fun getMoneys(travel: Travel) {
        _moneyList = getMoneysUseCase.invoke(travel.id!!, null)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    private fun getCurrencies(travel: Travel) {
        Timber.tag(TAG).d("getCurrencies(${travel.currencyCodes})")
        _currencyList = getCurrenciesByCodeUseCase.invoke(travel.currencyCodes)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun setSelectedDate(date: LocalDate?) {
        viewModelScope.launch {
            _selectedDate.emit(date)
        }
    }

    fun deleteMoney(money: Money) {
        viewModelScope.launch {
            deleteMoneysUseCase.invoke(money)
        }
    }

    fun updateSelectedMoneyList(moneyList: MutableList<Pair<LocalDate, List<Money>>>) {
        viewModelScope.launch {
            _selectedMoneyList.emit(moneyList)
        }
    }


    companion object {
        const val TAG = "MoneyViewModel"
    }

}