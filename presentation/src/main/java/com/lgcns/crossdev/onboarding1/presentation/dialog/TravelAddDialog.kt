package com.lgcns.crossdev.onboarding1.presentation.dialog

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.lgcns.crossdev.onboarding1.domain.model.Currency
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.databinding.DialogTravelAddBinding
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.AllCurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.CurrencyListAdapter
import com.lgcns.crossdev.onboarding1.presentation.ui.travelList.TravelListViewModel
import com.lgcns.crossdev.onboarding1.presentation.util.adapter.ViewBindingAdapter.setVisible
import com.lgcns.crossdev.onboarding1.presentation.util.extension.getSerializable
import com.lgcns.crossdev.onboarding1.presentation.util.extension.periodToString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate


class TravelAddDialog: DialogFragment() {
    private lateinit var binding: DialogTravelAddBinding
    private val viewModel: TravelListViewModel by activityViewModels()
    private var travel: Travel? = null

    // 다이얼로그에서 선택된 값
    private var dateStart: LocalDate? = null
    private var dateEnd: LocalDate? = null
    private var coverImg: String? = null

    private lateinit var currencyListAdapter: CurrencyListAdapter

    private val picClickListener = View.OnClickListener {
        val dialog = PicAddDialog(object : PicAddDialogListener {
            override fun onPictureTake(uri: Uri, file: File) {
                binding.ivThumbnail.setVisible(true)
                binding.tvThumbnailGuide.setVisible(false)
                coverImg = uri.toString()
                Glide.with(it.context)
                    .load(uri)
                    .into(binding.ivThumbnail)
            }

            override fun onGalleryGet(uri: Uri) {
                binding.ivThumbnail.setVisible(true)
                binding.tvThumbnailGuide.setVisible(false)
                coverImg = uri.toString()
                Glide.with(it.context)
                    .load(uri)
                    .into(binding.ivThumbnail)
            }
        })
        dialog.show(requireActivity().supportFragmentManager, "PicAddDialog")
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
        binding = DialogTravelAddBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        travel = arguments?.getSerializable<Travel>("travel")?.copy()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencyListAdapter = CurrencyListAdapter(object : CurrencyListAdapter.OnItemClickListener {
            override fun onItemClick(currencyCode: String) {
                if (currencyCode == "+") { //추가

                    val dialog = CurrencyAddDialog(object : AllCurrencyListAdapter.OnItemClickListener {
                        override fun onItemClick(currency: Currency) {
                            viewModel.addSelectedCurrencyCodes(currency.code)
                        }
                    })
                    dialog.show(requireActivity().supportFragmentManager, "CurrencyAddDialog")
                }
                else { //삭제
                    val dialog = BaseDialog.Builder(requireContext())
                    dialog.create()
                        .setTitle(getString(R.string.delete_label))
                        .setMessage(getString(R.string.delete_confirm_msg))
                        .setOkButton {
                            viewModel.removeSelectedCurrencyCodes(currencyCode)
                            dialog.dismissDialog()
                        }
                        .show()
                }
            }
        })
        binding.rvCurrency.adapter = currencyListAdapter

        if(travel == null) { //여행 추가
            viewModel.setSelectedCurrencyCodes(emptyList())
            binding.tvDialogTitle.text = getString(R.string.add_travel_label)
        }
        else { //여행 수정
            binding.travel = travel
            dateStart = travel!!.dateStart
            dateEnd = travel!!.dateEnd
            coverImg = travel!!.img
            viewModel.setSelectedCurrencyCodes(travel!!.currencyCodes)
            binding.tvDialogTitle.text = getString(R.string.edit_travel_label)
            Glide.with(requireContext())
                .load(coverImg)
                .into(binding.ivThumbnail)
        }

        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val size = Point()
        dialog!!.window!!.windowManager.defaultDisplay.getSize(size)
        dialog!!.window!!.attributes.let{
            it.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            it.width = (size.x * 0.872f).toInt()
        }

        binding.btnClose.setOnClickListener { dismiss() }

        binding.btnOk.setOnClickListener {
            val title = binding.tvTravelTitle.text.toString()
            val guideDialog = BaseDialog.Builder(requireContext()).create().setTitle(getString(R.string.alarm_label))
            if(title.isEmpty())
                guideDialog.setMessage(getString(R.string.input_title_guide))
                    .setOkButton(getString(R.string.close_label)) {guideDialog.dismissDialog() }
                    .show()
            else if(dateStart == null || dateEnd == null)
                guideDialog.setMessage(getString(R.string.input_date_guide))
                    .setOkButton(getString(R.string.close_label)) { guideDialog.dismissDialog() }
                    .show()
            else {
                val currencyCodes = viewModel.selectedCurrencyCodes.value.toMutableList()
                currencyCodes.remove("+")
                if(travel == null) {
                    viewModel.insertTravel(
                        Travel(title = title, dateStart = dateStart!!, dateEnd = dateEnd!!, img = coverImg, currencyCodes = currencyCodes)
                    )
                } else {
                    travel!!.title = title
                    travel!!.dateStart = dateStart!!
                    travel!!.dateEnd = dateEnd!!
                    travel!!.currencyCodes = currencyCodes
                    travel!!.img = coverImg
                    viewModel.insertTravel(travel!!)
//                    viewModel.updateTravel(travel!!)
                }
                dismiss()
            }
        }

        binding.tvDate.setOnClickListener {
            makeCalendarDialog()
        }

        binding.ivThumbnail.setOnClickListener(picClickListener)
        binding.tvThumbnailGuide.setOnClickListener(picClickListener)

    }

    private fun makeCalendarDialog() {
        val calendarDialog = CalendarDialog.Builder(requireContext()).create()
        calendarDialog.setButtonOk{
            dateStart = calendarDialog.getStartDate()
            dateEnd = calendarDialog.getEndDate()
            calendarDialog.dismissDialog()
            if (dateStart != null && dateEnd != null) {
                binding.tvDate.text = periodToString(dateStart!!, dateEnd!!)
            }
        }
        dateStart?.let { calendarDialog.setStartDate(dateStart!!) }
        dateEnd?.let { calendarDialog.setEndDate(dateEnd!!) }
        calendarDialog.show()
    }
}