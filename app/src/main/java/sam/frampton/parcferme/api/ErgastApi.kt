package sam.frampton.parcferme.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import sam.frampton.parcferme.api.dtos.*

class ErgastApi {

    companion object {
        private const val BASE_URL = "https://ergast.com/api/"
        private const val DEFAULT_LIMIT = 100
        private val apiService: ErgastService =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create()
    }

    suspend fun getSeasons(): List<SeasonDto> {
        return apiService.seasons(DEFAULT_LIMIT)
            .motorRacingData.seasonTable?.seasons ?: emptyList()
    }

    suspend fun getDrivers(season: Int): List<DriverDto> {
        return apiService.drivers(season, DEFAULT_LIMIT)
            .motorRacingData
            .driverTable
            ?.drivers
            ?: emptyList()
    }

    suspend fun getConstructors(season: Int): List<ConstructorDto> {
        return apiService.constructors(season, DEFAULT_LIMIT)
            .motorRacingData
            .constructorTable
            ?.constructors
            ?: emptyList()
    }

    suspend fun getRaces(season: Int): List<RaceDto> {
        return apiService.races(season, DEFAULT_LIMIT)
            .motorRacingData
            .raceTable
            ?.races
            ?: emptyList()
    }

    suspend fun getRaceResults(season: Int, round: Int): List<RaceResultDto> {
        return apiService.raceResults(season, round, DEFAULT_LIMIT)
            .motorRacingData
            .raceTable
            ?.races
            ?.firstOrNull()
            ?.results
            ?: emptyList()
    }

    suspend fun getQualifyingResults(season: Int, round: Int): List<QualifyingResultDto> {
        return apiService.qualifyingResults(season, round, DEFAULT_LIMIT)
            .motorRacingData
            .raceTable
            ?.races
            ?.firstOrNull()
            ?.qualifyingResults
            ?: emptyList()
    }

    suspend fun getDriverStandings(season: Int): List<DriverStandingDto> {
        return apiService.driverStandings(season, DEFAULT_LIMIT)
            .motorRacingData
            .standingsTable
            ?.standingsLists
            ?.firstOrNull()
            ?.driverStandings
            ?: emptyList()
    }

    suspend fun getDriverStandings(driverId: String): List<Pair<Int, DriverStandingDto>> {
        return apiService.driverStandings(driverId, DEFAULT_LIMIT)
            .motorRacingData
            .standingsTable
            ?.standingsLists
            ?.mapNotNull { standingsList ->
                standingsList.driverStandings?.firstOrNull()?.let {
                    Pair(standingsList.season, it)
                }
            }
            ?: emptyList()
    }

    suspend fun getConstructorStandings(season: Int): List<ConstructorStandingDto> {
        return apiService.constructorStandings(season, DEFAULT_LIMIT)
            .motorRacingData
            .standingsTable
            ?.standingsLists
            ?.firstOrNull()
            ?.constructorStandings
            ?: emptyList()
    }

    suspend fun getConstructorStandings(
        constructorId: String
    ): List<Pair<Int, ConstructorStandingDto>> {
        return apiService.constructorStandings(constructorId, DEFAULT_LIMIT)
            .motorRacingData
            .standingsTable
            ?.standingsLists
            ?.mapNotNull { standingsList ->
                standingsList.constructorStandings?.firstOrNull()?.let {
                    Pair(standingsList.season, it)
                }
            }
            ?: emptyList()
    }
}