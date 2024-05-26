package za.co.mealprep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.dto.StepDTO;
import za.co.mealprep.entities.Recipe;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.pojo.MealRotation;
import za.co.mealprep.pojo.MealType;
import za.co.mealprep.repository.RecipeRepository;
import za.co.mealprep.service.IngredientService;
import za.co.mealprep.service.RecipeService;
import za.co.mealprep.service.StepService;
import za.co.mealprep.utils.IdConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private StepService stepService;

    @Override
    public RecipeDTO create(RecipeDTO recipeDTO) throws RestException {
        try {
            Recipe recipe = recipeRepository.save(new Recipe(recipeDTO));
            String recipeId = IdConverter.convertId(recipe.getId());
            for(IngredientDTO ingredientDTO:recipeDTO.getIngredients()){
                ingredientDTO.setRecipeId(recipeId);
                String entityId = ingredientService.create(ingredientDTO).getId();
                ingredientDTO.setId(entityId);
            }
            for(StepDTO stepDTO:recipeDTO.getSteps()){
                stepDTO.setRecipeId(recipeId);
                String entityId = stepService.create(stepDTO).getId();
                stepDTO.setId(entityId);
            }
            recipeDTO.setId(recipeId);
            return recipeDTO;
        }
        catch (RestException re){
            throw re;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public RecipeDTO update(RecipeDTO recipeDTO) throws RestException {
        try {
            if(recipeDTO.getId()==null){
                throw new RestException(ErrorConstants.DOES_NOT_EXIST);
            }
            recipeRepository.save(new Recipe(recipeDTO));
            for(IngredientDTO ingredientDTO: recipeDTO.getIngredients()){
                if(ingredientDTO.getRecipeId() == null){
                    ingredientDTO.setRecipeId(recipeDTO.getId());
                    ingredientDTO.setId(ingredientService.create(ingredientDTO).getId());
                }
                else{
                    ingredientService.update(ingredientDTO);
                }
            }

            for(StepDTO stepDTO: recipeDTO.getSteps()){
                if(stepDTO.getRecipeId() == null){
                    stepDTO.setRecipeId(recipeDTO.getId());
                    stepDTO.setId(stepService.create(stepDTO).getId());
                }
                else{
                    stepService.update(stepDTO);
                }
            }
            for(IngredientDTO ingredientDTO:recipeDTO.getIngredients()){
                ingredientService.update(ingredientDTO);
            }
            return  recipeDTO;
        }catch(RestException re){
            throw re;
        }
        catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void delete(RecipeDTO recipeDTO) throws RestException {
        try {
            if(recipeDTO.getId()==null){
                throw new RestException(ErrorConstants.DOES_NOT_EXIST);
            }
            ingredientService.deleteByRecipeId(recipeDTO.getId());
            stepService.deleteByRecipeId(recipeDTO.getId());
            recipeRepository.deleteById(IdConverter.convertId(recipeDTO.getId()));
        }
        catch (RestException re){
            throw re;
        }
        catch (Exception e) {
            throw new RestException(e.getMessage(),ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }
    @Override
    public List<RecipeDTO> getRandomMealsForPrep(int total, MealType mealType) throws RestException {
        List<Recipe> recipes = recipeRepository.findAllByInRotationAndMealType(MealRotation.IN,mealType);
        List<RecipeDTO> dtos = new ArrayList<>();

        if(recipes.size() < total){
            //todo flip all into rotation
            //recipeRepository.changeMealRotation(MealRotation.IN,MealRotation.OUT);
            //recipes = recipeRepository.findAllByInRotationAndMealType(MealRotation.IN,mealType);
            if(recipes.size() < total){
                throw new RestException(ErrorConstants.NEED_DATA);
            }
        }
        Collections.shuffle(recipes);
        for(int a = 0; a<total; a++){
            String recipeId = IdConverter.convertId(recipes.get(a).getId());
            dtos.add(new RecipeDTO(recipes.get(a),stepService.getAllForRecipe(recipeId),ingredientService.getAllForRecipe(recipeId)));
        }
        //todo take selected out of rotation
        return dtos;
    }

    @Override
    public List<RecipeDTO> getAll() throws RestException{
        List<Recipe> recipes = recipeRepository.findAll();
        return mapList(recipes);
    }

    @Override
    public RecipeDTO getById(String recipeId) throws RestException{
        try {
            Optional<Recipe> optional= recipeRepository.findById(IdConverter.convertId(recipeId));
            if(optional.isEmpty()){
                throw new RestException(ErrorConstants.DOES_NOT_EXIST);
            }
            Recipe recipe = optional.get();
            return new RecipeDTO(recipe,stepService.getAllForRecipe(recipeId),ingredientService.getAllForRecipe(recipeId));

        }
        catch (RestException re){
            throw re;
        }
        catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }

    }

    private List<RecipeDTO> mapList(List<Recipe> entities) throws RestException {
        List<RecipeDTO> dtos = new ArrayList<>();
        for (Recipe entity : entities) {
            RecipeDTO recipeDTO = new RecipeDTO(entity);
            recipeDTO.setIngredients(ingredientService.getAllForRecipe(recipeDTO.getId()));
            recipeDTO.setSteps(stepService.getAllForRecipe(recipeDTO.getId()));
            dtos.add(recipeDTO);
        }
        return dtos;
    }
}