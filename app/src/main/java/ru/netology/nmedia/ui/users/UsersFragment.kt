package ru.netology.nmedia.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentFeedBinding

@AndroidEntryPoint
class UsersFragment : Fragment() {
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        binding.fabAdd.visibility = View.GONE

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = UsersAdapter { user ->
            val bundle = Bundle().apply { putLong("userId", user.id) }
            findNavController().navigate(R.id.action_usersFragment_to_profileFragment, bundle)
        }
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.data.collectLatest { adapter.submitList(it) }
        }

        return binding.root
    }
}
