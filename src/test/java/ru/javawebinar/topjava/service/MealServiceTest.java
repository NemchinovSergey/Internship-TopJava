package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
public class MealServiceTest {

    private List<Meal> MEALS;

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        MEALS = new ArrayList<>(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1));
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        Meal meal = mealService.get(MEAL2.getId(), USER_ID);
        MATCHER.assertEquals(MEAL2, meal);
    }

    @Test
    public void testDelete() throws Exception {
        MEALS.remove(MEAL2);
        mealService.delete(MEAL2.getId(), USER_ID);
        MATCHER.assertCollectionEquals(MEALS, mealService.getAll(USER_ID));
    }

    @Test
    public void testGetBetweenDates() throws Exception {
        LocalDate start = LocalDate.of(2015, Month.MAY, 30);
        LocalDate end = LocalDate.of(2015, Month.MAY, 31);
        List<Meal> filteredList = MEALS.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), start, end))
                .collect(Collectors.toList());

        MATCHER.assertCollectionEquals(filteredList, mealService.getBetweenDates(start, end, USER_ID));
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        LocalDateTime start = LocalDateTime.of(2015, Month.MAY, 30, 7, 0);
        LocalDateTime end = LocalDateTime.of(2015, Month.MAY, 31, 11, 0);
        List<Meal> filteredList = MEALS.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), start, end))
                .collect(Collectors.toList());

        MATCHER.assertCollectionEquals(filteredList, mealService.getBetweenDateTimes(start, end, USER_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        MATCHER.assertCollectionEquals(MEALS, mealService.getAll(USER_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        final int ID = 2;
        Meal updatedMeal = new Meal(MEALS.get(ID).getDateTime(), "Другой завтрак", 700);
        updatedMeal.setId(MEALS.get(ID).getId());
        MEALS.set(ID, updatedMeal);
        mealService.update(updatedMeal, USER_ID);
        MATCHER.assertCollectionEquals(MEALS, mealService.getAll(USER_ID));
    }

    @Test
    public void testSave() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.of(2015, Month.JUNE, 1, 18, 0), "Новый ужин", 300);
        Meal savedMeal = mealService.save(newMeal, USER_ID);
        newMeal.setId(savedMeal.getId());
        MEALS.add(0, newMeal);
        MATCHER.assertCollectionEquals(MEALS, mealService.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        mealService.get(MEAL1.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        mealService.delete(MEAL2.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotFound() throws Exception {
        mealService.update(MEAL1, ADMIN_ID);
    }

}