package com.codingstudio.super50pscattandance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codingstudio.super50pscattandance.model.Attendance
import com.codingstudio.super50pscattandance.model.TicketPerson

class AdapterTicketPersonListView(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolderAttendance(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewName = itemView.findViewById<TextView>(R.id.textViewName)
        private val textViewPhoneNo = itemView.findViewById<TextView>(R.id.textViewPhoneNo)
        private val textViewGender = itemView.findViewById<TextView>(R.id.textViewGender)
        private val textViewAge = itemView.findViewById<TextView>(R.id.textViewAge)
        private val textViewDate = itemView.findViewById<TextView>(R.id.textViewDate)
        private val deleteImageView = itemView.findViewById<ImageView>(R.id.deleteImageView)

        fun bind(ticketPerson: TicketPerson) {

            textViewName.text = "Name: ${ticketPerson.person_name}"
            textViewPhoneNo.text = "Phone No : ${ticketPerson.person_phone}"
            textViewGender.text = "Gender On: ${ticketPerson.person_gender}"
            textViewAge.text = "Age On: ${ticketPerson.person_age}"

            if(ticketPerson.is_attended){
                textViewDate.text = "Attendance On: ${ticketPerson.attendance_datetime}"
                deleteImageView.visibility = VISIBLE
            } else {
                deleteImageView.visibility = GONE
            }

            deleteImageView.setOnClickListener {
                onAttendanceDeleteClickListener?.let {
                    it(ticketPerson)
                }
            }

            itemView.setOnClickListener {
                onAttendanceAddClickListener?.let {
                    it(ticketPerson)
                }
            }

        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<TicketPerson>() {

        override fun areItemsTheSame(oldItem: TicketPerson, newItem: TicketPerson): Boolean {
            return oldItem.pk_ticket_person_id == newItem.pk_ticket_person_id
        }

        override fun areContentsTheSame(oldItem: TicketPerson, newItem: TicketPerson): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderAttendance(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_ticket_person, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val attendance = differ.currentList[position]
        (holder as ViewHolderAttendance).bind(attendance)
    }

    private var onAttendanceDeleteClickListener : ((TicketPerson) -> Unit) ?= null

    fun setOnAttendanceDeleteClickListener(listener : (TicketPerson) -> Unit) {
        onAttendanceDeleteClickListener = listener
    }

    private var onAttendanceAddClickListener : ((TicketPerson) -> Unit) ?= null

    fun setOnAttendanceAddClickListener(listener : (TicketPerson) -> Unit) {
        onAttendanceAddClickListener = listener
    }




}