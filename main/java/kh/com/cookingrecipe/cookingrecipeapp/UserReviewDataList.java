package kh.com.cookingrecipe.cookingrecipeapp;

public class UserReviewDataList{

    private int userID;
    private int imgId;
    private String name;
    private int rating;
    private String date;
    private String description;

    public UserReviewDataList(int imgId, String name, String date, String description, int rating) {
        this.imgId = imgId;
        this.name = name;
        this.date = date;
        this.description = description;
        this.rating = rating;
    }
    public UserReviewDataList(String name, String date, String description, int rating) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getImgId(){
        return imgId;
    }
    public void setImgId(int imgId){
        this.imgId = imgId;
    }

    public  String getDate(){
        return date;
    }
    public void setDate(){
        this.date = date;
    }

    public  String getDescription(){
        return description;
    }
    public void setDescription(){
        this.description = description;
    }

    public int getRating(){
        return rating;
    }
    public void setRating(int rating){
        this.rating = rating;
    }

    public int getUserID(){
        return userID;
    }
    public void setUserID(int userID){
        this.userID = userID;
    }


}
