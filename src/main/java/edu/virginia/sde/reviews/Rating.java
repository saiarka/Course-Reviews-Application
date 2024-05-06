package edu.virginia.sde.reviews;

public class Rating {

    public String commentText;
    public int ratingNumber;

    public Rating(String commentText, int ratingNumber) {
        this.commentText = commentText;
        this.ratingNumber = ratingNumber;
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
