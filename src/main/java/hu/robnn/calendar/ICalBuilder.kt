package hu.robnn.calendar

import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.DateTime
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.CalScale
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.Version
import net.fortuna.ical4j.util.RandomUidGenerator
import java.util.*


class ICalBuilder {

    companion object {
        const val dayTime = "N"
        const val nightTime = "É"
        const val dayTimeString = "Nappalos"
        const val nightTimeString = "Éjszakás"
    }
    fun buildICalendar(startTime: java.util.Date, days: List<String>): Calendar {
        val timezone = TimeZone.getDefault()

        val cal = java.util.Calendar.getInstance()
        cal.time = startTime
        cal.add(java.util.Calendar.DATE, -1)
        val iCalendar = Calendar()
        iCalendar.properties.add(ProdId("-//Events Calendar//iCal4j 1.0//EN"));
        iCalendar.properties.add(CalScale.GREGORIAN);
        iCalendar.properties.add(Version.VERSION_2_0)

        val events: List<VEvent?> = days.map { s ->
            cal.add(java.util.Calendar.DATE, 1)
            return@map when (s) {
                dayTime -> {
                    val startDate = GregorianCalendar()
                    startDate.timeZone = timezone
                    startDate.time = cal.time
                    startDate.set(java.util.Calendar.HOUR_OF_DAY, 6)
                    startDate.set(java.util.Calendar.MINUTE, 0)
                    startDate.set(java.util.Calendar.SECOND, 0)

                    val endDate = GregorianCalendar()
                    endDate.time = startDate.time
                    endDate.add(java.util.Calendar.HOUR, 12)
                    buildVEvent(startDate, endDate, dayTimeString)
                }
                nightTime -> {
                    val startDate = GregorianCalendar()
                    startDate.timeZone = timezone
                    startDate.time = cal.time
                    startDate.set(java.util.Calendar.HOUR_OF_DAY, 18)
                    startDate.set(java.util.Calendar.MINUTE, 0)
                    startDate.set(java.util.Calendar.SECOND, 0)

                    val endDate = GregorianCalendar()
                    endDate.time = startDate.time
                    endDate.add(java.util.Calendar.HOUR, 12)
                    buildVEvent(startDate, endDate, nightTimeString)
                }
                else -> null
            }

        }
        events.filterNotNull().forEach { event ->
            val ug = RandomUidGenerator()
            val uid = ug.generateUid()
            event.properties.add(uid)

            iCalendar.components.add(event)
        }
       return iCalendar

    }

    private fun buildVEvent(startTime: java.util.Calendar, endTime: java.util.Calendar, type: String): VEvent {
        val start = DateTime(startTime.time)
        val end = DateTime(endTime.time)
        return VEvent(start, end, type)
    }
}