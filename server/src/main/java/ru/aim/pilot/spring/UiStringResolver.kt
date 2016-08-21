package ru.aim.pilot.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import ru.aim.pilot.App
import kotlin.reflect.KProperty

@Component
open class UiStringResolver
@Autowired constructor(private val messages: MessageSource) {

    fun resolveFrom(property: KProperty<*>?): String =
            when (property) {
                null -> ""
                else -> resolveKey(property.name)
            }

    fun resolveFrom(properties: List<KProperty<*>?>): List<String> = properties.map { resolveFrom(it) }

    fun resolveKey(key: String?): String =
            when (key) {
                null -> ""
                else -> messages.getMessage(key, null, "", App.DEFAULT_LOCALE)
            }
}