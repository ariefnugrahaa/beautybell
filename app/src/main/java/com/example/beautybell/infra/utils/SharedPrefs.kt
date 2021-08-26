package com.example.beautybell.infra.utils

import android.content.Context
import android.content.SharedPreferences

@Suppress("UNCHECKED_CAST")
class SharedPrefs (private val context: Context) {
    companion object {
        private const val PREF = "MyAppPrefName"
        private const val PREF_NAME = "user_name"
        private const val PREF_BIRTH = "user_birth"
        private const val PREF_EMAIL = "user_email"
        private const val PREF_AVATAR = "user_avatar"

    }

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)


    fun saveName(name: String){
        put(PREF_NAME, name)
    }

    fun saveBirth(birth: String){
        put(PREF_BIRTH, birth)
    }

    fun saveEmail(email: String){
        put(PREF_EMAIL, email)
    }

    fun saveAvatar(avatar: String){
        put(PREF_AVATAR, avatar)
    }

    fun getName() : String {
        return get(PREF_NAME, String::class.java)
    }

    fun getBirth() : String {
        return get(PREF_BIRTH, String::class.java)
    }

    fun getEmail() : String {
        return get(PREF_EMAIL, String::class.java)
    }

    fun getAvatar() : String {
        return get(PREF_AVATAR, String::class.java)
    }

    private fun <T> get(key: String, clazz: Class<T>): T =
        when (clazz) {
            String::class.java -> sharedPref.getString(key, "")
            Boolean::class.java -> sharedPref.getBoolean(key, false)
            Float::class.java -> sharedPref.getFloat(key, -1f)
            Double::class.java -> sharedPref.getFloat(key, -1f)
            Int::class.java -> sharedPref.getInt(key, -1)
            Long::class.java -> sharedPref.getLong(key, -1L)
            else -> null
        } as T

    private fun <T> put(key: String, data: T) {
        val editor = sharedPref.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Double -> editor.putFloat(key, data.toFloat())
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
        }
        editor.apply()
    }

    fun clear() {
        sharedPref.edit().run {
            remove(PREF_NAME)
            remove(PREF_BIRTH)
            remove(PREF_EMAIL)
            remove(PREF_AVATAR)
        }.apply()
    }
}