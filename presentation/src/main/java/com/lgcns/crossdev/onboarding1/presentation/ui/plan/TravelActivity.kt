package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.forEach
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.libraries.places.api.Places
import com.lgcns.crossdev.onboarding1.presentation.BuildConfig
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.base.BaseActivity
import com.lgcns.crossdev.onboarding1.presentation.databinding.ActivityPlanBinding
import com.lgcns.crossdev.onboarding1.presentation.ui.money.MoneyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelActivity : BaseActivity<ActivityPlanBinding>(
    R.layout.activity_plan
) {
    private val planViewModel: PlanViewModel by viewModels()
    private val moneyViewModel: MoneyViewModel by viewModels()
    private val navController by lazy { findNavController(R.id.nav_host_fragment_content_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY);
        }
    }

    override fun loadData() {
        val travelId = intent.getLongExtra("travelId", -1)
        planViewModel.setTravelId(travelId)
        planViewModel.getTravelByTravelId()
        moneyViewModel.setTravelId(travelId)
        moneyViewModel.getTravelByTravelId()
    }

    override fun onSupportNavigateUp(): Boolean {
        return if(navController.currentDestination?.id == R.id.nav_plan) {
            finish()
            true
        }
        else {
            super.onSupportNavigateUp()
        }
    }

    override fun initView() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.setOnMenuItemClickListener {
            if(it.itemId == R.id.menuMoney) {
                val action = PlanFragmentDirections.actionNavPlanToNavMoney()
                navController.navigate(action)
            }
            else if(it.itemId == android.R.id.home) {
                finish()
            }
            false
        }
    }

    fun setToolbarTitle(title: String) {
        binding.toolbar.title = title
    }

    fun setToolbarMenuVisible(visible: Boolean) {
        if(visible) {
            binding.toolbar.menu.forEach { it.isVisible = true }
        }
        else {
            binding.toolbar.menu.forEach { it.isVisible = false }
        }
    }
}