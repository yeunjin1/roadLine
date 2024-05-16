package com.lgcns.crossdev.onboarding1.presentation.ui.money

import android.graphics.Color
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.base.BaseFragment
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentDetailMoneyBinding
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentMoneyBinding
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.CategoryListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.DateListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.TravelActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailMoneyFragment : BaseFragment<FragmentDetailMoneyBinding>(
    R.layout.fragment_detail_money
) {
    private val viewModel: MoneyViewModel by activityViewModels()
    private lateinit var categoryListAdapter: CategoryListAdapter
    private val chartColors = arrayOf(Color.rgb(95,157,212),
        Color.rgb(162, 211, 223),
        Color.rgb(96, 176, 214),
        Color.rgb(96, 174, 191),
        Color.rgb(140, 207, 223),
        Color.rgb(96, 209, 214),
        Color.rgb(92, 204, 192))

    override fun initView() {
        binding.viewModel = viewModel

        (requireActivity() as TravelActivity).setToolbarMenuVisible(false)

        // 리스트
        val totalMoneyList = mutableListOf<Money>()
        viewModel.selectedMoneyList.value.forEach { totalMoneyList.addAll(it.second) }
        val totalPrice = totalMoneyList.sumOf { it.korPrice!! }
        val categoryMoneyPairList = totalMoneyList.groupBy { it.category }.toList()
        categoryListAdapter = CategoryListAdapter()
        binding.rvMoney.adapter = categoryListAdapter
        binding.rvMoney.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        categoryListAdapter.submitList(categoryMoneyPairList)

        // 차트
        binding.chartMoney.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(5F, 10F, 5F, 5F)
            dragDecelerationFrictionCoef = 0.95f
            isDrawHoleEnabled = false
            setHoleColor(Color.WHITE)
            transparentCircleRadius = 61f
            animateY(1000, Easing.EaseInOutCubic)
            val chartValues = mutableListOf<PieEntry>()
            categoryMoneyPairList.forEach { pair ->
                val percentage = ((pair.second.sumOf { it.korPrice!! } / totalPrice * 100.0) * 10.0).roundToInt() / 10.0
                chartValues.add(PieEntry(percentage.toFloat(), getCategoryName(pair.first)))
            }

            val dataSet = PieDataSet(chartValues, "").apply {
                sliceSpace = 3f
                selectionShift = 5f
                colors = chartColors.toList()
            }

            data = PieData(dataSet).apply {
                setValueTextSize(18f)
                setValueTextColor(Color.rgb(82,82,82))
            }
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