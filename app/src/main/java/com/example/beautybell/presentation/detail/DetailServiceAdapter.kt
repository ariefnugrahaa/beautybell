package com.example.beautybell.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beautybell.data.detail.remote.dto.DetailResponse
import com.example.beautybell.databinding.ItemDetailServicesBinding

class DetailServiceAdapter(private val detailServices: MutableList<DetailResponse.Service>) :
    RecyclerView.Adapter<DetailServiceAdapter.ViewHolder>() {

    fun updateList(mServices: List<DetailResponse.Service>){
        detailServices.clear()
        detailServices.addAll(mServices)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailServiceAdapter.ViewHolder {
        val view = ItemDetailServicesBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailServiceAdapter.ViewHolder, position: Int) {
        holder.bind(detailServices[position])
    }

    override fun getItemCount(): Int = detailServices.size

    inner class ViewHolder(private val itemBinding: ItemDetailServicesBinding) :
        RecyclerView.ViewHolder(itemBinding.root){

        fun bind(artisanServices: DetailResponse.Service){
            ("Rp " + artisanServices.price).also { itemBinding.textviewItemPrice.text = it }
            itemBinding.textviewItemDescription.text = artisanServices.caption
            itemBinding.textviewItemServiceName.text = artisanServices.name
        }
    }
}