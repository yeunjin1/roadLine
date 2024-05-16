package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.base.BaseFragment
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentAddPlanBinding
import com.lgcns.crossdev.onboarding1.presentation.dialog.BaseDialog
import com.lgcns.crossdev.onboarding1.presentation.dialog.TimePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddPlanFragment : BaseFragment<FragmentAddPlanBinding>(
    R.layout.fragment_add_plan
) {
    private val viewModel: AddPlanViewModel by viewModels()
    private val autoCompleteFragment by lazy {
        childFragmentManager.findFragmentById(R.id.fragSearch) as AutocompleteSupportFragment
    }
    private val addMapFragment by lazy {
        childFragmentManager.findFragmentById(R.id.fragMap) as SupportMapFragment
    }
    private lateinit var googleMap: GoogleMap
    private lateinit var markerIcon: Bitmap

    override fun initView() {
        (requireActivity() as TravelActivity).setToolbarMenuVisible(false)

        val markerBitmap = (ContextCompat.getDrawable(requireActivity(), R.drawable.marker) as BitmapDrawable).bitmap
        markerIcon = Bitmap.createScaledBitmap(markerBitmap, 71, 100, false)

        val args = arguments?.let {
            AddPlanFragmentArgs.fromBundle(it)
        }
        viewModel.initPlan(args?.plan!!)
        binding.viewModel = viewModel
        if(args.plan!!.id == null) {
            (requireActivity() as TravelActivity).setToolbarTitle(getString(R.string.plan_add_label))
        }
        else {
            (requireActivity() as TravelActivity).setToolbarTitle(getString(R.string.plan_edit_label))
        }


        autoCompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Timber.tag(TAG).d("Place: ${place.name}, ${place.id}, ${place.latLng}")
                place.latLng?.let { latLng ->
                    viewModel.setPlan(locationX = latLng.longitude
                        , locationY = latLng.latitude
                        , name = place.name
                        , nameAlter = place.name)
                }
                setPlaceOnMap()
            }

            override fun onError(status: Status) {
                Timber.tag(TAG).e("An error occurred: $status")
            }
        })

        addMapFragment.getMapAsync { p0 ->
            googleMap = p0
            if(viewModel.plan.value.id == null) { // 추가
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(DEFAULT_LOCATION_Y, DEFAULT_LOCATION_X),12f))

            }
            else{
                setPlaceOnMap()
            }
        }

        binding.btnConfirm.setOnClickListener {
            if(viewModel.plan.value.name.isEmpty()) {
                val dialog = BaseDialog.Builder(requireContext()).create()
                dialog.setTitle(getString(R.string.alarm_label))
                    .setMessage(getString(R.string.input_plan_place_guide))
                    .setOkButton(getString(R.string.confirm_label)) {
                        dialog.dismissDialog()
                    }
                    .show()
            }
            else {
                viewModel.addPlan()
                findNavController().popBackStack()
            }
        }

        binding.tvTime.setOnClickListener {
            val dialog = TimePickerDialog(viewModel.plan.value.time, object : TimePickerDialog.OnTimeSelectedListener {
                override fun onTimeSelected(time: Int) {
                    viewModel.setPlan(time = time)
                }
            })
            dialog.show(requireActivity().supportFragmentManager, "TimePickerDialog")
        }
    }

    private fun setPlaceOnMap() {
        if(::googleMap.isInitialized) {
            googleMap.clear()
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(viewModel.plan.value.locationY, viewModel.plan.value.locationX),12f))
            googleMap.addMarker(MarkerOptions().position(LatLng(viewModel.plan.value.locationY, viewModel.plan.value.locationX)).icon(BitmapDescriptorFactory.fromBitmap(markerIcon)))
        }
    }


    companion object {
        const val TAG = "AddPlanFragment"
        const val DEFAULT_LOCATION_X = 126.984719
        const val DEFAULT_LOCATION_Y = 37.552420
    }
}