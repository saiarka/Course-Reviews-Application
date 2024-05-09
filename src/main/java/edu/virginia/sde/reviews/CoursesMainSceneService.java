package edu.virginia.sde.reviews;

//Handles business logic for searching on Course search page
public class CoursesMainSceneService{

    public CoursesMainSceneService(){}

    public boolean validateMnemonic(String mnemonic)
    {
       if(mnemonic.compareTo("") != 0)  {
        if(mnemonic.length() > 4 || mnemonic.length() < 2) {
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

   public boolean addMnemonicValidate(String mnemonic) {
       if(mnemonic.compareTo("") == 0) {
           return false;
       }
       if(mnemonic.length() > 4 || mnemonic.length() < 2) {
           return false;
       }
        return true;
   }

    public boolean addTitleValidate(String title)    {
       if(title.compareTo("") == 0) {
          return false;
       }
       if(title.length() > 50 || title.isEmpty()) {
           return false;
       }
       return true;
    }

    public boolean addCourseNumberValidate(String number) {
        if(number.compareTo("") == 0) {
            return false;
        }
        if(number.length() != 4) {
            return false;
        }
        return true;
    }
}
