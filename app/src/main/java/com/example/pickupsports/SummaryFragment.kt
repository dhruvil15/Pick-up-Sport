package com.example.pickupsports

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues.TAG
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pickupsports.databinding.FragmentSummaryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    private lateinit var referer: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        database = Firebase.database.reference

        // check referer
        referer = arguments?.getString("referer").toString()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.updateQuitBtn.text = if (isOwner("userID")) "UPDATE" else "QUIT"

        /**
         * check the source fragment
         * if comes from create, show the create summary
         * else: comes from home page, display the selected event info
         */
        if (referer.equals("create", true)) {
            val _eventID = database.child("latestEventPost").get().addOnSuccessListener {


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
        } else if (referer.equals("home", true)) {
            binding.summaryTitle.text = "Check/Update/Quit"
            val eventID = arguments?.getString("eventId").toString()
            val dbref = FirebaseDatabase.getInstance().getReference("events")
            dbref.child(eventID).get().addOnSuccessListener {
                if (it.exists()) {
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
            // go back to home page
            view.findViewById<Button>(R.id.back_btn).setOnClickListener {
                it.findNavController()
                    .navigate(R.id.action_QuitEvent_or_BackToHome_SummaryFragment_to_HomeFragment)
            }
        } else {
            Log.w(TAG, "Something went wrong")
        }

        // copy event id
        view.findViewById<Button>(R.id.copy_eventID_btn).setOnClickListener {
            copyEventIdToClipboard(binding.summaryEventIDDisplay.text.toString())
        }

        // quit(non-owner user) or update (owner user) action
        view.findViewById<Button>(R.id.update_quit_btn).setOnClickListener {
            // TODO: implement quit event and update event
            if ((binding.updateQuitBtn.text as String).equals(
                    "UPDATE",
                    true
                )
            ) it.findNavController()
                .navigate(R.id.action_ModifyEvent_SummaryFragment_to_CreateEvent) else it.findNavController()
                .navigate(R.id.action_QuitEvent_or_BackToHome_SummaryFragment_to_HomeFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // copy to clipboard
    private fun copyEventIdToClipboard(eventID: String) {

        val clipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("EventID", eventID)
        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(activity, "Event ID copied to clipboard!", Toast.LENGTH_LONG).show()
    }

    // check if the current user enters a summary page that the user is the host(owner)
    // take in the userID and check against the db
    private fun isOwner(userID: String): Boolean {
        return false
    }
}