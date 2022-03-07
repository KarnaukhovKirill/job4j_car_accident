package ru.job4j.accident.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashSet;

@Repository
@Primary
public class AccidentJdbcTemplate implements AccidentStore {
    private final JdbcTemplate jdbc;

    public AccidentJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Accident create(Accident accident, String[] rulesIds) {
        if (accident.getId() == 0) {
            save(accident);
        } else {
            jdbc.update("delete from accidents where id = ?",
                    accident.getId());
            save(accident);
        }
        addRules(accident, rulesIds);
        return accident;
    }

    private void save(Accident accident) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String insertSql = "insert into accidents (name, text, address, type_id) values (?, ?, ?, ?)";
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[] {"id"});
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getText());
            ps.setString(3, accident.getAddress());
            ps.setInt(4, accident.getType().getId());
            return ps;
        }, keyHolder);
        accident.setId(keyHolder.getKey().intValue());
    }

    @Override
    public Accident findAccidentById(int id) {
        return jdbc.queryForObject(
                "select a.id, a.name, a.text, a.address, t.id as typeId, t.name as typeName from accidents a "
                        + "join types t on a.type_id = t.id where a.id = ?",
                accidentRowMapper,
                id);
    }

    private void addRules(Accident accident, String[] rulesIds) {
        for (String rule : rulesIds) {
            jdbc.update("insert into accident_rules (accident_id, rule_id) values (?, ?)",
                    accident.getId(), Integer.parseInt(rule));
        }
    }


    public Collection<Accident> getAllAccidents() {
        Collection<Accident> rsl = jdbc.query("select a.id, a.name, a.text, a.address, t.id as typeId, t.name as typeName from accidents a "
                        + "join types t on a.type_id = t.id",
                accidentRowMapper);
        for (Accident accident : rsl) {
            getRulesForAccident(accident);
        }
        return rsl;
    }

    private void getRulesForAccident(Accident accident) {
        Collection<Rule> rsl = jdbc.query("select * from rules join accident_rules on id = rule_id where accident_id = ?",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                }, accident.getId());
        accident.setRules(new HashSet<>(rsl));
    }

    @Override
    public Collection<AccidentType> getAllTypes() {
        return jdbc.query("select * from types",
                (rs, row) -> AccidentType.of(rs.getInt("id"), rs.getString("name")));
    }

    @Override
    public Collection<Rule> getAllRules() {
        return jdbc.query("select * from rules",
                (rs, row) -> Rule.of(rs.getInt("id"), rs.getString("name")));
    }

    private final RowMapper<Accident> accidentRowMapper = (rs, row) -> {
        Accident accident = new Accident();
        accident.setId(rs.getInt("id"));
        accident.setName(rs.getString("name"));
        accident.setText(rs.getString("text"));
        accident.setAddress(rs.getString("address"));
        AccidentType type = new AccidentType();
        type.setId(rs.getInt("typeId"));
        type.setName(rs.getString("typeName"));
        accident.setType(type);
        return accident;
    };
}
