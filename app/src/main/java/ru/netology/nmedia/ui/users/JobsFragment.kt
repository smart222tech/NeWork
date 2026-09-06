package ru.netology.nmedia.ui.users

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
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.auth.AuthViewModel
import ru.netology.nmedia.databinding.FragmentFeedBinding

@AndroidEntryPoint
class JobsFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModels({ requireParentFragment() })
    private val authViewModel: AuthViewModel by activityViewModels()

    companion object {
        fun newInstance(userId: Long) = JobsFragment().apply {
            arguments = Bundle().apply { putLong("userId", userId) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val userId = arguments?.getLong("userId") ?: 0L
        val authState = authViewModel.authState.value
        val isOwnProfile = authState is AuthState.Authenticated && authState.id == userId

        if (isOwnProfile) {
            binding.fabAdd.visibility = View.VISIBLE
            binding.fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_newJobFragment)
            }
        } else {
            binding.fabAdd.visibility = View.GONE
        }

        val adapter = JobAdapter(onRemove = { viewModel.deleteJob(it.id) })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userJobs.collectLatest { jobs ->
                adapter.submitList(jobs)
            }
        }

        return binding.root
    }
}
