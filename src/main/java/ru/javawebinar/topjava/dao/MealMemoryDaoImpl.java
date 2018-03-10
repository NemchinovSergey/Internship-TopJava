package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealMemoryDaoImpl implements MealDao {
    private final Logger LOG = getLogger(MealMemoryDaoImpl.class);

    private final AtomicInteger counter = new AtomicInteger(0);
    public final Map<Integer, Meal> MEALS = new ConcurrentHashMap<>();

    {
        LOG.debug("initialization");

        List<Meal> mealList = Arrays.asList(
                new Meal(LocalDateTime.of(2017, Month.MAY, 29, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2017, Month.MAY, 29, 13, 0), "Обед", 1500),
                new Meal(LocalDateTime.of(2017, Month.MAY, 29, 20, 0), "Ужин", 700),
                new Meal(LocalDateTime.of(2017, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2017, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2017, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2017, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2017, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2017, Month.MAY, 31, 20, 0), "Ужин", 510),
                new Meal(LocalDateTime.of(2017, Month.JUNE, 1, 10, 0), "Завтрак", 300),
                new Meal(LocalDateTime.of(2017, Month.JUNE, 1, 13, 0), "Обед", 900),
                new Meal(LocalDateTime.of(2017, Month.JUNE, 1, 20, 0), "Ужин", 750)
        );

        mealList.forEach(this::save);
    }
    
    @Override
    public Meal save(Meal meal) {
        LOG.debug("save: {}", meal);
        if (meal.getId() == null) {
            meal.setId(counter.getAndIncrement());
        }
        return MEALS.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        LOG.debug("delete: {}", id);
        MEALS.remove(id);
    }

    @Override
    public Meal get(int id) {
        LOG.debug("get: {}", id);
        Meal meal = MEALS.get(id);
        LOG.debug("meal: {}", meal);
        return meal;
    }

    @Override
    public List<Meal> getAll() {
        LOG.debug("getAll()");
        return new ArrayList<>(MEALS.values());
    }
}
