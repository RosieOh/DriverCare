package com.drivercare.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "vehicle_data")
data class VehicleDataEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(nullable = false)
    val vehicleId: String,
    
    @Column(nullable = false)
    val speed: Double,
    
    @Column(nullable = false)
    val rpm: Int,
    
    @Column(nullable = false)
    val fuelLevel: Double,
    
    @Column(nullable = false)
    val engineTemperature: Double,
    
    @Column(nullable = false)
    val batteryVoltage: Double,
    
    @Column(nullable = false)
    val latitude: Double,
    
    @Column(nullable = false)
    val longitude: Double,
    
    @Column(nullable = false)
    val altitude: Double,
    
    @Column(nullable = false)
    val heading: Double,
    
    @Column(nullable = false)
    val accuracy: Double,
    
    @Column(nullable = false)
    val isEngineRunning: Boolean,
    
    @Column(nullable = false)
    val gearPosition: String,
    
    @Column(nullable = false)
    val odometer: Double,
    
    @Column(nullable = false)
    val tripDistance: Double,
    
    @Column(nullable = false)
    val averageSpeed: Double,
    
    @Column(nullable = false)
    val fuelConsumption: Double,
    
    @Column(nullable = false)
    val timestamp: LocalDateTime,
    
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) 