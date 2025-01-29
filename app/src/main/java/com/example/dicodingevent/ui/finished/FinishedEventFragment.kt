package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.detail.DetailEventFragment
import com.example.dicodingevent.ui.home.HorizontalEventAdapter
import com.example.dicodingevent.ui.home.VerticalEventAdapter
import com.google.android.material.snackbar.Snackbar

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val finishedEventViewModel: FinishedEventViewModel by viewModels()

    private val finishedEventAdapter by lazy {
        HorizontalEventAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        initObserver()
    }

    private fun initObserver() = with(binding) {
        finishedEventViewModel.isLoading.observe(viewLifecycleOwner) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        finishedEventViewModel.errorMessage.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(root, it.toString(), Snackbar.LENGTH_SHORT)
            snackBar.show()
        }

        finishedEventViewModel.eventList.observe(viewLifecycleOwner) {
            if (it.error == true) {
                val snackBar = Snackbar.make(root, it.message ?: "Error", Snackbar.LENGTH_SHORT)
                snackBar.show()
            } else {
                finishedEventAdapter.submitList(it.event)
            }
        }
    }

    private fun setupRecyclerView() = with(binding) {
        rvFinishedEvent.layoutManager = GridLayoutManager(requireContext(), 2)
        rvFinishedEvent.adapter = finishedEventAdapter
        finishedEventAdapter.setOnItemClickCallback(object :
            HorizontalEventAdapter.OnItemClickCallback {
            override fun onItemClickedHorizontal(data: EventResponse.EventData) {
                val bundle = Bundle().apply {
                    putInt(DetailEventFragment.EVENT_ID, data.id ?: 0)
                }
                val navController = findNavController()
                navController.navigate(R.id.action_navigation_finished_to_navigation_detail, bundle)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}