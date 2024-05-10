package edu.virginia.sde.reviews;

import java.sql.SQLException;
import java.util.List;

public class Course {
    private int coursenumber;
    private String coursename;
    private String coursemnemonic;
    private double avgRating;
    private List<Rating> ratingList;

    DatabaseDriver driver= new DatabaseDriver();

    public Course(int coursenumber, String coursename, String coursemnemonic, double avgRating, List<Rating> ratingList) throws SQLException {
        this.coursenumber = coursenumber;
        this.coursename = coursename;
        this.coursemnemonic = coursemnemonic;
        String avgRatingString= calculateAverageRating();
        try {
            double avgRatingDouble = Double.parseDouble(avgRatingString);
            this.avgRating = avgRatingDouble;
        } catch (NumberFormatException e) {
            // Handle parsing exception (e.g., set avgRatingDouble to a default value)
            this.avgRating=avgRating;
        }

        this.ratingList = ratingList;
    }
    public Course(int coursenumber, String coursename, String coursemnemonic, double avgRating) {
        this.coursenumber = coursenumber;
        this.coursename = coursename;
        this.coursemnemonic = coursemnemonic;
        this.avgRating = avgRating;
    }

    public int getCoursenumber() {
        return coursenumber;
    }

    public void setCoursenumber(int coursenumber) {
        this.coursenumber = coursenumber;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getCoursemnemonic() {
        return coursemnemonic;
    }

    public void setCoursemnemonic(String coursemnemonic) {
        this.coursemnemonic = coursemnemonic;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }




    public String calculateAverageRating() throws SQLException {
        int courseidnum=getCourseID();
        driver.connect();
        List<Double> ratingList= driver.getAllNumberRatingsForCourse(courseidnum);

        driver.disconnect();
        if (ratingList == null || ratingList.isEmpty()) {
            return "";
        }

        double sum = 0.0;
        for (Double num : ratingList) {
            sum += num;
        }

        double average = sum / ratingList.size();
        return String.format("%.2f", average); // Format the average rating to two decimal places
    }
    public int getCourseID() throws SQLException {
        int coursenum=coursenumber;
        driver.connect();
        int output= driver.getCourseID(coursemnemonic, coursename, coursenum);
        driver.disconnect();
        return output;
    }

}
