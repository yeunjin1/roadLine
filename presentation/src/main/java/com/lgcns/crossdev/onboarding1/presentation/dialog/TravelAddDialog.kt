package com.lgcns.crossdev.onboarding1.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.databinding.DialogTravelAddBinding
import com.lgcns.crossdev.onboarding1.presentation.dialog.CalendarDialog
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.TravelListViewModel
import com.lgcns.crossdev.onboarding1.presentation.dialog.BaseDialog
import com.lgcns.crossdev.onboarding1.presentation.util.extension.periodToString
import java.time.LocalDate

class TravelAddDialog(context: Context): Dialog(context) {

    val binding: DialogTravelAddBinding by lazy {
        DialogTravelAddBinding.inflate(LayoutInflater.from(context))
    }

    class Builder(private val mContext: Context, private val viewModel: TravelListViewModel) {
        private val dialog = TravelAddDialog(mContext)
        private var dateStart: LocalDate? = null
        private var dateEnd: LocalDate? = null
        fun create(travel: Travel? = null): Builder {
            dialog.create()
            dialog.setContentView(dialog.binding.root)
            travel?.let{
                dialog.binding.travel = it
                dateStart = it.dateStart
                dateEnd = it.dateEnd
            }

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

//        private fun setCurrencyAdapter(adapter: ArrayAdapter<String>): Builder {
//            dialog.binding.spCurrency.adapter = adapter
//            dialog.binding.spCurrency.setPositiveButton(mContext.getString(R.string.cancel_label))
//            dialog.binding.spCurrency.setSelection(adapter.count)
//            return this
//        }

        private fun dismissDialog() {
            dialog.dismiss()
        }

        fun show(): TravelAddDialog {
            dialog.show()
            return dialog
        }
    }
}