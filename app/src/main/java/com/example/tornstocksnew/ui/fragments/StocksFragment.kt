package com.streamplate.streamplateandroidapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import com.example.tornstocksnew.databinding.FragmentStocksBinding
import com.example.tornstocksnew.ui.activities.MainActivity
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.Status
import com.example.tornstocksnew.viewmodels.StocksViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StocksFragment : Fragment() {
    private val TAG = "StocksFragment"

    private lateinit var binding: FragmentStocksBinding
    private val stockViewModel: StocksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStocksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        startAnimation(view)

        printStockData()
    }

    private fun printStockData() {
        stockViewModel.getStocks(Constants.TEST_API).observe(requireActivity(), Observer {
            when (it.status){
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "${it.data}", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> { binding.progressBar.visibility = View.VISIBLE }
                Status.ERROR -> {
                    Log.d(TAG, "printStockData: ${it.message}")
                }
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