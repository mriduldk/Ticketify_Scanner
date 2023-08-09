package com.codingstudio.super50pscattandance.api

import com.codingstudio.super50pscattandance.model.ResponseAttendance
import com.codingstudio.super50pscattandance.model.ResponseInsert
import com.codingstudio.super50pscattandance.model.ResponseTicket
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface RetrofitAPI {

    @POST("attendance/login")
    @FormUrlEncoded
    suspend fun userLogin(
        @Field("username")
        username: String,
        @Field("password")
        password: String
    ): Response<ResponseInsert>

    @POST("attendance/store")
    @FormUrlEncoded
    suspend fun studentAttendanceStore(
        @Field("fk_user_id")
        fk_user_id: String,
        @Field("fk_application_no")
        fk_application_no: String,
        @Field("center")
        center: String,
        @Field("created_by")
        created_by: String
    ): Response<ResponseInsert>


    @POST("attendance/delete")
    @FormUrlEncoded
    suspend fun deleteAttendance(
        @Field("fk_application_no")
        fk_application_no: String,
        @Field("modified_by")
        modified_by: String
    ): Response<ResponseInsert>

    @POST("attendance/getall")
    @FormUrlEncoded
    suspend fun getAllAttendance(
        @Field("center")
        center: String = "All"
    ): Response<ResponseAttendance>

    @POST("attendance/getbycenter")
    @FormUrlEncoded
    suspend fun getAllAttendance_ByCenter(
        @Field("center")
        center: String
    ): Response<ResponseAttendance>


    @POST("attendance/GetTicketDetailsByTicketId")
    @FormUrlEncoded
    suspend fun getTicketDetailsByTicketId(
        @Field("pk_log_id")
        pk_log_id: String,
        @Field("pk_ticket_id")
        pk_ticket_id: String
    ): Response<ResponseTicket>


}