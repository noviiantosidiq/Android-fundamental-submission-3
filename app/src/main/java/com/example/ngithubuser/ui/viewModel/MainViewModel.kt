package com.example.ngithubuser.ui.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.example.ngithubuser.config.ApiConfig
import com.example.ngithubuser.config.SettingPreferences
import com.example.ngithubuser.getData.SearchRespone
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userData = MutableLiveData<SearchRespone>()
    val userData: LiveData<SearchRespone> = _userData

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    init {
        val username = ""
        cariUser(username)
    }

    fun cariUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchResult(username)
        client.enqueue(object : Callback<SearchRespone> {
            override fun onFailure(call: Call<SearchRespone>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.localizedMessage}")
            }

            override fun onResponse(
                call: Call<SearchRespone>,
                response: Response<SearchRespone>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userData.value = responseBody!!
                    }
                } else {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")

                }
            }
        })
    }

}