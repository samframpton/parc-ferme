package sam.frampton.parcferme.adapters

import android.text.TextPaint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import sam.frampton.parcferme.R
import sam.frampton.parcferme.data.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val countryFlags = mapOf(
    "American" to R.drawable.ic_us,
    "Argentine" to R.drawable.ic_ar,
    "Australia" to R.drawable.ic_au,
    "Australian" to R.drawable.ic_au,
    "Austria" to R.drawable.ic_at,
    "Austrian" to R.drawable.ic_at,
    "Azerbaijan" to R.drawable.ic_az,
    "Bahrain" to R.drawable.ic_bh,
    "Belgian" to R.drawable.ic_be,
    "Belgium" to R.drawable.ic_be,
    "Brazil" to R.drawable.ic_br,
    "Brazilian" to R.drawable.ic_br,
    "British" to R.drawable.ic_gb,
    "Canada" to R.drawable.ic_ca,
    "Canadian" to R.drawable.ic_ca,
    "China" to R.drawable.ic_cn,
    "Colombian" to R.drawable.ic_co,
    "Czech" to R.drawable.ic_cz,
    "Danish" to R.drawable.ic_dk,
    "Dutch" to R.drawable.ic_nl,
    "Finnish" to R.drawable.ic_fi,
    "France" to R.drawable.ic_fr,
    "French" to R.drawable.ic_fr,
    "German" to R.drawable.ic_de,
    "Germany" to R.drawable.ic_de,
    "Hungarian" to R.drawable.ic_hu,
    "Hungary" to R.drawable.ic_hu,
    "India" to R.drawable.ic_in,
    "Indian" to R.drawable.ic_in,
    "Indonesian" to R.drawable.ic_id,
    "Irish" to R.drawable.ic_ie,
    "Italian" to R.drawable.ic_it,
    "Italy" to R.drawable.ic_it,
    "Japan" to R.drawable.ic_jp,
    "Japanese" to R.drawable.ic_jp,
    "Korea" to R.drawable.ic_kr,
    "Malaysia" to R.drawable.ic_my,
    "Malaysian" to R.drawable.ic_my,
    "Mexican" to R.drawable.ic_mx,
    "Mexico" to R.drawable.ic_mx,
    "Monaco" to R.drawable.ic_mc,
    "Monegasque" to R.drawable.ic_mc,
    "Netherlands" to R.drawable.ic_nl,
    "New Zealander" to R.drawable.ic_nz,
    "Polish" to R.drawable.ic_pl,
    "Portugal" to R.drawable.ic_pt,
    "Portuguese" to R.drawable.ic_pt,
    "Rhodesian" to R.drawable.ic_zw,
    "Russia" to R.drawable.ic_ru,
    "Russian" to R.drawable.ic_ru,
    "Saudi Arabia" to R.drawable.ic_sa,
    "Singapore" to R.drawable.ic_sg,
    "South Africa" to R.drawable.ic_za,
    "South African" to R.drawable.ic_za,
    "Spain" to R.drawable.ic_es,
    "Spanish" to R.drawable.ic_es,
    "Swedish" to R.drawable.ic_se,
    "Swiss" to R.drawable.ic_ch,
    "Thai" to R.drawable.ic_th,
    "Turkey" to R.drawable.ic_tr,
    "UAE" to R.drawable.ic_ae,
    "UK" to R.drawable.ic_gb,
    "Uruguayan" to R.drawable.ic_uy,
    "USA" to R.drawable.ic_us,
    "Venezuelan" to R.drawable.ic_ve
)

@BindingAdapter("countryFlag")
fun ImageView.setCountryFlag(country: String) {
    setImageResource(countryFlags[country] ?: R.drawable.ic_default_flag)
    contentDescription = country
}

@BindingAdapter("season")
fun TextView.setSeason(season: Int) {
    text = season.toString()
}

@BindingAdapter("driverName")
fun TextView.setDriverName(driver: Driver) {
    text = context.getString(R.string.driver_full_name, driver.givenName, driver.familyName)
}

@BindingAdapter("driverNumber")
fun TextView.setDriverNumber(driver: Driver) {
    visibility = driver.permanentNumber?.let {
        minWidth = measureText(this, "88")
        text = it.toString()
        View.VISIBLE
    } ?: View.GONE
}

