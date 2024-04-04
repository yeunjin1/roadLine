package com.lgcns.crossdev.onboarding1.presentation.ui.travelList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemTravelBinding

class TravelListAdapter(private val onItemClickListener: OnItemClickListener): ListAdapter<Travel, TravelListAdapter.TravelViewHolder>(
    TravelDiffUtil()
) {
    interface OnItemClickListener{
        fun onItemClick(travel: Travel)
        fun onEditClick(travel: Travel)
        fun onDeleteClick(travel: Travel)
    }

    inner class TravelViewHolder(private var binding: ItemTravelBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item:Travel){
            binding.item = item
            binding.onItemClickListener = onItemClickListener
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        return TravelViewHolder(ItemTravelBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TravelDiffUtil : DiffUtil.ItemCallback<Travel>() {
    override fun areItemsTheSame(oldItem: Travel, newItem: Travel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Travel, newItem: Travel): Boolean {
        return oldItem == newItem
    }
}