package ru.netology.nmedia.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AuthViewModel
import ru.netology.nmedia.databinding.FragmentFeedBinding

@AndroidEntryPoint
class EventsFragment : Fragment() {
    private val viewModel: EventsViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = EventsAdapter(
            onParticipate = { viewModel.participate(it) },
            onEventClick = { event ->
                val bundle = Bundle().apply { putLong("eventId", event.id) }
                findNavController().navigate(R.id.action_eventsFragment_to_eventDetailFragment, bundle)
            }
        )
        binding.recyclerView.adapter = adapter

        binding.fabAdd.setOnClickListener {
            if (authViewModel.authState.value is ru.netology.nmedia.auth.AuthState.Authenticated) {
                findNavController().navigate(R.id.action_eventsFragment_to_newEventFragment)
            } else {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.data.collectLatest { adapter.submitList(it) }
        }

        return binding.root
    }
}
