package com.example.ngithubuser.ui.main

import android.content.ContentValues
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
import com.example.ngithubuser.getData.DataUser
import com.example.ngithubuser.getData.DatabaseContract
import com.example.ngithubuser.getData.DetailResponse
import com.example.ngithubuser.getData.FavHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {


    private lateinit var bind: ActivityDetailProfileBinding
    private lateinit var favHelper: FavHelper
    private var username: String? = ""
    private var values = ContentValues()
    private var isFav: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailProfileBinding.inflate(layoutInflater)
        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = bind.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tab: TabLayout = bind.tabs
        setContentView(bind.root)
        username = intent.getStringExtra(USERNAME)

        favHelper = FavHelper.getInstance(applicationContext)
        favHelper.open()

        showLoading(true)

        if (username != null) {
            val detail = intent.getParcelableExtra<DetailResponse>(DETAILNYA) as DetailResponse
            username = detail.login
            bind.root.postDelayed({ showLoading(false) }, 2000)
            bind.tvUsername.text = detail.login
            bind.tvNama.text = detail.name
            if (detail.location != null) {
                values.put(DatabaseContract.FavColumns.LOCATION, detail.location)
                bind.tvAsal.text = detail.location
            } else {
                bind.tvAsal.text = resources.getString(R.string.kosong)
                values.put(
                    DatabaseContract.FavColumns.LOCATION,
                    resources.getString(R.string.kosong)
                )
            }
            bind.tvRepo.text = StringBuilder(detail.publicRepos.toString()).append(" Repository")
            if (detail.company != null) {
                bind.tvCompany.text = detail.company
                values.put(DatabaseContract.FavColumns.COMPANY, detail.company)
            } else {
                values.put(
                    DatabaseContract.FavColumns.COMPANY,
                    resources.getString(R.string.kosong)
                )
                bind.tvCompany.text = resources.getString(R.string.kosong)
            }
            Glide.with(bind.root).load(detail.avatarUrl).into(bind.imgFoto)

            TabLayoutMediator(tab, viewPager) { tb, position ->
                if (position == 0) {
                    tb.text = resources.getString(TAB_TITLES[position]) + " (${detail.followers})"
                } else {
                    tb.text = resources.getString(TAB_TITLES[position]) + " (${detail.following})"
                }
            }.attach()

            supportActionBar?.elevation = 0f
            sectionPagerAdapter.username = detail.login.toString()

            values.put(DatabaseContract.FavColumns.LOGIN, detail.login)
            values.put(DatabaseContract.FavColumns.NAME, detail.name)
            values.put(DatabaseContract.FavColumns.AVATAR, detail.avatarUrl)
            values.put(DatabaseContract.FavColumns.FOLLOWERS, detail.followers)
            values.put(DatabaseContract.FavColumns.FOLLOWING, detail.following)
            values.put(DatabaseContract.FavColumns.REPOSITORY, detail.publicRepos)
            values.put(DatabaseContract.FavColumns.DATE, getCurrentDate())

        } else {
            bind.root.postDelayed( { showLoading(false) }, 2000)
            val detail = intent.getParcelableExtra<DataUser>(DETAILNYA) as DataUser
            username = detail.usename
            bind.tvUsername.text = detail.usename
            bind.tvNama.text = detail.name
            bind.tvAsal.text = detail.location
            bind.tvRepo.text = StringBuilder(detail.repository).append(" Repository")
            bind.tvCompany.text = detail.company
            bind.imgFoto.setImageResource(detail.avatar)

            TabLayoutMediator(tab, viewPager) { tb, position ->
                if (position == 0) {
                    tb.text = resources.getString(TAB_TITLES[position]) + " (${detail.followers})"
                } else {
                    tb.text = resources.getString(TAB_TITLES[position]) + " (${detail.following})"
                }
            }.attach()

            supportActionBar?.elevation = 0f
            sectionPagerAdapter.username = detail.usename

            values.put(DatabaseContract.FavColumns.LOGIN, detail.usename)
            values.put(DatabaseContract.FavColumns.LOCATION, detail.location)
            values.put(DatabaseContract.FavColumns.COMPANY, detail.company)
            values.put(DatabaseContract.FavColumns.NAME, detail.name)
            values.put(DatabaseContract.FavColumns.AVATAR, detail.avatar)
            values.put(DatabaseContract.FavColumns.FOLLOWERS, detail.followers)
            values.put(DatabaseContract.FavColumns.FOLLOWING, detail.following)
            values.put(DatabaseContract.FavColumns.REPOSITORY, detail.repository)
            values.put(DatabaseContract.FavColumns.DATE, getCurrentDate())
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)
        val count = favHelper.queryById(username).count
        if (count == 0) {
            isFav = true
            menu?.findItem(R.id.btFav)?.setIcon(R.drawable.ic_baseline_favorite_border_24)
        } else {
            isFav = false
            menu?.findItem(R.id.btFav)?.setIcon(R.drawable.ic_baseline_favorite_24)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btFav -> {

                if (isFav) {
                    showAlertDialog(ALERT_DIALOG_ADD)
                } else {
                    showAlertDialog(ALERT_DIALOG_DELETE)
                }
            }
        }
        return true
    }

    private fun showAlertDialog(type: Int) {
        val isDialogadd = type == ALERT_DIALOG_ADD
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogadd) {
            dialogTitle = "Tambah"
            dialogMessage = "Apakah anda ingin menambahkan $username ke daftar favorite?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus $username dari daftar favorite?"
            dialogTitle = "Hapus dari favorite"
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                if (isDialogadd) {
                    favHelper.insert(values)
                    Toast.makeText(this@DetailActivity, "Added to favorite", Toast.LENGTH_SHORT)
                        .show()
                    recreate()
                    isFav = false
                } else {
                    if (!isFav) {
                        favHelper.deleteById(username)
                        Toast.makeText(
                            this@DetailActivity,
                            "Deleted from favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                        recreate()
                        isFav = true
                    } else {
                        Toast.makeText(
                            this@DetailActivity,
                            "Gagal menghapus data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
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
        const val USERNAME = ""
        const val ALERT_DIALOG_ADD = 10
        const val ALERT_DIALOG_DELETE = 20

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}