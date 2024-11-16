package za.co.mealprep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.entities.Ingredient;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.repository.IngredientRepository;
import za.co.mealprep.service.FoodTypeService;
import za.co.mealprep.service.IngredientService;
import za.co.mealprep.utils.IdConverter;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private FoodTypeService foodTypeService;


    @Override
    public IngredientDTO create(IngredientDTO ingredientDTO) throws RestException {
        try {

            List<FoodTypeDTO> foodTypeDTOS = foodTypeService.checkForExisting(ingredientDTO.getFoodTypeDTO().getName().toLowerCase());
            if(foodTypeDTOS.isEmpty()){
                ingredientDTO.setFoodTypeDTO(foodTypeService.create(ingredientDTO.getFoodTypeDTO()));
            }
            else{
                ingredientDTO.setFoodTypeDTO(foodTypeDTOS.get(0));
            }

            if (ingredientDTO.getRecipeId() == null) {
                throw new RestException(ErrorConstants.NEEDS_PARENT);
            }
            Ingredient ingredient = ingredientRepository.save(new Ingredient(ingredientDTO));
            return new IngredientDTO(ingredient, ingredientDTO.getFoodTypeDTO());
        } catch (RestException re) {
            throw re;
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public List<IngredientDTO> getAllForRecipe(String recipeId) throws RestException {
        try {
            List<Ingredient> ingredients = ingredientRepository.findAllByRecipeId(IdConverter.convertId(recipeId));
            return mapList(ingredients);
        }
        catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    private List<IngredientDTO> mapList(List<Ingredient> entities) throws RestException {
        List<IngredientDTO> dtos = new ArrayList<>();
        for (Ingredient entity : entities) {
            FoodTypeDTO foodType = foodTypeService.findById(IdConverter.convertId(entity.getFoodTypeId()));
            dtos.add(new IngredientDTO(entity, foodType));
        }
        return dtos;
    }
}