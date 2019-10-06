package com.example.chatclient.Model

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object Time {
    private val formatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault())

    fun getTime(): String {
        val currentTime = Instant.now()
        val timeStamp = formatter.format(currentTime)
        return timeStamp
    }
}