package ru.aim.pilot.ui.export

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.vaadin.viritin.fields.MTable
import ru.aim.pilot.model.Revision
import ru.aim.pilot.spring.UiStringResolver
import java.io.File
import kotlin.reflect.KProperty

@Component
open class ExcelExportService
@Autowired constructor(private val uiStringResolver: UiStringResolver) {

    companion object {
        const val FILE_DATE_FORMAT = "HH:mm d/M/yyyy"
        const val EXCEL_MIME_TYPE = "application/vnd.ms-excel"
    }

    fun export(list: List<Revision>): File {
        val properties = listOf(Revision::subjectName,
                Revision::address, Revision::inn, Revision::typeSafeSystem,
                Revision::checkCount, Revision::allViolationsCount,
                Revision::fixedViolationsCount, Revision::violationsDesc,
                Revision::violationsMark)

        val table = MTable(Revision::class.java)
                .withProperties(properties.map { it.name })
                .withColumnHeaders(properties.map { it.asUi })

        table.setBeans(list)
        val tableExport = SpringExport(table)
        tableExport.setUiStringResolver(uiStringResolver)
        tableExport.setDateDataFormat(FILE_DATE_FORMAT)
        tableExport.export()
        val file = tableExport.tempFile
        return file
    }

    private val KProperty<*>.asUi: String
        get() = uiStringResolver.resolveFrom(this)

    private fun <T> MTable<T>.withColumnHeaders(headers: List<String>): MTable<T> {
        this.withColumnHeaders(*headers.toTypedArray())
        return this
    }
}