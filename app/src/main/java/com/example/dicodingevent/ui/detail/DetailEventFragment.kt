package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.DetailEvent
import com.example.dicodingevent.databinding.FragmentDetailEventBinding
import com.google.android.material.snackbar.Snackbar


class DetailEventFragment : Fragment() {

    private var _binding: FragmentDetailEventBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailEventViewModel by viewModels()

    private val idEvent by lazy {
        arguments?.getInt(EVENT_ID) ?: 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hitDetailEvent()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            it.actionBar?.setDisplayHomeAsUpEnabled(true)
        }
        detailEventObserver()
    }

    private fun hitDetailEvent() {
        detailViewModel.getDetailEvent(idEvent)
    }

    private fun detailEventObserver() = with(binding) {
        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            progressBarDetailEvent.visibility = if (it) View.VISIBLE else View.GONE
        }

        detailViewModel.errorMessage.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(root, it.toString(), Snackbar.LENGTH_SHORT)
            snackBar.show()
        }

        detailViewModel.detailEvent.observe(viewLifecycleOwner) {
            if (it.error == true) {
                val snackBar = Snackbar.make(root, it.message ?: "Error", Snackbar.LENGTH_SHORT)
                snackBar.show()
            } else {
                initView(it.event ?: DetailEvent())
            }
        }
    }

    private fun initView(data: DetailEvent) = with(binding) {
        val quotaTicket = (data.quota ?: 0).minus(data.registrants ?: 0)
        Glide.with(requireContext())
            .load(data.imageLogo)
            .into(imgEventDetail)
        tvTitleEventDetail.text = data.name
        tvOrganizerDetail.text = data.ownerName
        tvQuotaDetail.text = quotaTicket.toString()
        tvDateDetail.text = data.endTime
        tvDescriptionDetail.text = Html.fromHtml(data.description, Html.FROM_HTML_MODE_COMPACT)
        btnRegister.setOnClickListener {
            val url = data.link
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EVENT_ID = "EventId"
    }

}