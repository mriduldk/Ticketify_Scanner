package com.codingstudio.super50pscattandance

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingstudio.super50pscattandance.model.Resource
import com.codingstudio.super50pscattandance.viewmodel.AttendanceViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var buttonLogin : Button
    private lateinit var editTextUsername : EditText
    private lateinit var editTextPassword : EditText
    private lateinit var relativeLayoutParent : RelativeLayout
    private lateinit var relativeLayoutLoader : RelativeLayout


    private val TAG = "LoginActivity"
    private val attendanceViewModel : AttendanceViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin = findViewById(R.id.buttonLogin)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        relativeLayoutParent = findViewById(R.id.relativeLayoutParent)
        relativeLayoutLoader = findViewById(R.id.relativeLayoutLoader)

        buttonLogin.setOnClickListener {

            if(TextUtils.isEmpty(editTextUsername.text.toString())){

                editTextUsername.error = "Username is empty"
                editTextUsername.requestFocus()

            } else if (TextUtils.isEmpty(editTextPassword.text.toString())) {

                editTextPassword.error = "Password is empty"
                editTextPassword.requestFocus()

            } else{
                userLogin(editTextUsername.text.toString().trim(), editTextPassword.text.toString().trim())
            }
        }

        observeAttendanceDetails()
    }

    private fun userLogin(username: String, password: String){

        attendanceViewModel.userLogin(username, password)

    }

    private fun observeAttendanceDetails() {

        attendanceViewModel.usserLogin.observe(this, Observer { res ->

            res.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { attendanceResponse ->

                            if (attendanceResponse.status == 200) {

                                SharedPref().setBoolean(this, Constants.IS_LOGIN, true)

                                startActivity(Intent(this, MainActivity::class.java))
                                Snackbar.make(relativeLayoutParent, "${attendanceResponse.message}", Snackbar.LENGTH_LONG).show()

                                finish()
                            } else {
                                showErrorMessage()
                                Snackbar.make(relativeLayoutParent, "${attendanceResponse.message}", Snackbar.LENGTH_LONG).show()
                            }

                        }
                    }

                    is Resource.Error -> {
                        showErrorMessage()
                        response.status?.let { errorMessage ->
                            Log.e(TAG, "An Error Occurred : $errorMessage")
                            when (errorMessage) {
                                Constants.STATUS_NO_INTERNET -> {

                                    Snackbar.make(
                                        relativeLayoutParent,
                                        "No internet connection",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                                Constants.STATUS_CONFLICT -> {

                                    Snackbar.make(
                                        relativeLayoutParent,
                                        "Something went wrong. Please try again.",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                                else -> {

                                    Snackbar.make(
                                        relativeLayoutParent,
                                        "Failed to login",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        })

    }

    private fun showProgressBar() {
        relativeLayoutLoader.visibility = VISIBLE
        buttonLogin.isEnabled = false
    }

    private fun hideProgressBar() {
        relativeLayoutLoader.visibility = GONE
        buttonLogin.isEnabled = true
    }

    private fun showErrorMessage() {
        relativeLayoutLoader.visibility = GONE
        buttonLogin.isEnabled = true
    }












}