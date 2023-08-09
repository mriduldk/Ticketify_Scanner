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
import com.codingstudio.super50pscattandance.model.Ticket
import com.codingstudio.super50pscattandance.model.TicketPerson
import com.codingstudio.super50pscattandance.viewmodel.AttendanceViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ScanTicketActivity : AppCompatActivity() {

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


    private var ticket : Ticket ?= null

    private val TAG = "ScanTicketActivity"
    private val attendanceViewModel : AttendanceViewModel by viewModels()
    private lateinit var adapterAttendance: AdapterTicketPersonListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_ticket)

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
        btnLogout = findViewById(R.id.btnLogout)

        ticket = intent.extras?.getParcelable("ticket")

        ticket?.let {

            adapterAttendance = AdapterTicketPersonListView(this)
            recyclerViewAttendance.apply {
                adapter = adapterAttendance
                layoutManager = LinearLayoutManager(this@ScanTicketActivity)
                addItemDecoration(DividerItemDecoration(this@ScanTicketActivity, DividerItemDecoration.VERTICAL))
            }
            adapterAttendance.differ.submitList(ticket?.ticketPerson?.toList())

            adapterAttendance.setOnAttendanceDeleteClickListener { tickePerson ->

                alertDialogDelete(tickePerson)

            }

            adapterAttendance.setOnAttendanceAddClickListener { ticketPerson ->

            }

        }

        observeAttendanceDetails()
    }


    private fun getAllAttendanceDetails(district: String){

        val center = SharedPref().getStringPref(this, Constants.CENTER)
        textViewDistrict.text = "District : $district"
        textViewCenter.text = "Examination center : $center"

        attendanceViewModel.getAllAttendance_ByCenter(district)
    }

    private fun observeAttendanceDetails() {

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

    private fun alertDialogDelete(tickePerson: TicketPerson) {

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Attendance..!!")
        alertDialog.setMessage("Are you sure you want to delete attendance of ${tickePerson.person_name} ?")

        alertDialog.setPositiveButton("Delete", DialogInterface.OnClickListener { dialogInterface, i ->

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