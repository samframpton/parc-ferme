package sam.frampton.parcferme.api

import com.google.gson.annotations.SerializedName

data class DriverStandingDto(
    val position: Int,
    val points: Double,
    val wins: Int,
    @SerializedName("Driver")
    val driver: DriverDto,
    @SerializedName("Constructors")
    val constructors: List<ConstructorDto>
)