package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {
    private HashMap<Integer, Accident> accidents = new HashMap<>();
    private AtomicInteger id = new AtomicInteger(5);

    public AccidentMem() {
        for (int i = 1; i < 5; i++) {
            accidents.put(i, new Accident(i, "Name " + i, "Text " + i, "Address " + 1));
        }
    }

    public Collection<Accident> getAll() {
        return accidents.values();
    }

    public Accident create(Accident accident) {
        return accidents.putIfAbsent(id.incrementAndGet(), accident);
    }
}
