package com.example.beautybell.presentation.artisan

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beautybell.databinding.ItemArtisanBinding
import com.example.beautybell.domain.artisan.entity.ArtisanEntity

class ArtisanAdapter(private val orders: MutableList<ArtisanEntity>, private val context: Context) :
    RecyclerView.Adapter<ArtisanAdapter.ViewHolder>() {

    interface OnItemTap {
        fun onTap(product: ArtisanEntity)
    }

    fun setItemTapListener(l: OnItemTap){
        onTapListener = l
    }

    fun removeAllData() {
       orders.clear()
        notifyDataSetChanged()
    }

    fun updateList(mProducts: List<ArtisanEntity>){
        orders.clear()
        orders.addAll(mProducts)
        notifyDataSetChanged()
    }

    private var onTapListener: OnItemTap? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtisanAdapter.ViewHolder {
        val view = ItemArtisanBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtisanAdapter.ViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    inner class ViewHolder(private val itemBinding: ItemArtisanBinding) :
        RecyclerView.ViewHolder(itemBinding.root){

        fun bind(artisan: ArtisanEntity){
            itemBinding.textviewItemUser.text = artisan.name
            itemBinding.textviewItemDescription.text = artisan.description
            Glide.with(context).load(artisan.avatar).circleCrop()
                .into(itemBinding.imageviewItem)
            itemBinding.root.setOnClickListener {
                onTapListener?.onTap(artisan)
            }
        }
    }
}