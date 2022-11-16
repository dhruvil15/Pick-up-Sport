package com.example.pickupsports.persistence

import com.example.pickupsports.model.Event

// TODO: complete the event
object EventsStorage {

    val events = ArrayList<Event>()

    init {
        for (i in 1..2){
            events.add(Event("Event #$i", "Body #$i"))
        }
    }
}