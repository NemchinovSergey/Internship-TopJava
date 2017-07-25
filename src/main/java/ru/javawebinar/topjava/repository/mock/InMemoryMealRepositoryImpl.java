package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        log.debug("object initialization");
        MealsUtil.MEALS.forEach(meal -> save(meal.getUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.debug("save: {}", meal);
        if (meal.isNew() || Objects.equals(meal.getUserId(), userId)) {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
            }
            repository.put(meal.getId(), meal);
            return meal;
        }
        else {
            log.debug("Access denied! meal id {}, user id {}", meal.getId(), userId);
            return null;
        }
    }

    @Override
    public boolean delete(int userId, int mealId) {
        log.debug("delete: userId {}, mealId {}", userId, mealId);
        Meal meal = repository.get(mealId);
        if (Objects.equals(meal.getUserId(), userId)) {
            log.debug("Delete meal: user id {}, meal id {}", userId, meal.getId());
            repository.remove(mealId);
            return true;
        }
        else {
            log.debug("Access denied! user id {}, meal id {}", userId, meal.getId());
            return false;
        }
    }

    @Override
    public Meal get(int userId, int mealId) {
        log.debug("get: userId {}, mealId {}", userId, mealId);
        Meal meal = repository.get(mealId);
        return Objects.equals(meal.getUserId(), userId) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.debug("getAll: {}", userId);
        return repository.values().stream()
                .filter(u -> Objects.equals(u.getUserId(), userId))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

