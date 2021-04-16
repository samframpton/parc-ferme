package sam.frampton.parcferme.data

data class ConstructorStanding(
    val position: Int,
    val points: Double,
    val wins: Int,
    val constructor: Constructor,
    val season: Int
) : Comparable<ConstructorStanding> {
    override fun compareTo(other: ConstructorStanding): Int = position.compareTo(other.position)
}