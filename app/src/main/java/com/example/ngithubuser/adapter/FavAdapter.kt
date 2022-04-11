package com.example.ngithubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ngithubuser.R
import com.example.ngithubuser.databinding.ListProfileBinding
import com.example.ngithubuser.getData.Fav

class FavAdapter(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    var listFav = ArrayList<Fav>()
        set(listFav) {
            this.listFav.addAll(listFav)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavAdapter.FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_profile, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavAdapter.FavViewHolder, position: Int) {
        holder.bind(listFav[position])
    }

    override fun getItemCount(): Int = this.listFav.size

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListProfileBinding.bind(itemView)
        fun bind(fav: Fav) {
            binding.tvDnama.text = fav.nama
            binding.tvDRepo.text = fav.repository
            if (fav.avatar.toIntOrNull() is Int){
            binding.ImgDfoto.setImageResource(fav.avatar.toInt())
            }else{
                Glide.with(binding.root).load(fav.avatar).into(binding.ImgDfoto)
            }
            binding.tvDFollowers.text =
                StringBuilder("Followers : " + fav.followers)
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(fav, adapterPosition)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(selectedFave: Fav, position: Int?)
    }

}