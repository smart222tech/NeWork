package ru.netology.nmedia.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.api.dto.Post

class PostsAdapter(
    private val onLike: (Post) -> Unit,
    private val onDelete: (Post) -> Unit,
    private val onEdit: (Post) -> Unit,
    private val onPostClick: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view, onLike, onDelete, onEdit, onPostClick)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PostViewHolder(
        itemView: View,
        private val onLike: (Post) -> Unit,
        private val onDelete: (Post) -> Unit,
        private val onEdit: (Post) -> Unit,
        private val onPostClick: (Post) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val avatar: ImageView = itemView.findViewById(R.id.avatar)
        private val author: TextView = itemView.findViewById(R.id.author)
        private val published: TextView = itemView.findViewById(R.id.published)
        private val content: TextView = itemView.findViewById(R.id.content)
        private val likeButton: TextView = itemView.findViewById(R.id.likeButton)
        private val deleteButton: View = itemView.findViewById(R.id.deleteButton)
        private val editButton: View = itemView.findViewById(R.id.editButton)
        private val attachment: ImageView = itemView.findViewById(R.id.attachment)

        fun bind(post: Post) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeButton.text = "♥ ${post.likes}"

            Glide.with(itemView).load(post.authorAvatar).circleCrop().into(avatar)

            if (post.attachment != null) {
                attachment.visibility = View.VISIBLE
                Glide.with(itemView).load(post.attachment.url).into(attachment)
            } else {
                attachment.visibility = View.GONE
            }

            likeButton.setOnClickListener { onLike(post) }
            deleteButton.setOnClickListener { onDelete(post) }
            editButton.setOnClickListener { onEdit(post) }
            itemView.setOnClickListener { onPostClick(post) }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    }
}
