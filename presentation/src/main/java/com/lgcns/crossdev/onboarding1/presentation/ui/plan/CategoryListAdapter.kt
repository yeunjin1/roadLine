package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemDateButtonBinding
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemMoneyCategoryBinding
import java.time.LocalDate


class CategoryListAdapter: ListAdapter<Pair<Int, List<Money>>, CategoryListAdapter.CategoryViewHolder>(
    CategoryDiffUtil()
) {

    inner class CategoryViewHolder(private var binding: ItemMoneyCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pair: Pair<Int, List<Money>>?){
            if(pair == null) {
                binding.sumPrice = currentList.sumOf { it.second.sumOf { it.korPrice!! } }
                binding.category = "합계"
            }
            else {
                binding.sumPrice = pair.second.sumOf { it.korPrice!! }
                binding.category = getCategoryName(pair.first)
            }

        }
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemMoneyCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        if(position == itemCount - 1) {
            holder.bind(null)
        }
        else {
            holder.bind(getItem(position))
        }
    }

    private fun getCategoryName(categoryId: Int): String {
        return when(categoryId) {
            0 -> "식사"
            1 -> "쇼핑"
            2 -> "교통"
            3 -> "관광"
            4 -> "숙박"
            else -> "기타"
        }
    }


}

class CategoryDiffUtil : DiffUtil.ItemCallback<Pair<Int, List<Money>>>() {
    override fun areItemsTheSame(oldItem: Pair<Int, List<Money>>, newItem: Pair<Int, List<Money>>): Boolean {
        return oldItem.first == newItem.first
    }

    override fun areContentsTheSame(oldItem: Pair<Int, List<Money>>, newItem: Pair<Int, List<Money>>): Boolean {
        return oldItem.second.size == newItem.second.size
    }
}