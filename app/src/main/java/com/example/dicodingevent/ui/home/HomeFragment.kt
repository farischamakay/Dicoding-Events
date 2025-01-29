package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.detail.DetailEventFragment.Companion.EVENT_ID
import com.example.dicodingevent.ui.finished.FinishedEventViewModel
import com.example.dicodingevent.ui.upcoming.UpcomingEventViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeBannerAdapter by lazy {
        HorizontalEventAdapter()
    }

    private val verticalEventAdapter by lazy {
        VerticalEventAdapter()
    }

    private val upComingEventVM: UpcomingEventViewModel by viewModels()
    private val finishedEventVM: FinishedEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initRecyclerView()
        observerLiveDataEvent()
    }

    private fun initListener() = with(binding) {
        icSearchEvent.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_navigation_home_to_searchEventFragment)
        }
    }

    private fun initRecyclerView() = with(binding) {
        context?.let {
            rvUpcomingEventHomepage.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvUpcomingEventHomepage.adapter = homeBannerAdapter
            homeBannerAdapter.setOnItemClickCallback(object :
                HorizontalEventAdapter.OnItemClickCallback {
                override fun onItemClickedHorizontal(data: EventResponse.EventData) {
                    val bundle = Bundle().apply {
                        putInt(EVENT_ID, data.id ?: 0)
                    }
                    val navController = findNavController()
                    navController.navigate(R.id.action_navigation_home_to_navigation_detail, bundle)
                }
            })

            rvFinishedEventHomepage.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvFinishedEventHomepage.adapter = verticalEventAdapter
            verticalEventAdapter.setOnItemClickCallback(object :
                VerticalEventAdapter.OnItemClickCallback {
                override fun onItemClicked(data: EventResponse.EventData) {
                    val bundle = Bundle().apply {
                        putInt(EVENT_ID, data.id ?: 0)
                    }
                    val navController = findNavController()
                    navController.navigate(R.id.action_navigation_home_to_navigation_detail, bundle)
                }
            })
        }
    }

    private fun observerLiveDataEvent() = with(binding) {
        upComingEventVM.isLoading.observe(viewLifecycleOwner) {
            progressBarUpcomingEvent.visibility = if (it) View.VISIBLE else View.GONE
        }

        finishedEventVM.isLoading.observe(viewLifecycleOwner) {
            progressBarFinishedEvent.visibility = if (it) View.VISIBLE else View.GONE
        }

        upComingEventVM.errorMessage.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(root, it.toString(), Snackbar.LENGTH_SHORT)
            snackBar.show()
        }

        finishedEventVM.errorMessage.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(root, it.toString(), Snackbar.LENGTH_SHORT)
            snackBar.show()
        }

        upComingEventVM.eventList.observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.error == true) {
                    val snackBar = Snackbar.make(root, it.message ?: "Error", Snackbar.LENGTH_SHORT)
                    snackBar.show()
                } else {
                    homeBannerAdapter.submitList(it.event?.take(5))
                }
            }
        }

        finishedEventVM.eventList.observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.error == true) {
                    val snackBar = Snackbar.make(root, it.message ?: "Error", Snackbar.LENGTH_SHORT)
                    snackBar.show()
                } else {
                    verticalEventAdapter.submitList(it.event?.take(5))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}