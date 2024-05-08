package edu.virginia.sde.reviews;

//Handles business logic for searching on Course search page
public class CoursesMainSceneService{

    public CoursesMainSceneService(){}

    public boolean validateMnemonic(String mnemonic)
    {
       if(mnemonic.compareTo("") != 0)  {
        if(mnemonic.length() > 4) {
            return false;
        }
       }
        return true;}

   public boolean validateCourseNumber(String number) {
       if(number.compareTo("") != 0)  {
           if(number.length() != 4) {
               return false;
           }
       }
       return true;
   }

   public boolean validateCourseName(String courseName) {
       if(courseName.compareTo("") != 0)  {
            if(courseName.length() > 50) {
                return false;
            }
       }
        return true;
   }

}
