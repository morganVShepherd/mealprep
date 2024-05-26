package za.co.mealprep.pojo;

public enum DayOfWeekRef {
    DAY1(0),
    DAY2(1),
    DAY3(2),
    DAY4(3),
    DAY5(4),
    DAY6(5),
    DAY7(6);
    int value;
    DayOfWeekRef(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}
