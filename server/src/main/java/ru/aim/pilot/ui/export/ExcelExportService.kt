package ru.aim.pilot.ui.export

import com.vaadin.addon.tableexport.TemporaryFileDownloadResource
import com.vaadin.data.Container
import com.vaadin.ui.UI
import org.springframework.stereotype.Component
import org.vaadin.haijian.filegenerator.ExcelFileBuilder
import ru.aim.pilot.model.RevisionType
import java.io.File
import java.util.*

@Component
open class ExcelExportService {

    companion object {
        const val FILE_DATE_FORMAT = "HH:mm d/M/yyyy"
        const val EXCEL_MIME_TYPE = "application/vnd.ms-excel"
    }

    fun export(container: Container, type: RevisionType, ui: UI) {
        val file = export(container, type)
        val resource = TemporaryFileDownloadResource(ui, "report.xls", EXCEL_MIME_TYPE, file)
        UI.getCurrent().page.open(resource, null, false)
    }

    fun export(container: Container, type: RevisionType): File {
        val fileBuilder = ExcelFileBuilder(container)
        fileBuilder.header = "Отчёт по ${type.uiName}"
        fileBuilder.setDateFormat(FILE_DATE_FORMAT)

        fileBuilder.setVisibleColumns(arrayOf("subjectName", "address", "inn", "typeSafeSystem",
                "checkCount", "allViolationsCount", "fixedViolationsCount",
                "violationsDesc", "lastUpdateDate"))

        fileBuilder.setColumnHeader("subjectName", "Наименование субъекта Российской Федерации")
        fileBuilder.setColumnHeader("address", "Наименование ОПО, ГТС, наименование, адрес, ИНН эксплуатирующей организации")
        fileBuilder.setColumnHeader("inn", "ИНН организации")
        fileBuilder.setColumnHeader("typeSafeSystem", "Вид проверяемых систем, режима и охраны")
        fileBuilder.setColumnHeader("checkCount", "Количество проверок")
        fileBuilder.setColumnHeader("allViolationsCount", "Общее число нарушений")
        fileBuilder.setColumnHeader("fixedViolationsCount", "Число устранённых нарушений")
        fileBuilder.setColumnHeader("violationsDesc", "Выявленные нарушения проверяемых систем, режима и охраны")
        fileBuilder.setColumnHeader("violationsMark", "Отметка об устранении нарушений")
        fileBuilder.setColumnHeader("lastUpdateDate", "Дата последнего изменения")

        fileBuilder.setLocale(Locale("ru"))

        return fileBuilder.file
    }
}