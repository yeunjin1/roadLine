package com.lgcns.crossdev.onboarding1.presentation.util.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.domain.model.PlanViewType
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.presentation.ui.money.MoneyDateListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.money.MoneyGridListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.CategoryListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.DateListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.VerticalPlanListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.AllCurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.CurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.TravelListAdapter
import timber.log.Timber
import java.time.LocalDate

object RecyclerViewBindingAdapter {
    @BindingAdapter("listData")
    @JvmStatic
    fun RecyclerView.setTravelData(items: MutableList<Travel>?){
        items?.let{
            Timber.tag("RecyclerViewBindingAdapter").d("TravelListAdapter currentList: ${(adapter as TravelListAdapter).currentList}")
            Timber.tag("RecyclerViewBindingAdapter").d("TravelListAdapter newList: ${it}")
            (adapter as TravelListAdapter).submitList(it) //For ListAdapter
        }
    }

    @BindingAdapter("listData")
    @JvmStatic
    fun RecyclerView.setCurrencyCodeData(items: MutableList<String>?){
        items?.let{
            (adapter as CurrencyListAdapter).submitList(it) //For ListAdapter
        }
    }

    @BindingAdapter("listData")
    @JvmStatic
    fun RecyclerView.setAllCurrencyData(items: MutableList<Currency>?){
        items?.let{
            (adapter as AllCurrencyListAdapter).submitList(it) //For ListAdapter
        }
    }

    @BindingAdapter("listData")
    @JvmStatic
    fun RecyclerView.setDateData(items: MutableList<LocalDate>?){
        items?.let{
            (adapter as DateListAdapter).submitList(it) //For ListAdapter
        }
    }

    @BindingAdapter("listData")
    @JvmStatic
    fun RecyclerView.setMoneyData(items: MutableList<Money>?){
        items?.let{
            (adapter as MoneyGridListAdapter).submitList(it) //For ListAdapter
        }
    }

    @BindingAdapter("listData", "selectedDate")
    @JvmStatic
    fun RecyclerView.setMoneyDateData(items: MutableList<Money>?, date: LocalDate?){
        items?.let{
            if(items.isNotEmpty()) {
                val result = mutableListOf<Pair<LocalDate, List<Money>>>()
                if(date == null) { //전체 일자
                    val map = it.groupBy { money -> money.date }.toMutableMap()
                    map.forEach { (date, list) ->
                        result.add(Pair(date, list))
                    }
                }
                else {
                    val list = it.groupBy { money -> money.date }[date]
                    if(list == null) {
                        result.add(Pair(date, emptyList()))
                    }
                    else {
                        result.add(Pair(date, list))
                    }
                }
                (adapter as MoneyDateListAdapter).submitList(result) //For ListAdapter
            }
        }
    }

    @BindingAdapter("listData", "selectedDate")
    @JvmStatic
    fun RecyclerView.setVerticalPlanListData(items: MutableList<Plan>?, date: LocalDate?){
        items?.let{ planList ->
            val result = if(date == null) {
                planList.map { it.copy() }
            }
            else {
                planList.filter { it.date == date }.map { it.copy() }
            }
            var date: LocalDate? = null
            result.mapIndexed { index, plan ->
                if(date == plan.date) {
                    result[index].viewType = PlanViewType.MIDDLE
                }
                else {
                    date = plan.date
                    if(index - 1 >= 0) {
                        result[index - 1].viewType = if(index - 2 >= 0 && result[index - 2].date != result[index - 1].date
                            || index - 2 < 0) {
                            PlanViewType.ONE
                        }
                        else {
                            PlanViewType.BOTTOM
                        }
                    }
                    result[index].viewType = PlanViewType.TOP
                }
            }
            if(result.isNotEmpty()) {
                if(result.size - 2 >= 0 && result[result.size - 2].date == date) {
                    result[result.size - 1].viewType = PlanViewType.BOTTOM
                }
                else {
                    result[result.size - 1].viewType = PlanViewType.ONE
                }
            }
            Timber.tag("RecyclerViewBindingAdapter").d("setVerticalPlanListData(${result} $date)")
            (adapter as VerticalPlanListAdapter).submitList(result)
        }
    }

}
