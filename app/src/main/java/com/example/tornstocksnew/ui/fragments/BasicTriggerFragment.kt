package com.example.tornstocksnew.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tornstocksnew.R
import com.example.tornstocksnew.databinding.FragmentBasicTriggerBinding
import com.example.tornstocksnew.models.TRIGGER_TYPE
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.utils.TriggerCreator
import com.streamplate.streamplateandroidapp.ui.fragments.CreateEditTriggerFragment

class BasicTriggerFragment : Fragment(), TriggerCreator {

    private lateinit var binding: FragmentBasicTriggerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBasicTriggerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun createTrigger(): Trigger? {
        if (binding.triggerPriceEt.text.isNotEmpty()) {
            val stock = (parentFragment as CreateEditTriggerFragment).stock
            return Trigger(
                TRIGGER_TYPE.DEFAULT,
                stock?.stock_id!!,
                stock.name,
                stock.acronym,
                0,
                binding.triggerPriceEt.text.toString().toFloat(),
                binding.deleteSwitch.isChecked,
                stock.current_price
            )
        } else {
            Toast.makeText(requireContext(), "Trigger price required", Toast.LENGTH_SHORT).show()
            return null
        }
    }

}