@BindingAdapter("birthDate")
fun TextView.setBirthDate(birthDate: LocalDate) {
    text = context.getString(
        R.string.birth_date,
        birthDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
    )
}

@BindingAdapter("driverChampionships")
fun TextView.setDriverChampionships(driverStandings: List<DriverStanding>) {
    text = driverStandings.count { it.position == 1 }.let {
        context.resources.getQuantityString(R.plurals.championships, it, it)
    }
}

@BindingAdapter("driverWins")
fun TextView.setDriverWins(driverStandings: List<DriverStanding>) {
    text = driverStandings.sumOf { it.wins }.let {
        context.resources.getQuantityString(R.plurals.wins, it, it)
    }
}

@BindingAdapter("constructors")
fun TextView.setConstructors(constructors: List<Constructor>) {
    text = constructors.map(Constructor::name).joinToString()
}

@BindingAdapter("constructorChampionships")
fun TextView.setConstructorChampionships(constructorStandings: List<ConstructorStanding>) {
    text = constructorStandings.count { it.position == 1 }.let {
        context.resources.getQuantityString(R.plurals.championships, it, it)
    }
}

@BindingAdapter("constructorWins")
fun TextView.setConstructorWins(constructorStandings: List<ConstructorStanding>) {
    text = constructorStandings.sumOf { it.wins }.let {
        context.resources.getQuantityString(R.plurals.wins, it, it)
    }
}

@BindingAdapter("position")
fun TextView.setPosition(position: Int) {
    minWidth = measureText(this, context.getString(R.string.ordinal_other, 88))
    text = if (position > 10 && position.toString().let { it[it.length - 2] == '1' }) {
        context.getString(R.string.ordinal_other, position)
    } else {
        when (position.toString().last()) {
            '1' -> context.getString(R.string.ordinal_one, position)
            '2' -> context.getString(R.string.ordinal_two, position)
            '3' -> context.getString(R.string.ordinal_three, position)
            else -> context.getString(R.string.ordinal_other, position)
        }
    }
}

@BindingAdapter("points")
fun TextView.setPoints(points: Double) {
    minWidth = measureText(this, context.getString(R.string.points, "888.5"))
    text = context.getString(
        R.string.points,
        if (points.rem(1) == 0.0) {
            (points.toInt())
        } else {
            points
        }.toString()
    )
}

@BindingAdapter("wins")
fun TextView.setWins(wins: Int) {
    minWidth = measureText(
        this,
        context.resources.getQuantityString(R.plurals.wins, 88, 88)
    )
    text = context.resources.getQuantityString(R.plurals.wins, wins, wins)
}

@BindingAdapter("dateShort")
fun TextView.setDateShort(date: LocalDate) {
    text = date.format(DateTimeFormatter.ofPattern("MMM dd"))
}

@BindingAdapter("dateFull")
fun TextView.setDateFull(date: LocalDate) {
    text = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
}

@BindingAdapter("timeFull")
fun TextView.setTimeFull(time: LocalTime?) {
    visibility = time?.let {
        text = context.getString(
            R.string.time_full,
            time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        )
        View.VISIBLE
    } ?: View.GONE
}

@BindingAdapter("raceTime")
fun TextView.setRaceTime(raceResult: RaceResult) {
    width = measureText(this, "8:88:88.888")
    text = if (!raceResult.time.isNullOrEmpty()) {
        raceResult.time
    } else {
        raceResult.status
    }
}

@BindingAdapter("qualifyingTime")
fun TextView.setQualifyingTime(qualifyingResult: QualifyingResult) {
    width = measureText(this, "8:88.888")
    text = if (!qualifyingResult.q3.isNullOrEmpty()) {
        qualifyingResult.q3
    } else if (!qualifyingResult.q2.isNullOrEmpty()) {
        qualifyingResult.q2
    } else if (!qualifyingResult.q1.isNullOrEmpty()) {
        qualifyingResult.q1
    } else {
        context.getString(R.string.no_time)
    }
}

private fun measureText(textView: TextView, text: String): Int =
    TextPaint().let {
        it.textSize = textView.textSize
        it.measureText(text)
    }.toInt()