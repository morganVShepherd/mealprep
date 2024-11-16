package za.co.mealprep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.dto.WeekDataItem;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class MealPrepServiceImpl implements MealPrepService {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private MealPrepRepository mealPrepRepository;

    @Override
    public MealPrepDTO generateWeeklyMealPrep() throws RestException {
        try {
            return populateAndSaveWeeksMeals();
        } catch (RestException re) {
            throw re;
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }


    @Override
    public List<ShoppingListItem> generateShoppingList() throws RestException {
        try {

            MealPrepDTO mealPrepDTO = mapFromEntity(mealPrepRepository.findFirstByOrderByIdDesc().get());

            List<IngredientDTO> allIngredients = new ArrayList<>();
            allIngredients.addAll(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY1.getDayOfWeekName()).getIngredients());
            allIngredients.addAll(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY2.getDayOfWeekName()).getIngredients());
            allIngredients.addAll(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY3.getDayOfWeekName()).getIngredients());
            allIngredients.addAll(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY4.getDayOfWeekName()).getIngredients());
            allIngredients.addAll(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY5.getDayOfWeekName()).getIngredients());
            allIngredients.addAll(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY6.getDayOfWeekName()).getIngredients());
            allIngredients.addAll(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY7.getDayOfWeekName()).getIngredients());

            HashMap<String, ShoppingListItem> shoppingListItemHashMap = new HashMap<>();

            for (IngredientDTO currentIngredient : allIngredients) {
                ShoppingListItem shoppingListItem = shoppingListItemHashMap.get(currentIngredient.getFoodTypeDTO().getId());
                if (shoppingListItem != null) {
                    long qntyToAdd = currentIngredient.getQuantity();
                    shoppingListItem.setQuantity(shoppingListItem.getQuantity() + qntyToAdd);
                } else {
                    shoppingListItem = new ShoppingListItem(currentIngredient.getFoodTypeDTO().getName(), currentIngredient.getQuantity(), currentIngredient.getMetric());
                }
                shoppingListItemHashMap.put(currentIngredient.getFoodTypeDTO().getId(), shoppingListItem);
            }
            shoppingListItemHashMap.remove(IdConverter.convertId(Constants.NO_FOOD_TYPE_ID));

            List<ShoppingListItem> shoppingListItems = new ArrayList<>(shoppingListItemHashMap.values());
            shoppingListItems.sort(Comparator.comparing(ShoppingListItem::getFoodName));
            return shoppingListItems;
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public String generateWhatsAppList() throws RestException {
        try {
            MealPrepDTO mealPrepDTO = mapFromEntity(mealPrepRepository.findFirstByOrderByIdDesc().get());
            StringBuilder whatsappList = new StringBuilder("Meal prep for the week \n");

            whatsappList.append(generateListItem(IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY1.getDayOfWeekName()).getId()),
                    mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY1.getDayOfWeekName()).getName(), DayOfWeekRef.DAY1.getDayOfWeekName()));

            whatsappList.append(generateListItem(IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY2.getDayOfWeekName()).getId()),
                    mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY2.getDayOfWeekName()).getName(), DayOfWeekRef.DAY2.getDayOfWeekName()));

            whatsappList.append(generateListItem(IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY3.getDayOfWeekName()).getId()),
                    mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY3.getDayOfWeekName()).getName(), DayOfWeekRef.DAY3.getDayOfWeekName()));

            whatsappList.append(generateListItem(IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY4.getDayOfWeekName()).getId()),
                    mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY4.getDayOfWeekName()).getName(), DayOfWeekRef.DAY4.getDayOfWeekName()));

            whatsappList.append(generateListItem(IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY5.getDayOfWeekName()).getId()),
                    mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY5.getDayOfWeekName()).getName(), DayOfWeekRef.DAY5.getDayOfWeekName()));

            whatsappList.append(generateListItem(IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY6.getDayOfWeekName()).getId()),
                    mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY6.getDayOfWeekName()).getName(), DayOfWeekRef.DAY6.getDayOfWeekName()));

            whatsappList.append(generateListItem(IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY7.getDayOfWeekName()).getId()),
                    mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY7.getDayOfWeekName()).getName(), DayOfWeekRef.DAY7.getDayOfWeekName()));

            return whatsappList.toString();

        } catch (RestException re) {
            throw re;
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public List<WeekDataItem> generateWeekView() throws RestException {
        try {
            MealPrepDTO mealPrepDTO = mapFromEntity(mealPrepRepository.findFirstByOrderByIdDesc().get());
            List<WeekDataItem> weekDataItems = new ArrayList<>();
            RecipeDTO recipeDTOForProcessing = mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY1.getDayOfWeekName());
            weekDataItems.add(new WeekDataItem(DayOfWeekRef.DAY1.getDayOfWeekName(), recipeDTOForProcessing.getName(), recipeDTOForProcessing.getId()));

            recipeDTOForProcessing = mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY2.getDayOfWeekName());
            weekDataItems.add(new WeekDataItem(DayOfWeekRef.DAY2.getDayOfWeekName(), recipeDTOForProcessing.getName(), recipeDTOForProcessing.getId()));

            recipeDTOForProcessing = mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY3.getDayOfWeekName());
            weekDataItems.add(new WeekDataItem(DayOfWeekRef.DAY3.getDayOfWeekName(), recipeDTOForProcessing.getName(), recipeDTOForProcessing.getId()));

            recipeDTOForProcessing = mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY4.getDayOfWeekName());
            weekDataItems.add(new WeekDataItem(DayOfWeekRef.DAY4.getDayOfWeekName(), recipeDTOForProcessing.getName(), recipeDTOForProcessing.getId()));

            recipeDTOForProcessing = mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY5.getDayOfWeekName());
            weekDataItems.add(new WeekDataItem(DayOfWeekRef.DAY5.getDayOfWeekName(), recipeDTOForProcessing.getName(), recipeDTOForProcessing.getId()));

            recipeDTOForProcessing = mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY6.getDayOfWeekName());
            weekDataItems.add(new WeekDataItem(DayOfWeekRef.DAY6.getDayOfWeekName(), recipeDTOForProcessing.getName(), recipeDTOForProcessing.getId()));

            recipeDTOForProcessing = mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY7.getDayOfWeekName());
            weekDataItems.add(new WeekDataItem(DayOfWeekRef.DAY7.getDayOfWeekName(), recipeDTOForProcessing.getName(), recipeDTOForProcessing.getId()));

            return weekDataItems;

        } catch (RestException re) {
            throw re;
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    private String generateListItem(Long recipeId, String desc, String day) {
        String messageLineItem = "";

        if (Objects.equals(recipeId, Constants.NO_RECIPE_DINNER_ID)) {
            messageLineItem = String.format(Constants.LIST_TEMPLATE, day, MealType.DINNER.getLabel(), "Out");
        } else {
            messageLineItem = String.format(Constants.LIST_TEMPLATE, day, MealType.DINNER.getLabel(), desc);
        }
        return messageLineItem;
    }

    private MealPrepDTO populateAndSaveWeeksMeals() throws RestException {
        MealPrepDTO mealPrepDTO = new MealPrepDTO();
        List<RecipeDTO> recipes = recipeService.getRandomMealsForPrep(Constants.GENERAL_MEAL_PREP_DINNER_TOTAL, MealType.DINNER);
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY1.getDayOfWeekName(), recipes.get(DayOfWeekRef.DAY1.getValue()));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY2.getDayOfWeekName(), recipes.get(DayOfWeekRef.DAY2.getValue()));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY3.getDayOfWeekName(), recipes.get(DayOfWeekRef.DAY3.getValue()));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY4.getDayOfWeekName(), recipes.get(DayOfWeekRef.DAY4.getValue()));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY5.getDayOfWeekName(), recipes.get(DayOfWeekRef.DAY5.getValue()));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY6.getDayOfWeekName(), recipes.get(DayOfWeekRef.DAY6.getValue()));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY7.getDayOfWeekName(), recipes.get(DayOfWeekRef.DAY7.getValue()));


        MealPrep mealPrep = new MealPrep(mealPrepDTO);

        mealPrep = mealPrepRepository.save(mealPrep);
        mealPrepDTO.setId(IdConverter.convertId(mealPrep.getId()));
        return mealPrepDTO;
    }

    private MealPrepDTO mapFromEntity(MealPrep mealPrep) throws RestException {
        MealPrepDTO mealPrepDTO = new MealPrepDTO();
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY1.getDayOfWeekName(), recipeService.getById(IdConverter.convertId(mealPrep.getMonDinnerId())));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY2.getDayOfWeekName(), recipeService.getById(IdConverter.convertId(mealPrep.getTuesDinnerId())));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY3.getDayOfWeekName(), recipeService.getById(IdConverter.convertId(mealPrep.getWedDinnerId())));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY4.getDayOfWeekName(), recipeService.getById(IdConverter.convertId(mealPrep.getThursDinnerId())));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY5.getDayOfWeekName(), recipeService.getById(IdConverter.convertId(mealPrep.getFriDinnerId())));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY6.getDayOfWeekName(), recipeService.getById(IdConverter.convertId(mealPrep.getSatDinnerId())));
        mealPrepDTO.getWeeksRecipes().put(DayOfWeekRef.DAY7.getDayOfWeekName(), recipeService.getById(IdConverter.convertId(mealPrep.getSunDinnerId())));
        return mealPrepDTO;
    }
}