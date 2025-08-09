package com.drivercare.domain.repository

import com.drivercare.domain.entity.VehicleDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface VehicleDataRepository : JpaRepository<VehicleDataEntity, Long> {
    
    fun findByVehicleIdOrderByTimestampDesc(vehicleId: String): List<VehicleDataEntity>
    
    fun findByVehicleIdAndTimestampBetweenOrderByTimestampDesc(
        vehicleId: String, 
        startTime: LocalDateTime, 
        endTime: LocalDateTime
    ): List<VehicleDataEntity>
    
    @Query("SELECT v FROM VehicleDataEntity v WHERE v.vehicleId = :vehicleId AND v.timestamp >= :startTime ORDER BY v.timestamp DESC LIMIT 1")
    fun findLatestByVehicleId(@Param("vehicleId") vehicleId: String, @Param("startTime") startTime: LocalDateTime): VehicleDataEntity?
    
    @Query("SELECT AVG(v.speed) FROM VehicleDataEntity v WHERE v.vehicleId = :vehicleId AND v.timestamp >= :startTime")
    fun getAverageSpeedByVehicleId(@Param("vehicleId") vehicleId: String, @Param("startTime") startTime: LocalDateTime): Double?
    
    @Query("SELECT SUM(v.tripDistance) FROM VehicleDataEntity v WHERE v.vehicleId = :vehicleId AND v.timestamp >= :startTime")
    fun getTotalDistanceByVehicleId(@Param("vehicleId") vehicleId: String, @Param("startTime") startTime: LocalDateTime): Double?
} 