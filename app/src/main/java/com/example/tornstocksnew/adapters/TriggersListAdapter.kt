package com.example.tornstocksnew.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tornstocksnew.R
import com.example.tornstocksnew.databinding.TriggersListItemBinding
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.models.TRIGGER_TYPE
import com.example.tornstocksnew.models.Trigger
import com.streamplate.streamplateandroidapp.ui.fragments.TRIGGER_PAGE_MODE

class TriggersListAdapter(
    var triggers: List<Trigger>,
    val context: Context,
    var triggerType: TRIGGER_TYPE,
) :
    RecyclerView.Adapter<TriggersListAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = TriggersListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(triggers[position])
        holder.itemView.apply {
            setOnClickListener { listener.onClick(position) }
            setOnLongClickListener { listener.onLongClick(position) }
        }
    }

    override fun getItemCount() = triggers.size

    fun updateTriggers(triggers: List<Trigger>) {
        this.triggers = triggers
        notifyDataSetChanged()
    }

    fun updateTriggerMode(position: Int, mode: TRIGGER_PAGE_MODE){
        triggers[position].mode = mode
        notifyItemChanged(position)
    }

    fun toggleTriggerMode(position: Int){
        triggers[position].let {
            if (it.mode == TRIGGER_PAGE_MODE.NORMAL){
                //Toast.makeText(context, "Change to Delete", Toast.LENGTH_SHORT).show()
                it.mode = TRIGGER_PAGE_MODE.DELETE
            } else {
                it.mode = TRIGGER_PAGE_MODE.NORMAL
                //Toast.makeText(context, "Change to Normal", Toast.LENGTH_SHORT).show()
            }
        }
        notifyItemChanged(position)
    }

    fun updateAllTriggersMode(mode: TRIGGER_PAGE_MODE){
        for (trigger in triggers){
            trigger.mode = mode
        }
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: TriggersListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trigger: Trigger) {
            binding.apply {
                if (trigger.mode == TRIGGER_PAGE_MODE.DELETE){ // Delete mode, highlight the item
                    selectedBg.visibility = View.VISIBLE
                } else {
                    selectedBg.visibility = View.GONE
                }
                when (trigger.trigger_type) {
                    TRIGGER_TYPE.DEFAULT -> {
                        defaultTrigger.layout.visibility = View.VISIBLE
                        defaultTrigger.nameTv.text = trigger.name
                        defaultTrigger.acronymTv.text = trigger.acronym
                        defaultTrigger.triggerPriceTv.text = trigger.trigger_price.toString()
                        if (trigger.trigger_price > trigger.stock_price) {
                            defaultTrigger.aboveBelowTv.text = "Above"
                        } else {
                            defaultTrigger.aboveBelowTv.text = "Below"
                        }
                        if (!trigger.single_use) {
                            defaultTrigger.singleUseTv.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
        fun onLongClick(position: Int): Boolean
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

