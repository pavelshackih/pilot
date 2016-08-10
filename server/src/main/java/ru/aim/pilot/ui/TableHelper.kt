package ru.aim.pilot.ui

import org.vaadin.viritin.fields.MTable
import ru.aim.pilot.model.Revision

fun buildRevisionTable(): MTable<Revision> =
        MTable(Revision::class.java).
                withProperties("subjectName", "address", "inn", "checkCount", "allViolationsCount",
                        "fixedViolationsCount", "violationsDesc", "violationsMark", "lastUpdateDate").
                withColumnHeaders("Наименование субъекта Российской Федерации",
                        "Наименование ОПО, ГТС, наименование, адрес, ИНН эксплуатирующей организации",
                        "ИНН организации", "Количество проверок", "Общее число нарушений", "Число устранённых нарушений",
                        "Выявленные нарушения проверяемых систем, режима и охраны",
                        "Отметка об устранении нарушений", "Дата последнего изменения").
                withFullWidth()

