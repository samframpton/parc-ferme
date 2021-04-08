package sam.frampton.parcferme.api

data class DriverDto(
    val driverId: String,
    val givenName: String,
    val familyName: String,
    val dateOfBirth: String,
    val nationality: String,
    val code: String?,
    val permanentNumber: Int?,
    val url: String?
)