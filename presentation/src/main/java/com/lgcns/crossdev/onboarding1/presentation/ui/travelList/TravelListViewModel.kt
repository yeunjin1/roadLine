package com.lgcns.crossdev.onboarding1.presentation.ui.travelList

import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.domain.usecase.GetAllTravelsUseCase
import com.lgcns.crossdev.onboarding1.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.usecase.DeleteTravelUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.GetTravelUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.InsertTravelUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.LoadRemoteCurrencyUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.UpdateTravelUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TravelListViewModel @Inject constructor(
    private val getAllTravelsUseCase: GetAllTravelsUseCase,
    private val getTravelUseCase: GetTravelUseCase,
    private val insertTravelUseCase: InsertTravelUseCase,
    private val updateTravelUseCase: UpdateTravelUseCase,
    private val deleteTravelUseCase: DeleteTravelUseCase,
    private val loadRemoteCurrencyUseCase: LoadRemoteCurrencyUseCase
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


    private val _status = MutableStateFlow<LoadStatus>(LoadStatus.DEFAULT)
    val status: StateFlow<LoadStatus>
        get() = _status.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

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
            loadRemoteCurrencyUseCase.invoke()
            _status.update { LoadStatus.DONE }
        }
    }
}