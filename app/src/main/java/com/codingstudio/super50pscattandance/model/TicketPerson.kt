package com.codingstudio.super50pscattandance.model

data class TicketPerson(
    var pk_ticket_person_id : String? = null,
    var fk_ticket_id : String? = null,
    var person_name : String? = null,
    var person_phone : String? = null,
    var person_gender : String? = null,
    var person_age : String? = null,
    var is_attended : Boolean = false,
    var attendance_datetime : String? = null,
    var attendance_gate : String? = null,

   
)
