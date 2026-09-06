package ru.netology.nmedia.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.api.dto.Job
import ru.netology.nmedia.databinding.ItemJobBinding

class JobAdapter(
    private val onRemove: (Job) -> Unit
) : ListAdapter<Job, JobAdapter.JobViewHolder>(JobDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding, onRemove)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class JobViewHolder(
        private val binding: ItemJobBinding,
        private val onRemove: (Job) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(job: Job) {
            binding.company.text = job.name
            binding.position.text = job.position
            val finish = job.finish ?: "По настоящее время"
            binding.dates.text = "${job.start} - $finish"

            binding.remove.setOnClickListener { onRemove(job) }
        }
    }

    class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Job, newItem: Job) = oldItem == newItem
    }
}
