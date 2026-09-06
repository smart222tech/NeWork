package ru.netology.nmedia.ui.events

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
import ru.netology.nmedia.databinding.FragmentEventDetailBinding

@AndroidEntryPoint
class EventDetailFragment : Fragment() {

    private val viewModel: EventsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventDetailBinding.inflate(inflater, container, false)

        val eventId = arguments?.getLong("eventId") ?: 0L

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.data.collectLatest { events ->
                val event = events.find { it.id == eventId } ?: return@collectLatest
                
                binding.author.text = event.author
                binding.content.text = event.content
                binding.datetime.text = event.datetime
                binding.type.text = event.type
                
                Glide.with(this@EventDetailFragment).load(event.authorAvatar).circleCrop().into(binding.avatar)
                
                if (event.attachment != null) {
                    binding.attachment.visibility = View.VISIBLE
                    Glide.with(this@EventDetailFragment).load(event.attachment.url).into(binding.attachment)
                } else {
                    binding.attachment.visibility = View.GONE
                }

                binding.participate.setOnClickListener { viewModel.participate(event) }
            }
        }

        return binding.root
    }
}
