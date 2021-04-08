package sam.frampton.parcferme.api

import com.google.gson.annotations.SerializedName

data class QualifyingResultDto(
    val number: Int,
    val position: Int,
    @SerializedName("Driver")
    val driver: DriverDto,
    @SerializedName("Constructor")
    val constructor: ConstructorDto,
    @SerializedName("Q1")
    val q1: String?,
    @SerializedName("Q2")
    val q2: String?,
    @SerializedName("Q3")
    val q3: String?
)