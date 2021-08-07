package hu.robnn.calendar

import java.lang.IllegalArgumentException
import java.nio.file.Path
import java.util.*
import java.nio.file.Files
import net.fortuna.ical4j.data.CalendarOutputter
import java.io.FileOutputStream

fun main() {
    val startDate = Calendar.getInstance()
    startDate.set(Calendar.MONTH, Calendar.AUGUST);
    startDate.set(Calendar.DAY_OF_MONTH, 7);
    startDate.set(Calendar.YEAR, 2021);
    startDate.set(Calendar.HOUR_OF_DAY, 0);
    startDate.set(Calendar.MINUTE, 0);
    startDate.set(Calendar.SECOND, 0);
    val resource = ICalBuilder::class.java.classLoader.getResource("2021.txt") ?: throw IllegalArgumentException("Resource not found")
    val fileName = Path.of(resource.toURI())
    val days = Files.readString(fileName).split(" ")
    val calBuilder = ICalBuilder()
    val buildICalendar = calBuilder.buildICalendar(startDate.time, days)
    val fout = FileOutputStream("output.ics")

    val outputter = CalendarOutputter()
    outputter.output(buildICalendar, fout)
}
