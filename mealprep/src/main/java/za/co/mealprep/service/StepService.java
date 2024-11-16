package za.co.mealprep.service;

import za.co.mealprep.dto.StepDTO;
import za.co.mealprep.exception.RestException;

import java.util.List;

public interface StepService {

    StepDTO create(StepDTO ingredientDTO) throws RestException;

    List<StepDTO> getAllForRecipe(String recipeId) throws RestException;
}
