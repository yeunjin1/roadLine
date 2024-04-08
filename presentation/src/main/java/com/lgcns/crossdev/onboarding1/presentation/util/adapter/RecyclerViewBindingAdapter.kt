package com.lgcns.crossdev.onboarding1.presentation.util.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.AllCurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.CurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.TravelListAdapter

object RecyclerViewBindingAdapter {
    @BindingAdapter("listData")
    @JvmStatic
    fun RecyclerView.setTravelData(items: MutableList<Travel>?){
        items?.let{
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
}