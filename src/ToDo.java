import java.util.ArrayList;
import java.util.Scanner;

public class ToDo {

    static class Task {
        private int id;
        private String title;
        private boolean completed;

        public Task(int id, String title) {
            this.id = id;
            this.title = title;
            this.completed = false;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        @Override
        public String toString() {
            String status = completed ? "[Completed]" : "[Pending]";
            return id + ". " + status + " " + title;
        }
    }

    private static final ArrayList<Task> tasks = new ArrayList<>();
    private static int nextId = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("==============================");
        System.out.println("       JAVA TO-DO LIST");
        System.out.println("==============================");

        while (running) {
            showMenu();
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addTask(scanner);
                    break;
                case 2:
                    showTasks();
                    break;
                case 3:
                    markTaskCompleted(scanner);
                    break;
                case 4:
                    deleteTask(scanner);
                    break;
                case 5:
                    clearCompletedTasks();
                    break;
                case 6:
                    running = false;
                    System.out.println("Thank you for using the To-Do List!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-6.");
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n----------- MENU -----------");
        System.out.println("1. Add Task");
        System.out.println("2. Show Tasks");
        System.out.println("3. Mark Task as Completed");
        System.out.println("4. Delete Task");
        System.out.println("5. Clear Completed Tasks");
        System.out.println("6. Exit");
        System.out.println("----------------------------");
    }

    private static void addTask(Scanner scanner) {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Task title cannot be empty.");
            return;
        }

        Task task = new Task(nextId++, title);
        tasks.add(task);
        System.out.println("Task added successfully.");
    }

    private static void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("\n----------- TASKS -----------");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println("-----------------------------");
    }

    private static void markTaskCompleted(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        showTasks();
        System.out.print("Enter task ID to mark as completed: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            Task task = findTaskById(id);

            if (task == null) {
                System.out.println("Task not found.");
                return;
            }

            task.setCompleted(true);
            System.out.println("Task marked as completed.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid task ID.");
        }
    }

    private static void deleteTask(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        showTasks();
        System.out.print("Enter task ID to delete: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            Task task = findTaskById(id);

            if (task == null) {
                System.out.println("Task not found.");
                return;
            }

            tasks.remove(task);
            System.out.println("Task deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid task ID.");
        }
    }

    private static void clearCompletedTasks() {
        int before = tasks.size();
        tasks.removeIf(Task::isCompleted);
        int removed = before - tasks.size();

        if (removed == 0) {
            System.out.println("There are no completed tasks to clear.");
        } else {
            System.out.println(removed + " completed task(s) cleared.");
        }
    }

    private static Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }
}
