package com.example.dicodingevent.ui.search

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.service.ApiConfig
import kotlinx.coroutines.launch
import java.io.IOException

class SearchEventViewModel : ViewModel() {
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _eventList = MutableLiveData<EventResponse>()
    val eventList: LiveData<EventResponse> = _eventList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getSearchItemList(query: String) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val response = ApiConfig.getService().searchEvents(query = query)
            _eventList.value = response
        } catch (e: IOException) {
            Log.e(ContentValues.TAG, "onFailure: ${e.message}")
            _errorMessage.value = "Network error: ${e.message}"
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "onFailure: ${e.message}")
            _errorMessage.value = "Network error: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
}