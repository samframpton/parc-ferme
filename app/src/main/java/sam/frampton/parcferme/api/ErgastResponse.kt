package sam.frampton.parcferme.api

import com.google.gson.annotations.SerializedName

data class ErgastResponse(
    @SerializedName("MRData")
    val motorRacingData: MotorRacingData
)

data class MotorRacingData(
    val series: String,
    val url: String,
    val limit: Int,
    val offset: Int,
    val total: Int,
    @SerializedName("SeasonTable")
    val seasonTable: SeasonTable?,
    @SerializedName("DriverTable")
    val driverTable: DriverTable?,
    @SerializedName("ConstructorTable")
    val constructorTable: ConstructorTable?,
    @SerializedName("RaceTable")
    val raceTable: RaceTable?,
    @SerializedName("StandingsTable")
    val standingsTable: StandingsTable?
)

data class SeasonTable(
    @SerializedName("Seasons")
    val seasons: List<SeasonDto>
)

data class DriverTable(
    val season: Int?,
    @SerializedName("Drivers")
    val drivers: List<DriverDto>
)

data class ConstructorTable(
    val season: Int?,
    @SerializedName("Constructors")
    val constructors: List<ConstructorDto>
)

data class RaceTable(
    val season: Int?,
    @SerializedName("Races")
    val races: List<RaceDto>
)

data class StandingsTable(
    val season: Int?,
    @SerializedName("StandingsLists")
    val standingsLists: List<StandingsList>
)