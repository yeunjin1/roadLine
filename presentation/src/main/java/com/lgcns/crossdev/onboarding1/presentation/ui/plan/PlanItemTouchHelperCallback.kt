package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

interface ItemTouchHelperListener {
    fun onItemMove(fromPos: Int, toPos: Int): Boolean
    fun onItemPositionUpdate()
}

class PlanItemTouchHelperCallback(val listener: ItemTouchHelperListener) :ItemTouchHelper.Callback(){

//    private val POSITION_UNKNOWN = -1
//    private var originPosition = POSITION_UNKNOWN
//    private var oldPosition = POSITION_UNKNOWN
//    private var newPosition = POSITION_UNKNOWN
//    var dateListAdapter = adapter
//    var context = context

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
//        val swipeFlag = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlag, 0)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        Timber.tag(TAG).d("onItemMove(fromPos=$p1.bindingAdapterPosition, toPos: $p2.bindingAdapterPosition)")
//        adapter.onItemMove(p1.bindingAdapterPosition, p2.bindingAdapterPosition)
        listener.onItemMove(p1.bindingAdapterPosition, p2.bindingAdapterPosition)
        return false
    }



    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//        if(newPosition == dateListAdapter.itemCount-1) newPosition--
//        dateListAdapter.onAttachedToRecyclerView(recyclerView)
//        dateListAdapter.moveItem(originPosition, newPosition)
//        oldPosition = POSITION_UNKNOWN;
//        newPosition = POSITION_UNKNOWN;
//        originPosition = POSITION_UNKNOWN;
        if (recyclerView.adapter is VerticalPlanListAdapter) {
//            (recyclerView.adapter as VerticalPlanListAdapter).updatePosition()
        }
        listener.onItemPositionUpdate()
        super.clearView(recyclerView, viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    companion object {
        const val TAG = "PlanItemTouchHelperCallback"
    }
}

