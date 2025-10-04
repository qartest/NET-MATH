//package ru.nsu.rush_c;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import ru.nsu.rush_c.dao.repository.RoleRepository;
//import ru.nsu.rush_c.dao.repository.UserRepository;
//import ru.nsu.rush_c.models.Role;
//import ru.nsu.rush_c.models.User;
//
//import java.time.ZonedDateTime;
//
//@Service
//public class DatabaseSeeder {
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public void seedDatabase() {
//        // Создаем роль
//        Role adminRole = new Role();
//        adminRole.setRole("Admin234");
//        roleRepository.save(adminRole);
//
//        // Создаем пользователя
//        User user = new User();
//        user.setNickname("JohnDoe");
//        user.setPassword("password123");
//        user.setData_create(ZonedDateTime.now());
//        user.setEmail("mail.com");
//        user.setRole(adminRole);
//
//        userRepository.save(user);
//
//        System.out.println("Данные успешно добавлены в базу данных!");
//    }
//}
