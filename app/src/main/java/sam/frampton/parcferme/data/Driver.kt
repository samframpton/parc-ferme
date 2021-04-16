package sam.frampton.parcferme.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Driver(
    val driverId: String,
    val givenName: String,
    val familyName: String,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val code: String?,
    val permanentNumber: Int?,
    val url: String?
) : Comparable<Driver>, Parcelable {
    override fun compareTo(other: Driver): Int =
        Comparator.comparing(Driver::familyName, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(Driver::givenName, String.CASE_INSENSITIVE_ORDER)
            .compare(this, other)
}