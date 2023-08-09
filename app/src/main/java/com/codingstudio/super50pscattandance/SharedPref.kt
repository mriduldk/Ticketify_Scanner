package com.codingstudio.super50pscattandance

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPref {

    private lateinit var sharedpreferences: SharedPreferences

    fun setInt(context: Context, key: String?, value: Int) {

        sharedpreferences = context.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedpreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun setString(context: Context, key: String?, value: String?) {

        sharedpreferences = context.getSharedPreferences(
            Constants.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = sharedpreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setBoolean(context: Context, key: String?, value: Boolean) {
        sharedpreferences = context.getSharedPreferences(
            Constants.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = sharedpreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanPref(context: Context, key: String?): Boolean {
        sharedpreferences = context.getSharedPreferences(
            Constants.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
        return sharedpreferences.getBoolean(key, false)
    }

    fun getStringPref(context: Context, key: String?): String? {
        sharedpreferences = context.getSharedPreferences(
            Constants.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
        return sharedpreferences.getString(key, "")
    }

    fun getIntPref(context: Context, key: String?): Int {
        sharedpreferences = context.getSharedPreferences(
            Constants.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
        return sharedpreferences.getInt(key, 0)
    }

    fun getUserID(context: Context): String? {
        sharedpreferences = context.getSharedPreferences(
            Constants.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
        return sharedpreferences.getString(Constants.USER_ID, "")
    }

    fun setUserID(context: Context, userId: String) {
        sharedpreferences = context.getSharedPreferences(
            Constants.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = sharedpreferences.edit()
        editor.putString(Constants.USER_ID, userId)
        editor.apply()
    }

    fun logoutUser(context: Context) {
        sharedpreferences = context.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE)

        val editor = sharedpreferences.edit()
        editor.clear()
        editor.apply()
    }
}