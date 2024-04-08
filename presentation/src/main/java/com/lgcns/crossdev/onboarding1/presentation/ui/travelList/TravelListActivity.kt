package com.lgcns.crossdev.onboarding1.presentation.ui.travelList

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import com.lgcns.crossdev.onboarding1.presentation.R
import com.lgcns.crossdev.onboarding1.presentation.base.BaseActivity
import com.lgcns.crossdev.onboarding1.presentation.databinding.ActivityTravelListBinding
import com.lgcns.crossdev.onboarding1.presentation.ui.plan.PlanActivity
import com.lgcns.crossdev.onboarding1.presentation.dialog.BaseDialog
import com.lgcns.crossdev.onboarding1.presentation.dialog.TravelAddDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class TravelListActivity : BaseActivity<ActivityTravelListBinding>(
    R.layout.activity_travel_list
) {
    private val viewModel: TravelListViewModel by viewModels()
    private lateinit var travelListAdapter: TravelListAdapter
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

    }

    override fun initView() {
        setupListAdapter()

        binding.btnAddTravel.setOnClickListener {
            val dialog = TravelAddDialog.Builder(this@TravelListActivity, viewModel)
            dialog.create().show()
        }

        binding.appBar.addOnOffsetChangedListener { _, verticalOffset: Int ->
            binding.tvSubTitle.alpha =
                if (verticalOffset == 0) 1f else if (verticalOffset < -200) 0f else (-20f / verticalOffset)
        }
        binding.btnSetting.setOnClickListener { binding.drawerLayout.openDrawer(binding.settingView) }
        binding.settingView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.btnRefreshCurrency -> {
                    val dialog = BaseDialog.Builder(this@TravelListActivity).create()
                    dialog.setTitle(getString(R.string.currency_load_label))
                        .setMessage("${getString(R.string.updated_at)} ${viewModel.getLatestCurrencyLoadDate()}")
                        .setOkButton(getString(R.string.update_label)) {
                            dialog.dismissDialog()
                            viewModel.loadRemoteCurrency()
                        }
                        .show()

                }
                R.id.btnRate -> {
                    try {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        )
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        )
                        startActivity(intent)
                    }
                }
                R.id.btnVersionInfo -> {
                    val dialog = BaseDialog.Builder(this@TravelListActivity).create()
                    dialog.setTitle(getString(R.string.version_info_label))
                        .setMessage("Ver ${packageManager.getPackageInfo(packageName, 0).versionName}")
                        .setOkButton(getString(R.string.close_label)) { dialog.dismissDialog() }
                        .show()
                }
            }
            binding.drawerLayout.closeDrawer(binding.settingView)
            true
        }
    }

    override fun setObserve() {
        lifecycleScope.launch {
            viewModel.status.collectLatest {
                if(it == TravelListViewModel.LoadStatus.LOADING) {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                else if (it == TravelListViewModel.LoadStatus.DONE) {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(this@TravelListActivity, getString(R.string.currency_load_done_msg), Toast.LENGTH_SHORT).show()
                }
                else if(it == TravelListViewModel.LoadStatus.ERROR) {
                    binding.pbLoading.visibility = View.GONE
                }
            }
        }
        lifecycleScope.launch {
            viewModel.errorMessage.collectLatest {
                if(it.isNotEmpty()) {
                    Toast.makeText(this@TravelListActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun setupListAdapter() {
        travelListAdapter = TravelListAdapter(object : TravelListAdapter.OnItemClickListener {
           override fun onItemClick(travel: Travel) {
               val intent = Intent(this@TravelListActivity, PlanActivity::class.java)
               intent.putExtra("travelId", travel.id)
               startActivity(intent)
           }

           override fun onEditClick(travel: Travel) {
               val dialog = TravelAddDialog.Builder(this@TravelListActivity, viewModel)
               dialog.create(travel).show()
           }

           override fun onDeleteClick(travel: Travel) {
               val dialog = BaseDialog.Builder(this@TravelListActivity)
               dialog.create()
                   .setTitle(getString(R.string.delete_label))
                   .setMessage(getString(R.string.delete_confirm_msg))
                   .setOkButton {
                       viewModel.deleteTravel(travel)
                       dialog.dismissDialog()
                   }
                   .show()
           }
        })
        binding.rvTravel.adapter = travelListAdapter
    }


}