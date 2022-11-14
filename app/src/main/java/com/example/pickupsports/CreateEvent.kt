package com.example.pickupsports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.pickupsports.databinding.FragmentSecondBinding
import com.example.pickupsports.model.Event
import com.example.pickupsports.persistence.EventsStorage

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateEvent : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveEventButton.setOnClickListener {
            val title = view.findViewById<EditText>(R.id.eventAddTitle).text
            val body = view.findViewById<EditText>(R.id.eventAddBody).text

            if(title.isNotEmpty() && body.isNotEmpty() ){
                EventsStorage.events.add(Event(title.toString(),body.toString()))
            }


            findNavController().navigate(R.id.action_CreateEventFragment_to_HomeFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}