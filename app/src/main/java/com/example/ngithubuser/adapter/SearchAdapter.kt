package com.example.ngithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ngithubuser.config.ApiConfig
import com.example.ngithubuser.databinding.ListProfileBinding
import com.example.ngithubuser.getData.DetailResponse
import com.example.ngithubuser.getData.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchAdapter(private val listuser: List<ItemsItem>) :
    RecyclerView.Adapter<SearchAdapter.ListViewHolder>() {
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
        val result = listuser[position]
        holder.bind.tvDnama.text = result.login
        Glide.with(holder.bind.root).load(result.avatarUrl).into(holder.bind.ImgDfoto)
        ApiConfig.getApiService().getDetail(result.login)
            .enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        holder.bind.tvDRepo.text = responseBody.publicRepos.toString()
                        holder.bind.tvDFollowers.text =
                            StringBuilder("Followers : " + responseBody.followers.toString())
                        holder.itemView.setOnClickListener {
                            onItemClickCallback.onItemClicked(
                                responseBody
                            )
                        }
                    } else {
                        holder.bind.tvDRepo.text = "0"
                        holder.bind.tvDFollowers.text = StringBuilder("Followers : 0")
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    holder.bind.tvDnama.text = StringBuilder("Null")
                    holder.bind.tvDRepo.text = StringBuilder("Null")
                }
            })

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DetailResponse)
    }

    class ListViewHolder(var bind: ListProfileBinding) : RecyclerView.ViewHolder(bind.root)


}
