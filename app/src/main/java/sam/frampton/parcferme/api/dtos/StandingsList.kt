package sam.frampton.parcferme.api.dtos

import com.google.gson.annotations.SerializedName

data class StandingsList(
    val season: Int,
    val round: Int,
    @SerializedName("DriverStandings")
    val driverStandings: List<DriverStandingDto>?,
    @SerializedName("ConstructorStandings")
    val constructorStandings: List<ConstructorStandingDto>?
)