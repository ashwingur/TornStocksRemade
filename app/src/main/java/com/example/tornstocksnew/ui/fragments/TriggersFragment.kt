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
import com.example.tornstocksnew.utils.Status


@AndroidEntryPoint
class TriggersFragment : Fragment() {

    private lateinit var binding: FragmentTriggersBinding
    private lateinit var adapter: TriggersListAdapter
    private var mode: TRIGGER_PAGE_MODE = TRIGGER_PAGE_MODE.NORMAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.apply {
            cancelBtn.setOnClickListener {
                adapter.updateAllTriggersMode(TRIGGER_PAGE_MODE.NORMAL)
                mode = TRIGGER_PAGE_MODE.NORMAL
                binding.toolbar.visibility = View.GONE
            }
            deleteBtn.setOnClickListener {
                mode = TRIGGER_PAGE_MODE.NORMAL
                for (trigger in adapter.triggers) {
                    if (trigger.mode == TRIGGER_PAGE_MODE.DELETE) {
                        (activity as MainActivity).mainViewModel.deleteTrigger(trigger)
                    }
                }
                observeTriggers()
                binding.toolbar.visibility = View.GONE
            }
        }
    }

    private fun observeTriggers() {
        (activity as MainActivity).mainViewModel.getAllTriggers().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    it.data?.let { it1 -> adapter.updateTriggers(it1) }
                    binding.progressBar.visibility = View.GONE
                }
                Status.ERROR -> {
                }
            }

        })
    }

    private fun initRecyclerView() {
        adapter = TriggersListAdapter(listOf(), requireContext(), TRIGGER_TYPE.DEFAULT)
        observeTriggers()
        binding.triggerRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.triggerRv.adapter = adapter
        Log.d(
            "DATABASE",
            "Livedata: ${(activity as MainActivity).mainViewModel.getAllTriggers().value}"
        )
        adapter.setOnItemClickListener(object : TriggersListAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                if (mode == TRIGGER_PAGE_MODE.DELETE) {
                    adapter.toggleTriggerMode(position)
                } else if (mode == TRIGGER_PAGE_MODE.NORMAL) {

                }
            }

            override fun onLongClick(position: Int): Boolean {
                if (mode != TRIGGER_PAGE_MODE.DELETE) {
                    mode = TRIGGER_PAGE_MODE.DELETE
                    adapter.updateTriggerMode(position, mode)
                    binding.toolbar.visibility = View.VISIBLE
                    return true
                }
                return false
            }

        })
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

enum class TRIGGER_PAGE_MODE {
    NORMAL,
    DELETE
}