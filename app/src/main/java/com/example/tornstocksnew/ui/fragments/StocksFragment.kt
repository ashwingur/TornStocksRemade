package com.example.tornstocksnew.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.tornstocksnew.R
import com.example.tornstocksnew.adapters.ConciseStocksAdapter
import com.example.tornstocksnew.adapters.StocksListAdapter
import com.example.tornstocksnew.databinding.FragmentStocksBinding
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.models.StocksResponseObject
import com.example.tornstocksnew.ui.activities.MainActivity
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.Status
import com.example.tornstocksnew.utils.StocksDisplayPreference
import com.example.tornstocksnew.utils.Utils
import com.example.tornstocksnew.viewmodels.MainActivityViewModel
import com.example.tornstocksnew.viewmodels.StocksViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class StocksFragment : Fragment() {
    private val TAG = "StocksFragment"

    private lateinit var binding: FragmentStocksBinding
    private val stockViewModel: StocksViewModel by viewModels()
    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var defaultAdapter: StocksListAdapter
    private lateinit var conciseAdapter: ConciseStocksAdapter

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

        stockViewModel.loadStocksDisplayPreference()
        mainViewModel = (activity as MainActivity).mainViewModel


        setupToolbar()
        setupRecyclerView()
        //setupConciseRecyclerView()
        setupPeriodicApiCall()
    }

    private fun setupRecyclerView() {
        when (Constants.STOCKS_DISPLAY_PREFERENCE){
            StocksDisplayPreference.DETAILED -> {
                setupDefaultRecyclerView()
            }
            StocksDisplayPreference.CONCISE -> {
                setupConciseRecyclerView()
            }
            StocksDisplayPreference.DEFAULT -> {
                setupDefaultRecyclerView()
            }
        }
    }

    private fun updateRecyclerViewAdapter(){
        when (Constants.STOCKS_DISPLAY_PREFERENCE){
            StocksDisplayPreference.DETAILED -> {
                defaultAdapter.updateStocks(mainViewModel.cachedStocks)
            }
            StocksDisplayPreference.CONCISE -> {
                conciseAdapter.updateStocks(mainViewModel.cachedStocks)
            }
            StocksDisplayPreference.DEFAULT -> {
                defaultAdapter.updateStocks(mainViewModel.cachedStocks)
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.stocks_menu_concise -> {
                    stockViewModel.saveStocksDisplayPreference(StocksDisplayPreference.CONCISE)
                }
                R.id.stocks_menu_default -> {
                    stockViewModel.saveStocksDisplayPreference(StocksDisplayPreference.DEFAULT)
                }
                R.id.stocks_menu_detailed -> {
                    stockViewModel.saveStocksDisplayPreference(StocksDisplayPreference.DETAILED)
                    Toast.makeText(requireContext(), "Detailed view currently unavailable", Toast.LENGTH_SHORT).show()
                }
            }
            setupRecyclerView()
            true
        }
    }


    private fun setupPeriodicApiCall() {
        // Based on the main activity, so unecessary calls arent made if the stock fragment keeps getting created
        mainViewModel.refreshStockBool.observe(viewLifecycleOwner , {
            if (it == true){
                getStockData()
                mainViewModel.refreshStockBool.value = false
            }
        })
    }

    private fun setupDefaultRecyclerView() {
        defaultAdapter = StocksListAdapter(mainViewModel.cachedStocks, requireContext())
        defaultAdapter.setOnItemClickListener(object : StocksListAdapter.OnItemClickListener{
            override fun onClick(position: Int) {
                val bundle = bundleOf(Constants.PARCEL_STOCK to mainViewModel.cachedStocks[position])
                findNavController().navigate(R.id.action_stocksFragment_to_createEditTriggerFragment, bundle)
            }
        })
        binding.stocksRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.stocksRv.adapter = defaultAdapter
        updateRecyclerViewAdapter()
    }

    private fun setupConciseRecyclerView(){
        conciseAdapter = ConciseStocksAdapter(mainViewModel.cachedStocks, requireContext())
        conciseAdapter.setOnItemClickListener(object : ConciseStocksAdapter.OnItemClickListener{
            override fun onClick(position: Int) {
                val bundle = bundleOf(Constants.PARCEL_STOCK to mainViewModel.cachedStocks[position])
                findNavController().navigate(R.id.action_stocksFragment_to_createEditTriggerFragment, bundle)
            }

        })
        binding.stocksRv.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.stocksRv.adapter = conciseAdapter
    }

    private fun getStockData() {
        Constants.API_KEY?.let {
            (activity as? MainActivity)?.mainViewModel?.getStocks(it)?.observe(this, Observer {
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
                            mainViewModel.cachedStocks = it.data?.stocks?.values?.toMutableList()!!
                            Collections.sort(mainViewModel.cachedStocks, Stock.PriceDescendingComparator)
                            binding.errorTv.visibility = View.GONE
                            updateRecyclerViewAdapter()
                        }
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        if (!Utils.isNetworkAvailable(requireContext())){
                            Toast.makeText(requireContext(), "Network connection unavailable", Toast.LENGTH_LONG).show()
                        }
                        Log.d(TAG, "printStockData: ${it.message}")
                        Toast.makeText(
                            requireContext(),
                            "Error retrieving stock data: ${it.message}",
                            Toast.LENGTH_LONG
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

}

