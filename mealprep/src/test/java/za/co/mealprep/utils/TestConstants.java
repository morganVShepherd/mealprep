package za.co.mealprep.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.dto.StepDTO;
import za.co.mealprep.pojo.Constants;
import za.co.mealprep.pojo.IngredientType;
import za.co.mealprep.pojo.MealRotation;
import za.co.mealprep.pojo.MealType;
import za.co.mealprep.pojo.Metric;

import java.util.ArrayList;
import java.util.List;

public class TestConstants {

    public static Long VALID_L_ID = Long.valueOf(22l);
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

    public static MealPrepDTO generateMealPrepDTO() {
        MealPrepDTO mealPrepDTO = new MealPrepDTO();
        mealPrepDTO.setId(VALID_S_ID);
        mealPrepDTO.setMonDinner(generateRecipeDTO());
        mealPrepDTO.setTuesDinner(generateRecipeDTO());
        mealPrepDTO.setWedDinner(generateRecipeDTO());
        mealPrepDTO.setThursDinner(generateRecipeDTO());
        mealPrepDTO.setFriDinner(generateRecipeDTO());
        mealPrepDTO.setSatDinner(generateRecipeDTO());
        mealPrepDTO.setSunDinner(generateRecipeDTO());
        mealPrepDTO.setSatBreak(generateRecipeDTO());
        mealPrepDTO.setSunBreak(generateRecipeDTO());
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
        ingredientDTO.setMetric(Metric.CUP);
        ingredientDTO.setFoodTypeDTO(generateFoodTypeDTO());
        ingredientDTO.setIngredientType(IngredientType.OTHER);
        return ingredientDTO;
    }

    public static RecipeDTO generateNoMealRecipeDto() {
        RecipeDTO recipeDTO = generateRecipeDTO();
        String recipeId = IdConverter.convertId(Constants.NO_RECIPE_DINNER_ID);

        FoodTypeDTO emptyFoodType = generateFoodTypeDTO();
        emptyFoodType.setId(IdConverter.convertId(Constants.NO_FOOD_TYPE_ID));
        emptyFoodType.setName("Nothing");

        List<StepDTO> stepDTOList = new ArrayList<>();
        StepDTO emptySteps = generateStepDTO(0);
        emptySteps.setId(IdConverter.convertId(Constants.NO_STEP_ID));
        emptySteps.setRecipeId(recipeId);
        emptySteps.setDetails("No Steps");
        stepDTOList.add(emptySteps);
        recipeDTO.setSteps(stepDTOList);

        List<IngredientDTO> ingredientDTOList = new ArrayList<>();
        IngredientDTO emptyIngredient = generateIngredientDTO();
        emptyIngredient.setRecipeId(recipeId);
        emptyIngredient.setId(IdConverter.convertId(Constants.NO_INGREDIENT_ID));
        emptyIngredient.setQuantity(0l);
        emptyIngredient.setMetric(Metric.BLANK);
        emptyIngredient.setFoodTypeDTO(emptyFoodType);
        ingredientDTOList.add(emptyIngredient);
        recipeDTO.setIngredients(ingredientDTOList);

        return recipeDTO;
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
