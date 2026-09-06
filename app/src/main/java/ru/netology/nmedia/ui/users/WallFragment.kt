package ru.netology.nmedia.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.ui.adapter.PostsAdapter
import ru.netology.nmedia.ui.feed.FeedViewModel

@AndroidEntryPoint
class WallFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModels({ requireParentFragment() })

    companion object {
        fun newInstance(userId: Long) = WallFragment().apply {
            arguments = Bundle().apply { putLong("userId", userId) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        binding.fabAdd.visibility = View.GONE

        val adapter = PostsAdapter(
            onLike = {},
            onDelete = {},
            onEdit = {},
            onPostClick = { post ->
                // You can also navigate to details from here
                val bundle = Bundle().apply { putLong("postId", post.id) }
                // findNavController().navigate(...) 
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userWall.collectLatest { posts ->
                adapter.submitList(posts)
            }
        }

        return binding.root
    }
}
