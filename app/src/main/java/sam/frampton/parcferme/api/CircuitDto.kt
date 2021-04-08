package sam.frampton.parcferme.api

import com.google.gson.annotations.SerializedName

data class CircuitDto(
    val circuitId: String,
    val circuitName: String,
    @SerializedName("Location")
    val location: Location,
    val url: String?
)

data class Location(
    val locality: String,
    val country: String,
    val lat: Double,
    val long: Double,
)