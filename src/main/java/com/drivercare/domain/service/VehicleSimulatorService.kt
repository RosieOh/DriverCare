package com.drivercare.domain.service

import com.drivercare.domain.model.*
import com.drivercare.domain.entity.VehicleDataEntity
import com.drivercare.domain.repository.VehicleDataRepository
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.*

@Service
class VehicleSimulatorService(
    private val vehicleDataRepository: VehicleDataRepository
) {
    
    private var currentVehicleStatus: VehicleStatus = VehicleStatus(
        vehicleData = VehicleData(),
        mediaInfo = MediaInfo(),
        climateControl = ClimateControl(),
        navigationInfo = NavigationInfo()
    )
    
    private var simulationJob: Job? = null
    private var isSimulationRunning = false
    
    // 시뮬레이션 경로 (서울 시청 -> 강남역)
    private val routePoints = listOf(
        Location(37.5665, 126.9780), // 서울 시청
        Location(37.5668, 126.9785),
        Location(37.5670, 126.9790),
        Location(37.5675, 126.9795),
        Location(37.5680, 126.9800),
        Location(37.5685, 126.9805),
        Location(37.5690, 126.9810),
        Location(37.5695, 126.9815),
        Location(37.5700, 126.9820),
        Location(37.5705, 126.9825),
        Location(37.5710, 126.9830),
        Location(37.5715, 126.9835),
        Location(37.5720, 126.9840),
        Location(37.5725, 126.9845),
        Location(37.5730, 126.9850),
        Location(37.5735, 126.9855),
        Location(37.5740, 126.9860),
        Location(37.5745, 126.9865),
        Location(37.5750, 126.9870),
        Location(37.5755, 126.9875),
        Location(37.5760, 126.9880),
        Location(37.5765, 126.9885),
        Location(37.5770, 126.9890),
        Location(37.5775, 126.9895),
        Location(37.5780, 126.9900),
        Location(37.5785, 126.9905),
        Location(37.5790, 126.9910),
        Location(37.5795, 126.9915),
        Location(37.5800, 126.9920),
        Location(37.5805, 126.9925),
        Location(37.5810, 126.9930),
        Location(37.5815, 126.9935),
        Location(37.5820, 126.9940),
        Location(37.5825, 126.9945),
        Location(37.5830, 126.9950),
        Location(37.5835, 126.9955),
        Location(37.5840, 126.9960),
        Location(37.5845, 126.9965),
        Location(37.5850, 126.9970),
        Location(37.5855, 126.9975),
        Location(37.5860, 126.9980),
        Location(37.5865, 126.9985),
        Location(37.5870, 126.9990),
        Location(37.5875, 126.9995),
        Location(37.5880, 127.0000),
        Location(37.5885, 127.0005),
        Location(37.5890, 127.0010),
        Location(37.5895, 127.0015),
        Location(37.5900, 127.0020),
        Location(37.5905, 127.0025),
        Location(37.5910, 127.0030),
        Location(37.5915, 127.0035),
        Location(37.5920, 127.0040),
        Location(37.5925, 127.0045),
        Location(37.5930, 127.0050),
        Location(37.5935, 127.0055),
        Location(37.5940, 127.0060),
        Location(37.5945, 127.0065),
        Location(37.5950, 127.0070),
        Location(37.5955, 127.0075),
        Location(37.5960, 127.0080),
        Location(37.5965, 127.0085),
        Location(37.5970, 127.0090),
        Location(37.5975, 127.0095),
        Location(37.5980, 127.0100),
        Location(37.5985, 127.0105),
        Location(37.5990, 127.0110),
        Location(37.5995, 127.0115),
        Location(37.6000, 127.0120), // 강남역 근처
        Location(37.4979, 127.0276)  // 강남역
    )
    
    private var currentRouteIndex = 0
    private var tripStartTime = LocalDateTime.now()
    private var totalDistance = 0.0
    
    fun startSimulation() {
        if (isSimulationRunning) return
        
        isSimulationRunning = true
        simulationJob = CoroutineScope(Dispatchers.Default).launch {
            while (isSimulationRunning) {
                updateVehicleStatus()
                delay(1000) // 1초마다 업데이트
            }
        }
    }
    
    fun stopSimulation() {
        isSimulationRunning = false
        simulationJob?.cancel()
    }
    
    fun getCurrentStatus(): VehicleStatus = currentVehicleStatus
    
    private fun updateVehicleStatus() {
        val now = LocalDateTime.now()
        
        // 위치 업데이트
        val newLocation = updateLocation()
        
        // 속도 계산 (위치 변화 기반)
        val speed = calculateSpeed(newLocation)
        
        // 엔진 상태 업데이트
        val isEngineRunning = speed > 0 || currentVehicleStatus.vehicleData.isEngineRunning
        
        // 연료 소모 시뮬레이션
        val fuelConsumption = if (speed > 0) 8.5 + (speed * 0.1) else 0.5
        val newFuelLevel = max(0.0, currentVehicleStatus.vehicleData.fuelLevel - (fuelConsumption / 3600))
        
        // 주행 거리 업데이트
        val distanceIncrement = if (speed > 0) speed / 3600 else 0.0
        totalDistance += distanceIncrement
        
        // 평균 속도 계산
        val tripDuration = java.time.Duration.between(tripStartTime, now).seconds.toDouble() / 3600
        val averageSpeed = if (tripDuration > 0) totalDistance / tripDuration else 0.0
        
        // RPM 계산
        val rpm = when {
            speed == 0.0 -> 800
            speed < 20.0 -> 1500
            speed < 40.0 -> 2000
            speed < 60.0 -> 2500
            speed < 80.0 -> 3000
            else -> 3500
        }
        
        // 엔진 온도
        val engineTemperature = if (isEngineRunning) {
            min(110.0, 90.0 + (speed * 0.2))
        } else {
            max(20.0, currentVehicleStatus.vehicleData.engineTemperature - 0.5)
        }
        
        // 배터리 전압
        val batteryVoltage = if (isEngineRunning) {
            14.2 + (sin(now.second.toDouble() * PI / 30) * 0.1)
        } else {
            12.6 - (now.second.toDouble() * 0.001)
        }
        
        // 미디어 정보 업데이트
        val mediaInfo = updateMediaInfo()
        
        // 공조 시스템 업데이트
        val climateControl = updateClimateControl()
        
        // 내비게이션 정보 업데이트
        val navigationInfo = updateNavigationInfo(newLocation)
        
        // 경고 메시지
        val warnings = mutableListOf<String>()
        if (newFuelLevel < 10.0) warnings.add("연료 부족")
        if (engineTemperature > 105.0) warnings.add("엔진 과열")
        if (batteryVoltage < 12.0) warnings.add("배터리 전압 낮음")
        
        currentVehicleStatus = VehicleStatus(
            vehicleData = VehicleData(
                id = "vehicle_001",
                speed = speed,
                rpm = rpm,
                fuelLevel = newFuelLevel,
                engineTemperature = engineTemperature,
                batteryVoltage = batteryVoltage,
                location = newLocation,
                timestamp = now,
                isEngineRunning = isEngineRunning,
                gearPosition = if (speed > 0) "D" else "P",
                odometer = currentVehicleStatus.vehicleData.odometer + distanceIncrement,
                tripDistance = totalDistance,
                averageSpeed = averageSpeed,
                fuelConsumption = fuelConsumption
            ),
            mediaInfo = mediaInfo,
            climateControl = climateControl,
            navigationInfo = navigationInfo,
            warnings = warnings,
            timestamp = now
        )
        
        // 데이터베이스에 차량 데이터 저장
        saveVehicleDataToDatabase(currentVehicleStatus.vehicleData)
        
        // WebSocket을 통해 실시간 데이터 전송은 컨트롤러에서 처리
    }
    
    private fun updateLocation(): Location {
        if (currentRouteIndex >= routePoints.size - 1) {
            currentRouteIndex = 0 // 경로 반복
        }
        
        val currentPoint = routePoints[currentRouteIndex]
        val nextPoint = routePoints[currentRouteIndex + 1]
        
        // 현재 위치에서 다음 지점으로 이동
        val progress = (System.currentTimeMillis() % 10000) / 10000.0 // 10초 주기로 이동
        
        val newLat = currentPoint.latitude + (nextPoint.latitude - currentPoint.latitude) * progress
        val newLng = currentPoint.longitude + (nextPoint.longitude - currentPoint.longitude) * progress
        
        // 방향 계산
        val heading = atan2(nextPoint.longitude - currentPoint.longitude, nextPoint.latitude - currentPoint.latitude) * 180 / PI
        
        // 다음 지점에 도달하면 인덱스 증가
        if (progress >= 0.99) {
            currentRouteIndex++
        }
        
        return Location(
            latitude = newLat,
            longitude = newLng,
            heading = heading,
            accuracy = 5.0
        )
    }
    
    private fun calculateSpeed(newLocation: Location): Double {
        val currentLocation = currentVehicleStatus.vehicleData.location
        val distance = calculateDistance(currentLocation, newLocation)
        return distance * 3600 // m/s를 km/h로 변환
    }
    
    private fun calculateDistance(loc1: Location, loc2: Location): Double {
        val r = 6371000 // 지구 반지름 (미터)
        val lat1 = loc1.latitude * PI / 180
        val lat2 = loc2.latitude * PI / 180
        val deltaLat = (loc2.latitude - loc1.latitude) * PI / 180
        val deltaLng = (loc2.longitude - loc1.longitude) * PI / 180
        
        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(lat1) * cos(lat2) * sin(deltaLng / 2) * sin(deltaLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        return r * c
    }
    
    private fun updateMediaInfo(): MediaInfo {
        val current = currentVehicleStatus.mediaInfo
        val now = LocalDateTime.now()
        
        // 음악 재생 시뮬레이션
        val isPlaying = current.isPlaying
        val currentTime = if (isPlaying) {
            (current.currentTime + 1) % current.duration
        } else {
            current.currentTime
        }
        
        return MediaInfo(
            isPlaying = isPlaying,
            currentTrack = "Shape of You",
            artist = "Ed Sheeran",
            album = "÷ (Divide)",
            duration = 233, // 3분 53초
            currentTime = currentTime,
            volume = current.volume,
            source = "Bluetooth"
        )
    }
    
    private fun updateClimateControl(): ClimateControl {
        val current = currentVehicleStatus.climateControl
        val outsideTemp = 25.0 + sin(System.currentTimeMillis() / 10000.0) * 5.0 // 외부 온도 시뮬레이션
        
        return ClimateControl(
            isEnabled = current.isEnabled,
            temperature = current.temperature,
            fanSpeed = current.fanSpeed,
            mode = current.mode,
            isAcOn = current.isAcOn,
            isHeatOn = current.isHeatOn,
            isDefrostOn = current.isDefrostOn
        )
    }
    
    private fun updateNavigationInfo(currentLocation: Location): NavigationInfo {
        val destination = "강남역"
        val destinationLocation = Location(37.4979, 127.0276)
        val distance = calculateDistance(currentLocation, destinationLocation)
        
        return NavigationInfo(
            isActive = true,
            destination = destination,
            estimatedTime = (distance / 1000 * 60 / 30).toInt(), // 30km/h 가정
            remainingDistance = distance / 1000,
            currentRoute = routePoints,
            nextTurn = "우회전",
            nextTurnDistance = 0.5
        )
    }
    
    // 미디어 제어 메서드들
    fun togglePlayPause() {
        currentVehicleStatus = currentVehicleStatus.copy(
            mediaInfo = currentVehicleStatus.mediaInfo.copy(
                isPlaying = !currentVehicleStatus.mediaInfo.isPlaying
            )
        )
    }
    
    fun setVolume(volume: Int) {
        currentVehicleStatus = currentVehicleStatus.copy(
            mediaInfo = currentVehicleStatus.mediaInfo.copy(
                volume = volume.coerceIn(0, 100)
            )
        )
    }
    
    // 공조 시스템 제어 메서드들
    fun setClimateTemperature(temperature: Double) {
        currentVehicleStatus = currentVehicleStatus.copy(
            climateControl = currentVehicleStatus.climateControl.copy(
                temperature = temperature.coerceIn(16.0, 30.0)
            )
        )
    }
    
    fun setClimateFanSpeed(fanSpeed: Int) {
        currentVehicleStatus = currentVehicleStatus.copy(
            climateControl = currentVehicleStatus.climateControl.copy(
                fanSpeed = fanSpeed.coerceIn(1, 5)
            )
        )
    }
    
    fun toggleClimateMode() {
        val currentMode = currentVehicleStatus.climateControl.mode
        val newMode = when (currentMode) {
            "Auto" -> "Manual"
            "Manual" -> "Defrost"
            else -> "Auto"
        }
        
        currentVehicleStatus = currentVehicleStatus.copy(
            climateControl = currentVehicleStatus.climateControl.copy(
                mode = newMode
            )
        )
    }
    
    // 데이터베이스에 차량 데이터 저장
    private fun saveVehicleDataToDatabase(vehicleData: VehicleData) {
        try {
            val entity = VehicleDataEntity(
                vehicleId = vehicleData.id,
                speed = vehicleData.speed,
                rpm = vehicleData.rpm,
                fuelLevel = vehicleData.fuelLevel,
                engineTemperature = vehicleData.engineTemperature,
                batteryVoltage = vehicleData.batteryVoltage,
                latitude = vehicleData.location.latitude,
                longitude = vehicleData.location.longitude,
                altitude = vehicleData.location.altitude,
                heading = vehicleData.location.heading,
                accuracy = vehicleData.location.accuracy,
                isEngineRunning = vehicleData.isEngineRunning,
                gearPosition = vehicleData.gearPosition,
                odometer = vehicleData.odometer,
                tripDistance = vehicleData.tripDistance,
                averageSpeed = vehicleData.averageSpeed,
                fuelConsumption = vehicleData.fuelConsumption,
                timestamp = vehicleData.timestamp
            )
            vehicleDataRepository.save(entity)
        } catch (e: Exception) {
            // 로그만 출력하고 애플리케이션 중단 방지
            println("Failed to save vehicle data: ${e.message}")
        }
    }
    
    // 최근 차량 데이터 조회
    fun getRecentVehicleData(vehicleId: String, limit: Int = 100): List<VehicleDataEntity> {
        return vehicleDataRepository.findByVehicleIdOrderByTimestampDesc(vehicleId).take(limit)
    }
    
    // 특정 기간의 차량 데이터 조회
    fun getVehicleDataByPeriod(vehicleId: String, startTime: LocalDateTime, endTime: LocalDateTime): List<VehicleDataEntity> {
        return vehicleDataRepository.findByVehicleIdAndTimestampBetweenOrderByTimestampDesc(vehicleId, startTime, endTime)
    }
    
    // 평균 속도 조회
    fun getAverageSpeed(vehicleId: String, startTime: LocalDateTime): Double? {
        return vehicleDataRepository.getAverageSpeedByVehicleId(vehicleId, startTime)
    }
    
    // 총 주행 거리 조회
    fun getTotalDistance(vehicleId: String, startTime: LocalDateTime): Double? {
        return vehicleDataRepository.getTotalDistanceByVehicleId(vehicleId, startTime)
    }
} 