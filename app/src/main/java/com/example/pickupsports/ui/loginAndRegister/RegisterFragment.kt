package com.example.pickupsports.ui.loginAndRegister

import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pickupsports.R

import com.example.pickupsports.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.format.DateTimeFormatter
import java.util.*

class RegisterFragment : Fragment() {
    private var TAG: String = "Register"
    private var _binding: FragmentRegisterBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val registerButton = binding.register
        val phoneButton = binding.phoneNumber
        val dob = binding.dateOfBirth
        val fullName = binding.name

        //https://www.geeksforgeeks.org/how-to-popup-datepicker-while-clicking-on-edittext-in-android/
        dob.setOnClickListener {

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
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dob.setText(dat)
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


        registerButton.setOnClickListener {
            Log.d(TAG, "Vro")
            auth.createUserWithEmailAndPassword(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            ).addOnCompleteListener() { task ->
                if (task.isSuccessful) {

                    uploadUserData(fullName.text.toString(),
                        phoneButton.text.toString(),
                        dob.text.toString())
                    // Register success
                    Log.d(TAG, "createUserWithEmail:success")
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                } else {
                    // If register fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity, "Registration failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun uploadUserData(fullName: String, phoneNumber: String, dob: String) {
        var firstName: String = fullName.split(" ")[0]
        var lastName: String = fullName.split(" ")[1]

        var user: UserData = UserData(phoneNumber, firstName, lastName, dob)
        var userID = auth.currentUser?.uid
        userID?.let { database.child("users").child(it) }?.setValue(user)

    }
}
