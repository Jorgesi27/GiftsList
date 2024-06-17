package com.example.application.user;

import com.example.application.domain.Usuario;
import com.example.application.enums.Rol;
import com.github.javafaker.Faker;

public class ObjectMother {

    private static final Faker faker = new Faker();

    public static Usuario createTestUser() {
        Usuario testUser = new Usuario();
        testUser.setEmail(faker.internet().emailAddress());
        testUser.setNombre(faker.name().firstName());
        testUser.setApellidos(faker.name().lastName());
        testUser.setPassword("password");
        testUser.setActive(true);
        testUser.addRole(Rol.USER);
        return testUser;
    }

    public static Usuario createTestUser(String email) {
        Usuario testUser = new Usuario();
        testUser.setEmail(email);
        testUser.setNombre(faker.name().firstName());
        testUser.setApellidos(faker.name().lastName());
        testUser.setPassword("password");
        testUser.setActive(true);
        testUser.addRole(Rol.USER);
        return testUser;
    }
}
