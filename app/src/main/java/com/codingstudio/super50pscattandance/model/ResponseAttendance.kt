package com.codingstudio.super50pscattandance.model

import com.google.gson.annotations.SerializedName

data class ResponseAttendance (
    @SerializedName("data")
    val data : Attendance_ ?= null,
    val status : Int,
    val message : String?= null
)



data class Attendance_ (
    @SerializedName("pscExamAttandanceNew")
    val attendance : List<Attendance> ?= null,
)