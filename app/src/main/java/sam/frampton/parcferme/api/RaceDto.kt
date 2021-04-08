package sam.frampton.parcferme.api

import com.google.gson.annotations.SerializedName

data class RaceDto(
    val season: Int,
    val round: Int,
    val raceName: String,
    @SerializedName("Circuit")
    val circuit: CircuitDto,
    val date: String,
    val time: String?,
    val url: String?,
    @SerializedName("Results")
    val results: List<RaceResultDto>,
    @SerializedName("QualifyingResults")
    val qualifyingResults: List<QualifyingResultDto>
)