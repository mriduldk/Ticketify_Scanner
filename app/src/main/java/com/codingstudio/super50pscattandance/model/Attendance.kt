package com.codingstudio.super50pscattandance.model

data class Attendance(
    var attendance_id : String? = null,
    var fk_user_id : String? = null,
    var fk_ticket_id : String? = null,
    var fk_ticket_person_id : String? = null,
    var stand : String? = null,
    var inner_gate : String? = null,
    var gate : String? = null,

    var created_on : String? = null,
    var created_by : String? = null,
    var modified_on : String? = null,
    var modified_by : String? = null,
    var is_deleted : Boolean = false,

    var first_name : String? = null,
    var last_name : String? = null,
    var user_image_url : String? = null,
)
