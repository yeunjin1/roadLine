package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.base.BaseFragment
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentPlanBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlanFragment : BaseFragment<FragmentPlanBinding>(
    R.layout.fragment_plan
) {
    private val viewModel: PlanViewModel by activityViewModels()
    private lateinit var dateListAdapter: DateListAdapter
    private var oldSelectedDate: LocalDate? = null

    private val onDateClickListener = object : DateListAdapter.OnItemClickListener {
        override fun onItemClick(date: LocalDate?) {
            viewModel.setSelectedDate(date)
        }
    }

    private val planFragments = arrayListOf<Fragment>(
        VerticalPlanFragment(),
//        HorizontalPlanFragment(),
        MapPlanFragment()
    )

    override fun initView() {
        binding.viewModel = viewModel

        dateListAdapter = DateListAdapter(onDateClickListener)
        binding.rvDates.adapter = dateListAdapter
        binding.btnAll.onItemClickListener = onDateClickListener
        binding.vpPlans.adapter = PlanViewPagerAdapter(planFragments, this)
        TabLayoutMediator(binding.tabs, binding.vpPlans){ tab, position ->
            tab.icon = when(position){
                0 -> { ContextCompat.getDrawable(requireActivity(), R.drawable.tab_list)}
                1 -> { ContextCompat.getDrawable(requireActivity(), R.drawable.tab_map)}
                else -> { ContextCompat.getDrawable(requireActivity(), R.drawable.tab_map)}
            }
            tab.view.alpha = 0.4f
        }.attach()
        binding.tabs.addOnTabSelectedListener( object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) { tab?.view?.alpha = 1f }
            override fun onTabUnselected(tab: TabLayout.Tab?) { tab?.view?.alpha = 0.4f }
            override fun onTabReselected(tab: TabLayout.Tab?) { tab?.view?.alpha = 1f }
        })

        // 일정 추가
        binding.fabAdd.setOnClickListener {
            val action = PlanFragmentDirections.actionNavPlanToNavAddPlan(plan = Plan(
                travelId = viewModel.travelId
                , date = viewModel.selectedDate.value!!
                , pos = viewModel.getPlanCount() + 1
            ))
            findNavController().navigate(action)
        }
    }

    override fun setObserve() {
        lifecycleScope.launch {
            viewModel.selectedDate.collectLatest {
                dateListAdapter.setDateSelected(oldSelectedDate, it)
                binding.btnAll.tvDateIcon.isSelected = it == null
                oldSelectedDate = it
            }
        }

        lifecycleScope.launch {
            viewModel.travel.collectLatest {
                it?.let {
                    (requireActivity() as TravelActivity).setToolbarTitle(it.title)
                    (requireActivity() as TravelActivity).setToolbarMenuVisible(true)
                }
            }
        }


    }

    companion object {
        const val TAG = "PlanFragment"
    }
}