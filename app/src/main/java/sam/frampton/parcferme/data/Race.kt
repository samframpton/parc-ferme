package sam.frampton.parcferme.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class Race(
    val season: Int,
    val round: Int,
    val raceName: String,
    val circuit: Circuit,
    val date: LocalDate,
    val time: LocalTime?,
    val url: String?
) : Parcelable