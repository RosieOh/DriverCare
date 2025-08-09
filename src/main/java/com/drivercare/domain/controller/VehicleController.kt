package com.drivercare.domain.controller

import com.drivercare.domain.model.VehicleStatus
import com.drivercare.domain.entity.VehicleDataEntity
import com.drivercare.domain.service.VehicleSimulatorService
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleSimulatorService: VehicleSimulatorService,
    private val messagingTemplate: SimpMessagingTemplate
) {
    
    // 주기적으로 차량 상태를 WebSocket으로 전송
    @Scheduled(fixedRate = 1000)
    fun broadcastVehicleStatus() {
        val status = vehicleSimulatorService.getCurrentStatus()
        messagingTemplate.convertAndSend("/topic/vehicle-status", status)
    }

    @GetMapping("/status")
    fun getVehicleStatus(): ResponseEntity<VehicleStatus> {
        return ResponseEntity.ok(vehicleSimulatorService.getCurrentStatus())
    }

    @PostMapping("/simulation/start")
    fun startSimulation(): ResponseEntity<String> {
        vehicleSimulatorService.startSimulation()
        return ResponseEntity.ok("Simulation started")
    }

    @PostMapping("/simulation/stop")
    fun stopSimulation(): ResponseEntity<String> {
        vehicleSimulatorService.stopSimulation()
        return ResponseEntity.ok("Simulation stopped")
    }

    // 미디어 제어
    @PostMapping("/media/toggle")
    fun toggleMedia(): ResponseEntity<String> {
        vehicleSimulatorService.togglePlayPause()
        return ResponseEntity.ok("Media toggled")
    }

    @PostMapping("/media/volume")
    fun setVolume(@RequestParam volume: Int): ResponseEntity<String> {
        vehicleSimulatorService.setVolume(volume)
        return ResponseEntity.ok("Volume set to $volume")
    }

    // 공조 시스템 제어
    @PostMapping("/climate/temperature")
    fun setTemperature(@RequestParam temperature: Double): ResponseEntity<String> {
        vehicleSimulatorService.setClimateTemperature(temperature)
        return ResponseEntity.ok("Temperature set to $temperature")
    }

    @PostMapping("/climate/fan")
    fun setFanSpeed(@RequestParam fanSpeed: Int): ResponseEntity<String> {
        vehicleSimulatorService.setClimateFanSpeed(fanSpeed)
        return ResponseEntity.ok("Fan speed set to $fanSpeed")
    }

    @PostMapping("/climate/mode")
    fun toggleClimateMode(): ResponseEntity<String> {
        vehicleSimulatorService.toggleClimateMode()
        return ResponseEntity.ok("Climate mode toggled")
    }

    // WebSocket을 통한 실시간 데이터 전송 (수동 호출용)
    fun broadcastVehicleStatus(status: VehicleStatus) {
        messagingTemplate.convertAndSend("/topic/vehicle-status", status)
    }
    
    // 데이터베이스 조회 API들
    @GetMapping("/data/recent")
    fun getRecentVehicleData(
        @RequestParam vehicleId: String = "vehicle_001",
        @RequestParam limit: Int = 100
    ): ResponseEntity<List<VehicleDataEntity>> {
        val data = vehicleSimulatorService.getRecentVehicleData(vehicleId, limit)
        return ResponseEntity.ok(data)
    }
    
    @GetMapping("/data/period")
    fun getVehicleDataByPeriod(
        @RequestParam vehicleId: String = "vehicle_001",
        @RequestParam startTime: String,
        @RequestParam endTime: String
    ): ResponseEntity<List<VehicleDataEntity>> {
        val start = LocalDateTime.parse(startTime)
        val end = LocalDateTime.parse(endTime)
        val data = vehicleSimulatorService.getVehicleDataByPeriod(vehicleId, start, end)
        return ResponseEntity.ok(data)
    }
    
    @GetMapping("/data/statistics")
    fun getVehicleStatistics(
        @RequestParam vehicleId: String = "vehicle_001",
        @RequestParam startTime: String
    ): ResponseEntity<Map<String, Any>> {
        val start = LocalDateTime.parse(startTime)
        val avgSpeed = vehicleSimulatorService.getAverageSpeed(vehicleId, start)
        val totalDistance = vehicleSimulatorService.getTotalDistance(vehicleId, start)
        
        val statistics = mutableMapOf<String, Any>()
        statistics["vehicleId"] = vehicleId
        statistics["startTime"] = startTime
        statistics["averageSpeed"] = avgSpeed ?: 0.0
        statistics["totalDistance"] = totalDistance ?: 0.0
        
        return ResponseEntity.ok(statistics)
    }
} 