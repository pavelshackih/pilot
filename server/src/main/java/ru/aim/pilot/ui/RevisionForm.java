package ru.aim.pilot.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.IntegerField;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import ru.aim.pilot.model.Revision;

public class RevisionForm extends AbstractForm<Revision> {

    private TextField subjectName = new MTextField("Наименование субъекта Российской Федерации");
    private TextField address = new MTextField("Наименование ОПО, ГТС, наименование, адрес, ИНН эксплуатирующей организации");
    private TextField inn = new MTextField("ИНН организации");
    private IntegerField checkCount = new IntegerField().withCaption("Количество проверок");
    private IntegerField allViolationsCount = new IntegerField().withCaption("Общее число нарушений");
    private IntegerField fixedViolationsCount = new IntegerField().withCaption("Число устранённых нарушений");
    private MTextArea violationsDesc = new MTextArea("Выявленные нарушения проверяемых систем, режима и охраны");
    private MTextArea violationsMark = new MTextArea("Отметка об устранении нарушений");

    public RevisionForm() {
        setSizeUndefined();
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        subjectName,
                        address,
                        inn,
                        checkCount,
                        allViolationsCount,
                        fixedViolationsCount,
                        violationsDesc,
                        violationsMark
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }

    @Override
    public String getCancelCaption() {
        return "Отмена";
    }

    @Override
    public String getSaveCaption() {
        return "Сохранить";
    }
}
