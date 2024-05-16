package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.base.BaseFragment
import com.lgcns.crossdev.onboarding1.presentation.databinding.FragmentMapPlanBinding
import com.lgcns.crossdev.onboarding1.presentation.util.extension.totalMinToString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class MapPlanFragment : BaseFragment<FragmentMapPlanBinding>(
    R.layout.fragment_map_plan
) {
    private val viewModel: PlanViewModel by activityViewModels()
    private val mapFragment by lazy {
        childFragmentManager.findFragmentById(R.id.fragMap) as SupportMapFragment
    }
    private lateinit var googleMap: GoogleMap


    override fun initView() {
        super.initView()
        binding.viewModel = viewModel
        mapFragment.getMapAsync { p0 ->
            googleMap = p0
            setPolyLines()
        }
    }

    private fun setPolyLines() {
        if(::googleMap.isInitialized) {
            googleMap.clear()
            val filteredPlanList = if(viewModel.selectedDate.value == null) {
                viewModel.planList.value
            }
            else {
                viewModel.planList.value.filter { plan -> plan.date == viewModel.selectedDate.value }
            }

            // 선 그리기
            googleMap.addPolyline(
                PolylineOptions()
                    .addAll(filteredPlanList.map { LatLng(it.locationY, it.locationX) })
                    .width(10f)
                    .color(requireActivity().getColor(R.color.blackAlpha))
                    .startCap(ButtCap())
                    .endCap(ButtCap())
            )

            // 마커 추가
            val markerBitmap = (ContextCompat.getDrawable(requireActivity(), R.drawable.marker) as BitmapDrawable).bitmap
            val markerIcon = Bitmap.createScaledBitmap(markerBitmap, 71, 100, false)
            val boundsBuilder = LatLngBounds.builder()
            filteredPlanList.forEach {
                val time = it.time?.let { time ->
                    totalMinToString(time)
                }

                googleMap.addMarker(MarkerOptions()
                    .position(LatLng(it.locationY, it.locationX))
                    .title(it.nameAlter?: it.name )
                    .snippet(time)
                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon)))
                boundsBuilder.include(LatLng(it.locationY, it.locationX))
            }
            if(filteredPlanList.isNotEmpty()) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100))
            }

        }

    }

    override fun setObserve() {
        lifecycleScope.launch {
            viewModel.selectedDate.collectLatest {
                setPolyLines()

            }
            viewModel.planList.collectLatest {
                setPolyLines()
            }
        }

    }

    companion object {
        const val TAG = "MapPlanFragment"
    }
}