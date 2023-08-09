package com.codingstudio.super50pscattandance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codingstudio.super50pscattandance.model.Attendance

class AdapterAttendanceListView(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolderAttendance(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewName = itemView.findViewById<TextView>(R.id.textViewName)
        private val textViewApplicationNo = itemView.findViewById<TextView>(R.id.textViewApplicationNo)
        private val textViewDate = itemView.findViewById<TextView>(R.id.textViewDate)
        private val deleteImageView = itemView.findViewById<ImageView>(R.id.deleteImageView)
        private val imageViewStudentImage = itemView.findViewById<ImageView>(R.id.imageViewStudentImage)

        fun bind(attendance: Attendance) {

            textViewName.text = "Name: ${attendance.first_name} ${attendance.last_name}"
            textViewApplicationNo.text = "Application No : "
            textViewDate.text = "Attendance On: ${attendance.created_on}"

            Glide.with(context).load(attendance.user_image_url).into(imageViewStudentImage)

            deleteImageView.setOnClickListener {
                onAttendanceDeleteClickListener?.let {
                    it(attendance)
                }
            }

        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Attendance>() {

        override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return oldItem.attendance_id == newItem.attendance_id
        }

        override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderAttendance(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_student_attendance, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val attendance = differ.currentList[position]
        (holder as ViewHolderAttendance).bind(attendance)
    }

    private var onAttendanceDeleteClickListener : ((Attendance) -> Unit) ?= null

    fun setOnAttendanceDeleteClickListener(listener : (Attendance) -> Unit) {
        onAttendanceDeleteClickListener = listener
    }




}