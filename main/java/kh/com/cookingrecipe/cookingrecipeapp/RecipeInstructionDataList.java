package kh.com.cookingrecipe.cookingrecipeapp;

public class RecipeInstructionDataList {
    private int step;
    private String name;
    private String detail;
    private String recipeID;

    public RecipeInstructionDataList(String recipeID, int step, String name, String detail) {
        this.step = step;
        this.name = name;
        this.detail = detail;
        this.recipeID = recipeID;
    }
    public RecipeInstructionDataList(int step, String name, String detail) {
        this.step = step;
        this.name = name;
        this.detail = detail;
    }

    public RecipeInstructionDataList(String name, String detail){
        this.name = name;
        this.detail = detail;
    }

    public  String getRecipeID(){
        return recipeID;
    }
    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }
    public  int getStep(){
        return step;
    }
    public void setStep(int step) {
        this.step = step;
    }
    public  String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public  String getDetail(){
        return detail;
    }
    public void setDetail(String detail){
        this.detail = detail;
    }
}
