package za.co.mealprep.utils;

import za.co.mealprep.pojo.Constants;
import za.co.mealprep.pojo.ShoppingListItem;

import java.util.HashMap;

public class PrettyPrint {
    //todo comment
    public static String generateStringShoppingList(HashMap<String, ShoppingListItem> shoppingListItemHashMap) {
        StringBuilder printedList = new StringBuilder(Constants.SHOPPING_LIST_TITLE);
        shoppingListItemHashMap.forEach((key, value) -> {
            printedList.append(String.format(Constants.LIST_TEMPLATE,value.getQuantity(), value.getMetric().toString(), value.getFoodName()));
        });

        return printedList.toString();
    }
}