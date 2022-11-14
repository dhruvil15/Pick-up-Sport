package com.example.pickupsports.persistence

import com.example.pickupsports.model.Note

object NotesStorage {

    val notes = ArrayList<Note>()

    init {
        for (i in 1..2){
            notes.add(Note("Note #$i", "Body #$i"))
        }
    }
}