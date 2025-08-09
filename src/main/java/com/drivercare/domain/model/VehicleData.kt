package com.drivercare.domain.model

import java.time.LocalDateTime

data class VehicleData(
    val id: String = "vehicle_001",
    val speed: Double = 0.0, // km/h
    val rpm: Int = 0,
    val fuelLevel: Double = 100.0, // %
    val engineTemperature: Double = 90.0, // °C
    val batteryVoltage: Double = 12.6, // V
    val location: Location = Location(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isEngineRunning: Boolean = false,
    val gearPosition: String = "P",
    val odometer: Double = 0.0, // km
    val tripDistance: Double = 0.0, // km
    val averageSpeed: Double = 0.0, // km/h
    val fuelConsumption: Double = 0.0 // L/100km
)

data class Location(
    val latitude: Double = 37.5665,
    val longitude: Double = 126.9780,
    val altitude: Double = 0.0,
    val heading: Double = 0.0, // degrees
    val accuracy: Double = 5.0 // meters
)

data class MediaInfo(
    val isPlaying: Boolean = false,
    val currentTrack: String = "",
    val artist: String = "",
    val album: String = "",
    val duration: Int = 0, // seconds
    val currentTime: Int = 0, // seconds
    val volume: Int = 50, // 0-100
    val source: String = "Bluetooth" // Bluetooth, USB, Radio, etc.
)

data class ClimateControl(
    val isEnabled: Boolean = false,
    val temperature: Double = 22.0, // °C
    val fanSpeed: Int = 2, // 1-5
    val mode: String = "Auto", // Auto, Manual, Defrost
    val isAcOn: Boolean = false,
    val isHeatOn: Boolean = false,
    val isDefrostOn: Boolean = false
)

data class NavigationInfo(
    val isActive: Boolean = false,
    val destination: String = "",
    val estimatedTime: Int = 0, // minutes
    val remainingDistance: Double = 0.0, // km
    val currentRoute: List<Location> = emptyList(),
    val nextTurn: String = "",
    val nextTurnDistance: Double = 0.0 // km
)

data class VehicleStatus(
    val vehicleData: VehicleData,
    val mediaInfo: MediaInfo,
    val climateControl: ClimateControl,
    val navigationInfo: NavigationInfo,
    val warnings: List<String> = emptyList(),
    val timestamp: LocalDateTime = LocalDateTime.now()
) 