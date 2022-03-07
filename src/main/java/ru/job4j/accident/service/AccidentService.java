package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentStore;
import java.util.Collection;

@Service
public class AccidentService {
    private final AccidentStore store;

    public AccidentService(AccidentStore store) {
        this.store = store;
    }

    public Collection<Accident> getAllAccidents() {
        return store.getAllAccidents();
    }

    public Collection<AccidentType> getAllTypes() {
        return store.getAllTypes();
    }

    public Collection<Rule> getAllRules() {
        return store.getAllRules();
    }

    public Accident add(Accident accident, String[] rulesIds) {
        return store.create(accident, rulesIds);
    }

    public Accident findById(int id) {
        return store.findAccidentById(id);
    }
}
