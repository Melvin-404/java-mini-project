package com.attendance.model;

public class WeeklySummary {
    private String label;
    private int attended;
    private int total;
    private double percentage;

    public WeeklySummary(String label, int attended, int total, double percentage) {
        this.label = label;
        this.attended = attended;
        this.total = total;
        this.percentage = percentage;
    }

    public String getLabel() {
        return label;
    }

    public int getAttended() {
        return attended;
    }

    public int getTotal() {
        return total;
    }

    public double getPercentage() {
        return percentage;
    }
}
