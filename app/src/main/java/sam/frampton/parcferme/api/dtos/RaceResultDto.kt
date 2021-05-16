package sam.frampton.parcferme.api.dtos

import com.google.gson.annotations.SerializedName

data class RaceResultDto(
    val number: Int,
    val position: Int,
    val positionText: String,
    val points: Double,
    @SerializedName("Driver")
    val driver: DriverDto,
    @SerializedName("Constructor")
    val constructor: ConstructorDto,
    val grid: Int,
    val laps: Int,
    val status: String,
    @SerializedName("Time")
    val time: TimeDto?,
    @SerializedName("FastestLap")
    val fastestLap: FastestLapDto?
)

data class TimeDto(
    val time: String,
    val millis: Int?
)

data class FastestLapDto(
    val rank: Int,
    val lap: Int,
    @SerializedName("Time")
    val time: TimeDto,
    @SerializedName("AverageSpeed")
    val averageSpeed: AverageSpeedDto
)

data class AverageSpeedDto(
    val units: String,
    val speed: Double
)