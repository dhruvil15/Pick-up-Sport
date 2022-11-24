package com.example.pickupsports.persistence

import android.content.ContentValues.TAG
import android.util.Log
import com.example.pickupsports.model.Event
import com.example.pickupsports.ui.loginAndRegister.UserData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

/**
 * Retrieve event data from db and store them into the [events] ArrayList
 */
object EventsStorage {

    private var database: DatabaseReference = Firebase.database.reference
    val events = ArrayList<Event>()

    init {

//        for (i in 0 until 4){
//            events.add(Event(null,"Event #$i", "Body #$i"))
//        }
        // TODO: can't init at first
        getEvents()
        Log.w(TAG, "ArraySize: " + events.size)
    }

    /**
     * get all exist events from db, return an ArrayList of events
     */
    private fun getEvents() {
        Log.w(TAG, "getEvents\n")
        database.child("events").get().addOnSuccessListener {
            if (it.exists()){
                Log.w(TAG,"Test: \n" + it.children.toList().forEach{
                  Log.w(TAG, "Item: ${it.key}")
                    events.add(buildEvent(it))
                })
            } else {
                Log.w(TAG, "Event Information not found.")
            }
        }
    }

    /**
     * helper function
     * build a single Event entity and return it
     */
    private fun buildEvent(entry: DataSnapshot): Event {
        // event info
        val owner: UserData? = null
        val eventId: String? = entry.key
        val location_text: String? = entry.child("location_text").value.toString()
        val location: LatLng? = null
        val time: String? = entry.child("time").value.toString()
        val date: String? = entry.child("date").value.toString()
        val sportName: String? = entry.child("sportName").value.toString()
        val capacity: Int? = entry.child("capacity").value.toString().toInt()
        val levelOfPlay: String? = entry.child("levelOfPlay").value.toString()
        val notice: String? = entry.child("notice").value.toString()

        return Event(owner, eventId, location_text, location, time, date, sportName, capacity, levelOfPlay, notice)
    }


}