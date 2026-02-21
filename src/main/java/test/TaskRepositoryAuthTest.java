package test;

import repository.TaskRepository;

public class TaskRepositoryAuthTest {
	public static void main(String[] args) {

		TaskRepository repo = new TaskRepository();

		// 初期データ前提：
		// tasks:
		// task_id 1,2 -> user_id=1
		// task_id 3,4 -> user_id=2
		// task_id 5 -> user_id=3

		int loginUserId = 1; // admin 想定
		int myTaskId = 1; // user_id=1 のタスク
		int otherTaskId = 3; // user_id=2 のタスク

		System.out.println("=== isOwner test ===");
		System.out.println("myTask (task_id=1, user_id=1) => " + repo.isOwner(myTaskId, loginUserId)); // true期待
		System.out.println("otherTask (task_id=3, user_id=1) => " + repo.isOwner(otherTaskId, loginUserId)); // false期待

		System.out.println("\n=== countByUserId test ===");
		System.out.println("user_id=1 count => " + repo.countByUserId(loginUserId)); // 2期待（初期データ通りなら）

		System.out.println("\n=== deleteByIdAndUserId test ===");
		System.out.println("delete otherTask (task_id=3, user_id=1) rows => "
				+ repo.deleteByIdAndUserId(otherTaskId, loginUserId)); // 0期待
		System.out.println(
				"delete myTask (task_id=1, user_id=1) rows => " + repo.deleteByIdAndUserId(myTaskId, loginUserId)); // 1期待

		System.out.println("\n=== verify after delete ===");
		System.out.println("user_id=1 count => " + repo.countByUserId(loginUserId)); // 1期待（1件消えたら）
	}
}
