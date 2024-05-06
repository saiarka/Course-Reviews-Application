package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Rating {

    private String commentText;
    private int ratingNumber;
    private Timestamp timestamp;

    public Rating(String commentText, int ratingNumber) {
        this.commentText = commentText;
        this.ratingNumber = ratingNumber;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getRatingNumber() {
        return ratingNumber;
    }

    public void setRatingNumber(int ratingNumber) {
        this.ratingNumber = ratingNumber;
    }
}
