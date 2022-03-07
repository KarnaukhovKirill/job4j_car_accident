package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import java.util.Collection;

@Repository
public interface AccidentStore {
    Accident create(Accident accident, String[] rulesIds);
    Collection<Accident> getAllAccidents();
    Accident findAccidentById(int id);
    Collection<AccidentType> getAllTypes();
    Collection<Rule> getAllRules();
}
