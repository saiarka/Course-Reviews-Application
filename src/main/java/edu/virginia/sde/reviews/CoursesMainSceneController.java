package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

public class CoursesMainSceneController {

   CoursesMainSceneService service = new CoursesMainSceneService();
   @FXML
   private VBox coursesContainer;
   @FXML
   private TextField courseMnemonic;
   @FXML
   private TextField courseNumber;
   @FXML
   private TextField courseTitle;
    @FXML
    private TextField courseMnemonicSearched;
    @FXML
    private TextField courseNumberSearched;
    @FXML
    private TextField courseTitleSearched;
    @FXML
    private Label errorLabel;

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
               System.out.println("IO EXCEPTION DURING COURSE MAIN SCREEN INITIALIZATION");
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
      SceneCreator sceneCreator = new SceneCreator();
      Scene scene = sceneCreator.createScene("my-review-scene.fxml");
      SceneSwitcher.setScene(scene);
   }

   public void handleSearch(){
       String courseTitleSearchedText = courseTitleSearched.getText();
       String courseMnemonicSearchedText = courseMnemonicSearched.getText();
       String courseNumberSearchedNum = courseNumberSearched.getText();

       if(service.validateCourseName(courseTitleSearchedText) || service.validateCourseNumber(courseNumberSearchedNum) || service.validateMnemonic(courseMnemonicSearchedText)){
        DatabaseDriver driver = new DatabaseDriver();
        try {
           List<Course> courseList = driver.getSearchedCourseList(courseMnemonicSearchedText, courseTitleSearchedText, courseNumberSearchedNum);

          coursesContainer.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-review-item.fxml"));
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
           //TODO : Add meaningful message in gui for sql exception
            e.printStackTrace();
        }
       }else {
           //TODO: Add meaningful excepction here shown in gui

       }


   }

   public void handleAddCourse(){

   }




}
