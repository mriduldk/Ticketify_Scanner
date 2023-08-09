package com.codingstudio.super50pscattandance

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingstudio.super50pscattandance.model.Attendance
import com.codingstudio.super50pscattandance.model.Resource
import com.codingstudio.super50pscattandance.viewmodel.AttendanceViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var fab_start_scanner : FloatingActionButton
    private lateinit var relativeLayoutParent : RelativeLayout
    private lateinit var relativeLayoutLoader : RelativeLayout
    private lateinit var linearLayoutDistrict : LinearLayout
    private lateinit var recyclerViewAttendance : RecyclerView
    private lateinit var cardViewDistrictInfo : CardView

    private lateinit var spinnerDistrict : Spinner

    private lateinit var textViewDistrict : TextView
    private lateinit var textViewCenter : TextView
    private lateinit var textViewCount : TextView
    private lateinit var imageViewEditDistrict : ImageView
    private lateinit var btnLogout : ImageView

    private val CAMERA_PERMISSION_REQUEST_CODE = 100


    private val TAG = "MainActivity"
    private val attendanceViewModel : AttendanceViewModel by viewModels()
    private lateinit var adapterAttendance: AdapterAttendanceListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_start_scanner = findViewById(R.id.fab_start_scanner)
        relativeLayoutParent = findViewById(R.id.relativeLayoutParent)
        relativeLayoutLoader = findViewById(R.id.relativeLayoutLoader)
        recyclerViewAttendance = findViewById(R.id.recyclerViewAttendance)
        linearLayoutDistrict = findViewById(R.id.linearLayoutDistrict)

        spinnerDistrict = findViewById(R.id.spinnerDistrict)
        cardViewDistrictInfo = findViewById(R.id.cardViewDistrictInfo)

        textViewDistrict = findViewById(R.id.textViewDistrict)
        textViewCenter = findViewById(R.id.textViewCenter)
        textViewCount = findViewById(R.id.textViewCount)
        imageViewEditDistrict = findViewById(R.id.imageViewEditDistrict)
        btnLogout = findViewById(R.id.btnLogout)


        fab_start_scanner.setOnClickListener {

            startActivity(Intent(this, ApplicationNoScannerActivity::class.java))
            ///scanQrCodeLauncher.launch(null)
        }

        imageViewEditDistrict.setOnClickListener {

            visibilityCenterNotSelected()
        }

        btnLogout.setOnClickListener {

            alertDialogLogout()
        }

        ArrayAdapter.createFromResource(this, R.array.district, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDistrict.adapter = adapter
        }
        spinnerDistrict.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when(position){
                    0 -> {
                        visibilityCenterNotSelected()
                    }
                    1 -> {
                        SharedPref().setString(this@MainActivity, Constants.DISTRICT, "Kokrajhar")
                        SharedPref().setString(this@MainActivity, Constants.CENTER, "UN ACADEMY, KOKRAJHAR")
                        visibilityCenterSelected()
                    }
                    2 -> {
                        SharedPref().setString(this@MainActivity, Constants.DISTRICT, "Udalguri")
                        SharedPref().setString(this@MainActivity, Constants.CENTER, "Udalguri College, Udalguri")
                        visibilityCenterSelected()
                    }
                    3 -> {
                        SharedPref().setString(this@MainActivity, Constants.DISTRICT, "Baksa")
                        SharedPref().setString(this@MainActivity, Constants.CENTER, "Barama College, Baksa")
                        visibilityCenterSelected()
                    }
                    4 -> {
                        SharedPref().setString(this@MainActivity, Constants.DISTRICT, "Chirang")
                        SharedPref().setString(this@MainActivity, Constants.CENTER, "Kashikotra Higher Secondary School, Chirang")
                        visibilityCenterSelected()
                    }
                    5 -> {
                        SharedPref().setString(this@MainActivity, Constants.DISTRICT, "Tamulpur")
                        SharedPref().setString(this@MainActivity, Constants.CENTER, "TAMULPUR HS SCHOOL, TAMULPUR")
                        visibilityCenterSelected()
                    }
                    else-> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



        checkPermission()
        observeAttendanceDetails()
    }

    private fun visibilityCenterSelected(){
        linearLayoutDistrict.visibility = GONE
        recyclerViewAttendance.visibility = VISIBLE
        fab_start_scanner.visibility = VISIBLE
        cardViewDistrictInfo.visibility = VISIBLE

        val center = SharedPref().getStringPref(this, Constants.DISTRICT)
        center?.let {
            getAllAttendanceDetails(center)
        }
    }

    private fun visibilityCenterNotSelected(){
        linearLayoutDistrict.visibility = VISIBLE
        recyclerViewAttendance.visibility = GONE
        fab_start_scanner.visibility = GONE
        cardViewDistrictInfo.visibility = GONE
    }

    private fun checkPermission() {

        val permissionGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        if(!permissionGranted){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Snackbar.make(
                    relativeLayoutParent,
                    "Permission needed to scan QR Code.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }


    private fun getAllAttendanceDetails(district: String){

        val center = SharedPref().getStringPref(this, Constants.CENTER)
        textViewDistrict.text = "District : $district"
        textViewCenter.text = "Examination center : $center"

        attendanceViewModel.getAllAttendance_ByCenter(district)
    }

    private fun observeAttendanceDetails() {

        adapterAttendance = AdapterAttendanceListView(this)
        recyclerViewAttendance.apply {
            adapter = adapterAttendance
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }

        attendanceViewModel.allAttendanceByCenter.observe(this, Observer { res ->

            res.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { attendanceResponse ->

                            if (attendanceResponse.status == 200) {

                                if (attendanceResponse.data != null) {

                                    adapterAttendance.differ.submitList(attendanceResponse.data.attendance)

                                    textViewCount.text = "Total Student Count : ${attendanceResponse.data.attendance?.size}"

                                }else {
                                    showErrorMessage()
                                    Snackbar.make(relativeLayoutParent, "No Attendance Found.", Snackbar.LENGTH_LONG).show()
                                    textViewCount.text = "Total Student Count : 0"
                                }
                            } else {
                                showErrorMessage()
                                Snackbar.make(relativeLayoutParent, "No Attendance Found.", Snackbar.LENGTH_LONG).show()
                                textViewCount.text = "Total Student Count : 0"
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
                                    textViewCount.text = "Total Student Count : 0"
                                }
                                Constants.STATUS_CONFLICT -> {

                                    Snackbar.make(
                                        relativeLayoutParent,
                                        "Something went wrong. Please try again.",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                    textViewCount.text = "Total Student Count : 0"
                                }
                                else -> {

                                    Snackbar.make(
                                        relativeLayoutParent,
                                        "Something went wrong. Please try again.",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                    textViewCount.text = "Total Student Count : 0"
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

        attendanceViewModel.deleteAttendance.observe(this, Observer { res ->

            res.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { attendanceResponse ->

                            if (attendanceResponse.status == 200) {

                                Snackbar.make(relativeLayoutParent, "Attendance Deleted Successfully.", Snackbar.LENGTH_LONG).show()

                                val district = SharedPref().getStringPref(this, Constants.DISTRICT)
                                getAllAttendanceDetails(district ?: "")

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

        adapterAttendance.setOnAttendanceDeleteClickListener { attendance ->

            alertDialogDelete(attendance)

        }

    }

    private fun showProgressBar() {
        //recyclerViewTaskReceivedByTa.visibility = GONE
        relativeLayoutLoader.visibility = VISIBLE
        //relativeLayoutErrorMessage.visibility = GONE
    }

    private fun hideProgressBar() {
        //recyclerViewTaskReceivedByTa.visibility = VISIBLE
        relativeLayoutLoader.visibility = GONE
        //relativeLayoutErrorMessage.visibility = GONE
    }

    private fun showErrorMessage() {
        //recyclerViewTaskReceivedByTa.visibility = GONE
        relativeLayoutLoader.visibility = GONE
        //relativeLayoutErrorMessage.visibility = VISIBLE
    }

    override fun onResume() {
        super.onResume()

        val district = SharedPref().getStringPref(this, Constants.DISTRICT)

        if (district.isNullOrEmpty() || district.isNullOrBlank() ){
            visibilityCenterNotSelected()
        }else{
            visibilityCenterSelected()
            getAllAttendanceDetails(district)
        }
    }

    private fun alertDialogLogout() {

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Logout..!!")
        alertDialog.setMessage("Are you sure you want to logout ?")

        alertDialog.setPositiveButton("Logout", DialogInterface.OnClickListener { dialogInterface, i ->

            SharedPref().logoutUser(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })

        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })

        val dialog = alertDialog.create()
        dialog.setCancelable(false)
        dialog.show()

    }

    private fun alertDialogDelete(attendance: Attendance) {

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Attendance..!!")
        alertDialog.setMessage("Are you sure you want to delete attendance of ${attendance.first_name} ${attendance.last_name}  ?")

        alertDialog.setPositiveButton("Delete", DialogInterface.OnClickListener { dialogInterface, i ->

            val district = SharedPref().getStringPref(this, Constants.DISTRICT)

            //attendanceViewModel.deleteAttendance(attendance.fk_application_no ?: "", "$district")
        })

        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })

        val dialog = alertDialog.create()
        dialog.setCancelable(false)
        dialog.show()

    }





}