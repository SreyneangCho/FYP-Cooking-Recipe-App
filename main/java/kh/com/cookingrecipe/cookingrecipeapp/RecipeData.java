package kh.com.cookingrecipe.cookingrecipeapp;

public class RecipeData {

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    private String recipe_name;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    private String imgId;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    private int time;

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    private int serving;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private int rating;

    public String getChef() {
        return chef;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    private String chef;

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    private String upload_time;

    public int getNumber_click() {
        return number_click;
    }

    public void setNumber_click(int number_click) {
        this.number_click = number_click;
    }

    private int number_click;

    RecipeData(){

    }

    public RecipeData(String recipe_name, String imgId, int time, int serving, int rating, String chef, String upload_time, int number_click) {
        this.recipe_name = recipe_name;
        this.imgId = imgId;
        this.time = time;
        this.serving = serving;
        this.rating = rating;
        this.chef = chef;
        this.upload_time = upload_time;
        this.number_click = number_click;
    }
}
