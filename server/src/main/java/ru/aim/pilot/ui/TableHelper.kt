package ru.aim.pilot.ui

import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.Component
import com.vaadin.ui.Grid
import com.vaadin.ui.GridLayout
import com.vaadin.ui.renderers.DateRenderer
import com.vaadin.ui.renderers.HtmlRenderer
import com.vaadin.ui.renderers.NumberRenderer
import org.vaadin.viritin.grid.MGrid
import org.vaadin.viritin.label.MLabel
import ru.aim.pilot.model.Revision
import ru.aim.pilot.spring.UiStringResolver
import java.util.*

val LOCALE_RU = Locale("ru")
val DATE_FORMAT = "%tF"

fun buildRevisionGrid(): MGrid<Revision> {
    val grid = MGrid<Revision>(Revision::class.java)

    grid.setSelectionMode(Grid.SelectionMode.SINGLE)

    grid.getColumn("subjectName").headerCaption = "Наименование субъекта Российской Федерации"

    grid.getColumn("address").headerCaption = "Наименование ОПО, ГТС, наименование, адрес, ИНН эксплуатирующей организации"
    grid.getColumn("address").renderer = HtmlRenderer("")

    grid.getColumn("inn").renderer = NumberRenderer(LOCALE_RU)
    grid.getColumn("inn").headerCaption = "ИНН организации"

    grid.getColumn("typeSafeSystem").headerCaption = "Вид проверяемых систем, режима и охраны"

    grid.getColumn("checkCount").headerCaption = "Количество проверок"

    grid.getColumn("allViolationsCount").headerCaption = "Общее число нарушений"

    grid.getColumn("fixedViolationsCount").headerCaption = "Число устранённых нарушений"

    grid.getColumn("violationsDesc").headerCaption = "Выявленные нарушения проверяемых систем, режима и охраны"

    grid.getColumn("violationsMark").headerCaption = "Отметка об устранении нарушений"

    grid.getColumn("lastUpdateDate").renderer = DateRenderer(DATE_FORMAT, LOCALE_RU)
    grid.getColumn("lastUpdateDate").headerCaption = "Дата последнего изменения"

    grid.getColumn("type").isHidden = true
    grid.getColumn("territory").isHidden = true
    grid.getColumn("id").isHidden = true

    grid.setColumnOrder("subjectName", "address", "inn", "typeSafeSystem",
            "checkCount", "allViolationsCount", "fixedViolationsCount",
            "violationsDesc", "lastUpdateDate")

    /*grid.detailsGenerator = GridDetailsGenerator()

    grid.addItemClickListener {
        if (it.isDoubleClick) {
            grid.setDetailsVisible(it.itemId, !grid.isDetailsVisible(it.itemId))
        }
    }*/

    grid.setSizeFull()

    return grid
}

class GridDetailsGenerator : Grid.DetailsGenerator {
    override fun getDetails(rowReference: Grid.RowReference?): Component {
        val revision = rowReference?.itemId as Revision
        val properties = UiStringResolver.findUiProperties(Revision::class)
        val pairs = properties.map {
            val uiString = UiStringResolver.resolve(it)
            if (uiString == null) null else Pair(uiString, it.call(revision).toString())
        }.filterNotNull()

        val gridLayout = GridLayout(2, pairs.size)
        gridLayout.isSpacing = true
        gridLayout.setWidth("100%")

        pairs.forEachIndexed { i, pair ->
            gridLayout.addComponent(MLabel(pair.first)
                    .withWidth(null), 0, i)
            gridLayout.addComponent(MLabel(pair.second)
                    .withFullHeight()
                    .withFullHeight()
                    .withContentMode(ContentMode.HTML), 1, i)
        }

        return gridLayout
    }
}

