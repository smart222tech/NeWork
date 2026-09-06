package ru.netology.nmedia.ui.auth

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.auth.AuthViewModel
import ru.netology.nmedia.databinding.FragmentRegistrationBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    private var avatarUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            avatarUri = it
            // In a real app, use ViewBinding properly
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        binding.avatar.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.registerButton.setOnClickListener {
            val login = binding.login.text.toString()
            val name = binding.name.text.toString()
            val password = binding.password.text.toString()
            val confirm = binding.confirmPassword.text.toString()

            if (login.isBlank() || name.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirm) {
                Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val avatarPart = avatarUri?.let { uri ->
                val file = uriToFile(uri)
                if (file.length() > 2 * 1024 * 1024) { // 2MB limit for avatar example
                     Toast.makeText(requireContext(), "Аватар слишком большой", Toast.LENGTH_SHORT).show()
                     return@setOnClickListener
                }
                MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }

            viewModel.register(login, password, name, avatarPart)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.authState.collectLatest { state ->
                when (state) {
                    is AuthState.Authenticated -> {
                        findNavController().navigateUp()
                    }
                    is AuthState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        return binding.root
    }

    private fun uriToFile(uri: Uri): File {
        val file = File(requireContext().cacheDir, "avatar.png")
        requireContext().contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}
