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
    "Australia" to R.drawable.ic_au,
    "Austria" to R.drawable.ic_at,
    "Azerbaijan" to R.drawable.ic_az,
    "Bahrain" to R.drawable.ic_bh,
    "Belgium" to R.drawable.ic_be,
    "Brazil" to R.drawable.ic_br,
    "Canada" to R.drawable.ic_ca,
    "China" to R.drawable.ic_cn,
    "France" to R.drawable.ic_fr,
    "Germany" to R.drawable.ic_de,
    "Hungary" to R.drawable.ic_hu,
    "India" to R.drawable.ic_in,
    "Italy" to R.drawable.ic_it,
    "Japan" to R.drawable.ic_jp,
    "Korea" to R.drawable.ic_kr,
    "Malaysia" to R.drawable.ic_my,
    "Mexico" to R.drawable.ic_mx,
    "Monaco" to R.drawable.ic_mc,
    "Netherlands" to R.drawable.ic_nl,
    "Portugal" to R.drawable.ic_pt,
    "Russia" to R.drawable.ic_ru,
    "Saudi Arabia" to R.drawable.ic_sa,
    "Singapore" to R.drawable.ic_sg,
    "Spain" to R.drawable.ic_es,
    "Turkey" to R.drawable.ic_tr,
    "UAE" to R.drawable.ic_ae,
    "UK" to R.drawable.ic_gb,
    "USA" to R.drawable.ic_us
)

private val nationalityFlags = mapOf(
    "American" to R.drawable.ic_us,
    "Australian" to R.drawable.ic_au,
    "Austrian" to R.drawable.ic_at,
    "Belgian" to R.drawable.ic_be,
    "Brazilian" to R.drawable.ic_br,
    "British" to R.drawable.ic_gb,
    "Canadian" to R.drawable.ic_ca,
    "Danish" to R.drawable.ic_dk,
    "Dutch" to R.drawable.ic_nl,
    "Finnish" to R.drawable.ic_fi,
    "French" to R.drawable.ic_fr,
    "German" to R.drawable.ic_de,
    "Indian" to R.drawable.ic_in,
    "Indonesian" to R.drawable.ic_id,
    "Italian" to R.drawable.ic_it,
    "Japanese" to R.drawable.ic_jp,
    "Malaysian" to R.drawable.ic_my,
    "Mexican" to R.drawable.ic_mx,
    "Monegasque" to R.drawable.ic_mc,
    "New Zealander" to R.drawable.ic_nz,
    "Polish" to R.drawable.ic_pl,
    "Portuguese" to R.drawable.ic_pt,
    "Russian" to R.drawable.ic_ru,
    "Spanish" to R.drawable.ic_es,
    "Swedish" to R.drawable.ic_se,
    "Swiss" to R.drawable.ic_ch,
    "Thai" to R.drawable.ic_th,
    "Venezuelan" to R.drawable.ic_ve
)

@BindingAdapter("country")
fun ImageView.setCountry(country: String) {
    this.setImageResource(countryFlags[country] ?: R.drawable.ic_default_flag)
    this.contentDescription = country
}

@BindingAdapter("nationality")
fun ImageView.setNationality(nationality: String) {
    this.setImageResource(nationalityFlags[nationality] ?: R.drawable.ic_default_flag)
    this.contentDescription = nationality
}

@BindingAdapter("season")
fun TextView.setSeason(season: Int) {
    this.text = season.toString()
}

@BindingAdapter("driverName")
fun TextView.setDriverName(driver: Driver) {
    this.text = this.context.getString(
        R.string.driver_full_name,
        driver.givenName,
        driver.familyName
    )
}

@BindingAdapter("driverNumber")
fun TextView.setDriverNumber(driver: Driver) {
    this.minWidth = measureText(this, "88")
    this.visibility = driver.permanentNumber?.let {
        this.text = it.toString()
        View.VISIBLE
    } ?: View.GONE
}

@BindingAdapter("birthDate")
fun TextView.setBirthDate(birthDate: LocalDate) {
    this.text = this.context.getString(
        R.string.birth_date,
        birthDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
    )
}

