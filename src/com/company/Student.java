package com.company;

class Student {
    private final String name;
    private int solvedTasks;
    private final int enoughSolvedTasks;

    public Student(String name) {
        this(name, 5);
    }

    public Student(String name, int enoughSolvedTasks) {
        this.name = name;
        this.solvedTasks = 0;
        this.enoughSolvedTasks = enoughSolvedTasks;
    }

    public int getSolvedTasks() {
        return this.solvedTasks;
    }

    public void setSolvedTasks(int tasks) {
        this.solvedTasks = tasks;
    }

    public void addSolvedTask() {
        this.solvedTasks += 1;
    }

    public int getEnoughSolvedTasks() {
        return this.enoughSolvedTasks;
    }

    public String getName() {
        return name;
    }
}
