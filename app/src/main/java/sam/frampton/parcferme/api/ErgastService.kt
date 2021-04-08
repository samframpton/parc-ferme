package sam.frampton.parcferme.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sam.frampton.parcferme.api.dtos.ErgastResponse

interface ErgastService {

    @GET("f1/seasons.json")
    suspend fun seasons(
        @Query("limit") limit: Int
    ): ErgastResponse

    @GET("f1/{season}/drivers.json")
    suspend fun drivers(
        @Path("season") season: Int,
        @Query("limit") limit: Int
    ): ErgastResponse

    @GET("f1/{season}/constructors.json")
    suspend fun constructors(
        @Path("season") season: Int,
        @Query("limit") limit: Int
    ): ErgastResponse

    @GET("f1/{season}.json")
    suspend fun races(
        @Path("season") season: Int,
        @Query("limit") limit: Int
    ): ErgastResponse

    @GET("f1/{season}/{round}/results.json")
    suspend fun raceResults(
        @Path("season") season: Int,
        @Path("round") round: Int,
        @Query("limit") limit: Int
    ): ErgastResponse

    @GET("f1/{season}/{round}/qualifying.json")
    suspend fun qualifyingResults(
        @Path("season") season: Int,
        @Path("round") round: Int,
        @Query("limit") limit: Int
    ): ErgastResponse

    @GET("f1/{season}/driverStandings.json")
    suspend fun driverStandings(
        @Path("season") season: Int,
        @Query("limit") limit: Int
    ): ErgastResponse

    @GET("f1/drivers/{driverId}/driverStandings.json")
    suspend fun driverStandings(
        @Path("driverId") driverId: String,
        @Query("limit") limit: Int
    ): ErgastResponse

    @GET("f1/{season}/constructorStandings.json")
    suspend fun constructorStandings(
        @Path("season") season: Int,
        @Query("limit") limit: Int
    ): ErgastResponse

    @GET("f1/constructors/{constructorId}/constructorStandings.json")
    suspend fun constructorStandings(
        @Path("constructorId") constructorId: String,
        @Query("limit") limit: Int
    ): ErgastResponse
}