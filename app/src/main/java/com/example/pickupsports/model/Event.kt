package com.example.pickupsports.model

import com.example.pickupsports.ui.loginAndRegister.UserData
import com.google.android.gms.maps.model.LatLng

/**
 * An event object
 * TODO: add features of sports event
 */
data class Event (val owner: UserData? = null,
             val eventId: String? = null,
             val location_text: String? = null,
             val location: LatLng? = null,
             val time: String? = null,
             val date: String? = null,
             val sportName: String? = null,
             val capacity: Int? = null,
             val levelOfPlay: String? = null,
             val notice: String? = null,
)