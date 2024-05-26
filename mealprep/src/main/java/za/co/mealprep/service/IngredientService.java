package za.co.mealprep.service;

import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.exception.RestException;

import java.util.List;

public interface IngredientService {

    IngredientDTO create(IngredientDTO ingredientDTO) throws RestException;
    IngredientDTO update(IngredientDTO ingredientDTO) throws RestException;
    void delete (IngredientDTO ingredientDTO) throws RestException;
    void deleteByRecipeId(String recipeId) throws RestException;
    List<IngredientDTO> getAll() throws RestException;
    List<IngredientDTO> getAllForRecipe(String recipeId) throws RestException;
}
