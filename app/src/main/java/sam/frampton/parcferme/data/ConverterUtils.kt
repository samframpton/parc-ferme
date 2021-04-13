package sam.frampton.parcferme.data

import sam.frampton.parcferme.api.dtos.*
import sam.frampton.parcferme.database.entities.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val DATE_PATTERN = "yyyy-MM-dd"
const val TIME_PATTERN = "HH:mm:ssX"

fun dateFromString(dateString: String): LocalDate =
    LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_PATTERN))

fun timeFromString(timeString: String): LocalTime =
    LocalTime.parse(timeString, DateTimeFormatter.ofPattern(TIME_PATTERN))

fun List<SeasonDto>.toSeasonEntityList(): List<SeasonEntity> =
    map { SeasonEntity(it.season, it.url) }

fun List<SeasonEntity>.toSeasonList(): List<Int> = map { it.season }

fun DriverDto.toDriverEntity(): DriverEntity =
    DriverEntity(
        driverId,
        givenName,
        familyName,
        dateOfBirth,
        nationality,
        code,
        permanentNumber,
        url
    )

fun DriverEntity.toDriver(): Driver =
    Driver(
        driverId,
        givenName,
        familyName,
        dateFromString(dateOfBirth),
        nationality,
        code,
        permanentNumber,
        url
    )

fun List<DriverDto>.toDriverEntityList(): List<DriverEntity> = map { it.toDriverEntity() }

fun List<DriverEntity>.toDriverList(): List<Driver> = map { it.toDriver() }

fun ConstructorDto.toConstructorEntity(): ConstructorEntity =
    ConstructorEntity(
        constructorId,
        name,
        nationality,
        url
    )

fun ConstructorEntity.toConstructor(): Constructor =
    Constructor(
        constructorId,
        name,
        nationality,
        url
    )

fun List<ConstructorDto>.toConstructorEntityList(): List<ConstructorEntity> =
    map { it.toConstructorEntity() }

fun List<ConstructorEntity>.toConstructorList(): List<Constructor> = map { it.toConstructor() }

fun RaceDto.toRaceEntity(): RaceEntity =
    RaceEntity(
        0,
        season,
        round,
        raceName,
        circuit.circuitId,
        date,
        time,
        url
    )

fun List<RaceAndCircuit>.toRaceList(): List<Race> = map {
    Race(
        it.race.season,
        it.race.round,
        it.race.raceName,
        it.circuit.toCircuit(),
        dateFromString(it.race.date),
        it.race.time?.let { time -> timeFromString(time) },
        it.race.url
    )
}

fun CircuitDto.toCircuitEntity(): CircuitEntity =
    CircuitEntity(
        circuitId,
        circuitName,
        location.locality,
        location.country,
        location.lat,
        location.long,
        url
    )

fun CircuitEntity.toCircuit(): Circuit =
    Circuit(
        circuitId,
        circuitName,
        locality,
        country,
        latitude,
        longitude,
        url
    )

fun RaceResultDto.toRaceResultEntity(season: Int, round: Int) =
    RaceResultEntity(
        0,
        number,
        position,
        positionText,
        points,
        driver.driverId,
        constructor.constructorId,
        grid,
        laps,
        status,
        time?.time,
        time?.millis,
        fastestLap?.lap,
        fastestLap?.rank,
        fastestLap?.time?.time,
        fastestLap?.averageSpeed?.speed,
        fastestLap?.averageSpeed?.units,
        season,
        round
    )

fun List<RaceResultAndDriverConstructor>.toRaceResultList(): List<RaceResult> = map {
    RaceResult(
        it.raceResult.number,
        it.raceResult.position,
        it.raceResult.positionText,
        it.raceResult.points,
        it.driver.toDriver(),
        it.constructor.toConstructor(),
        it.raceResult.grid,
        it.raceResult.laps,
        it.raceResult.status,
        it.raceResult.time,
        it.raceResult.millis,
        it.raceResult.fastestLap,
        it.raceResult.fastestLapRank,
        it.raceResult.fastestLapTime,
        it.raceResult.fastestLapAverageSpeed,
        it.raceResult.fastestLapAverageSpeedUnits
    )
}

fun QualifyingResultDto.toQualifyingResultEntity(season: Int, round: Int) =
    QualifyingResultEntity(
        0,
        number,
        position,
        driver.driverId,
        constructor.constructorId,
        q1,
        q2,
        q3,
        season,
        round
    )

fun List<QualifyingResultAndDriverConstructor>.toQualifyingResultList(): List<QualifyingResult> =
    map {
        QualifyingResult(
            it.qualifyingResult.number,
            it.qualifyingResult.position,
            it.driver.toDriver(),
            it.constructor.toConstructor(),
            it.qualifyingResult.q1,
            it.qualifyingResult.q2,
            it.qualifyingResult.q3
        )
    }

fun DriverStandingDto.toDriverStandingEntity(season: Int): DriverStandingEntity =
    DriverStandingEntity(
        0,
        position,
        points,
        wins,
        driver.driverId,
        season
    )

fun List<DriverStandingWithDriverConstructors>.toDriverStandingList(): List<DriverStanding> =
    map {
        DriverStanding(
            it.driverStanding.position,
            it.driverStanding.points,
            it.driverStanding.wins,
            it.driver.toDriver(),
            it.constructors.toConstructorList(),
            it.driverStanding.season
        )
    }

fun ConstructorStandingDto.toConstructorStandingEntity(season: Int): ConstructorStandingEntity =
    ConstructorStandingEntity(
        0,
        position,
        points,
        wins,
        constructor.constructorId,
        season
    )

fun List<ConstructorStandingAndConstructor>.toConstructorStandingList(): List<ConstructorStanding> =
    map {
        ConstructorStanding(
            it.constructorStanding.position,
            it.constructorStanding.points,
            it.constructorStanding.wins,
            it.constructor.toConstructor(),
            it.constructorStanding.season
        )
    }