package ru.aim.pilot.ui;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import ru.aim.pilot.model.Revision;
import ru.aim.pilot.model.RevisionType;
import ru.aim.pilot.model.Territory;
import ru.aim.pilot.repository.RevisionRepository;
import ru.aim.pilot.repository.TerritoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Title("Сведения")
@SpringUI(path = "/revision")
@Theme("valo")
public class RevisionUI extends UI {

    private final RevisionRepository revisionRepository;

    private TerritoryRepository territoryRepository;

    @Autowired
    public void setTerritoryRepository(TerritoryRepository territoryRepository) {
        this.territoryRepository = territoryRepository;
    }

    private Territory territory;

    private final MTable<Revision> opoTable = TableHelperKt.buildRevisionTable();
    private final MTable<Revision> gtsTable = TableHelperKt.buildRevisionTable();
    private MTable<Revision> currentTable = opoTable;

    private RevisionType currentType = RevisionType.OPO;

    private Button addNew = new MButton(FontAwesome.PLUS, "Добавить", this::add);
    private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, "Редактировать", this::edit);
    private Button delete = new ConfirmButton(FontAwesome.TRASH_O, "Удалить", "Вы действительно хотиту удалить данную запись?", this::remove);
    private Button export = new MButton(FontAwesome.FILE, "Экспортировать в Excel", this::exportToExcel);

    @Autowired
    public RevisionUI(RevisionRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    @Override
    protected void init(VaadinRequest request) {
        if (request.getParameter("id") != null) {
            String terId = request.getParameter("id");
            territory = territoryRepository.findOne(Long.parseLong(terId));
        }
        if (territory != null) {
            Page.getCurrent().setTitle(String.format("Сведения по %s", territory.getName()));
        }

        opoTable.setConverter("lastUpdateDate", new DateConverter());
        gtsTable.setConverter("lastUpdateDate", new DateConverter());

        TabSheet tabSheet = new TabSheet();
        tabSheet.setHeight(100.0f, Unit.PERCENTAGE);
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        tabSheet.addTab(opoTable, "Сведения по ОПО");
        tabSheet.addTab(gtsTable, "Сведения по ГТС");

        tabSheet.addSelectedTabChangeListener((TabSheet.SelectedTabChangeListener) event -> {
            Component component = event.getTabSheet().getSelectedTab();
            currentType = component == opoTable ? RevisionType.OPO : RevisionType.GTS;
            currentTable = (MTable<Revision>) component;
            adjustActionButtonState();
        });

        setContent(new MVerticalLayout(new MHorizontalLayout(addNew, edit, delete, export), tabSheet));

        fillTables();
        adjustActionButtonState();

        opoTable.addMValueChangeListener(e -> adjustActionButtonState());
        gtsTable.addMValueChangeListener(e -> adjustActionButtonState());
    }

    private void adjustActionButtonState() {
        boolean hasSelection = currentTable.getValue() != null;
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    private void fillTables() {
        opoTable.setBeans(findRevision(RevisionType.OPO));
        gtsTable.setBeans(findRevision(RevisionType.GTS));
    }

    private List<Revision> findRevision(RevisionType type) {
        if (territory == null) {
            return revisionRepository.findByType(type);
        } else {
            return revisionRepository.findByTerritoryIdAndType(territory.getId(), type);
        }
    }

    private void add(ClickEvent clickEvent) {
        Revision revision = new Revision();
        revision.setType(currentType);
        edit(revision);
    }

    private void edit(ClickEvent e) {
        edit(currentTable.getValue());
    }

    private void remove(ClickEvent e) {
        revisionRepository.delete(gtsTable.getValue());
        currentTable.setValue(null);
        fillTables();
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
        revisionRepository.save(entry);
        fillTables();
        closeWindow();
    }

    private void resetEntry(Revision entry) {
        fillTables();
        closeWindow();
    }

    private void exportToExcel(Button.ClickEvent clickEvent) {
        ExcelExport excelExport = new ExcelExport(currentTable);
        excelExport.excludeCollapsedColumns();
        excelExport.setReportTitle(String.format("Отчёт по %s", currentType.getUiName()));
        excelExport.export();
    }

    private void closeWindow() {
        getWindows().forEach(this::removeWindow);
    }
}
