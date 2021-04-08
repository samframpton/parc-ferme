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
    val time: Time?,
    @SerializedName("FastestLap")
    val fastestLap: FastestLap?
)

data class Time(
    val time: String,
    val millis: Int?
)

data class FastestLap(
    val rank: Int,
    val lap: Int,
    @SerializedName("Time")
    val time: Time,
    @SerializedName("AverageSpeed")
    val averageSpeed: AverageSpeed
)

data class AverageSpeed(
    val units: String,
    val speed: Double
)