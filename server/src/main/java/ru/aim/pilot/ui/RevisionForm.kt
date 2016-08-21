package ru.aim.pilot.ui

import com.vaadin.ui.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.vaadin.viritin.fields.IntegerField
import org.vaadin.viritin.fields.MTextArea
import org.vaadin.viritin.fields.MTextField
import org.vaadin.viritin.form.AbstractForm
import org.vaadin.viritin.layouts.MFormLayout
import org.vaadin.viritin.layouts.MVerticalLayout
import ru.aim.pilot.model.Revision
import ru.aim.pilot.spring.UiStringResolver
import org.springframework.stereotype.Component as SpringComponent

@SpringComponent
@Scope("prototype")
open class RevisionForm
@Autowired
constructor(private val resolver: UiStringResolver) : AbstractForm<Revision>() {

    private val subjectName = MTextField(Revision::subjectName.name)
    private val address = MTextArea(Revision::address.name)
    private val inn = MTextField(Revision::inn.name)
    private val typeSafeSystem = MTextArea(Revision::typeSafeSystem.name)
    private val checkCount = IntegerField().withCaption(Revision::checkCount.name)
    private val allViolationsCount = IntegerField().withCaption(Revision::allViolationsCount.name)
    private val fixedViolationsCount = IntegerField().withCaption(Revision::fixedViolationsCount.name)
    private val violationsDesc = MTextArea(Revision::violationsDesc.name)
    private val violationsMark = MTextArea(Revision::violationsMark.name)

    init {
        setSizeUndefined()
    }

    override fun createContent(): Component {
        val formLayout = MFormLayout(
                subjectName,
                address,
                inn,
                typeSafeSystem,
                checkCount,
                allViolationsCount,
                fixedViolationsCount,
                violationsDesc,
                violationsMark).withWidth("")
        formLayout.forEach { it.caption = resolver.resolveKey(it.caption) }
        return MVerticalLayout(
                formLayout,
                toolbar).withWidth("")
    }

    override fun getCancelCaption(): String {
        return resolver.resolveKey("cancel")
    }

    override fun getSaveCaption(): String {
        return resolver.resolveKey("save")
    }
}