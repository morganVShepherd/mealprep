package za.co.mealprep.service;

import za.co.mealprep.dto.StepDTO;
import za.co.mealprep.exception.RestException;

import java.util.List;

public interface StepService {

    StepDTO create(StepDTO ingredientDTO) throws RestException;

    StepDTO update(StepDTO ingredientDTO) throws RestException;

    void delete(StepDTO ingredientDTO) throws RestException;
    void deleteByRecipeId(String recpieId) throws RestException;

    List<StepDTO> getAllForRecipe(String recipeId) throws RestException;

}
