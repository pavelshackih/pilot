package ru.aim.pilot.ui

import ru.aim.pilot.spring.UserSessionInterceptor
import javax.servlet.http.HttpSession

fun HttpSession.getTerritoryId(fallbackId: Long?): Long? = this.getAttribute(UserSessionInterceptor.TERRITORY_ID) as Long? ?: fallbackId