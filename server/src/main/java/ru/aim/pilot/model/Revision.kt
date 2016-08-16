package ru.aim.pilot.model

import ru.aim.pilot.spring.UiString
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Revision(
        @Id @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO) var id: Long? = null,
        @UiString("Наименование субъекта Российской Федерации")
        var subjectName: String? = null,
        @UiString("Наименование ОПО, ГТС, наименование, адрес, ИНН эксплуатирующей организации")
        var address: String? = null,
        @UiString("ИНН организации")
        var inn: Long = 0,
        @UiString("Вид проверяемых систем, режима и охраны")
        var typeSafeSystem: String? = null,
        @UiString("Количество проверок")
        var checkCount: Int = 0,
        @UiString("Общее число нарушений")
        var allViolationsCount: Int = 0,
        @UiString("Число устранённых нарушений")
        var fixedViolationsCount: Int = 0,
        @UiString("Выявленные нарушения проверяемых систем, режима и охраны")
        var violationsDesc: String? = null,
        @UiString("Отметка об устранении нарушений")
        var violationsMark: String? = null,
        var type: RevisionType = RevisionType.OPO,
        @ManyToOne
        var territory: Territory? = null,
        @UiString("Дата последнего изменения")
        var lastUpdateDate: Date? = null
) {
    companion object {
        val headers = arrayOf("Наименование субъекта Российской Федерации",
                "Наименование ОПО, ГТС, наименование, адрес, ИНН эксплуатирующей организации",
                "ИНН организации", "Вид проверяемых систем, режима и охраны", "Количество проверок", "Общее число нарушений",
                "Число устранённых нарушений", "Выявленные нарушения проверяемых систем, режима и охраны",
                "Отметка об устранении нарушений",
                "Дата последнего изменения", "")
    }
}