package com.lgcns.crossdev.onboarding1.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.databinding.DialogTravelAddBinding
import com.lgcns.crossdev.onboarding1.presentation.dialog.CalendarDialog
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.TravelListViewModel
import com.lgcns.crossdev.onboarding1.presentation.dialog.BaseDialog
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.PlanActivity
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.AllCurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.CurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.TravelListAdapter
import com.lgcns.crossdev.onboarding1.presentation.util.extension.periodToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

class TravelAddDialog(context: Context): Dialog(context) {

    val binding: DialogTravelAddBinding by lazy {
        DialogTravelAddBinding.inflate(LayoutInflater.from(context))
    }

    class Builder(private val mContext: Context, private val viewModel: TravelListViewModel) {
        private val dialog = TravelAddDialog(mContext)
        private var dateStart: LocalDate? = null
        private var dateEnd: LocalDate? = null
//        private var currencyCodes = mutableListOf<String>("+")
        val currencyCodes = MutableStateFlow<List<String>>(listOf("+"))
        private lateinit var currencyListAdapter: CurrencyListAdapter

        fun create(travel: Travel? = null): Builder {
            dialog.create()
            dialog.setContentView(dialog.binding.root)
            dialog.binding.viewModel = viewModel
            if(travel == null) { //여행 추가
                viewModel.setSelectedCurrencyCodes(emptyList())
            }
            else { //여행 수정
                dialog.binding.travel = travel
                dateStart = travel.dateStart
                dateEnd = travel.dateEnd
                viewModel.setSelectedCurrencyCodes(travel.currencyCodes)
            }
//            dialog.binding.currencyCodes = currencyCodes
            currencyListAdapter = CurrencyListAdapter(object : CurrencyListAdapter.OnItemClickListener {
                override fun onItemClick(currencyCode: String) {
                    if (currencyCode == "+") { //추가
                        val currencyAddDialog = CurrencyAddDialog.Builder(mContext, object : AllCurrencyListAdapter.OnItemClickListener {
                            override fun onItemClick(currency: Currency) {
                                viewModel.addSelectedCurrencyCodes(currency.code)
//                                currencyCodes.add(currency.code)
                            }
                        })

                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.allCurrencyList.collectLatest {
                                currencyAddDialog.create(it).show()
                            }
                        }
                    }
                    else { //삭제
                        val dialog = BaseDialog.Builder(mContext)
                        dialog.create()
                            .setTitle(mContext.getString(R.string.delete_label))
                            .setMessage(mContext.getString(R.string.delete_confirm_msg))
                            .setOkButton {
                                viewModel.setSelectedCurrencyCodes(emptyList())
                                dialog.dismissDialog()
                            }
                            .show()
                    }
                }
            })
            dialog.binding.rvCurrency.adapter = currencyListAdapter


            // 화폐 선택 다이얼로그
//            dialog.binding.rvCurrency.setOnClickListener {
////                val dialog = CurrencyAddDialog.Builder(mContext, viewModel)
////                dialog.create(travel).show()
//
//                val allCurrencyCodes = viewModel.allCurrencyList.value.map { it.code }.toTypedArray()
//                val isCurrencySelected = viewModel.allCurrencyList.value.map {
//                    travel != null && travel.currencyCodes.contains(it.code)
//                }.toBooleanArray()
////                val selectedCurrencyCodes = mutableListOf<String>()
//
//                val currencyDialog = AlertDialog.Builder(mContext)
//                currencyDialog.apply {
//                    setTitle(mContext.getString(R.string.select_currency_guide))
//                    setMultiChoiceItems(allCurrencyCodes, isCurrencySelected) { _, which, isChecked ->
//                        if(isChecked) {
//                            currencyCodes.add(allCurrencyCodes[which])
//                        }
//                        else {
//                            currencyCodes.remove(allCurrencyCodes[which])
//                        }
//                    }
//                    setPositiveButton(mContext.getString(R.string.confirm_label)) { _, which ->
////                        dialog.binding.tvCurrency.text = currencyCodes.joinToString(",", "", "", -1)
////                        setCurrencyView()
//                    }
//                    show()
//                }
//            }

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val size = Point()
            dialog.window!!.windowManager.defaultDisplay.getSize(size)
            dialog.window!!.attributes.let{
                it.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                it.width = (size.x * 0.872f).toInt()
            }

            dialog.binding.btnClose.setOnClickListener { dialog.dismiss() }

            dialog.binding.btnOk.setOnClickListener {
                val title = dialog.binding.tvTravelTitle.text.toString()
                val guideDialog = BaseDialog.Builder(mContext).create().setTitle(mContext.getString(R.string.alarm_label))
                if(title.isEmpty())
                    guideDialog.setMessage(mContext.getString(R.string.input_title_guide))
                        .setOkButton(mContext.getString(R.string.close_label)) {guideDialog.dismissDialog() }
                        .show()
                else if(dateStart == null || dateEnd == null)
                    guideDialog.setMessage(mContext.getString(R.string.input_date_guide))
                        .setOkButton(mContext.getString(R.string.close_label)) { guideDialog.dismissDialog() }
                        .show()
                else {
                    if(travel == null) {
                        viewModel.insertTravel(
                            Travel(title = title, dateStart = dateStart!!, dateEnd = dateEnd!!)
                        )
                    } else {
                        travel.title = title
                        travel.dateStart = dateStart!!
                        travel.dateEnd = dateEnd!!
                        viewModel.updateTravel(travel)
                    }
                    dismissDialog()
                }
            }

            dialog.binding.tvDate.setOnClickListener {
                makeCalendarDialog()
            }

            return this
        }

        private fun makeCalendarDialog() {
            val calendarDialog = CalendarDialog.Builder(mContext).create()
            calendarDialog.setButtonOk{
                dateStart = calendarDialog.getStartDate()
                dateEnd = calendarDialog.getEndDate()
                calendarDialog.dismissDialog()
                if (dateStart != null && dateEnd != null) {
                    dialog.binding.tvDate.text = periodToString(dateStart!!, dateEnd!!)
                }
            }
            dateStart?.let { calendarDialog.setStartDate(dateStart!!) }
            dateEnd?.let { calendarDialog.setEndDate(dateEnd!!) }
            calendarDialog.show()
        }


        private fun dismissDialog() {
            dialog.dismiss()
        }

        fun show(): TravelAddDialog {
            dialog.show()
            return dialog
        }
    }
}