package com.example.tornstocksnew.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tornstocksnew.R
import com.example.tornstocksnew.databinding.FragmentBasicTriggerBinding

class BasicTriggerFragment : Fragment() {

    private lateinit var binding: FragmentBasicTriggerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBasicTriggerBinding.inflate(inflater, container, false)
        return binding.root
    }

}