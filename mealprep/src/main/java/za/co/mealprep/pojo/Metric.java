package za.co.mealprep.pojo;


public enum Metric {
    TABLE_SPOON("Tablespoon"),
    TEA_SPOON("Tea spoon"),
    CUP("Cup"),
    BLANK("  "),
    GRAM("g"),
    KILOGRAM("Kg"),
    MILILITER("ml"),
    LITER("l"),
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large"),
    PINCH("Pinch");
    private String label;
    Metric(String label) {
        this.label = label;
    }

    private String getLabel(){
        return this.label;
    }
}
