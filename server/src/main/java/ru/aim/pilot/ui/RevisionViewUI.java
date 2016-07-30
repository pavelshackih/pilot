package ru.aim.pilot.ui;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import ru.aim.pilot.model.Revision;
import ru.aim.pilot.repository.RevisionRepository;

@Title("Сведения по ОПО")
@SpringUI(path = "/revisionview")
@Theme("valo")
public class RevisionViewUI extends UI {

    private final RevisionRepository repo;

    private final MTable<Revision> list = new MTable<>(Revision.class)
            .withProperties("id", "subjectName", "address", "inn", "checkCount", "allViolationsCount",
                    "fixedViolationsCount", "violationsDesc", "violationsMark", "lastUpdateDate")
            .withColumnHeaders("#", "Наименование субъекта Российской Федерации",
                    "Наименование ОПО, ГТС, наименование, адрес, ИНН эксплуатирующей организации",
                    "ИНН организации", "Количество проверок", "Общее число нарушений", "Число устранённых нарушений",
                    "Выявленные нарушения проверяемых систем, режима и охраны",
                    "Отметка об устранении нарушений", "Дата последнего изменения")
            .withColumnWidth("address", 100)
            .withFullWidth();

    @Autowired
    public RevisionViewUI(RevisionRepository repo) {
        this.repo = repo;
    }

    private Button export = new MButton(FontAwesome.FILE, "Экспортировать в Excel", this::exportToExcel);

    @Override
    protected void init(VaadinRequest request) {
        list.setConverter("lastUpdateDate", new DateConverter());
        setContent(
                new MVerticalLayout(
                        new MLabel("Сведения по ОПО"),
                        export,
                        list
                )
        );
        listEntities();
    }

    private void exportToExcel(Button.ClickEvent clickEvent) {
        ExcelExport excelExport = new ExcelExport(list);
        excelExport.excludeCollapsedColumns();
        excelExport.setReportTitle("Отчёт");
        excelExport.export();
    }

    private void listEntities() {
        list.setBeans(repo.findAll());
    }
}
