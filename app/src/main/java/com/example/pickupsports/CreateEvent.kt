package com.example.pickupsports

import android.app.DatePickerDialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.pickupsports.databinding.FragmentCreateEventBinding
import com.example.pickupsports.model.Event
import com.example.pickupsports.model.UserData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateEvent : Fragment(), AdapterView.OnItemSelectedListener{

    private var _binding: FragmentCreateEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var vacancyFrameInput: EditText
    private lateinit var totalNumberFrameInput: EditText
    private lateinit var levelOfPlay: String

    private lateinit var database: DatabaseReference

    private lateinit var mGeocoder : Geocoder
    private lateinit var addressList: List<Address>

    private lateinit var auth: FirebaseAuth
    private lateinit var eventID: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vacancyFrameInput = binding.createFrameInput
        totalNumberFrameInput = binding.createFrameInput2
        mGeocoder = Geocoder(this.context)

        /*
        * dropdown menu
        * https://developer.android.com/develop/ui/views/components/spinner
        * */
        val spinner: Spinner = view.findViewById(R.id.create_level_play)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.level_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        //https://www.geeksforgeeks.org/how-to-popup-datepicker-while-clicking-on-edittext-in-android/
        binding.createInputDate.setOnClickListener {

            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                requireContext(),
                { _, birthYear, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + birthYear)
                    binding.createInputDate.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }

        /*
        * Add & minus button click function
        * */
        binding.createAddBtn.setOnClickListener{addClick(1)}
        binding.createMinusBtn.setOnClickListener{minusClick(1)}
        binding.createAddBtn2.setOnClickListener{addClick(2)}
        binding.createMinusBtn2.setOnClickListener{minusClick(2)}

        binding.createCancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_Cancel_Create_to_HomeFragment)
        }

        binding.createSaveBtn.setOnClickListener {

            /*https://www.javatpoint.com/android-google-map-search-location-using-geocodr*/
            val location = binding.createInputLocation.text.toString()

            try {
                addressList = mGeocoder.getFromLocationName(location.toString(), 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val address = addressList[0]
            val latlng = LatLng(address.latitude, address.longitude)
            val date = binding.createInputDate.text.toString()
            val time = binding.createInputTime.text.toString()
            val sportName = binding.createInputSportName.text.toString()
            val capacity = binding.createFrameInput2.text.toString().toInt()
            val currrentPlayer = binding.createFrameInput.text.toString().toInt()
            val notice = binding.createInputNotice.text.toString()

            eventID = uploadEvent(
                location,
                latlng,
                date,
                time,
                sportName,
                capacity,
                currrentPlayer,
                levelOfPlay,
                notice
            )

            val bundle = packBundle(
                location,
                date,
                time,
                sportName,
                capacity,
                currrentPlayer,
                levelOfPlay,
                notice
            )

            findNavController().navigate(R.id.action_CreateEvent_to_summaryFragment, bundle)
        }

    }

    //https://developer.android.com/develop/ui/views/components/spinner
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        levelOfPlay =  parent.getItemAtPosition(pos).toString()
    }
    //https://developer.android.com/develop/ui/views/components/spinner
    override fun onNothingSelected(parent: AdapterView<*>) {
        levelOfPlay = "Any"
    }

    /*The click function when add button clicked*/
    private fun addClick(fram_num: Int){

        if (fram_num == 1){

            val value : Int = vacancyFrameInput.text.toString().toInt() + 1

            vacancyFrameInput.setText(value.toString())

        }else{

            val value : Int = totalNumberFrameInput.text.toString().toInt() + 1

            totalNumberFrameInput.setText(value.toString())

        }

    }

    /*The click function when minus button clicked*/
    private fun minusClick(fram_num: Int){

        if (fram_num == 1){

            var value : Int = vacancyFrameInput.text.toString().toInt() - 1

            if (value < 0){

                value = 0

            }
            vacancyFrameInput.setText(value.toString())

        }else{

            var value : Int = totalNumberFrameInput.text.toString().toInt() - 1

            if (value < 0){

                value = 0

            }

            totalNumberFrameInput.setText(value.toString())

        }

    }

    private fun uploadEvent(
        location_text: String,
        location: LatLng,
        date: String,
        time: String,
        sportName: String,
        capacity: Int,
        currrentPlayer: Int,
        levelOfPlay: String,
        notice : String
    ): String {

        val userID = auth.currentUser?.uid
        val eventID = database.child("events").push().key

         userID?.let { it ->
            database.child("users").child(it).get().addOnSuccessListener {

                val owner  = UserData(
                    it.child("phoneNumber").value.toString(),
                    it.child("firstName").value.toString(),
                    it.child("lastName").value.toString(),
                    it.child("dob").value.toString()
                )

                val event = Event(
                    owner,
                    eventID,
                    location_text,
                    location,
                    date,
                    time,
                    sportName,
                    capacity,
                    currrentPlayer,
                    levelOfPlay,
                    notice
                )

                if (eventID != null) {
                    database.child("events").child(eventID).setValue(event)
                    database.child("participants").child(eventID).child(userID).setValue(owner)
                }

            }
        }

        return eventID.toString()
    }

    private fun packBundle(
        location: String,
        date: String,
        time: String,
        sportName: String,
        capacity: Int,
        currrentPlayer: Int,
        levelOfPlay: String,
        notice : String
    ): Bundle{

        val bundle = Bundle()
        bundle.putString("location", location)
        bundle.putString("lastEvent", eventID)
        bundle.putString("eventName", sportName)
        bundle.putString("time", time)
        bundle.putString("date", date)
        bundle.putString("level", levelOfPlay)
        bundle.putInt("capacity", capacity)
        bundle.putInt("currentPlayer", currrentPlayer)
        bundle.putString("notice", notice)

        return bundle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

