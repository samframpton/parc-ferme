package sam.frampton.parcferme.data

data class QualifyingResult(
    val number: Int,
    val position: Int,
    val driver: Driver,
    val constructor: Constructor,
    val q1: String?,
    val q2: String?,
    val q3: String?
)