package kh.com.cookingrecipe.cookingrecipeapp;

public class RecipeIngredientDataList {

    private int number;
    private String ingredient;
    private String quantity;
    private String recipe_id;

    public RecipeIngredientDataList(int number, String ingredient, String quantity) {
        this.number = number;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }
    public RecipeIngredientDataList(String recipe_id, int number, String ingredient, String quantity) {
        this.number = number;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.recipe_id = recipe_id;
    }

    public  int getNumber(){
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public  String getRecipeID(){
        return recipe_id;
    }
    public void setRecipeID(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public  String getIngredient(){
        return ingredient;
    }
    public void setIngredient(String ingredient){
        this.ingredient = ingredient;
    }

    public  String getQuantity(){
        return quantity;
    }
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }

}
