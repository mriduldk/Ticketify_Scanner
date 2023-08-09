package com.codingstudio.super50pscattandance

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.codingstudio.super50pscattandance.model.Resource
import com.codingstudio.super50pscattandance.viewmodel.AttendanceViewModel
import com.google.android.material.snackbar.Snackbar


class ApplicationNoScannerActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var relativeLayoutParent: RelativeLayout
    private lateinit var relativeLayoutLoader: RelativeLayout
    private lateinit var scanner_view : CodeScannerView
    private val TAG = "ScannerActivity"

    private val attendanceViewModel : AttendanceViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        scanner_view = findViewById<CodeScannerView>(R.id.scanner_view)
        relativeLayoutParent = findViewById<RelativeLayout>(R.id.relativeLayoutParent)
        relativeLayoutLoader = findViewById<RelativeLayout>(R.id.relativeLayoutLoader)

        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE

        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {

                it.text?.let { text ->

                    val data = text.split(" ");

                    Toast.makeText(this, "Application No : ${data[0]}", Toast.LENGTH_LONG).show()

                    //val center = SharedPref().getStringPref(this, Constants.CENTER)
                    val district = SharedPref().getStringPref(this, Constants.DISTRICT)

                    /*attendanceViewModel.studentAttendanceStore(
                        fk_user_id = data[0],
                        fk_application_no = data[0],
                        center = district ?: "",
                        created_by = "From Mobile App"
                    )*/

                    attendanceViewModel.getTicketDetailsByTicketId(data[1], data[0]);
                }
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
        //codeScanner.startPreview()

        observeAttendanceStorage()
    }

    override fun onResume() {
        super.onResume()
        if (this::codeScanner.isInitialized){
            codeScanner.startPreview()
        }

    }

    override fun onPause() {
        if (this::codeScanner.isInitialized){
            codeScanner.releaseResources()
        }
        super.onPause()
    }

    private fun observeAttendanceStorage() {

        attendanceViewModel.attendanceStore.observe(this, Observer { res->

            res.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { insertResponse ->

                            if (insertResponse.status == 200) {

                                Snackbar.make(relativeLayoutParent, "${insertResponse.message}", Snackbar.LENGTH_LONG).show()

                                alertDialogMessage("Success", "${insertResponse.message}")

                            } else {
                                showErrorMessage()
                                Snackbar.make(relativeLayoutParent, "${insertResponse.message}", Snackbar.LENGTH_LONG).show()
                                alertDialogMessage("Warning...!!!", "${insertResponse.message}")
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

                                    /*Snackbar.make(
                                        relativeLayoutParent,
                                        "Something went wrong. Please try again.",
                                        Snackbar.LENGTH_LONG
                                    ).show()*/

                                    alertDialogMessage("Warning...!!!", "User Attendance Already Exists.")
                                }
                                else -> {

                                    Snackbar.make(
                                        relativeLayoutParent,
                                        "Something went wrong. Please try again.",
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

        attendanceViewModel.getScannedAttendance.observe(this, Observer { res->

            res.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { insertResponse ->

                            if (insertResponse.status == 200) {

                                Snackbar.make(relativeLayoutParent, "${insertResponse.message}", Snackbar.LENGTH_LONG).show()

                                //alertDialogMessage("Success", "${insertResponse.message}")

                                val intent = Intent(this, ScanTicketActivity::class.java)
                                intent.putExtra("ticket", insertResponse.data)
                                startActivity(intent)

                            } else {
                                showErrorMessage()
                                Snackbar.make(relativeLayoutParent, "${insertResponse.message}", Snackbar.LENGTH_LONG).show()
                                alertDialogMessage("Warning...!!!", "${insertResponse.message}")
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

                                    /*Snackbar.make(
                                        relativeLayoutParent,
                                        "Something went wrong. Please try again.",
                                        Snackbar.LENGTH_LONG
                                    ).show()*/

                                    alertDialogMessage("Warning...!!!", "User Attendance Already Exists.")
                                }
                                else -> {

                                    Snackbar.make(
                                        relativeLayoutParent,
                                        "Something went wrong. Please try again.",
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
        //relativeLayoutProgressbar.visibility = VISIBLE
        //relativeLayoutErrorMessage.visibility = GONE
    }

    private fun hideProgressBar() {
        relativeLayoutLoader.visibility = GONE
        //relativeLayoutProgressbar.visibility = GONE
        //relativeLayoutErrorMessage.visibility = GONE
    }

    private fun showErrorMessage() {
        relativeLayoutLoader.visibility = GONE

        /*recyclerViewTaskReceivedByTa.visibility = GONE
        relativeLayoutProgressbar.visibility = GONE
        relativeLayoutErrorMessage.visibility = VISIBLE*/
    }

    private fun alertDialogMessage(title: String, body: String) {

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(body)

        alertDialog.setPositiveButton("Continue", DialogInterface.OnClickListener { dialogInterface, i ->

            codeScanner.startPreview()

        })

        alertDialog.setNegativeButton("Back", DialogInterface.OnClickListener { dialogInterface, i ->

            onBackPressed()
            dialogInterface.dismiss()
        })

        val dialog = alertDialog.create()
        dialog.setCancelable(false)
        dialog.show()

    }






}