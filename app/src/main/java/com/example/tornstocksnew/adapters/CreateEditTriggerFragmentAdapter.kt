package com.example.tornstocksnew.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.ui.fragments.DefaultTriggerFragment
import com.example.tornstocksnew.ui.fragments.PercentageTriggerFragment
import com.example.tornstocksnew.utils.Constants

class CreateEditTriggerFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var editTrigger: Trigger? = null

    fun setData(trigger: Trigger){
        editTrigger = trigger
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val fragment = DefaultTriggerFragment()
                fragment.arguments = bundleOf(Constants.PARCEL_TRIGGER to editTrigger)
                return fragment
            }
            1 -> {
                val fragment = PercentageTriggerFragment()
                fragment.arguments = bundleOf(Constants.PARCEL_TRIGGER to editTrigger)
                return fragment
            }
            else -> {
                val fragment = DefaultTriggerFragment()
                fragment.arguments = bundleOf(Constants.PARCEL_TRIGGER to editTrigger)
                return fragment
            }
        }
    }
}