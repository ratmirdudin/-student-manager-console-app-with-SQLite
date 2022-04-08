package com.company;

public class Main {

    public static void main(String[] args) {
        StudentManager manager = new StudentManager();
        do {
            manager.printMenu();
            switch (manager.selectAction()) {
                case "1":// Add student
                    manager.addStudent();
                    break;
                case "2":// Print students
                    manager.printStudents();
                    break;
                case "3":// Print students have enough solved tasks
                    manager.printStudentsHaveEnoughSolvedTasks();
                    break;
                case "4":// Select student to action
                    manager.performActionOnStudent();
                    break;
                case "5":// Clear information from Database
                    manager.clearInfo();
                    break;
                case "0":// Save and exit
                    manager.finishStudentManager();
                    break;
                default:// Incorrect input
                    manager.setActionMessage("Message:\n" +
                            "Please, enter 0..5 to select necessary action\n");
                    break;
            }
        } while (!manager.isFinished());
    }
}
