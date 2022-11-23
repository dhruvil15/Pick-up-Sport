package com.example.pickupsports.persistence

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pickupsports.R
import com.example.pickupsports.model.Event

class EventsRecyclerViewAdapter() : RecyclerView.Adapter<EventsRecyclerViewAdapter.EventListItem>() {
    private var events = emptyList<Event>()

    inner class EventListItem(eventListItemView : View?) : RecyclerView.ViewHolder(eventListItemView!!){
        val eventTitleText : TextView? = eventListItemView?.findViewById(R.id.eventTitle)
        val eventBodyText : TextView? = eventListItemView?.findViewById(R.id.eventDate)
        val eventCard : CardView? = eventListItemView?.findViewById(R.id.eventCard)
        var eventPosition = 0

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListItem {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val eventListView : View = layoutInflater.inflate(R.layout.event_view, parent, false)
        return EventListItem(eventListView)
    }

    override fun onBindViewHolder(holder: EventListItem, position: Int) {
        val event : Event = events[position]
        holder.eventTitleText?.text = event.eventTitle
        holder.eventBodyText?.text = event.eventBody

        // click to modify a selected event
        holder.eventCard?.setOnClickListener {
            it.findNavController().navigate(R.id.action_HomeFragment_to_CreateEventFragment)
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun setEvents(events : List<Event>){
        this.events = events;
        notifyDataSetChanged()
    }


}