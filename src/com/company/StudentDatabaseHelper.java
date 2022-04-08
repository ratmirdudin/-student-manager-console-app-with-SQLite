package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

class StudentDatabaseHelper {
    private Connection connection = null;
    private Statement statement = null;

    StudentDatabaseHelper() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:students.db");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void createTable(String tableName) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(50), " +
                    "tasks INTEGER )";
            this.statement = this.connection.createStatement();
            this.statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void dropTable(String tableName) {
        try {
            String query = "DROP TABLE IF EXISTS " + tableName;
            this.statement = this.connection.createStatement();
            this.statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void insertInTable(String tableName, String name, int tasks) {
        try {
            String query = "INSERT INTO " + tableName + " (name, tasks)" +
                    " VALUES ('" + name + "', " + tasks + ")";
            this.statement = this.connection.createStatement();
            this.statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Map<String, Student> selectStartInfo(String tableName) {
        Map<String, Student> studentMap = new LinkedHashMap<>();
        try {
            String query = "SELECT id, name, tasks FROM " + tableName;
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int tasks = resultSet.getInt("tasks");
                Student student = new Student(name);
                student.setSolvedTasks(tasks);
                studentMap.put(name, student);
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return studentMap;
    }

    public void clearInfoFromDatabase(String tableName) {
        try {
            String query = "DELETE FROM " + tableName;
            this.statement = this.connection.createStatement();
            this.statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void saveInfoInDatabase(String tableName, Map<String, Student> studentMap) {
        for (String name : studentMap.keySet()) {
            int tasks = studentMap.get(name).getSolvedTasks();
            insertInTable(tableName, name, tasks);
        }
    }

    public void close() {
        try {
            this.statement.close();
            this.connection.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
