package sam.frampton.parcferme.data

data class RaceResult(
    val number: Int,
    val position: Int,
    val positionText: String,
    val points: Double,
    val driver: Driver,
    val constructor: Constructor,
    val grid: Int,
    val laps: Int,
    val status: String,
    val time: String?,
    val millis: Int?,
    val fastestLap: Int?,
    val fastestLapRank: Int?,
    val fastestLapTime: String?,
    val fastestLapAverageSpeed: Double?,
    val fastestLapAverageSpeedUnits: String?
) : Comparable<RaceResult> {
    override fun compareTo(other: RaceResult): Int = position.compareTo(other.position)
}