package com.example.tornstocksnew.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tornstocksnew.R
import com.example.tornstocksnew.databinding.DetailedStockItemBinding
import com.example.tornstocksnew.databinding.StocksListItemBinding
import com.example.tornstocksnew.models.Stock
import java.text.DecimalFormat
import java.util.*

class DetailedStocksAdapter(
    var stocks: MutableList<Stock>, val context: Context
) :
    RecyclerView.Adapter<DetailedStocksAdapter.ViewHolder>() {

    private lateinit var showBenefitList: MutableList<Boolean>
    private lateinit var listener: OnItemClickListener
    private var expand: ScaleAnimation
    private var collapsingObject: View? = null

    init {
        initBools()
        expand = ScaleAnimation(
            1f, 1f,
            0f, 1f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f);
        expand.setDuration(350);
    }

    private fun initBools(){
        showBenefitList = mutableListOf()
        for (i in 1..stocks.size){
            showBenefitList.add(false)
        }
    }

    fun toggleBool(position: Int){
        showBenefitList[position] = !showBenefitList[position]
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = DetailedStockItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stocks[position], listener, position, showBenefitList[position], context)
        holder.itemView.setOnClickListener{ listener.onClick(position) }
    }

    override fun getItemCount() = stocks.size

    fun updateStocks(stocks: MutableList<Stock>) {
        this.stocks = stocks
        initBools()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: DetailedStockItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stock: Stock, listener: OnItemClickListener, position: Int, isExpanded: Boolean, context: Context) {
            binding.apply {
                benefitLayout.setOnClickListener { listener.onBenefitClick(position) }
                acronymTv.text = stock.acronym
                nameTv.text = stock.name
                priceTv.text = stock.current_price.toString()
                marketCapTv.text = DecimalFormat("#,###").format(stock.market_cap)
                totalSharesTv.text = DecimalFormat("#,###").format(stock.total_shares)
                typeTv.text = stock.benefit.type.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                frequencyTv.text = "${stock.benefit.frequency} Days"
                requirementTv.text = DecimalFormat("#,###").format(stock.benefit.requirement) + " Shares"
                if (stock.benefit.type == "passive"){
                    frequencyLabelTv.text = context.getString(R.string.available_after)
                }

                descriptionTv.text = stock.benefit.description.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }

                if (isExpanded) {
                    benefitDetailsLayout.visibility = View.VISIBLE
                    benefitDetailsLayout.startAnimation(expand)
                    benefitIcon.setImageResource(R.drawable.ic_up)
                } else {
                    collapsingObject = benefitDetailsLayout
                    benefitDetailsLayout.visibility = View.GONE
                    benefitIcon.setImageResource(R.drawable.ic_down)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
        fun onBenefitClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}