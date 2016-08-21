package ru.aim.pilot.ui;

import com.vaadin.annotations.Theme;
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
import ru.aim.pilot.service.RevisionService;
import ru.aim.pilot.spring.UiStringResolver;

import java.util.Date;

@SpringUI(path = "/revision/edit")
@Theme("valo")
public class EditRevisionUI extends UI {

    private final RevisionService revisionService;
    private final RevisionForm revisionForm;
    private final UiStringResolver uiStringResolver;

    private RevisionType revisionType;
    private String terId;

    private Button addNewButton = new MButton(FontAwesome.HOME, null, this::home);

    @Autowired
    public EditRevisionUI(RevisionService revisionService, RevisionForm revisionForm, UiStringResolver uiStringResolver) {
        this.revisionService = revisionService;
        this.revisionForm = revisionForm;
        this.uiStringResolver = uiStringResolver;
    }

    @Override
    protected void init(VaadinRequest request) {
        String id = request.getParameter("id");
        terId = request.getParameter("terId");
        revisionType = RevisionType.values()[Integer.parseInt(request.getParameter("revType"))];
        Revision revision;
        if (id == null) {
            revision = new Revision();
            getPage().setTitle(uiStringResolver.resolveKey("adding"));
        } else {
            revision = revisionService.findRevision(Long.parseLong(id));
            getPage().setTitle(uiStringResolver.resolveKey("edit"));
        }

        addNewButton.setCaption(uiStringResolver.resolveKey("goToRootPage"));
        revisionForm.setEntity(revision);
        revisionForm.setSavedHandler(this::saveEntry);
        revisionForm.setResetHandler(this::resetEntry);
        setContent(new MVerticalLayout(addNewButton, revisionForm));
    }

    private void home(Button.ClickEvent event) {
        getUI().getPage().setLocation("/");
    }

    private void resetEntry(Revision entry) {
        redirect();
    }

    private void saveEntry(Revision entry) {
        if (revisionType != null) {
            entry.setType(revisionType);
            Territory territory = revisionService.findTerritory(Long.parseLong(terId));
            entry.setTerritory(territory);
        }
        revisionService.saveRevision(entry);
        redirect();
    }

    private void redirect() {
        String tab = revisionType.name().toLowerCase();
        getUI().getPage().setLocation("/revision/list?id=" + terId + "#" + tab);
    }
}
