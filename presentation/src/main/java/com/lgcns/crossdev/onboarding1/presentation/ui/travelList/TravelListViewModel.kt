package com.lgcns.crossdev.onboarding1.presentation.ui.travelList

import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.domain.usecase.GetAllTravelsUseCase
import com.lgcns.crossdev.onboarding1.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.lgcns.crossdev.onboarding1.domain.common.exception.RoadLineException
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.usecase.DeleteTravelUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.GetAllCurrenciesUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.GetLatestCurrencyLoadDateUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.GetTravelUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.InsertCurrencyUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.InsertTravelUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.LoadRemoteCurrencyUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.UpdateTravelUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class TravelListViewModel @Inject constructor(
    private val getAllTravelsUseCase: GetAllTravelsUseCase,
    private val getTravelUseCase: GetTravelUseCase,
    private val insertTravelUseCase: InsertTravelUseCase,
    private val updateTravelUseCase: UpdateTravelUseCase,
    private val deleteTravelUseCase: DeleteTravelUseCase,
    private val loadRemoteCurrencyUseCase: LoadRemoteCurrencyUseCase,
    private val insertCurrencyUseCase: InsertCurrencyUseCase,
    private val getLatestCurrencyLoadDateUseCase: GetLatestCurrencyLoadDateUseCase,
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
) : BaseViewModel() {

    enum class LoadStatus {LOADING, ERROR, DONE, DEFAULT}

    private val _travelList: StateFlow<List<Travel>> = getAllTravelsUseCase.invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val travelList: StateFlow<List<Travel>>
        get() = _travelList

    private val _allCurrencyList: StateFlow<List<Currency>> = getAllCurrenciesUseCase.invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val allCurrencyList: StateFlow<List<Currency>>
        get() = _allCurrencyList

    private val _selectedCurrencyCodes = MutableStateFlow<List<String>>(listOf("+"))
    val selectedCurrencyCodes: StateFlow<List<String>>
        get() = _selectedCurrencyCodes.asStateFlow()

    private val _status = MutableStateFlow<LoadStatus>(LoadStatus.DEFAULT)
    val status: StateFlow<LoadStatus>
        get() = _status.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String>
        get() = _errorMessage.asSharedFlow()


    // 화폐 목록 초기화
    fun setSelectedCurrencyCodes(currencyCodeList: List<String>) {
        viewModelScope.launch {
            val result = currencyCodeList.toMutableList()
            result.add("+")
            _selectedCurrencyCodes.emit(result)
        }
    }

    // 화폐 목록 추가
    fun addSelectedCurrencyCodes(currencyCode: String) {
        viewModelScope.launch {
            val result = _selectedCurrencyCodes.value.toMutableList()
            result.remove("+")
            if(!result.contains(currencyCode)) {
                result.add(currencyCode)
            }
            result.add("+")
            _selectedCurrencyCodes.emit(result)
        }
    }

    // 화폐 목록 삭제
    fun removeSelectedCurrencyCodes(currencyCode: String) {
        viewModelScope.launch {
            val result = _selectedCurrencyCodes.value.toMutableList()
            result.remove("+")
            result.remove(currencyCode)
            result.add("+")
            _selectedCurrencyCodes.emit(result)
        }
    }

    fun getLatestCurrencyLoadDate() = getLatestCurrencyLoadDateUseCase.invoke()


    fun insertTravel(travel: Travel){
        viewModelScope.launch(Dispatchers.IO) {
            insertTravelUseCase.invoke(travel)
        }
    }

    fun updateTravel(travel: Travel){
        viewModelScope.launch(Dispatchers.IO) {
            updateTravelUseCase.invoke(travel)
        }
    }

    fun deleteTravel(travel: Travel){
        viewModelScope.launch(Dispatchers.IO){
            deleteTravelUseCase.invoke(travel)
        }
    }

    fun loadRemoteCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            _status.update { LoadStatus.LOADING }
            val result = loadRemoteCurrencyUseCase.invoke()
            result.onSuccess {
                makeCurrencyData(it)
                insertCurrencyUseCase.invoke(it)
                _status.update { LoadStatus.DONE }
            }.onFailure { throwable ->
                throwable as RoadLineException
                _status.update { LoadStatus.ERROR }
                _errorMessage.emit ( throwable.message )
            }
        }
    }

    private fun makeCurrencyData(list: List<Currency>) {
        list.forEach {
            if(it.code.contains("(")) {
                val results = it.code.split("(", ")")
                it.code = results[0]
                it.rate = it.rate?.div(results[1].toInt())
            }
            if(it.code == "KRW") {
                it.rate = 1.0
            }
        }
    }
}