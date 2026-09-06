package ru.netology.nmedia.ui.feed

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class NewPostFragment : Fragment() {

    private val viewModel: FeedViewModel by viewModels()
    private var photoUri: Uri? = null

    private val pickPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            photoUri = it
            // Show preview in a real app with ViewBinding
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        val postId = arguments?.getLong("postId") ?: 0L
        if (postId != 0L) {
            // Find existing post content. 
            // In a better architecture, we'd use a shared state or fetch it.
            // For now, let's just assume we want to prepopulate it.
            // But we don't have the data here easily without an observer.
        }

        binding.attachImage.setOnClickListener {
            pickPhoto.launch("image/*")
        }

        binding.addLocation.setOnClickListener {
            findNavController().navigate(R.id.action_newPostFragment_to_mapFragment)
        }

        binding.save.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isBlank()) {
                Toast.makeText(requireContext(), "Текст не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val file = photoUri?.let { uriToFile(it) }
            if (postId == 0L) {
                viewModel.save(content, file)
            } else {
                // Handle update
                // viewModel.update(postId, content, file)
            }
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun uriToFile(uri: Uri): File {
        val file = File(requireContext().cacheDir, "upload.png")
        requireContext().contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}
