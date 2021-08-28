package com.example.tornstocksnew.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.transition.TransitionInflater
import androidx.viewpager2.widget.ViewPager2
import com.example.tornstocksnew.R
import com.example.tornstocksnew.adapters.CreateEditTriggerFragmentAdapter
import com.example.tornstocksnew.databinding.FragmentCreateEditTriggerBinding
import com.example.tornstocksnew.databinding.FragmentSettingsBinding
import com.example.tornstocksnew.databinding.FragmentStocksBinding
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.models.TRIGGER_TYPE
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.ui.activities.MainActivity
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.TriggerCreator
import com.example.tornstocksnew.viewmodels.MainActivityViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat


@AndroidEntryPoint
class CreateEditTriggerFragment : Fragment() {

    private lateinit var binding: FragmentCreateEditTriggerBinding
    private lateinit var adapter: CreateEditTriggerFragmentAdapter
    var stock: Stock? = null
    private var editTrigger: Trigger? = null
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_up)
        exitTransition = inflater.inflateTransition(R.transition.slide_up)
        (activity as MainActivity).hideBottomNav(true)

        val bundle = arguments
        stock = bundle?.getParcelable(Constants.PARCEL_STOCK)
        editTrigger = bundle?.getParcelable(Constants.PARCEL_TRIGGER)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateEditTriggerBinding.inflate(inflater, container, false)
        (activity as MainActivity).setSupportActionBar(binding.toolbar)
        NavigationUI.setupWithNavController(binding.toolbar, findNavController())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        startAnimation(view)

        mainViewModel = (activity as MainActivity).mainViewModel

        setupStockCard()
        setupTabLayout()
        setupEditMode()
        setupCreateEditBtn()
    }

    private fun setupEditMode() {
        // Do this after setting up tab layout
        editTrigger?.let {
            binding.confirmButton.text = "Edit"
            adapter.setData(it)
        }
    }

    private fun setupCreateEditBtn() {
        binding.confirmButton.setOnClickListener {
            val trigger: Trigger? = (childFragmentManager.findFragmentByTag("f" + binding.viewPager2.currentItem) as? TriggerCreator)?.createTrigger()
            trigger?.let {
                if (editTrigger == null){
                    mainViewModel.insertTrigger(it)
                    Toast.makeText(requireContext(), "Trigger created", Toast.LENGTH_SHORT).show()
                } else {
                    mainViewModel.updateTrigger(it)
                    Toast.makeText(requireContext(), "Trigger edited", Toast.LENGTH_SHORT).show()
                }
                (activity as MainActivity).closeKeyboard()
                findNavController().popBackStack()
            } ?: kotlin.run {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        editTrigger?.let {
            var index = 0
            when (it.trigger_type){
                TRIGGER_TYPE.DEFAULT -> {}
                TRIGGER_TYPE.PERCENTAGE -> {
                    index = 1
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                binding.viewPager2.currentItem = index
            }, 500)
        }
    }


    private fun setupStockCard() {
        binding.stockDetailsCard.stockName.text = stock?.name
        binding.stockDetailsCard.stockPrice.text = stock?.current_price.toString()
        binding.stockDetailsCard.marketCap.text = "Market cap: " + DecimalFormat("#,###").format(stock?.market_cap)
    }

    private fun setupTabLayout() {
        adapter = CreateEditTriggerFragmentAdapter(childFragmentManager, lifecycle)
        binding.viewPager2.adapter = adapter
        //binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Continuous Trigger (Soon)"))

        val tabNames = listOf("Default", "Percentage")

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

    }

    private fun startAnimation(view: View) {
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).hideBottomNav(false)
    }

}