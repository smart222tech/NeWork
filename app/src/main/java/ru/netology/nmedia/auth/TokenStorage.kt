package ru.netology.nmedia.auth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveAuth(id: Long, token: String) {
        prefs.edit()
            .putLong("user_id", id)
            .putString("auth_token", token)
            .apply()
    }

    fun getToken(): String? = prefs.getString("auth_token", null)
    fun getUserId(): Long = prefs.getLong("user_id", 0L)

    fun clearToken() {
        prefs.edit()
            .remove("auth_token")
            .remove("user_id")
            .apply()
    }
}
