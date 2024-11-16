package za.co.mealprep.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.dto.StepDTO;
import za.co.mealprep.dto.WeekDataItem;
import za.co.mealprep.pojo.Constants;
import za.co.mealprep.pojo.DayOfWeekRef;
import za.co.mealprep.pojo.IngredientType;
import za.co.mealprep.pojo.MealRotation;
import za.co.mealprep.pojo.MealType;
import za.co.mealprep.pojo.Metric;
import za.co.mealprep.pojo.ShoppingListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestConstants {

    public static Long VALID_L_ID = 22L;
    public static String VALID_S_ID = IdConverter.convertId(VALID_L_ID);
    public static String GENERIC_NAME = "semaj";
    public static String GENERIC_DETAILS = "My BLT drive on my computer just went AWOL, and I've got this big project due tomorrow";
    public static int GENERIC_QUANTITY = 4;

    //should probably just use apache commons
    public static String LONG_STRING(int length){
        StringBuilder stringBuilder = new StringBuilder(" ");
        for(int a= 0 ; a<length; a++){
            stringBuilder.append("a");
        }
        return stringBuilder.toString();
    }

    public static List<ShoppingListItem> generateShoppingList(int items) {
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();
        for (int a =0; a< items; a++){
            shoppingListItems.add(new ShoppingListItem("food",a, Metric.CUP.getLabel()));
        }
        return shoppingListItems;
    }

    public static List<WeekDataItem> generateWeekView(int items) {
        List<WeekDataItem> weekDataItems = new ArrayList<>();
        for (int a =0; a< items; a++){
            weekDataItems.add(new WeekDataItem("day","Name",""+a));
        }
        return weekDataItems;
    }

    public static MealPrepDTO generateMealPrepDTO() {
        MealPrepDTO mealPrepDTO = new MealPrepDTO();
        mealPrepDTO.setId(VALID_S_ID);
        Map<String,RecipeDTO> recipeDTOMap = new HashMap<>();
        recipeDTOMap.put(DayOfWeekRef.DAY1.getDayOfWeekName(),generateRecipeDTO());
        recipeDTOMap.put(DayOfWeekRef.DAY2.getDayOfWeekName(),generateRecipeDTO());
        recipeDTOMap.put(DayOfWeekRef.DAY3.getDayOfWeekName(),generateRecipeDTO());
        recipeDTOMap.put(DayOfWeekRef.DAY4.getDayOfWeekName(),generateRecipeDTO());
        recipeDTOMap.put(DayOfWeekRef.DAY5.getDayOfWeekName(),generateRecipeDTO());
        recipeDTOMap.put(DayOfWeekRef.DAY6.getDayOfWeekName(),generateRecipeDTO());
        recipeDTOMap.put(DayOfWeekRef.DAY7.getDayOfWeekName(),generateRecipeDTO());
        mealPrepDTO.setWeeksRecipes(recipeDTOMap);
        return mealPrepDTO;

    }

    public static RecipeDTO generateRecipeDTO() {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(VALID_S_ID);
        List<IngredientDTO> ingredientDTOList = new ArrayList<>();
        List<StepDTO> stepDTOList = new ArrayList<>();
        for (int a = 0; a < GENERIC_QUANTITY; a++) {
            ingredientDTOList.add(generateIngredientDTO());
            stepDTOList.add(generateStepDTO(a+1));
        }
        recipeDTO.setIngredients(ingredientDTOList);
        recipeDTO.setSteps(stepDTOList);
        recipeDTO.setKcal(GENERIC_QUANTITY);
        recipeDTO.setRating(GENERIC_QUANTITY);
        recipeDTO.setServingSize(GENERIC_QUANTITY);
        recipeDTO.setNotes(GENERIC_DETAILS);
        recipeDTO.setName(GENERIC_NAME);
        recipeDTO.setMealType(MealType.DINNER);
        recipeDTO.setMealRotation(MealRotation.IN);
        return recipeDTO;
    }

    public static IngredientDTO generateIngredientDTO() {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(VALID_S_ID);
        ingredientDTO.setRecipeId(VALID_S_ID);
        ingredientDTO.setQuantity(GENERIC_QUANTITY);
        ingredientDTO.setMetric(Metric.CUP.name());
        ingredientDTO.setFoodTypeDTO(generateFoodTypeDTO());
        ingredientDTO.setIngredientType(IngredientType.OTHER);
        return ingredientDTO;
    }

    public static FoodTypeDTO generateFoodTypeDTO() {
        FoodTypeDTO foodTypeDTO = new FoodTypeDTO();
        foodTypeDTO.setId(VALID_S_ID);
        foodTypeDTO.setName(GENERIC_NAME);
        return foodTypeDTO;
    }

    public static StepDTO generateStepDTO(int number) {
        StepDTO stepDTO = new StepDTO();
        stepDTO.setId(VALID_S_ID);
        stepDTO.setStepNo(number);
        stepDTO.setDetails(GENERIC_DETAILS);
        stepDTO.setRecipeId(VALID_S_ID);
        return stepDTO;
    }

    public static String addToStringId(String id, int amount) {
        long total = IdConverter.convertId(id) + amount;
        return IdConverter.convertId(total);
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpHeaders defaultHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","application/json");
        return httpHeaders;
    }

}
