package com.lgcns.crossdev.onboarding1.presentation.ui.money

import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.base.BaseFragment
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentMoneyBinding
import com.lgcns.crossdev.onboarding1.presentation.dialog.BaseDialog
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.DateListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.PlanViewModel
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.TravelActivity
import com.lgcns.crossdev.onboarding1.presentation.util.extension.toFormatPrice
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DecimalFormat
import java.time.LocalDate
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MoneyFragment : BaseFragment<FragmentMoneyBinding>(
    R.layout.fragment_money
) {
    private val viewModel: MoneyViewModel by activityViewModels()
    private lateinit var dateListAdapter: DateListAdapter
    private lateinit var moneyDateListAdapter: MoneyDateListAdapter
    private var oldSelectedDate: LocalDate? = null
    private val onDateClickListener = object : DateListAdapter.OnItemClickListener {
        override fun onItemClick(date: LocalDate?) {
            viewModel.setSelectedDate(date)
        }
    }

    override fun initView() {
        binding.viewModel = viewModel

        (requireActivity() as TravelActivity).setToolbarMenuVisible(false)

        dateListAdapter = DateListAdapter(onDateClickListener)
        binding.rvDates.adapter = dateListAdapter
        binding.btnAll.onItemClickListener = onDateClickListener

        moneyDateListAdapter = MoneyDateListAdapter(object : MoneyGridListAdapter.OnItemClickListener {
            override fun onItemClick(money: Money?) {
                val action = MoneyFragmentDirections.actionNavMoneyToNavAddMoney(money)
                findNavController().navigate(action)
            }

            override fun onItemLongClick(money: Money): Boolean {
                val dialog = BaseDialog.Builder(requireContext()).create()
                dialog.setTitle(getString(R.string.alarm_label))
                    .setMessage(getString(R.string.delete_confirm_msg))
                    .setCancelButton {
                        dialog.dismissDialog()
                    }
                    .setOkButton {
                        viewModel.deleteMoney(money)
                        dialog.dismissDialog()
                    }
                    .show()
                return true
            }
        }
        , object : MoneyDateListAdapter.OnItemChangeListener {
                override fun onItemChange(
                    previousList: MutableList<Pair<LocalDate, List<Money>>>,
                    currentList: MutableList<Pair<LocalDate, List<Money>>>
                ) {
                    viewModel.updateSelectedMoneyList(currentList)
                }
            })
        binding.rvMoney.adapter = moneyDateListAdapter

        // 가계부 추가 버튼
        binding.fabAdd.setOnClickListener {
            val action = MoneyFragmentDirections.actionNavMoneyToNavAddMoney(money = Money(
                travelId = viewModel.travelId
                , date = viewModel.selectedDate.value!!
            ))
            findNavController().navigate(action)
        }

        // 가계부 상세 버튼
        binding.ivDetailMoney.setOnClickListener {
            Timber.tag(TAG).d( "selectedMoneyList: ${viewModel.selectedMoneyList.value}")
            if(viewModel.selectedMoneyList.value.isEmpty() || viewModel.selectedMoneyList.value[0].second.isEmpty()) {
                val dialog = BaseDialog.Builder(requireContext()).create()
                dialog.setTitle(getString(R.string.alarm_label))
                    .setMessage(getString(R.string.input_money_guide))
                    .setOkButton(getString(R.string.confirm_label)) {
                        dialog.dismissDialog()
                    }
                    .show()
            }
            else {
                val action = MoneyFragmentDirections.actionNavMoneyToNavDetailMoney()
                findNavController().navigate(action)
            }
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
            viewModel.selectedMoneyList.collectLatest {
                updateTotalMoneyView(it)
            }
        }


    }


    private fun updateTotalMoneyView(currentList: List<Pair<LocalDate, List<Money>>>) {
        var totalKorExpense = 0.0
        currentList.forEach { it.second.forEach {money ->
            totalKorExpense += money.korPrice ?: 0.0
        } }
        binding.tvTotalMoney.text = "Total ${totalKorExpense.toFormatPrice("KRW")} KRW"
    }

    companion object {
        const val TAG = "MoneyFragment"
    }
}