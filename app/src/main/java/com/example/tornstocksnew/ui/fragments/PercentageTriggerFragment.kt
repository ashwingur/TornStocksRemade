package com.example.tornstocksnew.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tornstocksnew.databinding.FragmentPercentageTriggerBinding
import com.example.tornstocksnew.models.TRIGGER_TYPE
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.utils.TriggerCreator
import com.streamplate.streamplateandroidapp.ui.fragments.CreateEditTriggerFragment
import com.streamplate.streamplateandroidapp.ui.fragments.TRIGGER_PAGE_MODE

class PercentageTriggerFragment : Fragment(), TriggerCreator {

    private lateinit var binding: FragmentPercentageTriggerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    override fun setExistingTriggerData() {

    }

    override fun createTrigger(): Trigger? {
        if (binding.triggerPriceEt.text.isNotEmpty()) {
            val stock = (parentFragment as CreateEditTriggerFragment).stock
            return Trigger(
                TRIGGER_TYPE.PERCENTAGE,
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
            Toast.makeText(requireContext(), "Trigger percentage required", Toast.LENGTH_SHORT).show()
            return null
        }
    }

}