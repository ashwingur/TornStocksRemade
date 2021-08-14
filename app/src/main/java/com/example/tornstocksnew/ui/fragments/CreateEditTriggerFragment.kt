package com.streamplate.streamplateandroidapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.transition.TransitionInflater
import com.example.tornstocksnew.R
import com.example.tornstocksnew.adapters.CreateEditTriggerFragmentAdapter
import com.example.tornstocksnew.databinding.FragmentCreateEditTriggerBinding
import com.example.tornstocksnew.databinding.FragmentSettingsBinding
import com.example.tornstocksnew.databinding.FragmentStocksBinding
import com.example.tornstocksnew.ui.activities.MainActivity
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateEditTriggerFragment : Fragment() {

    private lateinit var binding: FragmentCreateEditTriggerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_up)
        exitTransition = inflater.inflateTransition(R.transition.slide_up)
        (activity as MainActivity).hideBottomNav(true)
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

        setupTabLayout()
    }

    private fun setupTabLayout() {

        val fragmentAdapter = CreateEditTriggerFragmentAdapter(parentFragmentManager, lifecycle)
        binding.viewPager2.adapter = fragmentAdapter

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Basic Trigger"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Basic Trigger"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Basic Trigger"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Basic Trigger"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Basic Trigger"))

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