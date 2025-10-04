//package ru.nsu.rush_c;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class SeedController {
//
//    @Autowired
//    private DatabaseSeeder databaseSeeder;
//
//    @GetMapping("/seed")
//    public String seedDatabase() {
//        databaseSeeder.seedDatabase();
//        return "Данные добавлены!";
//    }
//}