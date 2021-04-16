package sam.frampton.parcferme.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Constructor(
    val constructorId: String,
    val name: String,
    val nationality: String,
    val url: String?
) : Comparable<Constructor>, Parcelable {
    override fun compareTo(other: Constructor): Int = name.compareTo(other.name, true)
}