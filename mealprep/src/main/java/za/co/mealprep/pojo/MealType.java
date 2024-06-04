package za.co.mealprep.pojo;

public enum MealType {
    DINNER("Dinner"),
    BREAKFAST("Breakfast");
    private String label;
    MealType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
