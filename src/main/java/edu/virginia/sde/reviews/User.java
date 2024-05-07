package edu.virginia.sde.reviews;

import java.util.List;

public class User {
   private String username;
   private String password;
   private List<Course> coursesReviewed;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Course> getCoursesReviewed() {
        return coursesReviewed;
    }

    public void setCoursesReviewed(List<Course> coursesReviewed) {
        this.coursesReviewed = coursesReviewed;
    }

    public void addCoursetoReviewedCourses(Course course) {
        this.coursesReviewed.add(course);
    }
}
