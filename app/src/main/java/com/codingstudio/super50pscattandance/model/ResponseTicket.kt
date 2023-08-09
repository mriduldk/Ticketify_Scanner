package com.codingstudio.super50pscattandance.model

import com.google.gson.annotations.SerializedName

data class ResponseTicket (
    @SerializedName("data")
    val data : Ticket ?= null,
    val status : Int,
    val message : String?= null
)
/*data class Ticket_ (
    @SerializedName("ticket")
    val ticket : Ticket ?= null,
)*/
