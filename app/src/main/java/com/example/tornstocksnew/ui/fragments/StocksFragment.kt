package com.streamplate.streamplateandroidapp.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.tornstocksnew.R
import com.example.tornstocksnew.adapters.StocksListAdapter
import com.example.tornstocksnew.databinding.FragmentStocksBinding
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.models.StocksResponseObject
import com.example.tornstocksnew.ui.activities.MainActivity
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.Status
import com.example.tornstocksnew.viewmodels.StocksViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class StocksFragment : Fragment() {
    private val TAG = "StocksFragment"

    private lateinit var binding: FragmentStocksBinding
    private val stockViewModel: StocksViewModel by viewModels()
    private lateinit var adapter: StocksListAdapter
    private var cachedStocks: MutableList<Stock> = mutableListOf()

    private val STOCK_UPDATE_DELAY = 60000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        TransitionInflater.from(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStocksBinding.inflate(inflater, container, false)
        binding.toolbar.title = "Stocks"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        startAnimation(view)

        setupToolbar()
        setupRecyclerView()
        setupPeriodicApiCall()
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.stocks_menu_concise -> {
                    Toast.makeText(requireContext(), "Concise", Toast.LENGTH_SHORT).show()
                }
                R.id.stocks_menu_default -> {
                    Toast.makeText(requireContext(), "Default", Toast.LENGTH_SHORT).show()
                }
                R.id.stocks_menu_detailed -> {
                    Toast.makeText(requireContext(), "Detailed", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }


    private fun setupPeriodicApiCall() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                activity.let {
                    getStockData()
                    mainHandler.postDelayed(this, STOCK_UPDATE_DELAY)
                }
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = StocksListAdapter((activity as MainActivity).cachedStocks, requireContext())
        adapter.setOnItemClickListener(object : StocksListAdapter.OnItemClickListener{
            override fun onClick(position: Int) {
                val bundle = bundleOf()
                findNavController().navigate(R.id.action_stocksFragment_to_createEditTriggerFragment, bundle)
            }

        })
        binding.stocksRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.stocksRv.adapter = adapter
    }

    private fun getStockData() {
        Constants.API_KEY?.let {
            stockViewModel.getStocks(it).observe(requireActivity(), Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        val response: StocksResponseObject? = it.data
                        binding.progressBar.visibility = View.GONE
                        if (it.data?.error != null) {
                            Toast.makeText(
                                requireContext(),
                                "Torn API error ${response?.error?.code}: ${response?.error?.warning}",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.errorTv.visibility = View.VISIBLE
                        } else {
                            cachedStocks = it.data?.stocks?.values?.toMutableList()!!
                            Collections.sort(cachedStocks, Stock.PriceDescendingComparator)
                            binding.errorTv.visibility = View.GONE
                            adapter.updateStocks(cachedStocks)
                        }
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        Log.d(TAG, "printStockData: ${it.message}")
                        Toast.makeText(
                            requireContext(),
                            "Error retrieving stock data: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } ?: kotlin.run {
            Toast.makeText(requireContext(), "Enter an Api key in settings", Toast.LENGTH_SHORT)
                .show()
        }
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

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).cachedStocks = cachedStocks
    }

}

