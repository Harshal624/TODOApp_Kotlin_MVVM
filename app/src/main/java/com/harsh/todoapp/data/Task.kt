package com.harsh.todoapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat


@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val name: String,
    val important: Boolean = false, //default value of false
    val completed_state: Boolean = false,
    val created: Long = System.currentTimeMillis(), //takes the current time of the system in ms
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
    //whenever this value is accessed , get function is executed
}