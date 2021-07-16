package kh.com.cookingrecipe.cookingrecipeapp;

public class RecipeDataList{
    private String recipe_name;
    private String imgId;
    private int time;
    private int serving;
    private int rating;
    private String chef;
    private String recipe_id;
    private String upload_time;
    private int number_click;

    public RecipeDataList(String recipe_name, String imgId, int time, int serving, int rating, String chef) {
        this.recipe_name = recipe_name;
        this.imgId = imgId;
        this.time = time;
        this.serving = serving;
        this.rating = rating;
        this.chef = chef;
    }
    public RecipeDataList(String recipe_id, String recipe_name, String imgId, int time, int serving, int rating, String chef) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.imgId = imgId;
        this.time = time;
        this.serving = serving;
        this.rating = rating;
        this.chef = chef;
    }

    public RecipeDataList(String recipe_id, String recipe_name, String imgId, int time, int serving, int rating, String chef, String upload_time, int number_click) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.imgId = imgId;
        this.time = time;
        this.serving = serving;
        this.rating = rating;
        this.chef = chef;
        this.upload_time = upload_time;
        this.number_click = number_click;
    }
    public RecipeDataList(String recipe_name, String imgId, int time, int serving, int rating, String chef, String upload_time, int number_click) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.imgId = imgId;
        this.time = time;
        this.serving = serving;
        this.rating = rating;
        this.chef = chef;
        this.upload_time = upload_time;
        this.number_click = number_click;
    }
    public RecipeDataList(){

    }

    public RecipeDataList(String recipe_name, String imgId, int time, int serving, int rating){
        this.recipe_name = recipe_name;
        this.imgId = imgId;
        this.time = time;
        this.serving = serving;
        this.rating = rating;
    }

    public String getRecipeName() {
        return recipe_name;
    }
    public void setRecipeName(String recipe_name) {
        this.recipe_name = recipe_name;
    }
    public String getImgId() {
        return imgId;
    }
    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public int getServing() {
        return serving;
    }
    public void setServing(int serving) {
        this.serving = serving;
    }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getChef(){ return chef; }
    public void setChef(String chef){ this.chef = chef; }
    public String getRecipeId() {
        return recipe_id;
    }
    public void setRecipeId(String recipe_id) {
        this.recipe_id = recipe_id;
    }
    public int getNumber_click() {
        return number_click;
    }

    public void setNumber_click(int number_click) {
        this.number_click = number_click;
    }
    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }
}
