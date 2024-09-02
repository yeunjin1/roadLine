package com.lgcns.crossdev.onboarding1.presentation.ui.travelList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemAllCurrencyBinding

class AllCurrencyListAdapter(private val onItemClickListener: OnItemClickListener): ListAdapter<Currency, AllCurrencyListAdapter.AllCurrencyViewHolder>(
    AllCurrencyDiffUtil()
) {

    interface OnItemClickListener {
        fun onItemClick(currency: Currency)
    }

    inner class AllCurrencyViewHolder(private var binding: ItemAllCurrencyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: Currency){
            binding.currency = currency
            binding.onItemClickListener = onItemClickListener
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCurrencyViewHolder {
        return AllCurrencyViewHolder(ItemAllCurrencyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: AllCurrencyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class AllCurrencyDiffUtil : DiffUtil.ItemCallback<Currency>() {
    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.code == newItem.code
    }
}