package za.co.mealprep.pojo;


public enum Metric {
    TABLE_SPOON("Tablespoon"),
    TEA_SPOON("Tea spoon"),
    CUP("Cup"),
    BLANK("  "),
    GRAM("g"),
    KILOGRAM("Kg"),
    MILLILITER("ml"),
    LITER("l"),
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large"),
    PINCH("Pinch");
    private String label;
    Metric(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Metric fromLabel(String label) {
        for (Metric b : Metric.values()) {
            if (b.label.equalsIgnoreCase(label)) {
                return b;
            }
        }
        return null;
    }
}
