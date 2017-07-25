package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));

            System.out.println("Users:");
            List<User> users = adminUserController.getAll();
            users.forEach(System.out::println);

            System.out.println("Attempting deletion some other user");
            ProfileRestController profileRestController = appCtx.getBean(ProfileRestController.class);
            profileRestController.delete(0);

            System.out.println("After deletion:");
            users = adminUserController.getAll();
            users.forEach(System.out::println);



            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.getAll().forEach(System.out::println);
            mealRestController.getAllWithExceed().forEach(System.out::println);

            Meal meal = mealRestController.get(12);

            //List<MealWithExceed> list = mealRestController.getFilteredWithExceed(null, null, LocalTime.of(10, 0), LocalTime.of(13, 0));
            List<MealWithExceed> list = mealRestController.getFilteredWithExceed(LocalDate.of(2015, 5, 31), null, LocalTime.of(10, 0), LocalTime.of(13, 0));
            list.forEach(System.out::println);
        }
    }
}
