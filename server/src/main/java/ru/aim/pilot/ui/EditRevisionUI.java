package ru.aim.pilot.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aim.pilot.model.Revision;
import ru.aim.pilot.model.RevisionType;
import ru.aim.pilot.model.Territory;
import ru.aim.pilot.repository.RevisionRepository;
import ru.aim.pilot.repository.TerritoryRepository;

import java.util.Date;


@Title("Редактировние")
@SpringUI(path = "/edit")
@Theme("valo")
public class EditRevisionUI extends UI {

    private final RevisionRepository revisionRepository;
    private final TerritoryRepository territoryRepository;

    private RevisionType revisionType;
    private String terId;

    @Autowired
    public EditRevisionUI(RevisionRepository revisionRepository, TerritoryRepository territoryRepository) {
        this.revisionRepository = revisionRepository;
        this.territoryRepository = territoryRepository;
    }

    @Override
    protected void init(VaadinRequest request) {
        String id = request.getParameter("id");
        terId = request.getParameter("terId");
        Revision revision;
        if (id == null) {
            revision = new Revision();
            revisionType = RevisionType.values()[Integer.parseInt(request.getParameter("revType"))];
            getPage().setTitle("Добавление");
        } else {
            revision = revisionRepository.findOne(Long.parseLong(id));
        }
        RevisionForm revisionForm = new RevisionForm();
        revisionForm.setEntity(revision);
        revisionForm.setSavedHandler(this::saveEntry);
        revisionForm.setResetHandler(this::resetEntry);
        setContent(revisionForm);
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
        getUI().getPage().setLocation("/rev?id=" + terId);
    }
}
