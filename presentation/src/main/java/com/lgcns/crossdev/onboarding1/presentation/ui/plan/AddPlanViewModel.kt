package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.domain.usecase.GetTravelUseCase
import com.lgcns.crossdev.onboarding1.domain.usecase.InsertPlanUseCase
import com.lgcns.crossdev.onboarding1.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddPlanViewModel @Inject constructor(
    private val insertPlanUseCase: InsertPlanUseCase,
): BaseViewModel() {
    private lateinit var _plan: MutableStateFlow<Plan>
    val plan: StateFlow<Plan>
        get() = _plan.asStateFlow()

    fun initPlan(plan: Plan) {
        _plan = MutableStateFlow<Plan>(plan)
    }

    fun setPlan(locationX: Double? = null
                , locationY: Double? = null
                , name: String? = null
                , nameAlter: String? = null
                , time: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            locationX?.let {
                _plan.update { p ->  p.copy(locationX = it) }
            }
            locationY?.let {
                _plan.update { p ->  p.copy(locationY = it) }
            }
            name?.let {
                _plan.update { p ->  p.copy(name = it) }
            }
            nameAlter?.let {
                _plan.update { p ->  p.copy(nameAlter = it) }
            }
            time?.let {
                _plan.update { p ->  p.copy(time = it) }
            }
        }

    }

    fun addPlan() {
        Timber.tag(AddPlanFragment.TAG).d("Add Plan: ${plan.value}")
        viewModelScope.launch(Dispatchers.IO) {
            insertPlanUseCase.invoke(_plan.value)
        }
    }
}