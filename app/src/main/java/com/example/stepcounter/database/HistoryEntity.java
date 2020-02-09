package com.example.stepcounter.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class HistoryEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private float day;
    private int stepsTaken;
    private String goalName;
    private int goalSteps;

    public HistoryEntity(int id, float day, int stepsTaken, String goalName, int goalSteps) {
        this.id = id;
        this.day = day;
        this.stepsTaken = stepsTaken;
        this.goalName = goalName;
        this.goalSteps = goalSteps;
    }

    @Ignore
    public HistoryEntity(float day, int stepsTaken, String goalName, int goalSteps) {
        this.day = day;
        this.stepsTaken = stepsTaken;
        this.goalName = goalName;
        this.goalSteps = goalSteps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getDay() {
        return day;
    }

    public void setDay(float day) {
        this.day = day;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public int getGoalSteps() {
        return goalSteps;
    }

    public void setGoalSteps(int goalSteps) {
        this.goalSteps = goalSteps;
    }
}
