package sam.frampton.parcferme.fragments

import android.content.Context
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sam.frampton.parcferme.R
import sam.frampton.parcferme.data.Driver

fun RecyclerView.setOrientedLayoutManager() {
    layoutManager =
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager(context)
        } else {
            GridLayoutManager(context, 2)
        }
}

fun Driver.getTitle(context: Context): String {
    return permanentNumber?.let {
        context.getString(R.string.driver_full_name_and_number, it, givenName, familyName)
    } ?: context.getString(R.string.driver_full_name, givenName, familyName)
}