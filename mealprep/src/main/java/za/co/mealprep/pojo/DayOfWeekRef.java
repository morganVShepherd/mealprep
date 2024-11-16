package za.co.mealprep.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DayOfWeekRef {
    DAY1(0,"Monday"),
    DAY2(1,"Tuesday"),
    DAY3(2,"Wednesday"),
    DAY4(3,"Thursday"),
    DAY5(4,"Friday"),
    DAY6(5,"Saturday"),
    DAY7(6,"Sunday"),;
    int value;
    String dayOfWeekName;
}