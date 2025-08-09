package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.VehicleStatus
import com.example.myapplication.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VehicleViewModel : ViewModel() {
    
    private val apiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // Android 에뮬레이터에서 로컬호스트 접근
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
    
    private val _vehicleStatus = MutableStateFlow<VehicleStatus?>(null)
    val vehicleStatus: StateFlow<VehicleStatus?> = _vehicleStatus.asStateFlow()
    
    private val _isSimulationRunning = MutableStateFlow(false)
    val isSimulationRunning: StateFlow<Boolean> = _isSimulationRunning.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // 차량 상태 조회
    fun fetchVehicleStatus() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getVehicleStatus()
                if (response.isSuccessful) {
                    _vehicleStatus.value = response.body()
                    _error.value = null
                } else {
                    _error.value = "Failed to fetch vehicle status"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // 시뮬레이션 시작
    fun startSimulation() {
        viewModelScope.launch {
            try {
                val response = apiService.startSimulation()
                if (response.isSuccessful) {
                    _isSimulationRunning.value = true
                    _error.value = null
                } else {
                    _error.value = "Failed to start simulation"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
    
    // 시뮬레이션 중지
    fun stopSimulation() {
        viewModelScope.launch {
            try {
                val response = apiService.stopSimulation()
                if (response.isSuccessful) {
                    _isSimulationRunning.value = false
                    _error.value = null
                } else {
                    _error.value = "Failed to stop simulation"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
    
    // 미디어 제어
    fun toggleMedia() {
        viewModelScope.launch {
            try {
                apiService.toggleMedia()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
    
    fun setVolume(volume: Int) {
        viewModelScope.launch {
            try {
                apiService.setVolume(volume)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
    
    // 공조 시스템 제어
    fun setTemperature(temperature: Double) {
        viewModelScope.launch {
            try {
                apiService.setTemperature(temperature)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
    
    fun setFanSpeed(fanSpeed: Int) {
        viewModelScope.launch {
            try {
                apiService.setFanSpeed(fanSpeed)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
    
    fun toggleClimateMode() {
        viewModelScope.launch {
            try {
                apiService.toggleClimateMode()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
    
    // 에러 초기화
    fun clearError() {
        _error.value = null
    }
} 