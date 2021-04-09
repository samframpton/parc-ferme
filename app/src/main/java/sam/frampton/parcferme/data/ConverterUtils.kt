package sam.frampton.parcferme.data

import sam.frampton.parcferme.api.dtos.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val DATE_PATTERN = "yyyy-MM-dd"
const val TIME_PATTERN = "HH:mm:ssX"

fun dateFromString(dateString: String): LocalDate {
    return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_PATTERN))
}

fun timeFromString(timeString: String): LocalTime {
    return LocalTime.parse(timeString, DateTimeFormatter.ofPattern(TIME_PATTERN))
}

fun List<SeasonDto>.toSeasonList(): List<Int> = map { it.season }

fun DriverDto.toDriver(): Driver =
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

fun List<DriverDto>.toDriverList(): List<Driver> = map { it.toDriver() }

fun ConstructorDto.toConstructor(): Constructor =
    Constructor(
        constructorId,
        name,
        nationality,
        url
    )

fun List<ConstructorDto>.toConstructorList(): List<Constructor> = map { it.toConstructor() }

fun List<RaceDto>.toRaceList(): List<Race> = map {
    Race(
        it.season,
        it.round,
        it.raceName,
        it.circuit.toCircuit(),
        dateFromString(it.date),
        it.time?.let { time -> timeFromString(time) },
        it.url
    )
}

fun CircuitDto.toCircuit(): Circuit =
    Circuit(
        circuitId,
        circuitName,
        location.locality,
        location.country,
        location.lat,
        location.long,
        url
    )

fun List<RaceResultDto>.toRaceResultList(): List<RaceResult> = map {
    RaceResult(
        it.number,
        it.position,
        it.positionText,
        it.points,
        it.driver.toDriver(),
        it.constructor.toConstructor(),
        it.grid,
        it.laps,
        it.status,
        it.time?.time,
        it.time?.millis,
        it.fastestLap?.lap,
        it.fastestLap?.rank,
        it.fastestLap?.time?.time,
        it.fastestLap?.averageSpeed?.speed,
        it.fastestLap?.averageSpeed?.units,
    )
}

fun List<QualifyingResultDto>.toQualifyingResultList(): List<QualifyingResult> = map {
    QualifyingResult(
        it.number,
        it.position,
        it.driver.toDriver(),
        it.constructor.toConstructor(),
        it.q1,
        it.q2,
        it.q3
    )
}

fun List<DriverStandingDto>.toDriverStandingList(season: Int): List<DriverStanding> = map {
    DriverStanding(
        it.position,
        it.points,
        it.wins,
        it.driver.toDriver(),
        it.constructors.toConstructorList(),
        season
    )
}

fun List<ConstructorStandingDto>.toConstructorStandingList(season: Int): List<ConstructorStanding> =
    map {
        ConstructorStanding(
            it.position,
            it.points,
            it.wins,
            it.constructor.toConstructor(),
            season
        )
    }