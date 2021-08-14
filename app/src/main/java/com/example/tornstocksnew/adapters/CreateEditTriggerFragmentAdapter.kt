package com.example.tornstocksnew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tornstocksnew.ui.fragments.BasicTriggerFragment

class CreateEditTriggerFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        when (position){
            1 -> { return BasicTriggerFragment()}
            else -> { return BasicTriggerFragment()}
        }
    }
}