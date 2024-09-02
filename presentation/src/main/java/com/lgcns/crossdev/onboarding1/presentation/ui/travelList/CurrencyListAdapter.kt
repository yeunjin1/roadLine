package com.lgcns.crossdev.onboarding1.presentation.ui.travelList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemCurrencyBinding

class CurrencyListAdapter(private val onItemClickListener: OnItemClickListener): ListAdapter<String, CurrencyListAdapter.CurrencyViewHolder>(
    CurrencyDiffUtil()
) {

    interface OnItemClickListener {
        fun onItemClick(currencyCode: String)
    }

    inner class CurrencyViewHolder(private var binding: ItemCurrencyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(code: String){
            binding.currencyCode = code
            binding.onItemClickListener = onItemClickListener
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CurrencyDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}