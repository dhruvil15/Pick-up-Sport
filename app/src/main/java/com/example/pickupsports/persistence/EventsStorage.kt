package com.example.pickupsports.persistence

import com.example.pickupsports.model.Event

/**
 * Retrieve event data from db and store them into the [events] ArrayList
 */
object EventsStorage {

    val events = ArrayList<Event>()

    init {
        for (i in 0 until events.size){
            events.add(Event("Event #$i", "Body #$i"))
        }
    }
}