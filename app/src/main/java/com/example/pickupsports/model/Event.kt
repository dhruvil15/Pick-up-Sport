package com.example.pickupsports.model

import android.location.Location

/**
 * An event object
 */
class Event (
    val eventId: Int? = null,
    val owner: UserData? = null,
    val location_text: String? = null,
    val location: Location? = null,
    val date: String? = null,
    val sportName: String? = null,
    val capacity: Int? = null,
    val levelOfPlay: String? = null,
    val participants: ArrayList<UserData>? = null
) {

}