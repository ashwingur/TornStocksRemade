package com.example.tornstocksnew.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tornstocksnew.R
import com.example.tornstocksnew.databinding.FragmentTriggersBinding
import com.example.tornstocksnew.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import com.example.tornstocksnew.adapters.TriggersListAdapter
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.models.TRIGGER_TYPE
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.viewmodels.MainActivityViewModel
import com.example.tornstocksnew.viewmodels.TriggersViewModel
import java.util.*


@AndroidEntryPoint
class TriggersFragment : Fragment() {

    private lateinit var binding: FragmentTriggersBinding
    private lateinit var adapter: TriggersListAdapter
    private var mode: TRIGGER_PAGE_MODE = TRIGGER_PAGE_MODE.NORMAL
    private val viewModel: TriggersViewModel by viewModels()
    private lateinit var mainViewModel: MainActivityViewModel

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

        mainViewModel = (activity as MainActivity).mainViewModel

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
                        viewModel.deleteTrigger(trigger)
                    }
                }
                binding.toolbar.visibility = View.GONE
            }
        }
    }

    private fun observeTriggers() {
        viewModel.getAllTriggers().observe(viewLifecycleOwner, {
            Collections.sort(it, Trigger.AlphabeticalAscendingComparator)
            adapter.updateTriggers(it as MutableList)
        })
    }

    private fun initRecyclerView() {
        adapter = TriggersListAdapter(mutableListOf(), requireContext(), TRIGGER_TYPE.DEFAULT)
        observeTriggers()
        binding.triggerRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.triggerRv.adapter = adapter
        adapter.setOnItemClickListener(object : TriggersListAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                if (mode == TRIGGER_PAGE_MODE.DELETE) {
                    Log.d("Triggger", "onClick: clicked item $position")
                    adapter.toggleTriggerMode(position)
                } else if (mode == TRIGGER_PAGE_MODE.NORMAL) {
                    val trigger: Trigger = adapter.triggers[position]
                    val bundle = bundleOf(Constants.PARCEL_TRIGGER to trigger,
                    Constants.PARCEL_STOCK to mainViewModel.getCachedStockById(trigger.stock_id))
                    findNavController().navigate(R.id.action_triggersFragment_to_createEditTriggerFragment, bundle)
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