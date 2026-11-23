package ru.netology.nmedia.ui.auth

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.auth.AuthViewModel
import ru.netology.nmedia.databinding.FragmentRegistrationBinding
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    private var avatarUri: Uri? = null
    private var avatarImageView: ImageView? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                avatarUri = it
                avatarImageView?.setImageURI(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            FragmentRegistrationBinding.inflate(inflater, container, false)

        avatarImageView = binding.avatar

        binding.avatar.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.registerButton.setOnClickListener {
            val login = binding.login.text.toString().trim()
            val name = binding.name.text.toString().trim()
            val password = binding.password.text.toString()
            val confirm = binding.confirmPassword.text.toString()

            if (login.isBlank() || name.isBlank() || password.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Заполните все поля",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (password != confirm) {
                Toast.makeText(
                    requireContext(),
                    "Пароли не совпадают",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            var avatarPart: MultipartBody.Part? = null

            avatarUri?.let { uri ->
                val file = try {
                    uriToFile(uri)
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Не удалось прочитать фотографию: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                if (file.length() > 2 * 1024 * 1024) {
                    file.delete()
                    Toast.makeText(
                        requireContext(),
                        "Аватар слишком большой (максимум 2 МБ)",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                val mimeType = requireContext()
                    .contentResolver
                    .getType(uri) ?: "image/jpeg"

                avatarPart = MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    file.asRequestBody(mimeType.toMediaTypeOrNull())
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
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> Unit
                }
            }
        }

        return binding.root
    }

    private fun uriToFile(uri: Uri): File {
        val resolver = requireContext().contentResolver
        val mimeType = resolver.getType(uri) ?: "image/jpeg"

        val extension = when (mimeType.lowercase()) {
            "image/png" -> ".png"
            "image/webp" -> ".webp"
            "image/gif" -> ".gif"
            else -> ".jpg"
        }

        val file = File.createTempFile(
            "avatar_",
            extension,
            requireContext().cacheDir
        )

        val input = resolver.openInputStream(uri)
            ?: throw IllegalStateException("Не удалось открыть изображение")

        input.use { source ->
            FileOutputStream(file).use { output ->
                source.copyTo(output)
            }
        }

        return file
    }

    override fun onDestroyView() {
        avatarImageView = null
        super.onDestroyView()
    }
}
