package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {
    private Map<Integer, Accident> accidents = new ConcurrentHashMap<>();
    private Map<Integer, AccidentType> accidentTypes = new ConcurrentHashMap<>();
    private AtomicInteger id = new AtomicInteger(5);

    public AccidentMem() {
        accidentTypes.put(1, AccidentType.of(1, "Две машины"));
        accidentTypes.put(2, AccidentType.of(2, "Машина и человек"));
        accidentTypes.put(3, AccidentType.of(3, "Машина и велосипед"));
        for (int i = 1; i < 5; i++) {
            accidents.put(i, new Accident(i, "Name " + i, "Text " + i, "Address " + 1, accidentTypes.get(1)));
        }
    }

    public Collection<Accident> getAllAccidents() {
        return accidents.values();
    }

    public Accident create(Accident accident) {
        if (accident.getId() == 0) {
            save(accident);
        } else {
            update(accident);
        }
        accident.setType(findTypeById(accident.getType().getId()));
        return accident;
    }

    private Accident update(Accident accident) {
        return accidents.replace(accident.getId(), accident);
    }

    private Accident save(Accident accident) {
        accident.setId(id.incrementAndGet());
        return accidents.put(accident.getId(), accident);
    }

    public Accident findAccidentById(int id) {
        return accidents.get(id);
    }

    public AccidentType findTypeById(int id) {
        return accidentTypes.get(id);
    }

    public Collection<AccidentType> getAllTypes() {
        return accidentTypes.values();
    }
}
