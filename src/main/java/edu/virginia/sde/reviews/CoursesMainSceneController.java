package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import org.hibernate.dialect.Database;

import java.util.List;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class CoursesMainSceneController {

   @FXML
   private VBox coursesContainer;
   @FXML
   private TextField courseMnemonicTextField;
   @FXML
   private TextField courseNumberTextField;
   @FXML
   private TextField courseTitleTextField;


   public void initalize() {
      DatabaseDriver driver = new DatabaseDriver();
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-review-item.fxml"));
      try {
        List<Course> courseList = driver.retrieveAllCoursesFromDatabase();
        coursesContainer.getChildren().clear(); //Potential error here
        for(Course course: courseList) {
           try {
               VBox vbox = (VBox) fxmlLoader.load();

               CoursesItemController controller = fxmlLoader.getController();
               controller.setCourseItemData(course.getCoursemnemonic(), course.getCoursename(), course.getCoursenumber(), course.getAvgRating());
               coursesContainer.getChildren().add(vbox);
           }catch (IOException e){
              //Not sure if this is the right way to deal with this exception
               e.printStackTrace();
           }
        }
      }catch (SQLException e) {
         //TODO: Maybe list some error here for when database has no courses
      }
   }

   public void handleLogOutButton() {
      SceneCreator sceneCreator = new SceneCreator();
      Scene scene = sceneCreator.createScene("login-signup.fxml");
      SceneSwitcher.setScene(scene);
   }

   public void handleMyReviewsButton(){
      //TODO: Change scene to my reviews fxml file
      SceneCreator sceneCreator = new SceneCreator();
      Scene scene = sceneCreator.createScene("hello-world.fxml");
      SceneSwitcher.setScene(scene);
   }

   public void handleSearch(){}




}
