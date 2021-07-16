package kh.com.cookingrecipe.cookingrecipeapp;

public class IngredientInfo {

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    private String ingredient_name;

    public String getIngredient_quantity() {
        return ingredient_quantity;
    }

    public void setIngredient_quantity(String ingredient_quantity) {
        this.ingredient_quantity = ingredient_quantity;
    }

    private String ingredient_quantity;

    IngredientInfo(String ingredient_name, String ingredient_quantity){
        this.ingredient_name = ingredient_name;
        this.ingredient_quantity = ingredient_quantity;
    }

    IngredientInfo(){

    }

}
