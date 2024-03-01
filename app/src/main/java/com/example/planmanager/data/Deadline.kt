package com.example.planmanager.data
import java.text.SimpleDateFormat
import java.util.*

data class Deadline(
    var title: String,
    var deadlineDate: String,
    var startDate: String,
) {
    val percentagePassed: Int
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = Calendar.getInstance().time
            val startDateParsed = dateFormat.parse(startDate) ?: return 0
            val deadlineDateParsed = dateFormat.parse(deadlineDate) ?: return 0

            val totalDuration = deadlineDateParsed.time - startDateParsed.time
            val passedDuration = currentDate.time - startDateParsed.time

            val percentage = passedDuration.toFloat() / totalDuration.toFloat()
            return (String.format("%.2f", percentage).toFloat()*100).toInt()
        }
}