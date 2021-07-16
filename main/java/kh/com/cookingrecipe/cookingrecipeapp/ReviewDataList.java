package kh.com.cookingrecipe.cookingrecipeapp;

public class ReviewDataList {

    public String getReview_rating() {
        return review_rating;
    }

    public void setReview_rating(String review_rating) {
        this.review_rating = review_rating;
    }

    private String review_rating;

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    private String review_date;

    public String getReview_description() {
        return review_description;
    }

    public void setReview_description(String review_description) {
        this.review_description = review_description;
    }

    private String review_description;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;

    ReviewDataList(String userID, String review_description, String review_rating, String review_date){
        this.userID = userID;
        this.review_description = review_description;
        this.review_rating = review_rating;
        this.review_date = review_date;
    }

    ReviewDataList(){}

}
