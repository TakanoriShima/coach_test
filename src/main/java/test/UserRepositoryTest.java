package test;

import model.User;
import repository.UserRepository;

import java.util.List;

public class UserRepositoryTest {
    public static void main(String[] args) {
        UserRepository repo = new UserRepository();

        System.out.println("=== findById(1) ===");
        User u1 = repo.findById(1);
        System.out.println(u1);

        System.out.println("\n=== findAll() ===");
        List<User> users = repo.findAll();
        users.forEach(System.out::println);

        System.out.println("\n=== findByUsername('taro') ===");
        User taro = repo.findByUsername("taro");
        System.out.println(taro);

        System.out.println("\n=== findById(999) (not found) ===");
        System.out.println(repo.findById(999));
        
        System.out.println("\n=== findByUsername('xxx') (not found) ===");
        System.out.println(repo.findByUsername("xxx"));

    }
}
