package com.example.stepcounter.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class HistoryEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long day;
    private int stepsTaken;
    private String goalName;
    private int goalSteps;

    public HistoryEntity(int id, long day, int stepsTaken, String goalName, int goalSteps) {
        this.id = id;
        this.day = day;
        this.stepsTaken = stepsTaken;
        this.goalName = goalName;
        this.goalSteps = goalSteps;
    }

    @Ignore
    public HistoryEntity(long day, int stepsTaken, Goal goal) {
        this.day = day;
        this.stepsTaken = stepsTaken;
        this.goalName = goal.getName();
        this.goalSteps = goal.getSteps();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
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
