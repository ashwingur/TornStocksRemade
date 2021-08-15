package com.example.tornstocksnew.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tornstocksnew.R
import com.example.tornstocksnew.databinding.TriggersListItemBinding
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.models.TRIGGER_TYPE
import com.example.tornstocksnew.models.Trigger

class TriggersListAdapter(var triggers: List<Trigger>, val context: Context, val mode: TRIGGER_TYPE) :
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
        holder.itemView.setOnClickListener(View.OnClickListener {
            listener.onClick(position)
        })
    }

    override fun getItemCount() = triggers.size

    fun updateTriggers(triggers: List<Trigger>) {
        this.triggers = triggers
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: TriggersListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trigger: Trigger) {
            binding.apply {
                when (trigger.trigger_type) {
                    TRIGGER_TYPE.DEFAULT -> {
                        defaultTrigger.layout.visibility = View.VISIBLE
                        defaultTrigger.nameTv.text = trigger.name
                        defaultTrigger.acronymTv.text = trigger.acronym
                        defaultTrigger.triggerPriceTv.text = trigger.trigger_price.toString()
                        if (trigger.trigger_price > trigger.stock_price) {
                            defaultTrigger.aboveBelowTv.text = "Above"
                        }
                        if (!trigger.single_use){
                            defaultTrigger.singleUseTv.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

