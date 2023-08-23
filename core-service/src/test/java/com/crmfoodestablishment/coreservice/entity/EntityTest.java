package com.crmfoodestablishment.coreservice.entity;

import com.crmfoodestablishment.coreservice.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class EntityTest {

    @Test
    void createMenu() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSession();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Menu menu = Menu.builder()
                .name("new_menu")
                .season("spring")
                .build();

        session.persist(menu);

        session.getTransaction().commit();
    }

    @Test
    void createDish() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSession();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Menu menu = Menu.builder()
                .name("menu2")
                .season("autumn")
                .build();
        Dish dish = Dish.builder()
                .menu(menu)
                .price(10.00)
                .ingredients("Potato, salt")
                .build();

        session.persist(menu);
        session.persist(dish);

        session.getTransaction().commit();
    }
    @Test
    void createOrder() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSession();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Order order = Order.builder()
                .comment("Hi")
                .creationDate(LocalDateTime.now())
                .totalPrice(100.0)
                .build();

        session.persist(order);

        session.getTransaction().commit();
    }
}
