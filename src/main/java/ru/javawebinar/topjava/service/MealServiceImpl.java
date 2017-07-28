package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
import static ru.javawebinar.topjava.util.ValidationUtil.checkUserId;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal save(int userId, Meal meal) {
        checkUserId(meal, userId);
        return repository.save(userId, meal);
    }

    @Override
    public void update(int userId, Meal meal) {
        checkNotFoundWithId(repository.get(userId, meal.getId()), meal.getId());
        checkUserId(meal, userId);
        repository.save(userId, meal);
    }

    @Override
    public void delete(int userId, int mealId) {
        checkNotFoundWithId(repository.delete(userId, mealId), mealId);
    }

    @Override
    public Meal get(int userId, int mealId) {
        return checkNotFoundWithId(repository.get(userId, mealId), mealId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return repository.getFiltered(userId, startDate, endDate, startTime, endTime);
    }
}