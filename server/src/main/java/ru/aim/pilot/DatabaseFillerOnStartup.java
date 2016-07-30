package ru.aim.pilot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.aim.pilot.model.Revision;
import ru.aim.pilot.repository.RevisionRepository;

@Component
public class DatabaseFillerOnStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RevisionRepository revisionRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        revisionRepository.saveAndFlush(new Revision(null, "test1", "test1", 1, 1, 1, 1, "test", "test", 1, null, null));
        revisionRepository.saveAndFlush(new Revision(null, "test1", "test1", 1, 1, 1, 1, "test", "test", 1, null, null));
        revisionRepository.saveAndFlush(new Revision(null, "test1", "test1", 1, 1, 1, 1, "test", "test", 1, null, null));
        revisionRepository.saveAndFlush(new Revision(null, "test1", "test1", 1, 1, 1, 1, "test", "test", 1, null, null));
    }
}
