package com.example.myapplication.network

import com.example.myapplication.data.VehicleStatus
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // 차량 상태 조회
    @GET("api/vehicle/status")
    suspend fun getVehicleStatus(): Response<VehicleStatus>
    
    // 시뮬레이션 제어
    @POST("api/vehicle/simulation/start")
    suspend fun startSimulation(): Response<String>
    
    @POST("api/vehicle/simulation/stop")
    suspend fun stopSimulation(): Response<String>
    
    // 미디어 제어
    @POST("api/vehicle/media/toggle")
    suspend fun toggleMedia(): Response<String>
    
    @POST("api/vehicle/media/volume")
    suspend fun setVolume(@Query("volume") volume: Int): Response<String>
    
    // 공조 시스템 제어
    @POST("api/vehicle/climate/temperature")
    suspend fun setTemperature(@Query("temperature") temperature: Double): Response<String>
    
    @POST("api/vehicle/climate/fan")
    suspend fun setFanSpeed(@Query("fanSpeed") fanSpeed: Int): Response<String>
    
    @POST("api/vehicle/climate/mode")
    suspend fun toggleClimateMode(): Response<String>
} 