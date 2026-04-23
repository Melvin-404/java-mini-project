package com.attendance.model;

import java.util.List;

public class StudentDashboard {
    private Student student;
    private int totalClasses;
    private int attendedClasses;
    private int presentCount;
    private int absentCount;
    private int lateCount;
    private double attendancePercentage;
    private int requiredClasses;
    private int safeBunk;
    private double nextClassMissedPercentage;
    private double futurePredictionPercentage;
    private int futurePredictionClasses;
    private String warningMessage;
    private String dangerLevel;
    private int attendanceStreak;
    private double consistencyScore;
    private List<AttendanceRecord> lastFiveRecords;
    private List<WeeklySummary> weeklySummaries;
    private List<MonthlySummary> monthlySummaries;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    public int getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(int attendedClasses) {
        this.attendedClasses = attendedClasses;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(int presentCount) {
        this.presentCount = presentCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public void setLateCount(int lateCount) {
        this.lateCount = lateCount;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public int getRequiredClasses() {
        return requiredClasses;
    }

    public void setRequiredClasses(int requiredClasses) {
        this.requiredClasses = requiredClasses;
    }

    public int getSafeBunk() {
        return safeBunk;
    }

    public void setSafeBunk(int safeBunk) {
        this.safeBunk = safeBunk;
    }

    public double getNextClassMissedPercentage() {
        return nextClassMissedPercentage;
    }

    public void setNextClassMissedPercentage(double nextClassMissedPercentage) {
        this.nextClassMissedPercentage = nextClassMissedPercentage;
    }

    public double getFuturePredictionPercentage() {
        return futurePredictionPercentage;
    }

    public void setFuturePredictionPercentage(double futurePredictionPercentage) {
        this.futurePredictionPercentage = futurePredictionPercentage;
    }

    public int getFuturePredictionClasses() {
        return futurePredictionClasses;
    }

    public void setFuturePredictionClasses(int futurePredictionClasses) {
        this.futurePredictionClasses = futurePredictionClasses;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public int getAttendanceStreak() {
        return attendanceStreak;
    }

    public void setAttendanceStreak(int attendanceStreak) {
        this.attendanceStreak = attendanceStreak;
    }

    public double getConsistencyScore() {
        return consistencyScore;
    }

    public void setConsistencyScore(double consistencyScore) {
        this.consistencyScore = consistencyScore;
    }

    public List<AttendanceRecord> getLastFiveRecords() {
        return lastFiveRecords;
    }

    public void setLastFiveRecords(List<AttendanceRecord> lastFiveRecords) {
        this.lastFiveRecords = lastFiveRecords;
    }

    public List<WeeklySummary> getWeeklySummaries() {
        return weeklySummaries;
    }

    public void setWeeklySummaries(List<WeeklySummary> weeklySummaries) {
        this.weeklySummaries = weeklySummaries;
    }

    public List<MonthlySummary> getMonthlySummaries() {
        return monthlySummaries;
    }

    public void setMonthlySummaries(List<MonthlySummary> monthlySummaries) {
        this.monthlySummaries = monthlySummaries;
    }
}
