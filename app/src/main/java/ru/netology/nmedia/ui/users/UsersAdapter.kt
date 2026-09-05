package ru.netology.nmedia.ui.users

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
import ru.netology.nmedia.api.dto.User

class UsersAdapter : ListAdapter<User, UsersAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val avatar = view.findViewById<ImageView>(R.id.avatar)
        private val name = view.findViewById<TextView>(R.id.name)
        private val login = view.findViewById<TextView>(R.id.login)

        fun bind(user: User) {
            name.text = user.name
            login.text = "@${user.login}"
            Glide.with(itemView).load(user.avatar).circleCrop().into(avatar)
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }
}
