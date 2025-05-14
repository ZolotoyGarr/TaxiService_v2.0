package javiki.course.driver;

import javiki.course.Achivement;

import java.util.List;

public class DriverDetails {
    final String workingSinceDate;
    final List<Achivement> achievements;
    final int numberOfRides;

    public DriverDetails(String workingSinceDate, List<Achivement> achievements, int numberOfRides) {
        this.workingSinceDate = workingSinceDate;
        this.achievements = achievements;
        this.numberOfRides = numberOfRides;
    }

    public String getWorkingSinceDate() {
        return workingSinceDate;
    }

    public List<Achivement> getAchievements() {
        return achievements;
    }

    public int getNumberOfRides() {
        return numberOfRides;
    }
}
