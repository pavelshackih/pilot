package ru.aim.pilot.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import ru.aim.pilot.model.Revision;
import ru.aim.pilot.model.RevisionType;
import ru.aim.pilot.model.Territory;
import ru.aim.pilot.repository.RevisionRepository;
import ru.aim.pilot.repository.TerritoryRepository;

import java.util.Date;


@Title("Редактировние")
@SpringUI(path = "/revision/edit")
@Theme("valo")
public class EditRevisionUI extends UI {

    private final RevisionRepository revisionRepository;
    private final TerritoryRepository territoryRepository;

    private RevisionType revisionType;
    private String terId;

    private Button addNew = new MButton(FontAwesome.HOME, "К списку управлений", this::home);

    @Autowired
    public EditRevisionUI(RevisionRepository revisionRepository, TerritoryRepository territoryRepository) {
        this.revisionRepository = revisionRepository;
        this.territoryRepository = territoryRepository;
    }

    @Override
    protected void init(VaadinRequest request) {
        String id = request.getParameter("id");
        terId = request.getParameter("terId");
        revisionType = RevisionType.values()[Integer.parseInt(request.getParameter("revType"))];
        Revision revision;
        if (id == null) {
            revision = new Revision();
            getPage().setTitle("Добавление");
        } else {
            revision = revisionRepository.findOne(Long.parseLong(id));
        }
        RevisionForm revisionForm = new RevisionForm();
        revisionForm.setEntity(revision);
        revisionForm.setSavedHandler(this::saveEntry);
        revisionForm.setResetHandler(this::resetEntry);
        setContent(new MVerticalLayout(addNew, revisionForm));
    }

    private void home(Button.ClickEvent event) {
        getUI().getPage().setLocation("/");
    }

    private void resetEntry(Revision entry) {
        redirect();
    }

    private void saveEntry(Revision entry) {
        entry.setLastUpdateDate(new Date());
        if (revisionType != null) {
            entry.setType(revisionType);
            Territory territory = territoryRepository.findOne(Long.parseLong(terId));
            entry.setTerritory(territory);
        }
        revisionRepository.saveAndFlush(entry);
        redirect();
    }

    private void redirect() {
        String tab = revisionType.name().toLowerCase();
        getUI().getPage().setLocation("/revision/list?id=" + terId + "#" + tab);
    }
}
