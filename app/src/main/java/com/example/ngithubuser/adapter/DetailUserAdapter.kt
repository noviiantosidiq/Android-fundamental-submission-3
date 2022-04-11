package com.example.ngithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ngithubuser.getData.DataUser
import com.example.ngithubuser.databinding.ListProfileBinding

class DetailUserAdapter(private val listuser: ArrayList<DataUser>) :
    RecyclerView.Adapter<DetailUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val bind =
            ListProfileBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(bind)
    }

    override fun getItemCount(): Int = listuser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (avatar, name, followers, repository) = listuser[position]
        holder.bind.ImgDfoto.setImageResource(avatar)
        holder.bind.tvDnama.text = name
        holder.bind.tvDFollowers.text = StringBuilder(followers).append(" Followers")
        holder.bind.tvDRepo.text = repository
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(
                listuser[position]
            )
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataUser)
    }

    class ListViewHolder(var bind: ListProfileBinding) : RecyclerView.ViewHolder(bind.root)


}