package com.lgcns.crossdev.onboarding1.presentation.dialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.databinding.CalendarDayPickerLayoutBinding
import com.lgcns.crossdev.onboarding1.presentation.databinding.CalendarDialogLayoutBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import com.lgcns.crossdev.onboarding1.presentation.util.extension.getDrawableCompat
import com.lgcns.crossdev.onboarding1.presentation.util.extension.setTextColorRes

class CalendarDialog (context: Context) : Dialog(context) {

    val binding: CalendarDialogLayoutBinding by lazy {
        CalendarDialogLayoutBinding.inflate(LayoutInflater.from(context))
    }


    class Builder(private val mContext: Context) {
        private var startDate: LocalDate? = null
        private var endDate: LocalDate? = null
        private val today = LocalDate.now()

        fun setStartDate(date: LocalDate) {
            startDate = date
        }

        fun setEndDate(date: LocalDate) {
            endDate = date
        }

        val dialog = CalendarDialog(mContext)
        fun create(): Builder {
            dialog.create()
            dialog.setContentView(dialog.binding.root)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            initCalendarView()
            dialog.binding.btnCancel.setOnClickListener { dialog.dismiss() }
            return this
        }

        private fun initCalendarView(){
            val rangeStartBackground = mContext.getDrawableCompat(R.drawable.calendar_day_picked_start)
            val rangeEndBackground = mContext.getDrawableCompat(R.drawable.calendar_day_picked_end)
            val rangeMiddleBackground = mContext.getDrawableCompat(R.drawable.calendar_day_picked_middle)
            val singleBackground = mContext.getDrawableCompat(R.drawable.calendar_day_picked_one)

            val dateFormat = DateTimeFormatter.ofPattern("yyyy년 MM월")
            class DayViewContainer(view: View) : ViewContainer(view) {
                lateinit var day: CalendarDay
                val binding = CalendarDayPickerLayoutBinding.bind(view)
                init {
                    view.setOnClickListener {
                        if (day.position == DayPosition.MonthDate) {
                            val date = day.date
                            if (startDate != null) {
                                if (date < startDate || endDate != null) {
                                    startDate = date
                                    endDate = null
                                } else if (date != startDate) {
                                    endDate = date
                                }
                            } else {
                                startDate = date
                            }
                            dialog.binding.calendarView.notifyCalendarChanged()
                        }
                    }
                }
            }

            dialog.binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.day = data
                    container.binding.calendarDayText.text = data.date.dayOfMonth.toString()
                    container.binding.calendarDayText.background = null
                    container.binding.roundView.visibility = View.INVISIBLE
                    if (data.position == DayPosition.MonthDate) {
                        when {
                            startDate == data.date && endDate == null -> {
                                container.binding.calendarDayText.setTextColorRes(R.color.white)
                                container.binding.roundView.visibility = View.VISIBLE
                                container.binding.roundView.background = singleBackground
                            }
                            data.date == startDate -> {
                                container.binding.calendarDayText.setTextColorRes(R.color.white)
                                container.binding.calendarDayText.background = rangeStartBackground
                            }
                            startDate != null && endDate != null && (data.date > startDate && data.date < endDate) -> {
                                container.binding.calendarDayText.setTextColorRes(R.color.white)
                                container.binding.calendarDayText.background = rangeMiddleBackground
                            }
                            data.date == endDate -> {
                                container.binding.calendarDayText.setTextColorRes(R.color.white)
                                container.binding.calendarDayText.background = rangeEndBackground
                            }
                            else -> {
                                container.binding.calendarDayText.setTextColorRes(R.color.darkGray)
                            }
                        }
                    }
                    else{
                        val startDate = startDate
                        val endDate = endDate
                        if (startDate != null && endDate != null) {
                            if ((data.position == DayPosition.InDate &&
                                        startDate.monthValue == data.date.monthValue &&
                                        endDate.monthValue != data.date.monthValue) ||
                                (data.position == DayPosition.OutDate &&
                                        startDate.monthValue != data.date.monthValue &&
                                        endDate.monthValue == data.date.monthValue) ||

                                (startDate < data.date && endDate > data.date &&
                                        startDate.monthValue != data.date.monthValue &&
                                        endDate.monthValue != data.date.monthValue)
                            ) {
                                container.binding.calendarDayText.setTextColorRes(R.color.white)
                                container.binding.calendarDayText.background = rangeMiddleBackground
                            }
                        }
                    }
                }
                override fun create(view: View) = DayViewContainer(view)
            }

            //캘린더 날짜 속성 초기화
            val currentMonth = YearMonth.now()
            val firstMonth = currentMonth.minusMonths(10)
            val lastMonth = currentMonth.plusMonths(10)
            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            dialog.binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
            dialog.binding.calendarView.scrollToMonth(currentMonth)
            dialog.binding.calendarView.monthScrollListener = {
                dialog.binding.calendarMonthText.text = it.yearMonth.format(dateFormat)
            }

            dialog.binding.leftButton.setOnClickListener {
               dialog.binding.calendarView.findFirstVisibleMonth()?.let {
                    dialog.binding.calendarView.smoothScrollToMonth(it.yearMonth.minusMonths(1))
                }
            }

            dialog.binding.rightButton.setOnClickListener {
                dialog.binding.calendarView.findFirstVisibleMonth()?.let {
                    dialog.binding.calendarView.smoothScrollToMonth(it.yearMonth.plusMonths(1))
                }
            }
        }


        fun setButtonOk(onOkClick: View.OnClickListener) {
            dialog.binding.btnOk.setOnClickListener(onOkClick)
        }

        fun dismissDialog() {
            dialog.dismiss()
        }

        fun show(): CalendarDialog {
            dialog.show()
            return dialog
        }

        fun getStartDate(): LocalDate? {
            return startDate
        }

        fun getEndDate(): LocalDate? {
            return endDate
        }

    }
}