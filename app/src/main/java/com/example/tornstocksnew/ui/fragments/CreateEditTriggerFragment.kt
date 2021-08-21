package com.streamplate.streamplateandroidapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.ui.activities.MainActivity
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.TriggerCreator
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat


@AndroidEntryPoint
class CreateEditTriggerFragment : Fragment() {

    private lateinit var binding: FragmentCreateEditTriggerBinding
    var stock: Stock? = null
    private lateinit var adapter: CreateEditTriggerFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_up)
        exitTransition = inflater.inflateTransition(R.transition.slide_up)
        (activity as MainActivity).hideBottomNav(true)

        val bundle = arguments
        stock = bundle?.getParcelable(Constants.PARCEL_STOCK)
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

        setupStockCard()
        setupTabLayout()
        setupCreateEditBtn()
    }

    private fun setupCreateEditBtn() {
        binding.confirmButton.setOnClickListener {
            val trigger: Trigger? = (childFragmentManager.findFragmentByTag("f" + binding.viewPager2.currentItem) as? TriggerCreator)?.createTrigger()
            trigger?.let {
                (activity as MainActivity).mainViewModel.insertTrigger(trigger)
                Toast.makeText(requireContext(), "Trigger created", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
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

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Default"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Percentage (Soon)"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Continuous Trigger (Soon)"))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //binding.viewPager2.currentItem = tab!!.position
                binding.viewPager2.currentItem = 0
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
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