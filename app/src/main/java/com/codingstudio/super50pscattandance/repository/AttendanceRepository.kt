package com.codingstudio.super50pscattandance.repository

import com.codingstudio.super50pscattandance.api.RetrofitInstance
import java.util.*

class AttendanceRepository() {

    /*suspend fun getAllDepartments() = RetrofitInstance.api.getAllDepartments()

    suspend fun getGasTestResult() = RetrofitInstance.api.getGasTestResult()

    suspend fun getAreaByDeptId(departmentId : String) = RetrofitInstance.api.getAreaByDeptId(
        departmentId = departmentId)*/

    suspend fun userLogin(username: String, password: String) = RetrofitInstance.api.userLogin(username = username, password = password)

    suspend fun studentAttendanceStore(
        fk_user_id: String,
        fk_application_no: String,
        center: String,
        created_by: String
    ) =
        RetrofitInstance.api.studentAttendanceStore(
            fk_user_id = fk_user_id,
            fk_application_no = fk_application_no,
            center = center,
            created_by = created_by)

    suspend fun deleteAttendance(
        fk_application_no: String,
        modified_by: String
    ) =
        RetrofitInstance.api.deleteAttendance(
            fk_application_no = fk_application_no,
            modified_by = modified_by)

    suspend fun getAllAttendance(center: String) = RetrofitInstance.api.getAllAttendance(center = center)

    suspend fun getAllAttendance_ByCenter(center: String) = RetrofitInstance.api.getAllAttendance_ByCenter(center = center)
    suspend fun getTicketDetailsByTicketId(pk_log_id: String, pk_ticket_id: String) = RetrofitInstance.api.getTicketDetailsByTicketId(pk_log_id = pk_log_id, pk_ticket_id = pk_ticket_id)




}