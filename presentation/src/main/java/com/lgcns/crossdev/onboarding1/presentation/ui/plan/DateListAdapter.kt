package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemDateButtonBinding
import java.time.LocalDate


class DateListAdapter(private val onItemClickListener: OnItemClickListener): ListAdapter<LocalDate, DateListAdapter.DateViewHolder>(
    DateDiffUtil()
) {

    private var _selectedDate: LocalDate? = null

    interface OnItemClickListener {
        fun onItemClick(date: LocalDate?)
    }

    inner class DateViewHolder(private var binding: ItemDateButtonBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(date: LocalDate){
            binding.date = date
            binding.onItemClickListener = onItemClickListener
            binding.tvDateIcon.isSelected = _selectedDate == date
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(ItemDateButtonBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setDateSelected(oldSelectedDate: LocalDate?, newSelectedDate: LocalDate?) {
        _selectedDate = newSelectedDate
        notifyItemChanged(currentList.indexOf(oldSelectedDate))
        notifyItemChanged(currentList.indexOf(newSelectedDate))
    }

}

class DateDiffUtil : DiffUtil.ItemCallback<LocalDate>() {
    override fun areItemsTheSame(oldItem: LocalDate, newItem: LocalDate): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: LocalDate, newItem: LocalDate): Boolean {
        return oldItem == newItem
    }
}