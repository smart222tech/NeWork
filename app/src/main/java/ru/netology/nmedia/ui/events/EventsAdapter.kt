package ru.netology.nmedia.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.api.dto.Event

class EventsAdapter(
    private val onParticipate: (Event) -> Unit
) : ListAdapter<Event, EventsAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view, onParticipate)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EventViewHolder(
        itemView: View,
        private val onParticipate: (Event) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val author = itemView.findViewById<TextView>(R.id.author)
        private val content = itemView.findViewById<TextView>(R.id.content)
        private val datetime = itemView.findViewById<TextView>(R.id.datetime)
        private val participateBtn = itemView.findViewById<Button>(R.id.participate)

        fun bind(event: Event) {
            author.text = event.author
            content.text = event.content
            datetime.text = event.datetime
            participateBtn.setOnClickListener { onParticipate(event) }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
    }
}
