package ru.aim.pilot.spring

import ru.aim.pilot.model.Revision
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.declaredMemberProperties
import kotlin.reflect.jvm.javaField

object UiStringResolver {

    fun resolve(property: KProperty<*>): String? {
        val uiString = property.javaField?.getAnnotation(UiString::class.java)
        if (uiString != null) {
            return uiString.value
        } else {
            return null
        }
    }

    fun findUiProperties(clazz: KClass<*>): Collection<KProperty<*>> {
        return clazz.declaredMemberProperties
    }

    fun getHeaders(clazz: KClass<*>) =
            clazz.declaredMemberProperties.map {
                val uiString = UiStringResolver.resolve(it)
                if (uiString == null) null else uiString
            }.filterNotNull()

    fun getRevisionHeaders() = getHeaders(Revision::class)

}