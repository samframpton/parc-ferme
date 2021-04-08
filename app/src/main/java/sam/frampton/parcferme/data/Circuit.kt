package sam.frampton.parcferme.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Circuit(
    val circuitId: String,
    val circuitName: String,
    val locality: String,
    val country: String,
    val lat: Double,
    val long: Double,
    val url: String?
) : Parcelable