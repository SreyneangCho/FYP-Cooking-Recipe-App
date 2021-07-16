package kh.com.cookingrecipe.cookingrecipeapp;

public class RecentSearchDataList {
    private String query;
    RecentSearchDataList(String query){
        this.query = query;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
}
