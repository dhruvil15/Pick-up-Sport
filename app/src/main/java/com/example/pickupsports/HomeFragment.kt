package com.example.pickupsports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickupsports.databinding.FragmentHomeBinding
import com.example.pickupsports.persistence.EventsRecyclerViewAdapter
import com.example.pickupsports.persistence.EventsStorage

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null;

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addEventButton.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_CreateEventFragment)
        }
        binding.profileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_profileFragment)
        }
        val recylerView : RecyclerView = view.findViewById(R.id.eventRV)
        recylerView.layoutManager = LinearLayoutManager(activity)

        val recyclerViewAdapter = EventsRecyclerViewAdapter()
        recylerView.adapter = recyclerViewAdapter

        recyclerViewAdapter.setEvents(EventsStorage.events)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}