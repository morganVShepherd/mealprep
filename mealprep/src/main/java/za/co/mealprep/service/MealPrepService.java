package za.co.mealprep.service;

import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.exception.RestException;

public interface MealPrepService {

    MealPrepDTO generateWeeklyMealPrep() throws RestException;
    String generateShoppingList(String mealPrepId) throws RestException;
    MealPrepDTO updateWeeklyMealPrep(MealPrepDTO mealPrepDTO) throws RestException;
    String generateWhatsAppList(String mealPrepId) throws RestException;
}
