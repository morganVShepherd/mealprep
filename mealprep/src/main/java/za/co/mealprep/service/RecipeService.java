package za.co.mealprep.service;

import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.pojo.MealType;

import java.util.List;

public interface RecipeService {

    RecipeDTO create(RecipeDTO recipeDTO) throws RestException;

    List<RecipeDTO> getRandomMealsForPrep(int total, MealType mealType) throws RestException;

    List<RecipeDTO> getAll() throws RestException;

    RecipeDTO getById(String recipeId) throws RestException;


}
