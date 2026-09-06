package ru.netology.nmedia.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.api.dto.Job
import ru.netology.nmedia.databinding.FragmentNewJobBinding
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewJobFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModels()
    private var startDate: String? = null
    private var finishDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewJobBinding.inflate(inflater, container, false)

        binding.pickStartDate.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker().build()
            picker.addOnPositiveButtonClickListener { timestamp ->
                startDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    .format(Date(timestamp))
                binding.pickStartDate.text = "С: " + SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(timestamp))
            }
            picker.show(parentFragmentManager, "start")
        }

        binding.pickFinishDate.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker().build()
            picker.addOnPositiveButtonClickListener { timestamp ->
                finishDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    .format(Date(timestamp))
                binding.pickFinishDate.text = "По: " + SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(timestamp))
            }
            picker.show(parentFragmentManager, "finish")
        }

        binding.save.setOnClickListener {
            val name = binding.name.text.toString()
            val position = binding.position.text.toString()
            val link = binding.link.text.toString()

            if (name.isBlank() || position.isBlank() || startDate == null) {
                Toast.makeText(requireContext(), "Заполните обязательные поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val job = Job(
                id = 0,
                name = name,
                position = position,
                start = startDate!!,
                finish = finishDate
            )
            viewModel.saveJob(job)
            findNavController().navigateUp()
        }

        return binding.root
    }
}
