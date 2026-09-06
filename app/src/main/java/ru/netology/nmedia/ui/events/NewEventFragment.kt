package ru.netology.nmedia.ui.events

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.databinding.FragmentNewEventBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewEventFragment : Fragment() {

    private val viewModel: EventsViewModel by viewModels()
    private var photoUri: Uri? = null
    private var selectedDate: Calendar = Calendar.getInstance()

    private val pickPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { photoUri = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewEventBinding.inflate(inflater, container, false)

        binding.pickDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener { timestamp ->
                selectedDate.timeInMillis = timestamp
                
                val timePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .build()
                timePicker.addOnPositiveButtonClickListener {
                    selectedDate.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                    selectedDate.set(Calendar.MINUTE, timePicker.minute)
                    
                    binding.dateText.text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                        .format(selectedDate.time)
                }
                timePicker.show(parentFragmentManager, "time")
            }
            datePicker.show(parentFragmentManager, "date")
        }

        binding.attachMedia.setOnClickListener {
            pickPhoto.launch("image/*")
        }

        binding.save.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isBlank()) {
                Toast.makeText(requireContext(), "Текст не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val type = if (binding.online.isChecked) "ONLINE" else "OFFLINE"
            val datetime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .format(selectedDate.time)

            val file = photoUri?.let { uriToFile(it) }
            viewModel.save(content, type, datetime, file)
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun uriToFile(uri: Uri): File {
        val file = File(requireContext().cacheDir, "upload_event.png")
        requireContext().contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}
