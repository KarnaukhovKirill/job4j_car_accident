package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentRepository;
import ru.job4j.accident.repository.AccidentTypeRepository;
import ru.job4j.accident.repository.RuleRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AccidentService {
    private final AccidentRepository accidentRep;
    private final RuleRepository ruleRep;
    private final AccidentTypeRepository typeRep;

    public AccidentService(AccidentRepository accident, RuleRepository rule, AccidentTypeRepository type) {
        this.accidentRep = accident;
        this.ruleRep = rule;
        this.typeRep = type;
    }

    public Collection<Accident> getAllAccidents() {
        return accidentRep.findAll();
    }

    public Collection<AccidentType> getAllTypes() {
        List<AccidentType> rsl = new ArrayList<>();
        typeRep.findAll().forEach(rsl::add);
        return rsl;
    }

    public Collection<Rule> getAllRules() {
        List<Rule> rsl = new ArrayList<>();
        ruleRep.findAll().forEach(rsl::add);
        return rsl;
    }

    public Accident add(Accident accident, String[] rulesIds) {
        AccidentType type = typeRep.findById(accident.getType().getId()).get();
        for (String i : rulesIds) {
            var rule = ruleRep.findById(Integer.parseInt(i)).get();
            accident.addRule(rule);
        }
        accident.setType(type);
        accidentRep.save(accident);
        return accident;
    }

    public Accident findById(int id) {
        return accidentRep.findById(id).get();
    }
}
