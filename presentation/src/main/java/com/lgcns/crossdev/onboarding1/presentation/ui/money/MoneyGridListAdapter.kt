package com.lgcns.crossdev.onboarding1.presentation.ui.money

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemMoneyBinding
import com.lgcns.crossdev.onboarding1.presentation.util.extension.getDrawableCompat
import com.lgcns.crossdev.onboarding1.presentation.util.extension.toFormatPrice
import java.time.LocalDate


class MoneyGridListAdapter(private val onItemClickListener: OnItemClickListener): ListAdapter<Money, MoneyGridListAdapter.MoneyGridViewHolder>(
    MoneyGridDiffUtil()
) {

    interface OnItemClickListener {
        fun onItemClick(money: Money?)
        fun onItemLongClick(money: Money): Boolean
    }

    inner class MoneyGridViewHolder(private var binding: ItemMoneyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(money: Money){
            binding.tvPrice.text = "${money.price?.toFormatPrice(money.currencyCode)} ${money.currencyCode}"
            val img = if(money.img == null) {
                when(money.category) {
                    0 -> binding.root.context.getDrawableCompat(R.drawable.meal)
                    1 -> binding.root.context.getDrawableCompat(R.drawable.shopping)
                    2 -> binding.root.context.getDrawableCompat(R.drawable.transport)
                    3 -> binding.root.context.getDrawableCompat(R.drawable.tour)
                    4 -> binding.root.context.getDrawableCompat(R.drawable.lodgment)
                    else -> binding.root.context.getDrawableCompat(R.drawable.etc)
                }
            }
            else {
                money.img
            }
            Glide.with(binding.root.context)
                .load(img)
                .into(binding.ivMoney)
            binding.money = money
            binding.listener = onItemClickListener
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyGridViewHolder {
        return MoneyGridViewHolder(ItemMoneyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MoneyGridViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class MoneyGridDiffUtil : DiffUtil.ItemCallback<Money>() {
    override fun areItemsTheSame(oldItem: Money, newItem: Money): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Money, newItem: Money): Boolean {
        return oldItem == newItem
    }
}