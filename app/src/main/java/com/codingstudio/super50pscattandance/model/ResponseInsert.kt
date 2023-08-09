package com.codingstudio.super50pscattandance.model

import com.google.gson.annotations.SerializedName

data class ResponseInsert (
    @SerializedName("data")
    val attendance : Attendance ?= null,
    val status : Int,
    val message : String?= null
)


