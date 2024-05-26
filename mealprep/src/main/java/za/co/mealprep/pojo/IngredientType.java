package za.co.mealprep.pojo;

public enum IngredientType {
    SPICE("Spice"),
    SAUCE("Sauce"),
    FRESH_PRODUCE("Fresh"),
    OTHER("Other");
    private String label;
    IngredientType(String label) {
        this.label = label;
    }
}
