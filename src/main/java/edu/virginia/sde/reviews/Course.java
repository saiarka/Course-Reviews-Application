package edu.virginia.sde.reviews;

import java.util.List;

public class Course {
    private int coursenumber;
    private String coursename;
    private String coursemnemonic;
    private double avgRating;
    private List<Rating> ratingList;

    public Course(int coursenumber, String coursename, String coursemnemonic, double avgRating, List<Rating> ratingList) {
        this.coursenumber = coursenumber;
        this.coursename = coursename;
        this.coursemnemonic = coursemnemonic;
        this.avgRating = avgRating;
        this.ratingList = ratingList;
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



}
