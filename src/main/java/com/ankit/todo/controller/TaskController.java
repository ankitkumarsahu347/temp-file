package com.ankit.todo.controller;

import com.ankit.todo.model.Task;
import com.ankit.todo.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        task.setCompleted(false);
        return repository.save(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task request) {
        return repository.findById(id)
                .map(task -> {
                    if (request.getTitle() != null && !request.getTitle().isBlank()) {
                        task.setTitle(request.getTitle().trim());
                    }
                    task.setCompleted(request.isCompleted());
                    return ResponseEntity.ok(repository.save(task));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/completed")
    public ResponseEntity<Void> clearCompleted() {
        repository.deleteAll(repository.findAll().stream()
                .filter(Task::isCompleted)
                .toList());
        return ResponseEntity.noContent().build();
    }
}
