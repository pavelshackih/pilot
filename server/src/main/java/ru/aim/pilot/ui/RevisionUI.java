package ru.aim.pilot.ui;

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
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import ru.aim.pilot.model.Revision;
import ru.aim.pilot.model.RevisionType;
import ru.aim.pilot.model.Territory;
import ru.aim.pilot.repository.RevisionRepository;
import ru.aim.pilot.repository.TerritoryRepository;
import ru.aim.pilot.ui.export.ExcelExportService;

import java.util.Date;
import java.util.List;

@Title("Сведения")
@SpringUI(path = "/list")
@Theme("valo")
public class RevisionUI extends UI {

    private RevisionRepository revisionRepository;

    @Autowired
    public void setRevisionRepository(RevisionRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    private TerritoryRepository territoryRepository;

    @Autowired
    public void setTerritoryRepository(TerritoryRepository territoryRepository) {
        this.territoryRepository = territoryRepository;
    }

    private ExcelExportService excelExportService;

    @Autowired
    public void setExcelExportService(ExcelExportService excelExportService) {
        this.excelExportService = excelExportService;
    }

    private Territory territory;

    private final MGrid<Revision> opoTable = TableHelperKt.buildRevisionGrid();
    private final MGrid<Revision> gtsTable = TableHelperKt.buildRevisionGrid();
    private MGrid<Revision> currentTable = opoTable;

    private RevisionType currentType = RevisionType.OPO;

    private Button addNew = new MButton(FontAwesome.PLUS, "Добавить", this::add);
    private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, "Редактировать", this::edit);
    private Button delete = new ConfirmButton(FontAwesome.TRASH_O, "Удалить", "Вы действительно хотиту удалить данную запись?", this::remove);
    private Button export = new MButton(FontAwesome.FILE, "Экспортировать в Excel", this::exportToExcel);

    @Override
    protected void init(VaadinRequest request) {
        if (request.getParameter("id") != null) {
            String terId = request.getParameter("id");
            territory = territoryRepository.findOne(Long.parseLong(terId));
        }
        if (territory != null) {
            Page.getCurrent().setTitle(String.format("Сведения по %s", territory.getName()));
        }

        Page.getCurrent().getStyles().add(".wordwrap-grid .v-grid-cell-wrapper {\n" +
                "   /* Do not specify any margins, paddings or borders here */\n" +
                "   white-space: normal;\n" +
                "   overflow: hidden;\n" +
                "}");

        fillTables();

        TabSheet tabSheet = new TabSheet();
        tabSheet.setHeight(100.0f, Unit.PERCENTAGE);
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        tabSheet.addTab(opoTable, "Сведения по ОПО");
        tabSheet.addTab(gtsTable, "Сведения по ГТС");

        tabSheet.addSelectedTabChangeListener((TabSheet.SelectedTabChangeListener) event -> {
            Component component = event.getTabSheet().getSelectedTab();
            currentType = component == opoTable ? RevisionType.OPO : RevisionType.GTS;
            currentTable = (MGrid<Revision>) component;
            adjustActionButtonState();
        });

        setContent(new MVerticalLayout(new MHorizontalLayout(addNew, edit, delete, export)
                .withHeightUndefined(), tabSheet)
                .withFullHeight().withExpand(tabSheet, 1));

        adjustActionButtonState();

        opoTable.addSelectionListener(event -> adjustActionButtonState());
        gtsTable.addSelectionListener(event -> adjustActionButtonState());

        opoTable.addStyleName("wordwrap-grid");
        gtsTable.addStyleName("wordwrap-grid");
    }

    private void adjustActionButtonState() {
        boolean hasSelection = currentTable.getSelectedRow() != null;
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    private void fillTables() {
        opoTable.setRows(findRevision(RevisionType.OPO));
        gtsTable.setRows(findRevision(RevisionType.GTS));
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
        edit(currentTable.getSelectedRow());
    }

    private void remove(ClickEvent e) {
        revisionRepository.delete(currentTable.getSelectedRow());
        currentTable.getContainerDataSource().removeAllItems();
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
        entry.setLastUpdateDate(new Date());
        revisionRepository.save(entry);
        fillTables();
        closeWindow();
    }

    private void resetEntry(Revision entry) {
        fillTables();
        closeWindow();
    }

    private void exportToExcel(Button.ClickEvent clickEvent) {
        // excelExportService.export(currentTable.getContainerDataSource(), currentType, this);
    }

    private void closeWindow() {
        getWindows().forEach(this::removeWindow);
    }
}
