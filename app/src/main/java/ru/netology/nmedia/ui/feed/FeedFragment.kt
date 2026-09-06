package ru.netology.nmedia.ui.feed

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
import ru.netology.nmedia.ui.adapter.PostsAdapter

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel: FeedViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var adapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PostsAdapter(
            onLike = { viewModel.like(it) },
            onDelete = { viewModel.delete(it) },
            onEdit = { post ->
                val bundle = Bundle().apply { putLong("postId", post.id) }
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment, bundle)
            },
            onPostClick = { post ->
                val bundle = Bundle().apply { putLong("postId", post.id) }
                findNavController().navigate(R.id.action_feedFragment_to_postDetailFragment, bundle)
            }
        )
        binding.recyclerView.adapter = adapter

        binding.fabAdd.setOnClickListener {
            if (authViewModel.authState.value is ru.netology.nmedia.auth.AuthState.Authenticated) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
            } else {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.posts.collectLatest { posts ->
                adapter.submitList(posts)
            }
        }

        return binding.root
    }
}
