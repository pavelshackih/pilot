package ru.aim.pilot.ui

import com.vaadin.data.util.converter.Converter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.util.*

class DateConverter : Converter<String, LocalDateTime> {

    var formatter = DateTimeFormatterBuilder()
            .appendValue(ChronoField.DAY_OF_MONTH)
            .appendLiteral(".")
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral(".")
            .appendPattern("u")
            .appendLiteral(" ")
            .appendValue(ChronoField.HOUR_OF_DAY)
            .appendLiteral(":")
            .appendText(ChronoField.MINUTE_OF_HOUR, TextStyle.NARROW_STANDALONE)
            .toFormatter()

    override fun getPresentationType(): Class<String> = String::class.java

    override fun convertToModel(value: String?, targetType: Class<out LocalDateTime>?, locale: Locale?): LocalDateTime {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getModelType(): Class<LocalDateTime> = LocalDateTime::class.java

    override fun convertToPresentation(value: LocalDateTime?, targetType: Class<out String>?, locale: Locale?): String =
            if (value == null) "" else formatter.format(value)
}
