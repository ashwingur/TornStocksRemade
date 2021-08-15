package com.streamplate.streamplateandroidapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.tornstocksnew.databinding.FragmentTriggersBinding
import com.example.tornstocksnew.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import com.example.tornstocksnew.adapters.TriggersListAdapter
import com.example.tornstocksnew.models.TRIGGER_TYPE


@AndroidEntryPoint
class TriggersFragment : Fragment() {

    private lateinit var binding: FragmentTriggersBinding
    private lateinit var adapter: TriggersListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTriggersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        startAnimation(view)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = TriggersListAdapter(listOf(), requireContext(), TRIGGER_TYPE.DEFAULT)
        (activity as MainActivity).mainViewModel.getAllTriggers().observe(viewLifecycleOwner, {
            Log.d("DATABASE", "initRecyclerView: ${it}")
            adapter.updateTriggers(it)
        })
        binding.triggerRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.triggerRv.adapter = adapter
        Log.d("DATABASE", "Livedata: ${(activity as MainActivity).mainViewModel.getAllTriggers().value}")
    }

    private fun startAnimation(view: View) {
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateBottomNav()
    }
}