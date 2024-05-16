package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.base.BaseFragment
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentVerticalPlanBinding
import com.lgcns.crossdev.onboarding1.presentation.dialog.BaseDialog
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.TravelListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class VerticalPlanFragment : BaseFragment<FragmentVerticalPlanBinding> (
    R.layout.fragment_vertical_plan
) {
    private val viewModel: PlanViewModel by activityViewModels()
    private lateinit var verticalPlanListAdapter: VerticalPlanListAdapter

    override fun initView() {
        super.initView()
        binding.viewModel = viewModel
        setupListAdapter()
    }

    private fun setupListAdapter() {
        verticalPlanListAdapter = VerticalPlanListAdapter(object : VerticalPlanListAdapter.OnItemClickListener {
            override fun onItemClick(plan: Plan) {
                if(verticalPlanListAdapter.editMode) {
                    verticalPlanListAdapter.toggleEditMode()
                }
                else {
                    val action = PlanFragmentDirections.actionNavPlanToNavAddPlan(plan = plan)
                    findNavController().navigate(action)
                }
            }

            override fun onItemPositionUpdate() {
                viewModel.updatePlansPosition(verticalPlanListAdapter.currentList)
            }

            override fun onItemDelete(plan: Plan) {
                val dialog = BaseDialog.Builder(requireContext()).create()
                dialog.setTitle(getString(R.string.alarm_label))
                    .setMessage(getString(R.string.delete_confirm_msg))
                    .setCancelButton {
                        dialog.dismissDialog()
                    }
                    .setOkButton {
                        viewModel.deletePlan(plan)
                        dialog.dismissDialog()
                    }
                    .show()
            }
        })

        verticalPlanListAdapter.setItemTouchHelper(binding.rvVerticalPlan)
        binding.rvVerticalPlan.adapter = verticalPlanListAdapter
    }

    override fun setObserve() {
        lifecycleScope.launch {
            viewModel.planList.collectLatest {
                Timber.tag(TAG).d(it.toString())
            }
        }

    }

    companion object {
        const val TAG = "VerticalPlanFragment"
    }

}