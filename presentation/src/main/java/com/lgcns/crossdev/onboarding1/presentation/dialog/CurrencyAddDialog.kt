package com.lgcns.crossdev.onboarding1.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.databinding.DialogCurrencyAddBinding
import com.lgcns.crossdev.onboarding1.presentation.databinding.DialogTravelAddBinding
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.AllCurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.CurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.TravelListViewModel
import com.lgcns.crossdev.onboarding1.presentation.util.extension.getSerializable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CurrencyAddDialog(private val listener: AllCurrencyListAdapter.OnItemClickListener): DialogFragment() {

    private lateinit var binding: DialogCurrencyAddBinding
    private val viewModel: TravelListViewModel by activityViewModels()
    private lateinit var allCurrencyListAdapter: AllCurrencyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCurrencyAddBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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
        allCurrencyListAdapter = AllCurrencyListAdapter(object : AllCurrencyListAdapter.OnItemClickListener {
            override fun onItemClick(currency: Currency) {
                listener.onItemClick(currency)
                dismiss()
            }
        })
        binding.rvCurrency.adapter = allCurrencyListAdapter
        binding.btnClose.setOnClickListener { dismiss() }
    }

}