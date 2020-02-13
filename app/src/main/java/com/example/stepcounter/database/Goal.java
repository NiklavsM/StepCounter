package com.example.stepcounter.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Goal {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int steps;
    private boolean active;

    public Goal(int id, String name, int steps, boolean active) {
        this.id = id;
        this.name = name;
        this.steps = steps;
        this.active = active;
    }

    @Ignore
    public Goal(String name, int steps, boolean active) {
        this.name = name;
        this.steps = steps;
        this.active = active;
    }

    @Ignore
    public Goal(String name, int steps) {
        this.name = name;
        this.steps = steps;
        this.active = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
