package com.lgcns.crossdev.onboarding1.presentation.ui.money

import androidx.lifecycle.viewModelScope
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.domain.model.Travel
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddMoneyViewModel @Inject constructor(
    private val insertMoneyUseCase: InsertMoneyUseCase,
): BaseViewModel() {

    private lateinit var _money: MutableStateFlow<Money>
    val money: StateFlow<Money>
        get() = _money.asStateFlow()

    fun initMoney(money: Money) {
        Timber.tag(TAG).d("initMoney: ${money}")
        _money = MutableStateFlow(money)
    }

    fun addMoney() {
        Timber.tag(TAG).d("addMoney: ${_money.value}")
        viewModelScope.launch(Dispatchers.IO) {
            insertMoneyUseCase.invoke(_money.value)
        }
    }

    fun setMoney(currencyCode: String? = null
                 , img: String? = null
                 , price: Double? = null
                 , korPrice: Double? = null
                 , category: Int? = null
                 , date: LocalDate? = null
                 , memo: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            currencyCode?.let {
                _money.update { p ->  p.copy(currencyCode = it) }
            }
            img?.let {
                _money.update { p ->  p.copy(img = it) }
            }
            price?.let {
                _money.update { p ->  p.copy(price = it) }
            }
            korPrice?.let {
                _money.update { p ->  p.copy(korPrice = it) }
            }
            category?.let {
                _money.update { p ->  p.copy(category = it) }
            }
            date?.let {
                _money.update { p ->  p.copy(date = it) }
            }
            memo?.let {
                _money.update { p ->  p.copy(memo = it) }
            }
        }
    }


    companion object {
        const val TAG = "AddMoneyViewModel"
    }

}