package com.example.tornstocksnew.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tornstocksnew.databinding.FragmentPercentageTriggerBinding
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.models.TRIGGER_TYPE
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.TriggerCreator
import com.streamplate.streamplateandroidapp.ui.fragments.CreateEditTriggerFragment
import com.streamplate.streamplateandroidapp.ui.fragments.TRIGGER_PAGE_MODE
import kotlin.math.absoluteValue

class PercentageTriggerFragment : Fragment(), TriggerCreator {

    private lateinit var binding: FragmentPercentageTriggerBinding
    private var trigger: Trigger? = null
    private var isPlus = true
    private lateinit var stock: Stock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trigger = arguments?.getParcelable(Constants.PARCEL_TRIGGER)
        stock = (parentFragment as CreateEditTriggerFragment).stock!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPercentageTriggerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setExistingTriggerData()
        getTriggerPriceByPercentage()
        setUpButtonGroup()
        setupEt()
    }

    private fun setupEt() {
        binding.triggerPercentageEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                binding.percentagePriceTv.text = "%.2f".format(getTriggerPriceByPercentage())
            }
        })
    }

    private fun setUpButtonGroup() {
        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.plusBtn.id -> {
                        isPlus = true
                    }
                    binding.minusBtn.id -> {
                        isPlus = false
                    }
                }
            }
            binding.percentagePriceTv.text = "%.2f".format(getTriggerPriceByPercentage())
        }
    }

    private fun getTriggerPriceByPercentage(): Float {
        if (binding.triggerPercentageEt.text.isNotEmpty()){
            if (isPlus) {
                return stock.current_price * (1 + binding.triggerPercentageEt.text.toString().toFloat() / 100)
            } else {
                return stock.current_price * (1 - binding.triggerPercentageEt.text.toString().toFloat() / 100)
            }
        } else {
            return stock.current_price
        }
    }

    override fun setExistingTriggerData() {
        trigger?.let {
            binding.apply {
                deleteSwitch.isChecked = it.single_use
                if (it.trigger_percentage >= 0) {
                    triggerPercentageEt.setText(it.trigger_percentage.toString())
                } else {
                    triggerPercentageEt.setText((it.trigger_percentage.absoluteValue.toString()))
                    toggleButton.check(binding.minusBtn.id)
                }
            }
        }
        binding.percentagePriceTv.text = getTriggerPriceByPercentage().toString()
    }

    override fun createTrigger(): Trigger? {
        if (binding.triggerPercentageEt.text.isNotEmpty()) {
            return Trigger(
                TRIGGER_TYPE.PERCENTAGE,
                stock.stock_id,
                stock.current_price,
                stock.name,
                stock.acronym,
                0,
                0F,
                binding.triggerPercentageEt.text.toString().toFloat(),
                binding.deleteSwitch.isChecked,
                TRIGGER_PAGE_MODE.NORMAL
            )
        } else {
            Toast.makeText(requireContext(), "Trigger percentage required", Toast.LENGTH_SHORT)
                .show()
            return null
        }
    }

}