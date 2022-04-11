package com.example.ngithubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ngithubuser.adapter.FavAdapter
import com.example.ngithubuser.config.MappingHelper
import com.example.ngithubuser.databinding.ActivityFavoriteBinding
import com.example.ngithubuser.getData.Fav
import com.example.ngithubuser.getData.FavHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favHelper: FavHelper
    private lateinit var bind: ActivityFavoriteBinding
    private lateinit var adapter: FavAdapter
    private var username: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(bind.root)

        showLoading(true)
        favHelper = FavHelper.getInstance(applicationContext)
        favHelper.open()

        bind.rvFav.layoutManager = LinearLayoutManager(this)
        bind.rvFav.setHasFixedSize(true)

        adapter = FavAdapter(object : FavAdapter.OnItemClickCallback {
            override fun onItemClicked(selectedFave: Fav, position: Int?) {
                username = selectedFave.usename
                val dialogTitle = "Lihat atau hapus"
                val alertDialogBuilder = AlertDialog.Builder(this@FavoriteActivity)
                alertDialogBuilder.setTitle(dialogTitle)
                alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("LIHAT") { _, _ ->
                        val intent = Intent(
                            this@FavoriteActivity,
                            DetailFavActivity::class.java
                        )
                        intent.putExtra(DetailFavActivity.DETAILNYA, selectedFave)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("HAPUS") { _, _ ->
                        favHelper.deleteById(username)
                        Toast.makeText(
                            this@FavoriteActivity,
                            "Deleted from favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                        recreate()
                    }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        })
        loadFav()
    }

    private fun loadFav() {
        lifecycleScope.launch {
            favHelper.open()
            showLoading(true)
            val defferedFav = async(Dispatchers.IO) {
                val cursor = favHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val fav = defferedFav.await()
            if (fav.size > 0) {
                bind.root.postDelayed({ showLoading(false) }, 2000)
                adapter.listFav = fav
                bind.rvFav.adapter = adapter
            } else {
                bind.root.postDelayed({ showLoading(false) }, 2000)
                adapter.listFav = ArrayList()
                showSnackbarMessage("Belum ada data")
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(bind.rvFav, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            bind.Loadbar.visibility = View.VISIBLE
        } else {
            bind.Loadbar.visibility = View.GONE
        }
    }

}