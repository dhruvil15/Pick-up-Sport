package com.example.pickupsports

import android.annotation.SuppressLint
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
import com.example.pickupsports.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class SummaryFragment : Fragment() {
    private var _binding: FragmentSummaryBinding? = null

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
    private lateinit var eventID: String
    private lateinit var eventOwnerID: String

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        database = Firebase.database.reference

        // check referer
        referer = arguments?.getString("referer").toString()

        //get created event information entered by the user
        eventID = arguments?.getString("eventId").toString()
        eventName = arguments?.getString("eventName").toString()
        time = arguments?.getString("time").toString().plus(", ")
            .plus(arguments?.getString("date").toString())
        location = arguments?.getString("location").toString()
        vacancy = arguments?.get("currentPlayer").toString().plus("/")
            .plus(arguments?.get("capacity").toString())
        level = arguments?.getString("level").toString()
        notice = arguments?.getString("notice").toString()
        eventOwnerID = arguments?.getString("ownerId").toString()


        auth = FirebaseAuth.getInstance()

        // change ui upon user identity
        checkParticipant()

        if (notice.equals("null")) {
            notice = " "
        }

        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * check the source fragment
         * if comes from create, show the create summary
         * else: comes from home page, display the selected event info
         */
        if (referer.equals("create", true)) {

            binding.summaryEventDisplay.text = eventName
            binding.summaryTimeDisplay.text = time
            binding.summaryLocationDisplay.text = location
            binding.summaryVacancyDisplay.text = vacancy
            binding.summaryLevelDisplay.text = level
            binding.summaryNoticeDisplay.text = notice
            binding.summaryEventIDDisplay.text = eventID

            binding.updateQuitBtn.visibility = View.GONE

        } else if (referer.equals("home", true)) {
            // come from home page: view selected event summary
            binding.summaryTitle.text = "Check/Update/Quit"
            val eventID = arguments?.getString("eventId").toString()
            database.child("events").child(eventID).get().addOnSuccessListener {
                if (it.exists()) {
                    eventName = it.child("sportName").value.toString()
                    time = it.child("time").value.toString().plus(", ")
                        .plus(it.child("date").value.toString())
                    location = it.child("locationText").value.toString()
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
        } else {
            Log.w(TAG, "Something went wrong")
        }

        // go back to home page
        view.findViewById<Button>(R.id.back_btn).setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_QuitEvent_or_BackToHome_SummaryFragment_to_HomeFragment)
        }

        // copy event id
        binding.copyEventIDBtn.setOnClickListener {
            copyEventIdToClipboard(binding.summaryEventIDDisplay.text.toString())
        }

        // quit(non-owner user) or update (owner user) action
        view.findViewById<Button>(R.id.update_quit_btn).setOnClickListener {
            if ((binding.updateQuitBtn.text as String).equals(
                    "UPDATE",
                    true
                )
            ) {
                val bundle = Bundle()
                bundle.putString("mode", "update")
                bundle.putString("eventID", eventID)

                it.findNavController()
                    .navigate(R.id.action_ModifyEvent_SummaryFragment_to_CreateEvent, bundle)

            } else if ((binding.updateQuitBtn.text as String).equals(
                    "QUIT",
                    true
                )
            ) {
                database.child("participants").child(eventID).child(auth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        database.child("participants").child(eventID).child(auth.currentUser!!.uid)
                            .removeValue()
                    }
// TODO: remove the event from the upcoming event page
                Toast.makeText(activity, "Event quit successfully!", Toast.LENGTH_LONG).show()

                it.findNavController()
                    .navigate(R.id.action_QuitEvent_or_BackToHome_SummaryFragment_to_HomeFragment)
            } else if ((binding.updateQuitBtn.text as String).equals(
                    "JOIN",
                    true
                )
            ) {
                // join an event
                val userID = auth.currentUser?.uid
                userID?.let { it ->
                    database.child("users").child(it).get().addOnSuccessListener {

                        //create User object for save
                        val owner = UserData(
                            it.child("phoneNumber").value.toString(),
                            it.child("firstName").value.toString(),
                            it.child("lastName").value.toString(),
                            it.child("dob").value.toString(),
                            auth.currentUser?.uid
                        )

                        //add current user to the participants
                        database.child("participants").child(eventID).child(userID).setValue(owner)
                        Toast.makeText(
                            activity,
                            "Event Joined\nNew event added tp the future event list!",
                            Toast.LENGTH_LONG
                        ).show()
                        // TODO: add the event to the upcoming event page
                    }
                }
                // go back to the home page
                it.findNavController()
                    .navigate(R.id.action_QuitEvent_or_BackToHome_SummaryFragment_to_HomeFragment)
            } else {
                Log.e("SummaryFrag", "Update/Join/Quit button")
            }
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
        Log.d("User ID: ", "user id: " + userID)
        return userID == auth.currentUser?.uid
    }

    /**
     * check if the current user is in the viewing event
     */
    private fun checkParticipant() {
        Log.d("event ID ", eventID)
        Log.d("curr user: ", auth.currentUser?.uid.toString())
        Log.d("owner id: ", eventOwnerID)
        database.child("participants/$eventID/${auth.currentUser?.uid}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("curr user: ", snapshot.exists().toString())
                    if (snapshot.exists()) {
                        //Change UI here
                        binding.updateQuitBtn.text = if (isOwner(eventOwnerID)) "UPDATE" else "QUIT"
                    } else {
                        binding.updateQuitBtn.text = "JOIN"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Summary", "Failure checking as participant")
                }
            })
    }
}