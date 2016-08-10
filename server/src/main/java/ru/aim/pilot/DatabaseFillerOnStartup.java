package ru.aim.pilot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.aim.pilot.model.Revision;
import ru.aim.pilot.model.RevisionType;
import ru.aim.pilot.model.Territory;
import ru.aim.pilot.repository.RevisionRepository;
import ru.aim.pilot.repository.TerritoryRepository;

@Component
public class DatabaseFillerOnStartup implements ApplicationListener<ContextRefreshedEvent> {

    private final RevisionRepository revisionRepository;

    private final TerritoryRepository territoryRepository;

    @Autowired
    public DatabaseFillerOnStartup(TerritoryRepository territoryRepository, RevisionRepository revisionRepository) {
        this.territoryRepository = territoryRepository;
        this.revisionRepository = revisionRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        territoryRepository.save(new Territory(null, "Центральное"));
        territoryRepository.save(new Territory(null, "Поволожское"));
        territoryRepository.save(new Territory(null, "Юго-Западное"));
        territoryRepository.save(new Territory(null, "Дальне-Восточное"));
        territoryRepository.save(new Territory(null, "Сибирское"));
        territoryRepository.save(new Territory(null, "Северо-Западное"));
        territoryRepository.flush();

        for (Territory territory : territoryRepository.findAll()) {
            for (int i = 0; i < 5; i++) {
                revisionRepository.save(new Revision(null, "test1", "test1", 1, 1, 1, 1, "test", "test", RevisionType.OPO, territory, null));
                revisionRepository.save(new Revision(null, "test1", "test1", 1, 1, 1, 1, "test", "test", RevisionType.GTS, territory, null));
            }
        }
        revisionRepository.flush();
    }
}