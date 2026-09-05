package com.ankit.todo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private boolean completed = false;

    public Task() {}

    public Task(String title) {
        this.title = title;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public boolean isCompleted() { return completed; }
    public void setTitle(String title) { this.title = title; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
