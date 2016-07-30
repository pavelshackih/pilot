package ru.aim.pilot.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import ru.aim.pilot.model.Revision;
import ru.aim.pilot.repository.RevisionRepository;

import java.time.LocalDateTime;

@Title("Сведения по ОПО")
@SpringUI(path = "/revision")
@Theme("valo")
public class RevisionUI extends UI {

    private final RevisionRepository repo;

    private final MTable<Revision> list = new MTable<>(Revision.class)
            .withProperties("id", "subjectName", "address", "inn", "checkCount", "allViolationsCount",
                    "fixedViolationsCount", "violationsDesc", "violationsMark")
            .withColumnHeaders("#", "Наименование субъекта Российской Федерации",
                    "Наименование ОПО, ГТС, наименование, адрес, ИНН эксплуатирующей организации",
                    "ИНН организации", "Количество проверок", "Общее число нарушений", "Число устранённых нарушений",
                    "Выявленные нарушения проверяемых систем, режима и охраны",
                    "Отметка об устранении нарушений").withColumnWidth("address", 100).withFullWidth();

    private Button addNew = new MButton(FontAwesome.PLUS, "Добавить", this::add);
    private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, "Редактировать", this::edit);
    private Button delete = new ConfirmButton(FontAwesome.TRASH_O, "Удалить", "Вы действительно хотиту удалить данную запись?", this::remove);

    @Autowired
    public RevisionUI(RevisionRepository repo) {
        this.repo = repo;
    }

    @Override
    protected void init(VaadinRequest request) {
        setContent(
                new MVerticalLayout(
                        new MLabel("Сведения по ОПО"),
                        new MHorizontalLayout(addNew, edit, delete),
                        list
                )
        );
        listEntities();
        adjustActionButtonState();
        list.addMValueChangeListener(e -> adjustActionButtonState());
    }

    private void adjustActionButtonState() {
        boolean hasSelection = list.getValue() != null;
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    private void listEntities() {
        list.setBeans(repo.findAll());
    }

    private void add(ClickEvent clickEvent) {
        edit(new Revision());
    }

    private void edit(ClickEvent e) {
        edit(list.getValue());
    }

    private void remove(ClickEvent e) {
        repo.delete(list.getValue());
        list.setValue(null);
        listEntities();
    }

    private void edit(final Revision revision) {
        AbstractForm<Revision> phoneBookEntryForm = new RevisionForm();
        phoneBookEntryForm.setEntity(revision);
        phoneBookEntryForm.setReadOnly(false);
        phoneBookEntryForm.setEnabled(true);
        phoneBookEntryForm.setModalWindowTitle(revision.getId() == null ? "Добавление" : "Редактирование");
        phoneBookEntryForm.openInModalPopup();
        phoneBookEntryForm.setSavedHandler(this::saveEntry);
        phoneBookEntryForm.setResetHandler(this::resetEntry);
    }

    private void saveEntry(Revision entry) {
        entry.setLastUpdateDate(LocalDateTime.now());
        repo.save(entry);
        listEntities();
        closeWindow();
    }

    private void resetEntry(Revision entry) {
        listEntities();
        closeWindow();
    }

    private void closeWindow() {
        getWindows().forEach(this::removeWindow);
    }
}
