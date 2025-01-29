package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.ui.detail.DetailEventFragment
import com.example.dicodingevent.ui.home.VerticalEventAdapter
import com.google.android.material.snackbar.Snackbar

class UpcomingEventFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val upcomingEventViewModel: UpcomingEventViewModel by viewModels()

    private val upcomingEventAdapter by lazy {
        VerticalEventAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecylerView()
        initObserver()
    }

    private fun initObserver() = with(binding) {
        upcomingEventViewModel.isLoading.observe(viewLifecycleOwner) {
            proggresBarUpcomingEvent.visibility = if (it) View.VISIBLE else View.GONE
        }

        upcomingEventViewModel.eventList.observe(viewLifecycleOwner) {
            if (it.error == true) {
                val snackBar = Snackbar.make(root, it.message ?: "Error", Snackbar.LENGTH_SHORT)
                snackBar.show()
            } else {
                upcomingEventAdapter.submitList(it.event)
            }
        }
    }

    private fun setupRecylerView() = with(binding) {
        rvUpcomingEvent.layoutManager = LinearLayoutManager(requireContext())
        rvUpcomingEvent.adapter = upcomingEventAdapter
        upcomingEventAdapter.setOnItemClickCallback(object :
            VerticalEventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: EventResponse.EventData) {
                val bundle = Bundle().apply {
                    putInt(DetailEventFragment.EVENT_ID, data.id ?: 0)
                }
                val navController = findNavController()
                navController.navigate(R.id.action_navigation_upcoming_to_navigation_detail, bundle)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}