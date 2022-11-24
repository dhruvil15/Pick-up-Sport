package com.example.pickupsports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickupsports.databinding.FragmentHomeBinding
import com.example.pickupsports.databinding.FragmentSummaryBinding
import com.example.pickupsports.model.Event
import com.example.pickupsports.model.UserData
import com.example.pickupsports.persistence.EventsRecyclerViewAdapter
import com.example.pickupsports.persistence.EventsStorage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class SummaryFragment : Fragment() {
    private var _binding: FragmentSummaryBinding? = null;

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    private lateinit var eventName: String
    private lateinit var time: String
    private lateinit var location: String
    private lateinit var vacancy: String
    private lateinit var level: String
    private lateinit var notice: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        database = Firebase.database.reference

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _eventID = database.child("latestEventPost").get().addOnSuccessListener{

            val eventID = it.child("1").value.toString()

            database.child("events").child(eventID).get().addOnSuccessListener {

                eventName = it.child("sportName").value.toString()
                time = it.child("time").value.toString().plus(", ")
                    .plus(it.child("date").value.toString())
                location = it.child("location_text").value.toString()
                vacancy = it.child("currentPlayer").value.toString().plus("/")
                    .plus(it.child("capacity").value.toString())
                level = it.child("levelOfPlay").value.toString()
                notice = it.child("notice").value.toString()

                binding.summaryEventDisplay.text = eventName
                binding.summaryTimeDisplay.text = time
                binding.summaryLocationDisplay.text = location
                binding.summaryVacancyDisplay.text = vacancy
                binding.summaryLevelDisplay.text = level
                binding.summaryNoticeDisplay.text = notice
                binding.summaryEventIDDisplay.text = eventID

            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}