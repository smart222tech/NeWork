package ru.netology.nmedia.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.databinding.FragmentPostDetailBinding
import ru.netology.nmedia.ui.users.UsersViewModel

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private val viewModel: FeedViewModel by viewModels()
    private val usersViewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostDetailBinding.inflate(inflater, container, false)

        val postId = arguments?.getLong("postId") ?: 0L
        
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.posts.collectLatest { posts ->
                val post = posts.find { it.id == postId } ?: return@collectLatest
                
                binding.author.text = post.author
                binding.published.text = post.published
                binding.content.text = post.content
                binding.likes.text = "♥ ${post.likes}"
                
                Glide.with(this@PostDetailFragment).load(post.authorAvatar).circleCrop().into(binding.avatar)
                
                if (post.attachment != null) {
                    binding.attachment.visibility = View.VISIBLE
                    Glide.with(this@PostDetailFragment).load(post.attachment.url).into(binding.attachment)
                } else {
                    binding.attachment.visibility = View.GONE
                }

                // Fetch latest job
                usersViewModel.getUserDetails(post.authorId)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            usersViewModel.userJobs.collectLatest { jobs ->
                val latestJob = jobs.lastOrNull { it.finish == null } ?: jobs.lastOrNull()
                binding.job.text = latestJob?.name ?: "В поиске работы"
            }
        }

        return binding.root
    }
}
