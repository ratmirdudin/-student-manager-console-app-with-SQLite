package com.company;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

class StudentManager {
    private Map<String, Student> studentMap = new LinkedHashMap<>();
    private String actionMessage = "";
    private boolean isFinished = false;

    private final StudentDatabaseHelper db = new StudentDatabaseHelper();
    private final String tableName = "students";

    public StudentManager() {
        this.db.createTable(this.tableName);
        this.studentMap = this.db.selectStartInfo(this.tableName);
    }

    public String getActionMessage() {
        return actionMessage;
    }

    public void setActionMessage(String actionMessage) {
        this.actionMessage = actionMessage;
    }

    public Map<String, Student> getStudentMap() {
        return this.studentMap;
    }

    public void printMenu() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("==========================================");
        if (!getActionMessage().isEmpty()) {
            System.out.print(getActionMessage());
        }
        System.out.println("Menu:");
        System.out.println(" 1) ADD STUDENT");
        System.out.println(" 2) SHOW STUDENTS");
        System.out.println(" 3) SHOW STUDENTS HAVE ENOUGH SOLVED TASKS");
        System.out.println(" 4) Select student to action");
        System.out.println(" 5) Clear info");
        System.out.println(" 0) Save and EXIT");
    }

    public void printStudentMenu(String name) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n==========================================");
        if (!getActionMessage().isEmpty()) {
            System.out.print(getActionMessage());
        }
        System.out.println("Menu:");
        System.out.println("Selected student is " + name);
        System.out.println("   1) ADD SOLVED TASK TO STUDENT");
        System.out.println("   2) SHOW INFO ABOUT STUDENT");
        System.out.println(" 0) GO BACK");
    }

    public String selectAction() {
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }


    public boolean hasDigit(String name) {
        boolean hasDigit = false;
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                hasDigit = true;
                break;
            }
        }
        return hasDigit;
    }

    public String readName() {
        Scanner in = new Scanner(System.in);
        String message = "";
        String name;
        boolean correctInput = false;
        do {
            if (!message.isEmpty()) {
                System.out.println(message);
            }
            System.out.print("Enter student's name: ");
            name = in.nextLine();
            if (!hasDigit(name) && !name.isEmpty()) {
                correctInput = true;
            } else {
                message = "\n\n\n\n\n\n\n\n\n\n\nMessage:\n" +
                        "Please, enter student's name not empty and without digits\n";
            }
        } while (!correctInput);
        return name;
    }

    public void addStudent() {
        String name = readName();
        Student student = new Student(name);
        this.studentMap.put(name, student);
        setActionMessage("Message:\n" +
                "Student " + name + " added in list\n");
    }

    public void printStudents() {
        if (this.studentMap.isEmpty()) {
            setActionMessage("There are no students\n");
        } else {
            String message = "Message:\n";
            int index = 0;
            for (String name : this.studentMap.keySet()) {
                int tasks = this.studentMap.get(name).getSolvedTasks();
                message += " " + (index + 1) + ") " + name + ": " + tasks + " solved tasks\n";
                index += 1;
            }
            setActionMessage(message);
        }
    }

    public void printStudentsHaveEnoughSolvedTasks() {
        if (this.studentMap.isEmpty()) {
            setActionMessage("There are no students\n");
        } else {
            String message = "Message:\n";
            boolean noOneHasEnoughTasksFlag = true;
            int index = 0;
            for (String name : this.studentMap.keySet()) {
                int enoughSolvedTasks = this.studentMap.get(name).getEnoughSolvedTasks();
                int tasks = this.studentMap.get(name).getSolvedTasks();
                if (tasks >= enoughSolvedTasks) {
                    noOneHasEnoughTasksFlag = false;
                    message += " " + (index + 1) + ") " + name + ": " + tasks + " solved tasks\n";
                }
                index += 1;
            }
            if (noOneHasEnoughTasksFlag) {
                message = "Message:\n" +
                        "No one has enough solved tasks\n";
            }
            setActionMessage(message);
        }
    }

    public String selectStudentToAction() {
        Scanner in = new Scanner(System.in);
        String message = "";
        ArrayList<String> studentNameList = new ArrayList<>(this.studentMap.keySet());
        int indexOfStudent = 0;
        String namesStr = "";
        do {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n==========================================");
            if (!message.isEmpty()) {
                System.out.println(message);
            }
            System.out.println("Select student to action: ");
            for (String nameOfStudent : this.studentMap.keySet()) {
                int tasks = this.studentMap.get(nameOfStudent).getSolvedTasks();
                namesStr += " " + (indexOfStudent + 1) + ") " + nameOfStudent + ": " + tasks + " solved tasks\n";
                indexOfStudent += 1;
            }
            System.out.println(namesStr);
            String selectStudent = in.nextLine();
            try {
                indexOfStudent = Integer.parseInt(selectStudent) - 1;
                if (indexOfStudent >= 0 && indexOfStudent < this.studentMap.size()) {
                    break;
                }
            } catch (NumberFormatException e) {
                message = "Message:\n" +
                        "Please, enter 1.." + this.studentMap.size() + " to select necessary student\n";
            }
        } while (true);
        return studentNameList.get(indexOfStudent);
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void finishStudentManager() {
        try {
            this.db.saveInfoInDatabase(this.tableName, this.studentMap);
            this.db.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Have a nice day!");
        setFinished(true);
    }

    public void addSolvedTasksToStudent(String name) {
        this.studentMap.get(name).addSolvedTask();
        setActionMessage("Message:\n" +
                "Added solved task to " + name + "(" + this.studentMap.get(name).getSolvedTasks() + ")" + "\n");
    }

    public void showInfoAboutStudent(String name) {
        setActionMessage("Message:\n"
                + name + " has " + this.studentMap.get(name).getSolvedTasks() + " solved tasks\n");
    }

    public void performActionOnStudent() {
        if (this.studentMap.isEmpty()) {
            setActionMessage("Message:\n" +
                    "There are no students\n");
        } else {
            setActionMessage("");
            String name = selectStudentToAction();
            boolean goBackFlag = false;

            do {
                printStudentMenu(name);
                setActionMessage("");
                switch (selectAction()) {
                    case "1":// Add solved task to student
                        addSolvedTasksToStudent(name);
                        break;
                    case "2":// Show info about student
                        showInfoAboutStudent(name);
                        break;
                    case "0":// Go back
                        goBackFlag = true;
                        setActionMessage("");
                        break;
                    default:// Incorrect input
                        setActionMessage("Message:\n" +
                                "Please, enter 0..2 to select necessary action\n");
                        break;
                }
            } while (!goBackFlag);
        }
    }

    public void clearInfo() {
        this.db.clearInfoFromDatabase(this.tableName);
        this.studentMap.clear();
        setActionMessage("Information about students cleared from Database\n");
    }
}
