package com.example.pickupsports

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickupsports.databinding.FragmentHomeBinding
import com.example.pickupsports.model.Event
import com.example.pickupsports.persistence.EventsRecyclerViewAdapter
import com.example.pickupsports.persistence.EventsStorage
import com.example.pickupsports.model.UserData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var dbref: DatabaseReference

    private lateinit var eventList: ArrayList<Event>
    private lateinit var recylerView : RecyclerView

    /**
     * Part of the Search and Filter Feature - incomplete
     */
//    private lateinit var tempEventList: ArrayList<Event>

    // This property is only valid between onCreateView and
    private val binding get() = _binding!!

    /**
     * Part of the Search and Filter Feature - incomplete
     */
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventList = arrayListOf<Event>()
//        tempEventList = arrayListOf<Event>()

        recylerView = view.findViewById(R.id.eventRV)
        recylerView.layoutManager = LinearLayoutManager(activity)

        val recyclerViewAdapter = EventsRecyclerViewAdapter()
        recylerView.adapter = recyclerViewAdapter
        getEvents(recyclerViewAdapter)
        Log.i(TAG, "events: " + EventsStorage.events)
    }

    /**
     * Part of the Search and Filter Feature - incomplete
     */
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_item, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//
//        val item = menu.findItem(R.id.search_action)
//        val searchView = item.actionView as SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                tempEventList.clear()
//                val searchText = newText!!.lowercase(Locale.getDefault())
//                if(searchText.isNotEmpty()){
//                    eventList.forEach{
//                        if(it.sportName!!.lowercase(Locale.getDefault()).contains(searchText)){
//                            tempEventList.add(it)
//                        }
//                    }
//                    recylerView.adapter!!.notifyDataSetChanged()
//                }
//                else{
//                    tempEventList.clear()
//                    tempEventList.addAll(eventList)
//                    recylerView.adapter!!.notifyDataSetChanged()
//                }
//                return false
//            }
//
//        })
//    }
    // onDestroyView.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * get all exist events from db, return an ArrayList of events
     */
    private fun getEvents(adapter: EventsRecyclerViewAdapter) {
        dbref = FirebaseDatabase.getInstance().getReference("events")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    for (eventSnapshot in snapshot.children) {
                        eventList.add(buildEvent(eventSnapshot))
                    }

                    /**
                     * Part of the Search and Filter Feature - incomplete
                     */
//                    tempEventList.addAll(eventList)
                    adapter.setEvents(eventList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // do nothing
            }
        })
    }

    /**
     * helper function
     * build a single Event entity and return it
     */
    private fun buildEvent(entry: DataSnapshot): Event {
        // event info
        val owner: UserData? = null
        val eventId: String? = entry.key
        val locationText: String? = entry.child("locationText").value.toString()
        val location: LatLng? = null
        val time: String? = entry.child("time").value.toString()
        val date: String? = entry.child("date").value.toString()
        val sportName: String? = entry.child("sportName").value.toString()
        val capacity: Int? = entry.child("capacity").value.toString().toInt()
        val currPlayer: Int? = entry.child("currentPlayer").value.toString().toInt()
        val levelOfPlay: String? = entry.child("levelOfPlay").value.toString()
        val notice: String? = entry.child("notice").value.toString()

        return Event(owner, eventId, locationText, location,date, time, sportName, capacity, currPlayer, levelOfPlay, notice)
    }

}