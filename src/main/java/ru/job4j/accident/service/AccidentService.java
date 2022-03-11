package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.*;
import ru.job4j.accident.repository.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AccidentService {
    private final AccidentRepository accidentRep;
    private final RuleRepository ruleRep;
    private final AccidentTypeRepository typeRep;
    private final UserRepository users;
    private final AuthorityRepository authorities;

    public AccidentService(AccidentRepository accident,
                           RuleRepository rule,
                           AccidentTypeRepository type,
                           UserRepository users,
                           AuthorityRepository authorities) {
        this.accidentRep = accident;
        this.ruleRep = rule;
        this.typeRep = type;
        this.users = users;
        this.authorities = authorities;
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

    public User add(User user) {
        return users.save(user);
    }

    public Authority findByAuthority(String roleUser) {
        return authorities.findByAuthority(roleUser);
    }
}
