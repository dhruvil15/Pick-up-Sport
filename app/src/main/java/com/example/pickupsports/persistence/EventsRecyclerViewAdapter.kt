package com.example.pickupsports.persistence

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pickupsports.R
import com.example.pickupsports.model.Event
import com.example.pickupsports.ui.loginAndRegister.UserData
import com.firebase.ui.auth.data.model.User

class EventsRecyclerViewAdapter() : RecyclerView.Adapter<EventsRecyclerViewAdapter.EventListItem>() {
    private var events = emptyList<Event>()

    inner class EventListItem(eventListItemView : View?) : RecyclerView.ViewHolder(eventListItemView!!){
        val eventCard : CardView? = eventListItemView?.findViewById(R.id.eventCard)
        val eventTitleText : TextView? = eventListItemView?.findViewById(R.id.eventTitle)
        val eventIcon : ImageView? = eventListItemView?.findViewById(R.id.sportIcon)
        val eventLocation: TextView? = eventListItemView?.findViewById(R.id.eventLocation)
        val eventTime: TextView? = eventListItemView?.findViewById(R.id.eventTime)
        val eventDate: TextView? = eventListItemView?.findViewById(R.id.eventDate)
        val availability: TextView? = eventListItemView?.findViewById(R.id.numberOfPlayer)
        val levelOfPlay: TextView? = eventListItemView?.findViewById(R.id.levelOfPlay)
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
        // set event icon accordingly
        holder.eventIcon?.setImageResource(getIcon("cycling"))

        // set time, date, location, availability and level of play
        // TODO: uncomment here when event is implemented
//        holder.eventTime?.text = event.eventTime
//        holder.eventDate?.text = event.date
//        holder.eventLocation?.text = event.location_text
//        holder.availability?.text = event.participants.size + "/" + event.capacity
//        holder.levelOfPlay?.text = event.levelOfPlay
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

    /**
     * Take in [eventName] and get the corresponding icon name
     * for demo purpose, only 5 events are selectable (and plus one default icon)
     * @return icon
     */
    fun getIcon(eventName: String): Int {
        return when (eventName) {
            "tennis" -> R.drawable.tennis_icon
            "football" -> R.drawable.football_icon
            "basketball" -> R.drawable.basketball_icon
            "cycling" -> R.drawable.cycling_icon
            "baseball" -> R.drawable.baseball_icon
            else -> R.drawable.default_icon
        }
    }


}