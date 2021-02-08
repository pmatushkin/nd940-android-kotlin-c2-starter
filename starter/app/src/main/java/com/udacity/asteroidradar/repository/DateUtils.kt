package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Constants
import java.text.SimpleDateFormat
import java.util.*


fun formatDate(date: Date, formatString: String = Constants.API_QUERY_DATE_FORMAT): String {
    return SimpleDateFormat(formatString, Locale.getDefault()).format(date)
}

fun addDays(date: Date, days: Int = Constants.DEFAULT_END_DATE_DAYS): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_YEAR, days)
    return calendar.time
}
