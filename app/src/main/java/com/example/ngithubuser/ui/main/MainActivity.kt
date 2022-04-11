package com.example.ngithubuser.ui.main

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngithubuser.R
import com.example.ngithubuser.adapter.DetailUserAdapter
import com.example.ngithubuser.adapter.SearchAdapter
import com.example.ngithubuser.config.SettingPreferences
import com.example.ngithubuser.config.ViewModelFactory
import com.example.ngithubuser.databinding.ActivityMainBinding
import com.example.ngithubuser.getData.DataUser
import com.example.ngithubuser.getData.DetailResponse
import com.example.ngithubuser.getData.ItemsItem
import com.example.ngithubuser.ui.viewModel.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    private lateinit var daftaruser: RecyclerView
    private lateinit var viewModel: MainViewModel
    private val list = ArrayList<DataUser>()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private var jenis = true


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        daftaruser = bind.rvProfile
        setContentView(bind.root)
        daftaruser.layoutManager = LinearLayoutManager(this)
        list.addAll(listUser)
        val pref = SettingPreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )
        if (viewModel.userData.value == null) {
            showRecyclerList()
        } else {
            val data = viewModel.userData.value
            if (data != null) {
                bind.rvProfile.adapter = SearchAdapter(data.items)
                lihatDetail(data.items)

            }
        }

        bind.fabAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val btnChoose = menu.findItem(R.id.btchoose)

        viewModel.getThemeSettings().observe(this, { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                btnChoose.setIcon(R.drawable.ic_baseline_wb_sunny_24)
                jenis = false
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                btnChoose.setIcon(R.drawable.ic_baseline_nights_stay_24)
                jenis = true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {

                val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val searchView = item.actionView as SearchView

                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
                searchView.queryHint = resources.getString(R.string.search_hint)

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String): Boolean {

                        showLoading(true)
                        viewModel.cariUser(query)
                        Toast.makeText(this@MainActivity, "Searching $query", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.isLoading.observe(this@MainActivity) {
                            showLoading(it)
                        }
                        viewModel.userData.observe(this@MainActivity) {
                            bind.rvProfile.adapter = SearchAdapter(it.items)
                            lihatDetail(it.items)
                        }

                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        return false
                    }

                })
                return true
            }
            R.id.btchoose -> {
                if (!jenis) {
                    item.setIcon(R.drawable.ic_baseline_nights_stay_24)
                    Toast.makeText(this@MainActivity, "Night mode Enabled", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.saveThemeSetting(jenis)
                    jenis = true
                } else {
                    item.setIcon(R.drawable.ic_baseline_wb_sunny_24)
                    Toast.makeText(this@MainActivity, "Night mode Disabled", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.saveThemeSetting(jenis)

                    jenis = false
                }
                return true
            }
            else -> return true
        }
    }

    private fun lihatDetail(user: List<ItemsItem>) {
        val detail = SearchAdapter(user)
        daftaruser.adapter = detail
        detail.setOnItemClickCallback(object :
            SearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DetailResponse) {
                Toast.makeText(
                    this@MainActivity,
                    "Melihat detail dari " + data.name,
                    Toast.LENGTH_SHORT
                ).show()
                val pindah =
                    Intent(this@MainActivity, DetailActivity::class.java)
                pindah.putExtra(DetailActivity.USERNAME, data.login)
                pindah.putExtra(DetailActivity.DETAILNYA, data)
                startActivity(pindah)
            }
        })
    }

    private fun showUser(user: DataUser) {
        Toast.makeText(this, "Melihat detail dari " + user.name, Toast.LENGTH_SHORT).show()
        val detailnya = Intent(this@MainActivity, DetailActivity::class.java)
        detailnya.putExtra(DetailActivity.DETAILNYA, user)
        startActivity(detailnya)
    }


    private val listUser: ArrayList<DataUser>
        @SuppressLint("Recycle")
        get() {
            val dName = resources.getStringArray(R.array.name)
            val dUser = resources.getStringArray(R.array.username)
            val dCom = resources.getStringArray(R.array.company)
            val dLoc = resources.getStringArray(R.array.location)
            val dRep = resources.getStringArray(R.array.repository)
            val dFolo = resources.getStringArray(R.array.followers)
            val dFoli = resources.getStringArray(R.array.following)
            val dAva = resources.obtainTypedArray(R.array.avatar)

            val listUser = ArrayList<DataUser>()
            for (i in dName.indices) {
                val user = DataUser(
                    dAva.getResourceId(i, -1),
                    dName[i],
                    dFolo[i],
                    dRep[i],
                    dLoc[i],
                    dCom[i],
                    dUser[i],
                    dFoli[i]
                )
                listUser.add(user)
            }
            return listUser
        }


    private fun showRecyclerList() {
        val listUserAdapter = DetailUserAdapter(list)
        daftaruser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : DetailUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataUser) {
                showUser(data)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            bind.progressBar.visibility = View.VISIBLE
        } else {
            bind.progressBar.visibility = View.GONE
        }
    }
}
