package com.example.humblr.enty

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.humblr.utils.FINISHED
import com.example.humblr.utils.ONBOARDING
import com.example.humblr.utils.TAG_T
import com.example.humblr.utils.TOKEN
import com.example.humblr.utils.TOKEN_SAVE
import javax.inject.Inject

class MySharedPreferences @Inject constructor() {


    @SuppressLint("CommitPrefEdits")
    fun onBoardingIsFinished(context: Context) {
        val sharedPreferences = context.getSharedPreferences(ONBOARDING, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(FINISHED, true)
        editor.apply()
    }

    fun onBoardingIsFinishedBoolean(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(ONBOARDING, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(FINISHED, false)
    }

    fun saveToken(context: Context, token: String?) {
        val sharedPreferences = context.getSharedPreferences(TOKEN_SAVE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(TOKEN_SAVE, Context.MODE_PRIVATE)
        val pref = sharedPreferences.getString(TOKEN, null).toString()
        Log.d(TAG_T, "pref $pref")
        return pref
    }

}