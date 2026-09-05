package ru.netology.nmedia.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AuthViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        
        val loginField = view.findViewById<EditText>(R.id.login)
        val passwordField = view.findViewById<EditText>(R.id.password)
        
        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            // В реальном дипломном проекте здесь должен быть вызов API
            // Для теста просто сохраняем фиктивный токен
            viewModel.login("fake_token")
            findNavController().navigateUp()
        }
        
        return view
    }
}
