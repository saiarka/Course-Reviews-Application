package edu.virginia.sde.reviews;

import java.sql.SQLException;
import java.sql.Timestamp;

public class ReviewSceneService {


    public void editReview(int reviewID,String commentText, int ratingNumber){
Rating rating = new Rating(commentText,ratingNumber);
        DatabaseDriver databaseDriver = new DatabaseDriver();

        try {

            databaseDriver.connect();

            databaseDriver.updateReview(reviewID, rating);
            databaseDriver.disconnect();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    {

    }
}
