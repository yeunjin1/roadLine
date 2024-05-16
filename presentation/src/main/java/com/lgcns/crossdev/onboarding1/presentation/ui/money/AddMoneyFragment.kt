package com.lgcns.crossdev.onboarding1.presentation.ui.money

import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Money
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.base.BaseFragment
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentAddMoneyBinding
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentMoneyBinding
import com.lgcns.crossdev.onboarding1.presentation.dialog.BaseDialog
import com.lgcns.crossdev.onboarding1.presentation.dialog.PicAddDialog
import com.lgcns.crossdev.onboarding1.presentation.dialog.PicAddDialogListener
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.AddPlanFragmentArgs
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.DateListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.TravelActivity
import com.lgcns.crossdev.onboarding1.presentation.util.extension.toFormatPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate

@AndroidEntryPoint
class AddMoneyFragment : BaseFragment<FragmentAddMoneyBinding>(
    R.layout.fragment_add_money
) {
    private val viewModel: AddMoneyViewModel by viewModels()
    private val moneyViewModel: MoneyViewModel by activityViewModels()
    private val picClickListener = View.OnClickListener {
        val dialog = PicAddDialog(object : PicAddDialogListener {
            override fun onPictureTake(uri: Uri, file: File) {
                viewModel.setMoney(img = uri.toString())
                Glide.with(it.context)
                    .load(uri)
                    .into(binding.ivAddMoney)
            }

            override fun onGalleryGet(uri: Uri) {
                viewModel.setMoney(img = uri.toString())
                Glide.with(it.context)
                    .load(uri)
                    .into(binding.ivAddMoney)
            }
        })
        dialog.show(requireActivity().supportFragmentManager, "PicAddDialog")
    }
    private lateinit var selectedCurrency: Currency
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    var exchange = 0.0

    override fun initView() {
        val args = arguments?.let {
            AddMoneyFragmentArgs.fromBundle(it)
        }
        viewModel.initMoney(args?.money!!)
        binding.viewModel = viewModel

        (requireActivity() as TravelActivity).setToolbarMenuVisible(false)

        // 가계부 사진
        binding.ivAddMoney.setOnClickListener(picClickListener)
        binding.tvAddMoney.setOnClickListener(picClickListener)
        Glide.with(binding.root.context)
            .load(viewModel.money.value.img)
            .into(binding.ivAddMoney)

        // 화폐 스피너
        lifecycleScope.launch {
            moneyViewModel.currencyList.collectLatest { currencyList ->
                val currencyCodesArr = currencyList.map { currency ->  currency.code }
                spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, currencyCodesArr)
                binding.spCurrency.adapter = spinnerAdapter
            }
        }

        binding.spCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCurrency = moneyViewModel.currencyList.value[0]
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCurrency = moneyViewModel.currencyList.value[position]
                if(binding.tvPrice.text.isNotEmpty()){
                    val exchange = selectedCurrency.rate?.times(binding.tvPrice.text.toString().toDouble())
                    binding.tvExchange.text = "${exchange?.toFormatPrice(selectedCurrency.code)}원"
                }
            }
        }
        if(spinnerAdapter.count == 1){
            binding.spCurrency.isEnabled = false
        }

        // 확인 버튼
        binding.btnConfirm.setOnClickListener {
            if(binding.tvPrice.text.toString().isEmpty()) {
                val dialog = BaseDialog.Builder(requireContext()).create()
                dialog.setTitle(getString(R.string.alarm_label))
                    .setMessage(getString(R.string.input_money_price_guide))
                    .setOkButton(getString(R.string.confirm_label)) {
                        dialog.dismissDialog()
                    }
                    .show()
            }
            else {
                viewModel.setMoney(price = binding.tvPrice.text.toString().toDouble()
                    , korPrice = exchange
                    , category = getCategoryId(binding.rgCategory.checkedRadioButtonId)
                    , currencyCode = selectedCurrency.code
                )
                viewModel.addMoney()
                findNavController().popBackStack()
            }
        }

        // 카테고리
        when(viewModel.money.value.category) {
            0 -> binding.rgCategory.check(R.id.btnMeal)
            1 -> binding.rgCategory.check(R.id.btnShopping)
            2 -> binding.rgCategory.check(R.id.btnTrans)
            3 -> binding.rgCategory.check(R.id.btnTour)
            4 -> binding.rgCategory.check(R.id.btnLodging)
            else -> binding.rgCategory.check(R.id.btnEtc)
        }

        // 가격 입력
        binding.tvPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(::selectedCurrency.isInitialized) {
                    if (s.toString().isEmpty()) {
                        binding.tvExchange.visibility = View.INVISIBLE
                    } else {
                        binding.tvExchange.visibility = View.VISIBLE
                        exchange = selectedCurrency.rate?.times(s.toString().toDouble()) ?: 0.0
                        binding.tvExchange.text = "${exchange.toFormatPrice("KRW")}원"
                    }
                }
            }
        })


    }


    private fun getCategoryId(checkedId: Int): Int? {
        return when(checkedId) {
            R.id.btnMeal -> 0
            R.id.btnShopping -> 1
            R.id.btnTrans -> 2
            R.id.btnTour -> 3
            R.id.btnLodging -> 4
            R.id.btnEtc -> 5
            else -> null
        }
    }
}