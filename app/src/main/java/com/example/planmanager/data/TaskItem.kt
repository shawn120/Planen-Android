package com.example.planmanager.data


import android.util.Log
import com.example.planmanager.util.TaskType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.FromJson
import java.util.Date
import java.util.UUID
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

@Entity
data class TaskItem(
    //    unique id
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val timeCreated: Long = System.currentTimeMillis(),
    //    Task type
    var isToDo: Boolean = false,
    var isDeadline: Boolean = false,
    var isSchedule: Boolean = false,

    //    Universal entry: title, and first date
    var title: String? = null,

    //    To-Do
    var dateToDo: String? = null, //for edit date, choose date on calendar(month)
    var completedToDo: Boolean? = false,

    //    Deadline
    var dateDeadline: String? = null,
    var startDateDeadline: String? = null,

    //    Schedule
    var locationSchedule: String? = null,
    var dateSchedule: String? = null,
    var timeSchedule: String? = null,

    var universalDateUsingStartDate: String = dateToDo?:startDateDeadline?:dateSchedule?:"",
    var universalDateUsingDeadlineDate: String = dateToDo?:dateDeadline?:dateSchedule?:"",


//    var currentUpdateDate: Date? = null
) {
    @Ignore
    //    Deadline progress variable
    var currentUpdateDate: Date? = Calendar.getInstance().time

    @Ignore
    fun updateCurrentDate(date: Date?) {
        currentUpdateDate = date
        Log.d("deadlineselect", "updateCurrentDate : $currentUpdateDate")
    }

    @Ignore
    val percentagePassed: (Date?) -> Int = { date ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateParsed = dateFormat.parse(startDateDeadline)
            val deadlineDateParsed = dateFormat.parse(dateDeadline)
            val currentDate = currentUpdateDate
            val totalDuration = deadlineDateParsed.time - startDateParsed.time

            val passedDuration = currentDate?.time?.minus(startDateParsed.time)

            var percentage = passedDuration?.toFloat()?.div(totalDuration.toFloat())
        if (percentage != null) {
            if (percentage < 0) {
                percentage = 0F
            }
        }
        if (percentage != null) {
            if (percentage > 1) {
                percentage = 1F
            }
        }
            (String.format("%.2f", percentage).toFloat()*100).toInt()
        }

    val taskType: TaskType
        get() {
            if (isToDo){
                return TaskType.TODO
            }
            if (isDeadline){
                return TaskType.DEADLINE
            }
            if (isSchedule){
                return TaskType.SCHEDULE
            }
            return TaskType.NONE
        }
}

