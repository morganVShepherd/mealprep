package za.co.mealprep.service;

import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.dto.WeekDataItem;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.pojo.ShoppingListItem;

import java.util.List;

public interface MealPrepService {

    MealPrepDTO generateWeeklyMealPrep() throws RestException;
    List<ShoppingListItem> generateShoppingList() throws RestException;
    String generateWhatsAppList() throws RestException;
    List<WeekDataItem> generateWeekView() throws RestException;
}
