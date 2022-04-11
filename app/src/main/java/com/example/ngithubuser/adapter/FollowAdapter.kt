package com.example.ngithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ngithubuser.config.ApiConfig
import com.example.ngithubuser.databinding.ListProfileBinding
import com.example.ngithubuser.getData.DetailResponse
import com.example.ngithubuser.getData.FollowResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowAdapter(private val listuser: List<FollowResponseItem>) :
    RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {

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
                    } else {
                        holder.bind.tvDRepo.text = "0"
                        holder.bind.tvDFollowers.text = StringBuilder("Followers : ")
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    holder.bind.tvDnama.text = StringBuilder("Null")
                    holder.bind.tvDRepo.text = StringBuilder("Null")
                }
            })
    }


    class ListViewHolder(var bind: ListProfileBinding) : RecyclerView.ViewHolder(bind.root)


}
