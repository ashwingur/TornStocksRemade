package com.example.tornstocksnew.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tornstocksnew.databinding.StocksListItemBinding
import com.example.tornstocksnew.models.Stock

class StocksListAdapter(var stocks: MutableList<Stock>, val context: Context) :
    RecyclerView.Adapter<StocksListAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = StocksListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stocks[position])
        holder.itemView.setOnClickListener(View.OnClickListener {
            listener.onClick(position)
        })
    }

    override fun getItemCount() = stocks.size

    fun updateStocks(stocks: MutableList<Stock>) {
        this.stocks = stocks
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: StocksListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stock: Stock) {
            binding.apply {
                acronymTv.text = stock.acronym
                nameTv.text = stock.name
                priceTv.text = stock.current_price.toString()
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