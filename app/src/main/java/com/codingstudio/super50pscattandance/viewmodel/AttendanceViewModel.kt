package com.codingstudio.super50pscattandance.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codingstudio.super50pscattandance.AppApplication
import com.codingstudio.super50pscattandance.Constants
import com.codingstudio.super50pscattandance.EventWrapper
import com.codingstudio.super50pscattandance.model.Resource
import com.codingstudio.super50pscattandance.model.ResponseAttendance
import com.codingstudio.super50pscattandance.model.ResponseInsert
import com.codingstudio.super50pscattandance.model.ResponseTicket
import com.codingstudio.super50pscattandance.repository.AttendanceRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class AttendanceViewModel(application: Application) : AndroidViewModel(application){

    private val TAG = "HotelViewModel"
    private var attendanceRepository: AttendanceRepository = AttendanceRepository()


    val attendanceStore : MutableLiveData<EventWrapper<Resource<ResponseInsert>>> = MutableLiveData()
    val usserLogin : MutableLiveData<EventWrapper<Resource<ResponseInsert>>> = MutableLiveData()
    val deleteAttendance : MutableLiveData<EventWrapper<Resource<ResponseInsert>>> = MutableLiveData()
    val allAttendance : MutableLiveData<EventWrapper<Resource<ResponseAttendance>>> = MutableLiveData()
    val allAttendanceByCenter : MutableLiveData<EventWrapper<Resource<ResponseAttendance>>> = MutableLiveData()
    val getScannedAttendance : MutableLiveData<EventWrapper<Resource<ResponseTicket>>> = MutableLiveData()




    fun userLogin(username: String, password: String) = viewModelScope.launch {

        usserLogin.postValue(EventWrapper(Resource.Loading()))
        try {
            if (hasInternetConnection()) {
                val response = attendanceRepository.userLogin(username = username, password = password)

                when {
                    response.code() == Constants.STATUS_SUCCESS -> {

                        usserLogin.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                    response.code() == Constants.STATUS_NOT_FOUND -> {

                        usserLogin.postValue(EventWrapper(Resource.Error(Constants.NOT_FOUND)))
                    }
                    response.code() == Constants.STATUS_INTERNAL_ERROR -> {

                        usserLogin.postValue(EventWrapper(Resource.Error(Constants.SERVER_ERROR)))
                    }
                    response.code() == Constants.STATUS_CONFLICT -> {

                        usserLogin.postValue(EventWrapper(Resource.Error(Constants.CONFLICT)))
                    }
                    else -> {
                        usserLogin.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                }
            } else {
                usserLogin.postValue(EventWrapper(Resource.Error(Constants.NO_INTERNET)))
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> usserLogin.postValue(EventWrapper(Resource.Error(Constants.NETWORK_FAILURE)))
                else -> usserLogin.postValue(EventWrapper(Resource.Error(Constants.CONVERSION_ERROR)))
            }
        }
    }

    fun studentAttendanceStore(
        fk_user_id: String,
        fk_application_no: String,
        center: String,
        created_by: String
    ) = viewModelScope.launch {

        attendanceStore.postValue(EventWrapper(Resource.Loading()))
        try {
            if (hasInternetConnection()) {
                val response = attendanceRepository.studentAttendanceStore(
                    fk_user_id = fk_user_id,
                    fk_application_no = fk_application_no,
                    center = center,
                    created_by = created_by)

                when {
                    response.code() == Constants.STATUS_SUCCESS -> {

                        attendanceStore.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                    response.code() == Constants.STATUS_NOT_FOUND -> {

                        attendanceStore.postValue(EventWrapper(Resource.Error(Constants.NOT_FOUND)))
                    }
                    response.code() == Constants.STATUS_INTERNAL_ERROR -> {

                        attendanceStore.postValue(EventWrapper(Resource.Error(Constants.SERVER_ERROR)))
                    }
                    response.code() == Constants.STATUS_CONFLICT -> {

                        attendanceStore.postValue(EventWrapper(Resource.Error(Constants.CONFLICT)))
                    }
                    else -> {
                        attendanceStore.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                }
            } else {
                attendanceStore.postValue(EventWrapper(Resource.Error(Constants.NO_INTERNET)))
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> attendanceStore.postValue(EventWrapper(Resource.Error(Constants.NETWORK_FAILURE)))
                else -> attendanceStore.postValue(EventWrapper(Resource.Error(Constants.CONVERSION_ERROR)))
            }
        }
    }

    fun deleteAttendance(
        fk_application_no: String,
        modified_by: String
    ) = viewModelScope.launch {

        deleteAttendance.postValue(EventWrapper(Resource.Loading()))
        try {
            if (hasInternetConnection()) {
                val response = attendanceRepository.deleteAttendance(
                    fk_application_no = fk_application_no,
                    modified_by = modified_by)

                when {
                    response.code() == Constants.STATUS_SUCCESS -> {

                        deleteAttendance.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                    response.code() == Constants.STATUS_NOT_FOUND -> {

                        deleteAttendance.postValue(EventWrapper(Resource.Error(Constants.NOT_FOUND)))
                    }
                    response.code() == Constants.STATUS_INTERNAL_ERROR -> {

                        deleteAttendance.postValue(EventWrapper(Resource.Error(Constants.SERVER_ERROR)))
                    }
                    response.code() == Constants.STATUS_CONFLICT -> {

                        deleteAttendance.postValue(EventWrapper(Resource.Error(Constants.CONFLICT)))
                    }
                    else -> {
                        deleteAttendance.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                }
            } else {
                deleteAttendance.postValue(EventWrapper(Resource.Error(Constants.NO_INTERNET)))
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> deleteAttendance.postValue(EventWrapper(Resource.Error(Constants.NETWORK_FAILURE)))
                else -> deleteAttendance.postValue(EventWrapper(Resource.Error(Constants.CONVERSION_ERROR)))
            }
        }
    }

    fun getAllAttendance() = viewModelScope.launch {

        allAttendance.postValue(EventWrapper(Resource.Loading()))
        try {
            if (hasInternetConnection()) {
                val response = attendanceRepository.getAllAttendance(center = "All")

                when {
                    response.code() == Constants.STATUS_SUCCESS -> {

                        allAttendance.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                    response.code() == Constants.STATUS_NOT_FOUND -> {

                        allAttendance.postValue(EventWrapper(Resource.Error(Constants.NOT_FOUND)))
                    }
                    response.code() == Constants.STATUS_INTERNAL_ERROR -> {

                        allAttendance.postValue(EventWrapper(Resource.Error(Constants.SERVER_ERROR)))
                    }
                    response.code() == Constants.STATUS_CONFLICT -> {

                        allAttendance.postValue(EventWrapper(Resource.Error(Constants.CONFLICT)))
                    }
                    else -> {
                        allAttendance.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                }
            } else {
                allAttendance.postValue(EventWrapper(Resource.Error(Constants.NO_INTERNET)))
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> allAttendance.postValue(EventWrapper(Resource.Error(Constants.NETWORK_FAILURE)))
                else -> allAttendance.postValue(EventWrapper(Resource.Error(Constants.CONVERSION_ERROR)))
            }
        }
    }

    fun getAllAttendance_ByCenter(center: String) = viewModelScope.launch {

        allAttendanceByCenter.postValue(EventWrapper(Resource.Loading()))
        try {
            if (hasInternetConnection()) {
                val response = attendanceRepository.getAllAttendance_ByCenter(center = center)

                when {
                    response.code() == Constants.STATUS_SUCCESS -> {

                        allAttendanceByCenter.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                    response.code() == Constants.STATUS_NOT_FOUND -> {

                        allAttendanceByCenter.postValue(EventWrapper(Resource.Error(Constants.NOT_FOUND)))
                    }
                    response.code() == Constants.STATUS_INTERNAL_ERROR -> {

                        allAttendanceByCenter.postValue(EventWrapper(Resource.Error(Constants.SERVER_ERROR)))
                    }
                    response.code() == Constants.STATUS_CONFLICT -> {

                        allAttendanceByCenter.postValue(EventWrapper(Resource.Error(Constants.CONFLICT)))
                    }
                    else -> {
                        allAttendanceByCenter.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                }
            } else {
                allAttendanceByCenter.postValue(EventWrapper(Resource.Error(Constants.NO_INTERNET)))
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> allAttendanceByCenter.postValue(EventWrapper(Resource.Error(Constants.NETWORK_FAILURE)))
                else -> allAttendanceByCenter.postValue(EventWrapper(Resource.Error(Constants.CONVERSION_ERROR)))
            }
        }
    }

    fun getTicketDetailsByTicketId(pk_log_id: String, pk_ticket_id: String) = viewModelScope.launch {

        getScannedAttendance.postValue(EventWrapper(Resource.Loading()))
        try {
            if (hasInternetConnection()) {
                val response = attendanceRepository.getTicketDetailsByTicketId(pk_log_id = pk_log_id, pk_ticket_id = pk_ticket_id)

                when {
                    response.code() == Constants.STATUS_SUCCESS -> {

                        getScannedAttendance.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                    response.code() == Constants.STATUS_NOT_FOUND -> {

                        getScannedAttendance.postValue(EventWrapper(Resource.Error(Constants.NOT_FOUND)))
                    }
                    response.code() == Constants.STATUS_INTERNAL_ERROR -> {

                        getScannedAttendance.postValue(EventWrapper(Resource.Error(Constants.SERVER_ERROR)))
                    }
                    response.code() == Constants.STATUS_CONFLICT -> {

                        getScannedAttendance.postValue(EventWrapper(Resource.Error(Constants.CONFLICT)))
                    }
                    else -> {
                        getScannedAttendance.postValue(EventWrapper(handleNetworkResponse(response)))
                    }
                }
            } else {
                getScannedAttendance.postValue(EventWrapper(Resource.Error(Constants.NO_INTERNET)))
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> getScannedAttendance.postValue(EventWrapper(Resource.Error(Constants.NETWORK_FAILURE)))
                else -> getScannedAttendance.postValue(EventWrapper(Resource.Error(Constants.CONVERSION_ERROR)))
            }
        }
    }




    /// ----------------------- HANDLE NETWORK RESPONSE ------------------------------ ///

    private fun <T> handleNetworkResponse(response: Response<T>): Resource<T> {

        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /// ----------------------- CHECK CONNECTION ------------------------------ ///


    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<AppApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork ?: return false
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}