@BindingAdapter("driverChampionships")
fun TextView.setDriverChampionships(driverStandings: List<DriverStanding>?) {
    this.text = driverStandings?.count { it.position == 1 }?.let {
        this.context.getString(R.string.championships, it)
    } ?: ""
}

@BindingAdapter("driverWins")
fun TextView.setDriverWins(driverStandings: List<DriverStanding>?) {
    this.text = driverStandings?.sumOf { it.wins }?.let {
        this.context.resources.getQuantityString(R.plurals.wins, it, it)
    } ?: ""
}

@BindingAdapter("constructors")
fun TextView.setConstructors(constructors: List<Constructor>) {
    this.text = constructors.map(Constructor::name).joinToString()
}

@BindingAdapter("constructorChampionships")
fun TextView.setConstructorChampionships(constructorStandings: List<ConstructorStanding>?) {
    this.text = constructorStandings?.count { it.position == 1 }?.let {
        this.context.getString(R.string.championships, it)
    } ?: ""
}

@BindingAdapter("constructorWins")
fun TextView.setConstructorWins(constructorStandings: List<ConstructorStanding>?) {
    this.text = constructorStandings?.sumOf { it.wins }?.let {
        this.context.resources.getQuantityString(R.plurals.wins, it, it)
    } ?: ""
}

@BindingAdapter("position")
fun TextView.setPosition(position: Int) {
    this.minWidth =
        measureText(this, this.context.getString(R.string.ordinal_other, 88))
    this.text =
        if (position > 10 && position.toString().let { it[it.length - 2] == '1' }) {
            this.context.getString(R.string.ordinal_other, position)
        } else {
            when (position.toString().last()) {
                '1' -> this.context.getString(R.string.ordinal_one, position)
                '2' -> this.context.getString(R.string.ordinal_two, position)
                '3' -> this.context.getString(R.string.ordinal_three, position)
                else -> this.context.getString(R.string.ordinal_other, position)
            }
        }
}

@BindingAdapter("points")
fun TextView.setPoints(points: Double) {
    this.minWidth =
        measureText(this, this.context.getString(R.string.points, "888.5"))
    this.text =
        this.context.getString(
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
    this.minWidth = measureText(
        this,
        this.context.resources.getQuantityString(R.plurals.wins, 88, 88)
    )
    this.text = this.context.resources.getQuantityString(R.plurals.wins, wins, wins)
}

@BindingAdapter("dateShort")
fun TextView.setDateShort(date: LocalDate) {
    this.text = date.format(DateTimeFormatter.ofPattern("MMM dd"))
}

@BindingAdapter("dateFull")
fun TextView.setDateFull(date: LocalDate) {
    this.text = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
}

@BindingAdapter("timeFull")
fun TextView.setTimeFull(time: LocalTime?) {
    this.visibility = time?.let {
        this.text = this.context.getString(
            R.string.time_full,
            time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        )
        View.VISIBLE
    } ?: View.GONE
}

@BindingAdapter("raceTime")
fun TextView.setRaceTime(raceResult: RaceResult) {
    this.width = measureText(this, "8:88:88.888")
    this.text =
        if (!raceResult.time.isNullOrEmpty()) {
            raceResult.time
        } else {
            raceResult.status
        }
}

@BindingAdapter("qualifyingTime")
fun TextView.setQualifyingTime(qualifyingResult: QualifyingResult) {
    this.width = measureText(this, "8:88.888")
    this.text =
        if (!qualifyingResult.q3.isNullOrEmpty()) {
            qualifyingResult.q3
        } else if (!qualifyingResult.q2.isNullOrEmpty()) {
            qualifyingResult.q2
        } else if (!qualifyingResult.q1.isNullOrEmpty()) {
            qualifyingResult.q1
        } else {
            this.context.getString(R.string.no_time)
        }
}

private fun measureText(textView: TextView, text: String): Int {
    return TextPaint().let {
        it.textSize = textView.textSize
        it.measureText(text)
    }.toInt()
}