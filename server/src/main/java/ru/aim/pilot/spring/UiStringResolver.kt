package ru.aim.pilot.spring

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
}