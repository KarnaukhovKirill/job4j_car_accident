package ru.job4j.accident.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Primary
public class AccidentHibernate implements AccidentStore {
    private final SessionFactory sf;

    public AccidentHibernate(SessionFactory sf) {
        this.sf = sf;
    }

    private <T> T doCommand(final Function<Session, T> command) {
        final Session session = sf.openSession();
        try {
            T rsl = command.apply(session);
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Accident create(Accident accident, String[] rulesIds) {
        addRules(accident, rulesIds);
        return doCommand(session -> {
            session.beginTransaction();
            session.saveOrUpdate(accident);
            session.getTransaction().commit();
            return accident;
        });
    }

    private void addRules(Accident accident, String[] rulesIds) {
        doCommand(session -> {
            var rules = (List<Rule>) session.createQuery("from Rule").list();
            List<Integer> rulesId = Arrays.stream(rulesIds)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            for (Rule rule : rules) {
                for (Integer i : rulesId) {
                    if (rule.getId() == i) {
                        accident.addRule(rule);
                    }
                }
            }
            return null;
        });
    }

    @Override
    public Collection<Accident> getAllAccidents() {
        return doCommand(session -> session.createQuery("select distinct a from Accident a "
                        + "join fetch a.rules "
                        + "join fetch a.type")
                .list());

    }

    @Override
    public Accident findAccidentById(int id) {
        return doCommand(session -> (Accident) session.createQuery("select distinct a from Accident a "
                            + "join fetch a.rules "
                            + "join fetch a.type "
                        + "where a.id = :id")
                .setParameter("id", id)
                .uniqueResult());
    }

    @Override
    public Collection<AccidentType> getAllTypes() {
        return doCommand(session -> session.createQuery("from AccidentType").list());
    }

    @Override
    public Collection<Rule> getAllRules() {
        return doCommand(session -> session.createQuery("from Rule").list());
    }
}
