package com.lgcns.crossdev.onboarding1.presentation.dialog

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.presentation.databinding.DialogTimePickerBinding
import com.lgcns.crossdev.onboarding1.presentation.util.extension.hourMinToTotalMin
import com.lgcns.crossdev.onboarding1.presentation.util.extension.totalMinToHour
import com.lgcns.crossdev.onboarding1.presentation.util.extension.totalMinToMin

class TimePickerDialog(private val time: Int?,
                       private val listener: OnTimeSelectedListener): DialogFragment()  {
    private lateinit var binding: DialogTimePickerBinding

    interface OnTimeSelectedListener {
        fun onTimeSelected(time: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTimePickerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val size = Point()
        dialog!!.window!!.windowManager.defaultDisplay.getSize(size)
        dialog!!.window!!.attributes.let{
            it.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            it.width = (size.x * 0.872f).toInt()
        }
        time?.let {
            binding.timePicker.hour = totalMinToHour(it)
            binding.timePicker.minute = totalMinToMin(it)
        }

        binding.btnOk.setOnClickListener {
            listener.onTimeSelected(hourMinToTotalMin(binding.timePicker.hour, binding.timePicker.minute))
            dismiss()
        }
        binding.btnClose.setOnClickListener { dismiss() }
        binding.btnCancel.setOnClickListener { dismiss() }
    }
}