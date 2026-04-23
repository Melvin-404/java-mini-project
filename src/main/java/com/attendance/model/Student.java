package com.attendance.model;

public class Student {
    private int id;
    private String name;
    private String usn;

    public Student() {
    }

    public Student(int id, String name, String usn) {
        this.id = id;
        this.name = name;
        this.usn = usn;
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

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }
}
