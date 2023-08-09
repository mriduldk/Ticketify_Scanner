package com.codingstudio.super50pscattandance.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Ticket(
    var pk_ticket_id : String? = null,
    var fk_match_id : String? = null,
    var no_of_person : String? = null,
    var ticket_type : String? = null,
    var payment_status : String? = null,
    var payment_order_id : String? = null,
    var gate : String? = null,
    var ticketPerson : @RawValue List<TicketPerson>? = null,
) : Parcelable
