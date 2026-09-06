package ru.netology.nmedia.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        val userId = arguments?.getLong("userId") ?: 0L
        viewModel.getUserDetails(userId)

        // In a real app, you'd fetch the User object too, or pass it
        // For now, we'll assume the name/login are part of the state or passed in
        binding.name.text = "User $userId"
        binding.login.text = "@user$userId"

        val adapter = ProfilePagerAdapter(this, userId)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = if (position == 0) "Wall" else "Jobs"
        }.attach()

        return binding.root
    }
}

class ProfilePagerAdapter(fragment: Fragment, private val userId: Long) : androidx.viewpager2.adapter.FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) WallFragment.newInstance(userId) else JobsFragment.newInstance(userId)
    }
}
