package com.example.dicodingevent.ui.search

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
import com.example.dicodingevent.databinding.FragmentSearchEventBinding
import com.example.dicodingevent.ui.detail.DetailEventFragment
import com.example.dicodingevent.ui.home.VerticalEventAdapter
import com.google.android.material.snackbar.Snackbar

class SearchEventFragment : Fragment() {

    private var _binding: FragmentSearchEventBinding? = null
    private val binding get() = _binding!!

    private val searchAdapter by lazy { VerticalEventAdapter() }

    private val viewModel: SearchEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSearchItemList("devcoach")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchEventBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            searchViewEvent.setupWithSearchBar(searchBarEvent)
        }
        initObserver()
        searchListener()
        initRecyclerView()
    }

    private fun searchListener() = with(binding) {
        searchViewEvent
            .editText
            .setOnEditorActionListener { v, actionId, event ->
                searchBarEvent.setText(searchViewEvent.text)
                searchViewEvent.hide()
                viewModel.getSearchItemList(searchViewEvent.text.toString().lowercase())
                false
            }
    }

    private fun initRecyclerView() = with(binding) {
        context?.let {
            rvSearchEvent.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvSearchEvent.adapter = searchAdapter
            searchAdapter.setOnItemClickCallback(object : VerticalEventAdapter.OnItemClickCallback {
                override fun onItemClicked(data: EventResponse.EventData) {
                    val bundle = Bundle().apply {
                        putInt(DetailEventFragment.EVENT_ID, data.id ?: 0)
                    }
                    val navController = findNavController()
                    navController.navigate(
                        R.id.action_searchEventFragment_to_navigation_detail,
                        bundle
                    )
                }
            })
        }
    }

    private fun initObserver() = with(binding) {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(root, it.toString(), Snackbar.LENGTH_SHORT)
            snackBar.show()
        }

        viewModel.eventList.observe(viewLifecycleOwner) {
            if (it.error == true) {
                val snackBar = Snackbar.make(root, it.message ?: "Error", Snackbar.LENGTH_SHORT)
                snackBar.show()
            } else {
                searchAdapter.submitList(it.event)
            }
        }
    }
}