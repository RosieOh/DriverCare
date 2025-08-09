package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.viewmodel.VehicleViewModel

@Composable
fun VehicleDashboard(
    vehicleViewModel: VehicleViewModel,
    modifier: Modifier = Modifier
) {
    val vehicleStatus by vehicleViewModel.vehicleStatus.collectAsState()
    val isSimulationRunning by vehicleViewModel.isSimulationRunning.collectAsState()
    val isLoading by vehicleViewModel.isLoading.collectAsState()
    val error by vehicleViewModel.error.collectAsState()
    
    LaunchedEffect(Unit) {
        vehicleViewModel.fetchVehicleStatus()
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E3C72))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 헤더
        Text(
            text = "🚗 IVI 시뮬레이터",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 시뮬레이션 제어 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { 
                    if (isSimulationRunning) {
                        vehicleViewModel.stopSimulation()
                    } else {
                        vehicleViewModel.startSimulation()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSimulationRunning) Color(0xFFFF4444) else Color(0xFF00D4FF)
                )
            ) {
                Text(
                    text = if (isSimulationRunning) "시뮬레이션 중지" else "시뮬레이션 시작",
                    color = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 로딩 상태
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        
        // 에러 메시지
        error?.let { errorMessage ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF4444))
            ) {
                Text(
                    text = errorMessage,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        // 차량 상태 정보
        vehicleStatus?.let { status ->
            // 속도 표시
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A5298))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${status.vehicleData.speed.toInt()}",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00FF88)
                    )
                    Text(
                        text = "km/h",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
            
            // 차량 정보 그리드
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(200.dp)
            ) {
                item {
                    InfoCard(
                        title = "RPM",
                        value = "${status.vehicleData.rpm}",
                        color = Color(0xFF00D4FF)
                    )
                }
                item {
                    InfoCard(
                        title = "연료",
                        value = "${status.vehicleData.fuelLevel.toInt()}%",
                        color = Color(0xFFFF8800)
                    )
                }
                item {
                    InfoCard(
                        title = "엔진 온도",
                        value = "${status.vehicleData.engineTemperature.toInt()}°C",
                        color = Color(0xFFFF4444)
                    )
                }
                item {
                    InfoCard(
                        title = "배터리",
                        value = "${status.vehicleData.batteryVoltage}V",
                        color = Color(0xFF00FF88)
                    )
                }
            }
            
            // 미디어 제어
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A5298))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎵 미디어",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${status.mediaInfo.currentTrack} - ${status.mediaInfo.artist}",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "음량: ${status.mediaInfo.volume}%",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { vehicleViewModel.toggleMedia() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D4FF))
                        ) {
                            Text(
                                text = if (status.mediaInfo.isPlaying) "일시정지" else "재생",
                                color = Color.White
                            )
                        }
                        Button(
                            onClick = { vehicleViewModel.setVolume(50) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D4FF))
                        ) {
                            Text(text = "음량 50%", color = Color.White)
                        }
                    }
                }
            }
            
            // 공조 시스템 제어
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A5298))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "❄️ 공조 시스템",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "온도: ${status.climateControl.temperature.toInt()}°C",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "팬: ${status.climateControl.fanSpeed}단계",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "모드: ${status.climateControl.mode}",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { vehicleViewModel.setTemperature(22.0) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D4FF))
                        ) {
                            Text(text = "22°C", color = Color.White)
                        }
                        Button(
                            onClick = { vehicleViewModel.setFanSpeed(3) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D4FF))
                        ) {
                            Text(text = "팬 3단계", color = Color.White)
                        }
                        Button(
                            onClick = { vehicleViewModel.toggleClimateMode() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D4FF))
                        ) {
                            Text(text = "모드 변경", color = Color.White)
                        }
                    }
                }
            }
            
            // 경고 메시지
            if (status.warnings.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFF4444))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "⚠️ 경고",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        status.warnings.forEach { warning ->
                            Text(
                                text = warning,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A5298)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
} 