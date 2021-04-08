package sam.frampton.parcferme.api.dtos

import com.google.gson.annotations.SerializedName

data class ConstructorStandingDto(
    val position: Int,
    val points: Double,
    val wins: Int,
    @SerializedName("Constructor")
    val constructor: ConstructorDto
)