package com.recoveryrecord.surveyandroid.example.util

import com.recoveryrecord.surveyandroid.example.model.Media
import com.recoveryrecord.surveyandroid.example.model.MediaType

fun parseTabArray(tabString: String): Array<String> {
    // Split the input string into individual tab entries
    val tabEntries = tabString.split(", ")

    // Create a list to store the tab names in the desired order
    val tabNames = Array(tabEntries.size) { "" }

    // Iterate through the tab entries to extract the tab names
    for (entry in tabEntries) {
        // Split each entry into a numeric order value and tab name
        val parts = entry.split(" ")
        if (parts.size == 2) {
            val order = parts[1]
            val media = parts[0]

            // Add the tab name to the list in the specified order
            order.toIntOrNull()?.let {
                tabNames[it - 1] = media
            }
        }
    }
    return tabNames
}

fun parseToString(tabArray: ArrayList<Media>): String {
    val tabEntries =
        tabArray.mapIndexed { index, tabName ->
            "${tabName.media} ${index + 1}" // Reconstruct each tab entry
        }
    return tabEntries.joinToString(", ") // Join tab entries with a comma and space
}

/*
    For push media
 */
fun convertToIdArray(mediaNames: String): List<String?> {
    val res = mutableListOf<String>()
    mediaNames.split(",").forEach {
        MediaType.mediasIDMap[it]?.apply { res.add(this) }
    }
    return res.toList()
}
