package sam.frampton.parcferme.api.dtos

import com.google.gson.annotations.SerializedName

data class CircuitDto(
    val circuitId: String,
    val circuitName: String,
    @SerializedName("Location")
    val location: LocationDto,
    val url: String?
)

data class LocationDto(
    val locality: String,
    val country: String,
    val lat: Double,
    val long: Double,
)