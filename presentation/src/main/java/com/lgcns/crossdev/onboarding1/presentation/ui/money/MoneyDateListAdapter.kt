package com.lgcns.crossdev.onboarding1.presentation.ui.money

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemDateButtonBinding
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemMoneyDateBinding
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.DateDiffUtil
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.DateListAdapter
import com.lgcns.crossdev.onboarding1.presentation.util.extension.toStringDate
import java.time.LocalDate


class MoneyDateListAdapter(private val onItemClickListener: MoneyGridListAdapter.OnItemClickListener
, private val onItemChangeListener: OnItemChangeListener): ListAdapter<Pair<LocalDate, List<Money>>, MoneyDateListAdapter.MoneyDateViewHolder>(
    MoneyDateDiffUtil()
) {

    interface OnItemChangeListener {
        fun onItemChange(previousList: MutableList<Pair<LocalDate, List<Money>>>,
                         currentList: MutableList<Pair<LocalDate, List<Money>>>)
    }

    inner class MoneyDateViewHolder(private var binding: ItemMoneyDateBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(moneyList: Pair<LocalDate, List<Money>>){
            binding.tvDate.text = moneyList.first.toStringDate()
            binding.moneyList = moneyList.second
            val gridListAdapter = MoneyGridListAdapter(onItemClickListener)
            binding.rvMoney.adapter = gridListAdapter
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyDateViewHolder {
        return MoneyDateViewHolder(ItemMoneyDateBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MoneyDateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Pair<LocalDate, List<Money>>>,
        currentList: MutableList<Pair<LocalDate, List<Money>>>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        onItemChangeListener.onItemChange(previousList, currentList)
    }
}

class MoneyDateDiffUtil : DiffUtil.ItemCallback<Pair<LocalDate, List<Money>>>() {
    override fun areItemsTheSame(oldItem: Pair<LocalDate, List<Money>>, newItem: Pair<LocalDate, List<Money>>): Boolean {
        return oldItem.second.size == newItem.second.size && oldItem.first == newItem.first
    }

    override fun areContentsTheSame(oldItem: Pair<LocalDate, List<Money>>, newItem: Pair<LocalDate, List<Money>>): Boolean {
        oldItem.second.forEachIndexed { index, money -> if(newItem.second[index] != money) return false }
        return true
    }
}