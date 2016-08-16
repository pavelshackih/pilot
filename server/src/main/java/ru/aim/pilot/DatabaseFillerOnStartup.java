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

import java.util.Date;

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
        territoryRepository.save(new Territory(null, "Уральское"));
        territoryRepository.save(new Territory(null, "Средне-Поволжское"));
        territoryRepository.save(new Territory(null, "Сибирское"));
        territoryRepository.save(new Territory(null, "Северо-Уральское"));
        territoryRepository.save(new Territory(null, "Северо-Кавказское"));
        territoryRepository.save(new Territory(null, "Северо-Западное"));
        territoryRepository.save(new Territory(null, "Северо-Восточное"));
        territoryRepository.save(new Territory(null, "Приокское"));
        territoryRepository.save(new Territory(null, "Приволжское"));
        territoryRepository.save(new Territory(null, "Нижне-Волжское"));
        territoryRepository.save(new Territory(null, "МТУ Ростехнадзора"));
        territoryRepository.save(new Territory(null, "Ленское"));
        territoryRepository.save(new Territory(null, "Крымское"));
        territoryRepository.save(new Territory(null, "Кавказское"));
        territoryRepository.save(new Territory(null, "Западно-Уральское"));
        territoryRepository.save(new Territory(null, "Забайкальское"));
        territoryRepository.save(new Territory(null, "Енисейское"));
        territoryRepository.save(new Territory(null, "Дальневосточное"));
        territoryRepository.save(new Territory(null, "Волжско-Окское"));
        territoryRepository.save(new Territory(null, "Верхне-Донское"));
        territoryRepository.flush();

        for (Territory territory : territoryRepository.findAll()) {
            for (int i = 0; i < 5; i++) {
                Date date = new Date();
                revisionRepository.save(new Revision(null, "test1", "test2", 1, "", 1, 1, 1, "test3", "test4", RevisionType.OPO, territory, date));
                revisionRepository.save(new Revision(null, "test1", "test2", 1, "", 1, 1, 1, "test3", "test4", RevisionType.GTS, territory, date));
            }
        }
        revisionRepository.flush();
    }
}