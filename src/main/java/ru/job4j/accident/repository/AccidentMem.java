package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem implements AccidentStore {
    private Map<Integer, Accident> accidents = new ConcurrentHashMap<>();
    private Map<Integer, AccidentType> accidentTypes = new ConcurrentHashMap<>();
    private Map<Integer, Rule> rules = new ConcurrentHashMap<>();
    private AtomicInteger id = new AtomicInteger(5);

    public AccidentMem() {
        accidentTypes.put(1, AccidentType.of(1, "Две машины"));
        accidentTypes.put(2, AccidentType.of(2, "Машина и человек"));
        accidentTypes.put(3, AccidentType.of(3, "Машина и велосипед"));
        rules.put(1, Rule.of(1, "Статья 1"));
        rules.put(2, Rule.of(2, "Статья 2"));
        rules.put(3, Rule.of(3, "Статья 3"));
        for (int i = 1; i < 5; i++) {
            var accident = new Accident(i, "Name " + i, "Text " + i, "Address " + 1, accidentTypes.get(1));
            accident.addRule(rules.get(1));
            accidents.put(i, accident);
        }
    }

    public Collection<Accident> getAllAccidents() {
        return accidents.values();
    }

    public Accident create(Accident accident, String[] rulesIds) {
        if (accident.getId() == 0) {
            save(accident);
        } else {
            update(accident);
        }
        addRules(accident, rulesIds);
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

    private void addRules(Accident accident, String[] rulesIds) {
        for (String id : rulesIds) {
            accident.addRule(findRuleById(Integer.parseInt(id)));
        }
    }

    public Accident findAccidentById(int id) {
        return accidents.get(id);
    }

    public Rule findRuleById(int id) {
        return rules.get(id);
    }

    public AccidentType findTypeById(int id) {
        return accidentTypes.get(id);
    }

    public Collection<AccidentType> getAllTypes() {
        return accidentTypes.values();
    }

    public Collection<Rule> getAllRules() {
        return rules.values();
    }
}
