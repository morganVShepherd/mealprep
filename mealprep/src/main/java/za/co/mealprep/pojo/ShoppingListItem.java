package za.co.mealprep.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingListItem {
    private String foodName;
    private long quantity;
    private Metric metric;

}
