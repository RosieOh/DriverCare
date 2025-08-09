package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class VehicleData(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("speed")
    val speed: Double,
    
    @SerializedName("rpm")
    val rpm: Int,
    
    @SerializedName("fuelLevel")
    val fuelLevel: Double,
    
    @SerializedName("engineTemperature")
    val engineTemperature: Double,
    
    @SerializedName("batteryVoltage")
    val batteryVoltage: Double,
    
    @SerializedName("location")
    val location: Location,
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("isEngineRunning")
    val isEngineRunning: Boolean,
    
    @SerializedName("gearPosition")
    val gearPosition: String,
    
    @SerializedName("odometer")
    val odometer: Double,
    
    @SerializedName("tripDistance")
    val tripDistance: Double,
    
    @SerializedName("averageSpeed")
    val averageSpeed: Double,
    
    @SerializedName("fuelConsumption")
    val fuelConsumption: Double
)

data class Location(
    @SerializedName("latitude")
    val latitude: Double,
    
    @SerializedName("longitude")
    val longitude: Double,
    
    @SerializedName("altitude")
    val altitude: Double,
    
    @SerializedName("heading")
    val heading: Double,
    
    @SerializedName("accuracy")
    val accuracy: Double
)

data class MediaInfo(
    @SerializedName("isPlaying")
    val isPlaying: Boolean,
    
    @SerializedName("currentTrack")
    val currentTrack: String,
    
    @SerializedName("artist")
    val artist: String,
    
    @SerializedName("album")
    val album: String,
    
    @SerializedName("duration")
    val duration: Int,
    
    @SerializedName("currentTime")
    val currentTime: Int,
    
    @SerializedName("volume")
    val volume: Int,
    
    @SerializedName("source")
    val source: String
)

data class ClimateControl(
    @SerializedName("isEnabled")
    val isEnabled: Boolean,
    
    @SerializedName("temperature")
    val temperature: Double,
    
    @SerializedName("fanSpeed")
    val fanSpeed: Int,
    
    @SerializedName("mode")
    val mode: String,
    
    @SerializedName("isAcOn")
    val isAcOn: Boolean,
    
    @SerializedName("isHeatOn")
    val isHeatOn: Boolean,
    
    @SerializedName("isDefrostOn")
    val isDefrostOn: Boolean
)

data class NavigationInfo(
    @SerializedName("isActive")
    val isActive: Boolean,
    
    @SerializedName("destination")
    val destination: String,
    
    @SerializedName("estimatedTime")
    val estimatedTime: Int,
    
    @SerializedName("remainingDistance")
    val remainingDistance: Double,
    
    @SerializedName("nextTurn")
    val nextTurn: String,
    
    @SerializedName("nextTurnDistance")
    val nextTurnDistance: Double
)

data class VehicleStatus(
    @SerializedName("vehicleData")
    val vehicleData: VehicleData,
    
    @SerializedName("mediaInfo")
    val mediaInfo: MediaInfo,
    
    @SerializedName("climateControl")
    val climateControl: ClimateControl,
    
    @SerializedName("navigationInfo")
    val navigationInfo: NavigationInfo,
    
    @SerializedName("warnings")
    val warnings: List<String>,
    
    @SerializedName("timestamp")
    val timestamp: String
) 