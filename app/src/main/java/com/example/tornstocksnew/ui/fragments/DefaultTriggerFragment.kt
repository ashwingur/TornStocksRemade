package com.example.tornstocksnew.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tornstocksnew.databinding.FragmentDefaultTriggerBinding
import com.example.tornstocksnew.models.TRIGGER_TYPE
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.TriggerCreator
import com.streamplate.streamplateandroidapp.ui.fragments.CreateEditTriggerFragment
import com.streamplate.streamplateandroidapp.ui.fragments.TRIGGER_PAGE_MODE

class DefaultTriggerFragment : Fragment(), TriggerCreator {

    private lateinit var binding: FragmentDefaultTriggerBinding
    private var trigger: Trigger? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        trigger = arguments?.getParcelable(Constants.PARCEL_TRIGGER)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDefaultTriggerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setExistingTriggerData()
    }

    override fun setExistingTriggerData() {
        trigger?.let {
            binding.apply {
                triggerPriceEt.setText(it.trigger_price.toString())
                deleteSwitch.isChecked = it.single_use
            }
        }
    }

    override fun createTrigger(): Trigger? {
        if (binding.triggerPriceEt.text.isNotEmpty()) {
            val stock = (parentFragment as CreateEditTriggerFragment).stock
            trigger?.let {
                it.stock_price = stock?.current_price!!
                it.trigger_price = binding.triggerPriceEt.text.toString().toFloat()
                it.single_use = binding.deleteSwitch.isChecked
                it.trigger_type = TRIGGER_TYPE.DEFAULT
                return it
            }
            return Trigger(
                TRIGGER_TYPE.DEFAULT,
                stock?.stock_id!!,
                stock.name,
                stock.acronym,
                0,
                binding.triggerPriceEt.text.toString().toFloat(),
                binding.deleteSwitch.isChecked,
                stock.current_price,
                TRIGGER_PAGE_MODE.NORMAL
            )
        } else {
            Toast.makeText(requireContext(), "Trigger price required", Toast.LENGTH_SHORT).show()
            return null
        }
    }

}