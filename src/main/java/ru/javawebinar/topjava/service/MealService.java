package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface MealService {
    Meal save(int userId, Meal meal);

    void update(int userId, Meal meal) throws NotFoundException;

    void delete(int userId, int mealId) throws NotFoundException;

    Meal get(int userId, int mealId) throws NotFoundException;

    List<Meal> getAll(int userId);

    List<Meal> getFiltered(int userId, LocalDateTime start, LocalDateTime end);
}