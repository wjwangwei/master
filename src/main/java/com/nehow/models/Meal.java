package com.nehow.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Meal {
    private String mealCode;
    private String mealDescription;
    private String breakfast;
    private String breakfastCode;
    private String breakfastDescription;

    public String getMealCode() {
        return mealCode;
    }

    public void setMealCode(String mealCode) {
        this.mealCode = mealCode;
    }

    public String getMealDescription() {
        return mealDescription;
    }

    public void setMealDescription(String mealDescription) {
        this.mealDescription = mealDescription;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getBreakfastCode() {
        return breakfastCode;
    }

    public void setBreakfastCode(String breakfastCode) {
        this.breakfastCode = breakfastCode;
    }

    public String getBreakfastDescription() {
        return getBreakfastDesc();
    }

    public void setBreakfastDescription(String breakfastDescription) {
        this.breakfastDescription = breakfastDescription;
    }

    /**
     * get breakfast description
     * @return String
     */
    @JsonIgnore
    public String getBreakfastDesc() {
        String strResult = breakfastDescription;

        if (strResult == null || strResult.isEmpty()) {
            strResult = mealDescription;
        }

        return strResult;
    }
}
