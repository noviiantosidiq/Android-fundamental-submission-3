package com.example.ngithubuser.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.ngithubuser.R
import com.example.ngithubuser.adapter.SectionPagerAdapter
import com.example.ngithubuser.databinding.ActivityDetailProfileBinding
import com.example.ngithubuser.getData.Fav
import com.example.ngithubuser.getData.FavHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailFavActivity: AppCompatActivity() {

    private lateinit var bind: ActivityDetailProfileBinding
    private var username: String? = ""
    private lateinit var favHelper: FavHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        bind = ActivityDetailProfileBinding.inflate(layoutInflater)
        val detail = intent.getParcelableExtra<Fav>(DETAILNYA) as Fav
        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = bind.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tab: TabLayout = bind.tabs
        setContentView(bind.root)

        showLoading(true)

        bind.root.postDelayed({ showLoading(false) }, 2000)
        username = detail.usename
        bind.tvUsername.text = detail.usename
        bind.tvNama.text = detail.nama
            bind.tvAsal.text = detail.location
        bind.tvRepo.text = StringBuilder(detail.repository).append(" Repository")
            bind.tvCompany.text = detail.company
        if (detail.avatar.toIntOrNull() is Int){
            detail.avatar.let { bind.imgFoto.setImageResource(it.toInt()) }
        }else{
            Glide.with(bind.root).load(detail.avatar).into(bind.imgFoto)
        }

        TabLayoutMediator(tab, viewPager) { tb, position ->
            if (position == 0) {
                tb.text = resources.getString(TAB_TITLES[position]) + " (${detail.followers})"
            } else {
                tb.text = resources.getString(TAB_TITLES[position]) + " (${detail.following})"
            }
        }.attach()

        supportActionBar?.elevation = 0f
        sectionPagerAdapter.username = detail.usename


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)
            menu?.findItem(R.id.btFav)?.setIcon(R.drawable.ic_baseline_favorite_24)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btFav -> {
                favHelper = FavHelper.getInstance(applicationContext)
                favHelper.open()
                val dialogTitle = "Hapus"
                val dialogMessage = "Apakah anda ingin menghapus $username dari daftar favorite?"
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle(dialogTitle)
                alertDialogBuilder
                    .setMessage(dialogMessage)
                    .setCancelable(false)
                    .setPositiveButton("Ya") { _, _ ->
                                favHelper.deleteById(username)
                                Toast.makeText(
                                    this@DetailFavActivity,
                                    "Deleted from favorite",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                        favHelper.close()
                            }.setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            bind.progressBar.visibility = View.VISIBLE
        } else {
            bind.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val DETAILNYA = "DETAIL_USER"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}