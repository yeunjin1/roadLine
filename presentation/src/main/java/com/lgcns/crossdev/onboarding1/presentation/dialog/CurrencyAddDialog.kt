package com.lgcns.crossdev.onboarding1.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.databinding.DialogCurrencyAddBinding
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.AllCurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.CurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.TravelListViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CurrencyAddDialog(context: Context): Dialog(context) {

    val binding: DialogCurrencyAddBinding by lazy {
        DialogCurrencyAddBinding.inflate(LayoutInflater.from(context))
    }

    class Builder(private val mContext: Context, private val listener: AllCurrencyListAdapter.OnItemClickListener) {
        private val dialog = CurrencyAddDialog(mContext)
        private lateinit var allCurrencyListAdapter: AllCurrencyListAdapter

        fun create(currencyList: List<Currency>): Builder {
            dialog.create()
            dialog.setContentView(dialog.binding.root)
            dialog.binding.currencyList = currencyList

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val size = Point()
            dialog.window!!.windowManager.defaultDisplay.getSize(size)
            dialog.window!!.attributes.let{
                it.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                it.width = (size.x * 0.872f).toInt()
            }

            allCurrencyListAdapter = AllCurrencyListAdapter(object : AllCurrencyListAdapter.OnItemClickListener {
                override fun onItemClick(currency: Currency) {
                    listener.onItemClick(currency)
                    dismissDialog()
                }
            })
            dialog.binding.rvCurrency.adapter = allCurrencyListAdapter

            dialog.binding.btnClose.setOnClickListener { dismissDialog() }


            return this
        }

        private fun dismissDialog() {
            dialog.dismiss()
        }

        fun show(): CurrencyAddDialog {
            dialog.show()
            return dialog
        }
    }
}