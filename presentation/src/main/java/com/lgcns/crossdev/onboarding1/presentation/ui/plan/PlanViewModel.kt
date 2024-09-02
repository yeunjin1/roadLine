package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import androidx.lifecycle.viewModelScope
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.domain.usecase.DeletePlanUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.GetPlansByDateUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.GetTravelUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.UpdatePlanPosUseCase
import com.lgcns.crossdev.onboarding1.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val getTravelUseCase: GetTravelUseCase,
    private val getPlansUseCase: GetPlansByDateUseCase,
    private val deletePlanUseCase: DeletePlanUseCase,
    private val updatePlanPosUseCase: UpdatePlanPosUseCase,
): BaseViewModel() {
    private var _travelId = -1L
    val travelId: Long
        get() = _travelId

    private lateinit var _travel: StateFlow<Travel?>
    val travel: StateFlow<Travel?>
        get() = _travel

    private lateinit var _planList: StateFlow<List<Plan>>
    val planList: StateFlow<List<Plan>>
        get() = _planList

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?>
        get() = _selectedDate.asStateFlow()


    fun setTravelId(id: Long) {
        _travelId = id
    }

    fun getTravelByTravelId() {
        _travel = getTravelUseCase.invoke(_travelId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
        getPlans(null)
    }

    private fun getPlans(date: LocalDate?) {
        Timber.tag(TAG).d("getPlans($date)")
        _planList = getPlansUseCase.invoke(_travelId, date)
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

    fun getPlanCount(): Int {
        return if(_selectedDate.value == null) {
            _planList.value.size
        } else {
            _planList.value.filter { it.date == _selectedDate.value }.size
        }
    }

    fun deletePlan(plan: Plan) {
        viewModelScope.launch {
            deletePlanUseCase.invoke(plan)
        }
    }

    fun updatePlansPosition(planList: List<Plan>) {
        planList.forEachIndexed { index, plan ->
            viewModelScope.launch {
                updatePlanPosUseCase.invoke(plan.id!!, index + 1)
            }
        }

    }

    companion object {
        const val TAG = "PlanViewModel"
    }


}