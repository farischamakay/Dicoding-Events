package com.example.dicodingevent.ui.detail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.DetailEventResponse
import com.example.dicodingevent.data.service.ApiConfig
import kotlinx.coroutines.launch
import java.io.IOException

class DetailEventViewModel : ViewModel() {
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _detailEvent = MutableLiveData<DetailEventResponse>()
    val detailEvent: MutableLiveData<DetailEventResponse> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    fun getDetailEvent(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val response = ApiConfig.getService().getDetailEvents(id)
            _detailEvent.value = response
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