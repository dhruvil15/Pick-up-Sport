package com.example.pickupsports.ui.loginAndRegister

import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pickupsports.R

import com.example.pickupsports.databinding.FragmentRegisterBinding
import com.example.pickupsports.model.UserData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import java.util.*


/**
 * Fragment for creating new users with Firebase Auth and storing additional
 * user fields in realtime database
 */
class RegisterFragment : Fragment() {
    private var TAG: String = "Register"
    private var _binding: FragmentRegisterBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneButton: EditText
    private lateinit var dob: EditText
    private lateinit var fullName: EditText

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

        usernameEditText = binding.username
        passwordEditText = binding.password
        phoneButton = binding.phoneNumber
        dob = binding.dateOfBirth
        fullName = binding.name

        val registerButton = binding.register
        val loginButton = binding.backToLogin

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
                { _, birthYear, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + birthYear)
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
            if(validateFields()) {
                //Create new account with Firebase Auth
                auth.createUserWithEmailAndPassword(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        //Store additional user data to DB
                        notificationSetup()
                        uploadUserData(fullName.toString(), phoneButton.toString(), dob.toString())

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

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun validateFields(): Boolean {
        var validated = true
        if(usernameEditText.length() == 0) {
            usernameEditText.error = "Field is required"
            validated = false
        }
        if(passwordEditText.length() == 0) {
            passwordEditText.error = "Field is required"
            validated = false
        }
        if(phoneButton.length() == 0) {
            phoneButton.error = "Field is required"
            validated = false
        }
        if(fullName.length() == 0) {
            fullName.error = "Field is required"
            validated = false
        }
        if(dob.length() == 0) {
            dob.error = "Field is required"
            validated = false
        }
        return validated
    }

    fun uploadUserData(fullName: String, phoneNumber: String, dob: String) {
        val firstName: String = fullName.split(" ")[0]
        val lastName: String = fullName.split(" ")[1]

        val user = UserData(phoneNumber, firstName, lastName, dob, auth.currentUser?.uid)
        val userID = auth.currentUser?.uid
        userID?.let { database.child("users").child(it) }?.setValue(user)
    }

    // Add FCM token so we can send notifications to specific users
    private fun notificationSetup() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            Firebase.messaging.subscribeToTopic("events")
                .addOnCompleteListener { task ->
                    var msg = "Subscribed"
                    if (!task.isSuccessful) {
                        msg = "Subscribe failed"
                    }
                    Log.d(TAG, msg)
                }
        })
    }
}
