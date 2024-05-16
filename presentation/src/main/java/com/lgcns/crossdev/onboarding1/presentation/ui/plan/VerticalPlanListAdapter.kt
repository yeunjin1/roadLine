package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemCurrencyBinding
import com.lgcns.crossdev.onboarding1.presentation.databinding.ItemVerticalPlanBinding
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.CurrencyListAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class VerticalPlanListAdapter(private val onItemClickListener: OnItemClickListener): ListAdapter<Plan, VerticalPlanListAdapter.VerticalPlanViewHolder>(
    VerticalPlanDiffUtil()
) {
    private var itemTouchHelper = ItemTouchHelper(PlanItemTouchHelperCallback(object : ItemTouchHelperListener {
        override fun onItemMove(fromPos: Int, toPos: Int): Boolean {
            val originList = currentList.toMutableList()
            val item = originList.removeAt(fromPos)
            originList.add(toPos, item)
            submitList(originList.toList())
            return true
        }

        override fun onItemPositionUpdate() {
            onItemClickListener.onItemPositionUpdate()
        }
    }))

    fun setItemTouchHelper(view: RecyclerView?) {
        itemTouchHelper.attachToRecyclerView(view)
    }

    private var _editMode = false
    val editMode: Boolean
        get() = _editMode


    fun toggleEditMode() {
        _editMode = !_editMode
        notifyItemRangeChanged(0, itemCount)
    }

    interface OnItemClickListener {
        fun onItemClick(plan: Plan)
//        fun onItemDrag(plan: Plan, viewHolder: VerticalPlanViewHolder)
        fun onItemDelete(plan: Plan)
        fun onItemPositionUpdate()
    }

    inner class VerticalPlanViewHolder(var binding: ItemVerticalPlanBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Plan){
            binding.plan = item
            binding.listener = onItemClickListener
            binding.editMode = _editMode
            binding.itemLayout.setOnLongClickListener {
                toggleEditMode()
                true
            }
            binding.btnDrag.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_DOWN) {
                    Timber.tag(TAG).d("setOnTouchListener MotionEvent.ACTION_DOWN")
                    itemTouchHelper.startDrag(this)
                }
                else if(event.action == MotionEvent.ACTION_UP) {
                    Timber.tag(TAG).d("setOnTouchListener MotionEvent.ACTION_UP")
                }
                v.performClick()
                false
            }
            Timber.tag(TAG).d("bind() layoutPosition: $layoutPosition itemCount: $itemCount")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalPlanListAdapter.VerticalPlanViewHolder {
        return VerticalPlanViewHolder(ItemVerticalPlanBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: VerticalPlanListAdapter.VerticalPlanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        const val TAG = "VerticalPlanListAdapter"
    }
}

class VerticalPlanDiffUtil : DiffUtil.ItemCallback<Plan>() {
    override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
        return oldItem == newItem
    }

}