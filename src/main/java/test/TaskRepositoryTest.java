package test;

import model.Task;
import repository.TaskRepository;

import java.util.List;

public class TaskRepositoryTest {
    public static void main(String[] args) {
        TaskRepository repo = new TaskRepository();
        
        System.out.println("isOwner(7,2) => " + new TaskRepository().isOwner(7, 2));

        System.out.println("=== findById(1) ===");
        Task t1 = repo.findById(1);
        System.out.println(t1);

        System.out.println("\n=== findAll() ===");
        List<Task> tasks = repo.findAll();
        tasks.forEach(System.out::println);

        System.out.println("\n=== findByUserId(1) ===");
        List<Task> user1Tasks = repo.findByUserId(1);
        user1Tasks.forEach(System.out::println);

        System.out.println("\n=== findById(999) (not found) ===");
        System.out.println(repo.findById(999));
    }
}
