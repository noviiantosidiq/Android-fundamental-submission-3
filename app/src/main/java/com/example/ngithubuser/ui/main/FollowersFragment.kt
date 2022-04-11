package com.example.ngithubuser.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngithubuser.R
import com.example.ngithubuser.adapter.FollowAdapter
import com.example.ngithubuser.config.ApiConfig
import com.example.ngithubuser.getData.FollowResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loading: ProgressBar = view.findViewById(R.id.progressBar)
        loading.visibility = View.VISIBLE
        val username = arguments?.getString(ARG_NAME)
        val rvfolow: RecyclerView = view.findViewById(R.id.rvFolowers)

        rvfolow.layoutManager = LinearLayoutManager(requireActivity())

        val client = ApiConfig.getApiService().getFollowers(username.toString())
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                loading.visibility = View.GONE
                val responseBody = response.body()
                if (responseBody != null) {
                    rvfolow.adapter = FollowAdapter(responseBody)

                } else {
                    loading.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        response.message(),
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                loading.visibility = View.GONE
                Toast.makeText(
                    requireActivity(),
                    t.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }

        })


    }

    companion object {
        const val ARG_NAME = "uerlogin"
    }


}