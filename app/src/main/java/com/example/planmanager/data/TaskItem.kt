package com.example.planmanager.data


import com.example.planmanager.util.TaskType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class TaskItem(
    //    unique id
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    //    Task type
    var isToDo: Boolean = false,
    var isDeadline: Boolean = false,
    var isSchedule: Boolean = false,

    //    Universal entry: title
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
) {
    //    Deadline progress variable
    val percentagePassed: Int
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = Calendar.getInstance().time
            val startDateParsed = dateFormat.parse(startDateDeadline) ?: return 0
            val deadlineDateParsed = dateFormat.parse(dateDeadline) ?: return 0

            val totalDuration = deadlineDateParsed.time - startDateParsed.time
            val passedDuration = currentDate.time - startDateParsed.time

            val percentage = passedDuration.toFloat() / totalDuration.toFloat()
            return (String.format("%.2f", percentage).toFloat()*100).toInt()
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
