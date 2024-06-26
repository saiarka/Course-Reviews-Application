package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import org.hibernate.annotations.processing.SQL;

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
    @FXML
    private Label addErrorLabel;
    @FXML
    private Label addSuccessLabel;

    DatabaseDriver driver = new DatabaseDriver();

   public void initalize() {
      DatabaseDriver driver = new DatabaseDriver();
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-review-item.fxml"));
      try {
        List<Course> courseList = driver.retrieveAllCoursesFromDatabase();
        coursesContainer.getChildren().clear();
        for(Course course: courseList) {
           try {
               VBox vbox = (VBox) fxmlLoader.load();

               CoursesItemController controller = fxmlLoader.getController();
               double avgRating = course.getAvgRating() != 0.0 ? course.getAvgRating() : -1.0; // Use -1.0 as a sentinel value
               controller.setCourseItemData(course.getCoursemnemonic(), course.getCoursename(), course.getCoursenumber(), avgRating);
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

   // TODO: Might need to change this to send reference over to my-review-screen controller for it to handle
   public void handleMyReviewsButton(){
      SceneCreator sceneCreator = new SceneCreator();
      Scene scene = sceneCreator.createScene("my-review-scene.fxml");
      SceneSwitcher.setScene(scene);
   }

   public void handleSearch(){
       String courseTitleSearchedText = courseTitleSearched.getText();
       String courseMnemonicSearchedText = courseMnemonicSearched.getText();
       String courseNumberSearchedNum = courseNumberSearched.getText();
        errorLabel.setText("");
       if(service.validateCourseName(courseTitleSearchedText) || service.validateCourseNumber(courseNumberSearchedNum) || service.validateMnemonic(courseMnemonicSearchedText)){
        try {
            driver.connect();
           List<Course> courseList = driver.getSearchedCourseList(courseMnemonicSearchedText, courseTitleSearchedText, courseNumberSearchedNum);
            driver.disconnect();
          coursesContainer.getChildren().clear();
          for(Course course: courseList) {
              FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-review-item.fxml"));
              VBox vbox = fxmlLoader.load();
              CoursesItemController controller = fxmlLoader.getController();
              controller.setCourseItemData(course.getCoursemnemonic(), course.getCoursename(), course.getCoursenumber(), course.getAvgRating());
              coursesContainer.getChildren().add(vbox);
          }

        }catch (SQLException | IOException e) {
            errorLabel.setText("Failed to load courses");
        }
       }else {
            errorLabel.setText("Invalid course parameters entered");
       }
   }

   public void handleAddCourse(){
       String courseMnemonicAdd = courseMnemonic.getText();
       String courseTitleAdd = courseTitle.getText();
       String courseNumberAdd = courseNumber.getText();
       addErrorLabel.setText("");
       addSuccessLabel.setText("");

       if(service.addMnemonicValidate(courseMnemonicAdd) && service.addTitleValidate(courseTitleAdd) && service.addCourseNumberValidate(courseNumberAdd)) {
           try {
               driver.connect();
               driver.addCourse(courseMnemonicAdd, courseTitleAdd, courseNumberAdd);
               driver.commit();
               addSuccessLabel.setText("Course Successfully Added!");
               driver.disconnect();
           }catch (SQLException e) {
               addErrorLabel.setText("Failed to add course");
               e.printStackTrace();
           }
       }else {
           addErrorLabel.setText("Invalid course info entered");
       }
   }


}