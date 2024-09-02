package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.base.BaseFragment
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentHorizontalPlanBinding
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentVerticalPlanBinding

class HorizontalPlanFragment : BaseFragment<FragmentHorizontalPlanBinding>(
    R.layout.fragment_horizontal_plan
) {
    private val viewModel: PlanViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
    }
}