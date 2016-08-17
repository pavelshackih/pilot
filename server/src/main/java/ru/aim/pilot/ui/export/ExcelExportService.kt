package ru.aim.pilot.ui.export

import org.springframework.stereotype.Component
import org.vaadin.viritin.fields.MTable
import ru.aim.pilot.model.Revision
import java.io.File

@Component
open class ExcelExportService {

    companion object {
        const val FILE_DATE_FORMAT = "HH:mm d/M/yyyy"
        const val EXCEL_MIME_TYPE = "application/vnd.ms-excel"
    }

    fun export(list: List<Revision>): File {
        val table = MTable(Revision::class.java).withProperties("subjectName", "address", "inn", "typeSafeSystem", "checkCount", "allViolationsCount",
                "fixedViolationsCount", "violationsDesc", "violationsMark").withColumnHeaders("Наименование субъекта Российской Федерации",
                "Наименование ОПО, ГТС, наименование, адрес, ИНН эксплуатирующей организации",
                "ИНН организации", "Вид проверяемых систем, режима и охраны",
                "Количество проверок", "Общее число нарушений", "Число устранённых нарушений",
                "Выявленные нарушения проверяемых систем, режима и охраны",
                "Отметка об устранении нарушений").withColumnWidth("address", 100).withFullWidth()
        table.setBeans(list)
        val tableExport = SpringExport(table)
        tableExport.setDateDataFormat(FILE_DATE_FORMAT)
        tableExport.export()
        val file = tableExport.tempFile
        return file
    }
}