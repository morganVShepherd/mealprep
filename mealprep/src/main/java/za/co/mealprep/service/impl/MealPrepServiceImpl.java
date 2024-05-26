package za.co.mealprep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.entities.MealPrep;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.pojo.Constants;
import za.co.mealprep.pojo.DayOfWeekRef;
import za.co.mealprep.pojo.MealType;
import za.co.mealprep.pojo.ShoppingListItem;
import za.co.mealprep.repository.MealPrepRepository;
import za.co.mealprep.service.MealPrepService;
import za.co.mealprep.service.RecipeService;
import za.co.mealprep.utils.IdConverter;
import za.co.mealprep.utils.PrettyPrint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class MealPrepServiceImpl implements MealPrepService {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private MealPrepRepository mealPrepRepository;

    @Override
    public MealPrepDTO generateWeeklyMealPrep() throws RestException{
        try {
            return populateAndSaveWeeksMeals();
        }
        catch (RestException re) {
            throw re;
        }
        catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public MealPrepDTO updateWeeklyMealPrep(MealPrepDTO mealPrepDTO) throws RestException{
        try {

            if (mealPrepDTO.getId() == null) {
                throw new RestException(ErrorConstants.DOES_NOT_EXIST);
            }

            Optional<MealPrep> optional = mealPrepRepository.findById(IdConverter.convertId(mealPrepDTO.getId()));
            if(optional.isEmpty()){
                throw new RestException(ErrorConstants.DOES_NOT_EXIST);
            }
            MealPrep mealPrep = optional.get();
            if (mealPrepDTO.getMonDinner() == null) {
                mealPrep.setMonDinnerId(Constants.NO_RECIPE_DINNER_ID);
            } else {
                recipeService.update(mealPrepDTO.getMonDinner());
            }
            if (mealPrepDTO.getTuesDinner() == null) {
                mealPrep.setTuesDinnerId(Constants.NO_RECIPE_DINNER_ID);
            } else {
                recipeService.update(mealPrepDTO.getTuesDinner());
            }
            if (mealPrepDTO.getWedDinner() == null) {
                mealPrep.setWedDinnerId(Constants.NO_RECIPE_DINNER_ID);
            } else {
                recipeService.update(mealPrepDTO.getWedDinner());
            }
            if (mealPrepDTO.getThursDinner() == null) {
                mealPrep.setThursDinnerId(Constants.NO_RECIPE_DINNER_ID);
            } else {
                recipeService.update(mealPrepDTO.getTuesDinner());
            }
            if (mealPrepDTO.getFriDinner() == null) {
                mealPrep.setFriDinnerId(Constants.NO_RECIPE_DINNER_ID);
            } else {
                recipeService.update(mealPrepDTO.getFriDinner());
            }
            if (mealPrepDTO.getSatDinner() == null) {
                mealPrep.setSatDinnerId(Constants.NO_RECIPE_DINNER_ID);
            } else {
                recipeService.update(mealPrepDTO.getSatDinner());
            }
            if (mealPrepDTO.getSunDinner() == null) {
                mealPrep.setSunDinnerId(Constants.NO_RECIPE_DINNER_ID);
            } else {
                recipeService.update(mealPrepDTO.getSatDinner());
            }
            if (mealPrepDTO.getSatBreak() == null) {
                mealPrep.setSatBreakId(Constants.NO_RECIPE_BREAKFAST_ID);
            } else {
                recipeService.update(mealPrepDTO.getSatBreak());
            }
            if (mealPrepDTO.getSunBreak() == null) {
                mealPrep.setSunBreakId(Constants.NO_RECIPE_BREAKFAST_ID);
            } else {
                recipeService.update(mealPrepDTO.getSunBreak());
            }
            return mapFromEntity(mealPrepRepository.save(mealPrep));
        }
        catch (RestException re){
            throw re;
        }
        catch (Exception e){
            throw new RestException(e.getMessage(),ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public String generateShoppingList(String mealPrepId) throws RestException{
        try {

            MealPrepDTO mealPrepDTO = mapFromEntity(mealPrepRepository.findById(IdConverter.convertId(mealPrepId)).get());

            List<IngredientDTO> allIngredients = new ArrayList<>();
            allIngredients.addAll(mealPrepDTO.getMonDinner().getIngredients());
            allIngredients.addAll(mealPrepDTO.getTuesDinner().getIngredients());
            allIngredients.addAll(mealPrepDTO.getWedDinner().getIngredients());
            allIngredients.addAll(mealPrepDTO.getThursDinner().getIngredients());
            allIngredients.addAll(mealPrepDTO.getFriDinner().getIngredients());
            allIngredients.addAll(mealPrepDTO.getSatDinner().getIngredients());
            allIngredients.addAll(mealPrepDTO.getSunDinner().getIngredients());
            allIngredients.addAll(mealPrepDTO.getSatBreak().getIngredients());
            allIngredients.addAll(mealPrepDTO.getSunBreak().getIngredients());

            HashMap<String, ShoppingListItem> shoppingListItemHashMap = new HashMap<String, ShoppingListItem>();

            for (IngredientDTO currentIngredient : allIngredients) {
                ShoppingListItem shoppingListItem = shoppingListItemHashMap.get(currentIngredient.getFoodTypeDTO().getId());
                if (shoppingListItem != null) {
                    long qntyToAdd = currentIngredient.getQuantity();
                    if (shoppingListItem.getMetric() != currentIngredient.getMetric()) {
                        //todo convert metric to same
                    }
                    shoppingListItem.setQuantity(shoppingListItem.getQuantity() + qntyToAdd);
                } else {
                    shoppingListItem = new ShoppingListItem(currentIngredient.getFoodTypeDTO().getName(), currentIngredient.getQuantity(), currentIngredient.getMetric());
                }
                shoppingListItemHashMap.put(currentIngredient.getFoodTypeDTO().getId(), shoppingListItem);
            }
            shoppingListItemHashMap.remove(IdConverter.convertId(Constants.NO_FOOD_TYPE_ID));
            return PrettyPrint.generateStringShoppingList(shoppingListItemHashMap);
        }
        catch (Exception e){
            throw new RestException(e.getMessage(),ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    private MealPrepDTO populateAndSaveWeeksMeals() throws RestException {
        MealPrepDTO mealPrepDTO = new MealPrepDTO();
        List<RecipeDTO> recipes = recipeService.getRandomMealsForPrep(Constants.GENERAL_MEAL_PREP_DINNER_TOTAL, MealType.DINNER);
        mealPrepDTO.setMonDinner(recipes.get(DayOfWeekRef.DAY1.getValue()));
        mealPrepDTO.setTuesDinner(recipes.get(DayOfWeekRef.DAY2.getValue()));
        mealPrepDTO.setWedDinner(recipes.get(DayOfWeekRef.DAY3.getValue()));
        mealPrepDTO.setThursDinner(recipes.get(DayOfWeekRef.DAY4.getValue()));
        mealPrepDTO.setFriDinner(recipes.get(DayOfWeekRef.DAY5.getValue()));
        mealPrepDTO.setSatDinner(recipes.get(DayOfWeekRef.DAY6.getValue()));
        mealPrepDTO.setSunDinner(recipes.get(DayOfWeekRef.DAY7.getValue()));

        recipes = recipeService.getRandomMealsForPrep(Constants.GENERAL_MEAL_PREP_BREAKFAST_TOTAL, MealType.BREAKFAST);
        mealPrepDTO.setSatBreak(recipes.get(DayOfWeekRef.DAY1.getValue()));
        mealPrepDTO.setSunBreak(recipes.get(DayOfWeekRef.DAY2.getValue()));

        MealPrep mealPrep = new MealPrep(mealPrepDTO);

        mealPrep = mealPrepRepository.save(mealPrep);
        mealPrepDTO.setId(IdConverter.convertId(mealPrep.getId()));
        return mealPrepDTO;
    }

    private MealPrepDTO mapFromEntity(MealPrep mealPrep) throws RestException {
        MealPrepDTO mealPrepDTO = new MealPrepDTO();
        mealPrepDTO.setMonDinner(recipeService.getById(IdConverter.convertId(mealPrep.getMonDinnerId())));
        mealPrepDTO.setTuesDinner(recipeService.getById(IdConverter.convertId(mealPrep.getTuesDinnerId())));
        mealPrepDTO.setWedDinner(recipeService.getById(IdConverter.convertId(mealPrep.getWedDinnerId())));
        mealPrepDTO.setThursDinner(recipeService.getById(IdConverter.convertId(mealPrep.getThursDinnerId())));
        mealPrepDTO.setFriDinner(recipeService.getById(IdConverter.convertId(mealPrep.getFriDinnerId())));
        mealPrepDTO.setSatDinner(recipeService.getById(IdConverter.convertId(mealPrep.getSatDinnerId())));
        mealPrepDTO.setSunDinner(recipeService.getById(IdConverter.convertId(mealPrep.getSunDinnerId())));
        mealPrepDTO.setSatBreak(recipeService.getById(IdConverter.convertId(mealPrep.getSatBreakId())));
        mealPrepDTO.setSunBreak(recipeService.getById(IdConverter.convertId(mealPrep.getSunBreakId())));
        return mealPrepDTO;
    }
}