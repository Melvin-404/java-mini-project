package com.attendance.service;

import com.attendance.model.AttendanceRecord;
import com.attendance.model.MonthlySummary;
import com.attendance.model.Student;
import com.attendance.model.StudentDashboard;
import com.attendance.model.WeeklySummary;

import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AnalyticsService {
    private static final double TARGET = 0.75;

    public StudentDashboard buildDashboard(Student student, List<AttendanceRecord> records, int futureClasses) {
        StudentDashboard dashboard = new StudentDashboard();
        dashboard.setStudent(student);
        dashboard.setFuturePredictionClasses(futureClasses);

        List<AttendanceRecord> sortedDesc = new ArrayList<>(records);
        sortedDesc.sort(Comparator.comparing(AttendanceRecord::getAttendanceDate).reversed());

        int total = sortedDesc.size();
        int present = 0;
        int absent = 0;
        int late = 0;

        for (AttendanceRecord record : sortedDesc) {
            switch (record.getStatus()) {
                case "PRESENT" -> present++;
                case "LATE" -> late++;
                default -> absent++;
            }
        }

        int attended = present + late;
        double percentage = total == 0 ? 0.0 : (attended * 100.0) / total;
        int requiredClasses = calculateRequiredClasses(total, attended);
        int safeBunk = calculateSafeBunk(total, attended);
        double nextMissed = (attended * 100.0) / (total + 1);
        double futurePrediction = (attended + futureClasses) * 100.0 / (total + futureClasses);

        dashboard.setTotalClasses(total);
        dashboard.setPresentCount(present);
        dashboard.setLateCount(late);
        dashboard.setAbsentCount(absent);
        dashboard.setAttendedClasses(attended);
        dashboard.setAttendancePercentage(roundToTwoDecimals(percentage));
        dashboard.setRequiredClasses(requiredClasses);
        dashboard.setSafeBunk(safeBunk);
        dashboard.setNextClassMissedPercentage(roundToTwoDecimals(nextMissed));
        dashboard.setFuturePredictionPercentage(roundToTwoDecimals(futurePrediction));
        dashboard.setWarningMessage(percentage < 75.0 ? "Attendance below 75%" : "Attendance is on track");
        dashboard.setDangerLevel(resolveDangerLevel(percentage));
        dashboard.setAttendanceStreak(calculateStreak(sortedDesc));
        dashboard.setConsistencyScore(roundToTwoDecimals(calculateConsistencyScore(sortedDesc)));
        dashboard.setLastFiveRecords(sortedDesc.subList(0, Math.min(5, sortedDesc.size())));
        dashboard.setWeeklySummaries(buildWeeklySummaries(sortedDesc));
        dashboard.setMonthlySummaries(buildMonthlySummaries(sortedDesc));

        return dashboard;
    }

    private int calculateRequiredClasses(int total, int attended) {
        if (total == 0) {
            return 0;
        }
        if ((attended * 1.0 / total) >= TARGET) {
            return 0;
        }
        double numerator = (TARGET * total) - attended;
        return Math.max(0, (int) Math.ceil(numerator / (1 - TARGET)));
    }

    private int calculateSafeBunk(int total, int attended) {
        if (total == 0) {
            return 0;
        }
        return Math.max(0, (int) Math.floor((attended / TARGET) - total));
    }

    private String resolveDangerLevel(double percentage) {
        if (percentage > 75.0) {
            return "SAFE";
        }
        if (percentage >= 65.0) {
            return "RISK";
        }
        return "DANGER";
    }

    private int calculateStreak(List<AttendanceRecord> sortedDesc) {
        int streak = 0;
        for (AttendanceRecord record : sortedDesc) {
            if ("PRESENT".equals(record.getStatus())) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }

    private double calculateConsistencyScore(List<AttendanceRecord> sortedDesc) {
        if (sortedDesc.isEmpty()) {
            return 0.0;
        }

        List<AttendanceRecord> chronological = new ArrayList<>(sortedDesc);
        Collections.reverse(chronological);

        double weightedTotal = 0.0;
        int changes = 0;
        String previousStatus = null;

        for (AttendanceRecord record : chronological) {
            weightedTotal += switch (record.getStatus()) {
                case "PRESENT" -> 1.0;
                case "LATE" -> 0.7;
                default -> 0.0;
            };

            if (previousStatus != null && !previousStatus.equals(record.getStatus())) {
                changes++;
            }
            previousStatus = record.getStatus();
        }

        double weightedScore = (weightedTotal / chronological.size()) * 100.0;
        double stabilityPenalty = chronological.size() <= 1 ? 0.0 : (changes * 100.0 / (chronological.size() - 1)) * 0.25;
        return Math.max(0.0, Math.min(100.0, weightedScore - stabilityPenalty));
    }

    private List<WeeklySummary> buildWeeklySummaries(List<AttendanceRecord> sortedDesc) {
        Map<String, int[]> summaryMap = new LinkedHashMap<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (AttendanceRecord record : sortedDesc) {
            int week = record.getAttendanceDate().get(weekFields.weekOfWeekBasedYear());
            int year = record.getAttendanceDate().get(weekFields.weekBasedYear());
            String label = year + " W" + week;
            summaryMap.putIfAbsent(label, new int[2]);
            int[] counts = summaryMap.get(label);
            counts[1]++;
            if (!"ABSENT".equals(record.getStatus())) {
                counts[0]++;
            }
        }

        List<WeeklySummary> summaries = new ArrayList<>();
        int added = 0;
        for (Map.Entry<String, int[]> entry : summaryMap.entrySet()) {
            int attended = entry.getValue()[0];
            int total = entry.getValue()[1];
            double percentage = total == 0 ? 0.0 : (attended * 100.0) / total;
            summaries.add(new WeeklySummary(entry.getKey(), attended, total, roundToTwoDecimals(percentage)));
            added++;
            if (added == 4) {
                break;
            }
        }
        return summaries;
    }

    private List<MonthlySummary> buildMonthlySummaries(List<AttendanceRecord> sortedDesc) {
        Map<String, int[]> summaryMap = new LinkedHashMap<>();

        for (AttendanceRecord record : sortedDesc) {
            YearMonth yearMonth = YearMonth.from(record.getAttendanceDate());
            String label = yearMonth.toString();
            summaryMap.putIfAbsent(label, new int[2]);
            int[] counts = summaryMap.get(label);
            counts[1]++;
            if (!"ABSENT".equals(record.getStatus())) {
                counts[0]++;
            }
        }

        List<MonthlySummary> summaries = new ArrayList<>();
        int added = 0;
        for (Map.Entry<String, int[]> entry : summaryMap.entrySet()) {
            int attended = entry.getValue()[0];
            int total = entry.getValue()[1];
            double percentage = total == 0 ? 0.0 : (attended * 100.0) / total;
            summaries.add(new MonthlySummary(entry.getKey(), attended, total, roundToTwoDecimals(percentage)));
            added++;
            if (added == 4) {
                break;
            }
        }
        return summaries;
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
