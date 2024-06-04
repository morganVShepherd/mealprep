package za.co.mealprep.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.entities.MealPrep;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.pojo.Constants;
import za.co.mealprep.pojo.MealType;
import za.co.mealprep.repository.MealPrepRepository;
import za.co.mealprep.service.impl.MealPrepServiceImpl;
import za.co.mealprep.utils.IdConverter;
import za.co.mealprep.utils.TestConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("Unit-Test")
public class MealPrepServiceTest {

    @Mock
    private MealPrepRepository mealPrepRepository;
    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private MealPrepServiceImpl mealPrepService;

    @Test
    void create_pass() throws RestException {
        List<RecipeDTO> dinnerDTOS = new ArrayList<>();
        List<RecipeDTO> breakFastDTOS = new ArrayList<>();
        for (int a = 0; a < Constants.GENERAL_MEAL_PREP_DINNER_TOTAL; a++) {
            RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
            recipeDTO.setId(TestConstants.addToStringId(recipeDTO.getId(), a));
            dinnerDTOS.add(recipeDTO);
        }
        for (int a = 0; a < Constants.GENERAL_MEAL_PREP_BREAKFAST_TOTAL; a++) {
            RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
            recipeDTO.setId(TestConstants.addToStringId(recipeDTO.getId(), a));
            breakFastDTOS.add(recipeDTO);
        }
        when(recipeService.getRandomMealsForPrep(Constants.GENERAL_MEAL_PREP_DINNER_TOTAL, MealType.DINNER)).thenReturn(dinnerDTOS);
        when(recipeService.getRandomMealsForPrep(Constants.GENERAL_MEAL_PREP_BREAKFAST_TOTAL, MealType.BREAKFAST)).thenReturn(breakFastDTOS);
        MealPrep entity = new MealPrep(TestConstants.generateMealPrepDTO());
        entity.setId(TestConstants.VALID_L_ID);
        when(mealPrepRepository.save(any())).thenReturn(entity);

        MealPrepDTO mealPrepDTO = mealPrepService.generateWeeklyMealPrep();

        assertNotNull(mealPrepDTO);
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getMonDinner().getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 1), mealPrepDTO.getTuesDinner().getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 2), mealPrepDTO.getWedDinner().getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 3), mealPrepDTO.getThursDinner().getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 4), mealPrepDTO.getFriDinner().getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 5), mealPrepDTO.getSatDinner().getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 6), mealPrepDTO.getSunDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatBreak().getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 1), mealPrepDTO.getSunBreak().getId());
        verify(mealPrepRepository, atLeast(1)).save(any());
    }

    @Test
    void createNotEnoughDinners_fail() throws RestException {
        when(recipeService.getRandomMealsForPrep(Constants.GENERAL_MEAL_PREP_DINNER_TOTAL, MealType.DINNER)).thenThrow(new RestException(ErrorConstants.NEED_DATA));

        RestException thrown = assertThrows(
                RestException.class,
                () -> mealPrepService.generateWeeklyMealPrep(),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.NEED_DATA, thrown.getError());
        verify(mealPrepRepository, never()).save(any());
    }

    @Test
    void createNotEnoughBreakfasts_fail() throws RestException {
        List<RecipeDTO> dinnerDTOS = new ArrayList<>();
        for (int a = 0; a < Constants.GENERAL_MEAL_PREP_DINNER_TOTAL; a++) {
            RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
            recipeDTO.setId(TestConstants.addToStringId(recipeDTO.getId(), a));
            dinnerDTOS.add(recipeDTO);
        }
        when(recipeService.getRandomMealsForPrep(Constants.GENERAL_MEAL_PREP_DINNER_TOTAL, MealType.DINNER)).thenReturn(dinnerDTOS);
        when(recipeService.getRandomMealsForPrep(Constants.GENERAL_MEAL_PREP_BREAKFAST_TOTAL, MealType.BREAKFAST)).thenThrow(new RestException(ErrorConstants.NEED_DATA));

        RestException thrown = assertThrows(
                RestException.class,
                () -> mealPrepService.generateWeeklyMealPrep(),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.NEED_DATA, thrown.getError());
        verify(mealPrepRepository, never()).save(any());
    }

    @Test
    void updateAll_pass() throws RestException {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        MealPrep entity = new MealPrep(mealPrepDTO);
        when(mealPrepRepository.findById(TestConstants.VALID_L_ID)).thenReturn(Optional.of(entity));
        when(mealPrepRepository.save(any())).thenReturn(entity);
        when(recipeService.getById(any())).thenReturn(TestConstants.generateRecipeDTO());

        mealPrepDTO = mealPrepService.updateWeeklyMealPrep(mealPrepDTO);

        assertNotNull(mealPrepDTO);
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getMonDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getTuesDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getWedDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getThursDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getFriDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatBreak().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunBreak().getId());
        verify(mealPrepRepository, atLeast(1)).save(any());
    }

    @Test
    void updateMon_pass() throws RestException {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        mealPrepDTO.setMonDinner(null);
        MealPrep entity = new MealPrep(mealPrepDTO);
        when(mealPrepRepository.findById(TestConstants.VALID_L_ID)).thenReturn(Optional.of(entity));
        when(mealPrepRepository.save(any())).thenReturn(entity);
        when(recipeService.getById(TestConstants.VALID_S_ID)).thenReturn(TestConstants.generateRecipeDTO());
        RecipeDTO replacement = TestConstants.generateRecipeDTO();
        String noDinnerId = IdConverter.convertId(Constants.NO_RECIPE_DINNER_ID);
        replacement.setId(noDinnerId);
        when(recipeService.getById(noDinnerId)).thenReturn(replacement);

        mealPrepDTO = mealPrepService.updateWeeklyMealPrep(mealPrepDTO);

        assertNotNull(mealPrepDTO);
        assertEquals(noDinnerId, mealPrepDTO.getMonDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getTuesDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getWedDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getThursDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getFriDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatBreak().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunBreak().getId());
        verify(mealPrepRepository, atLeast(1)).save(any());
    }

    @Test
    void updateTues_pass() throws RestException {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        mealPrepDTO.setTuesDinner(null);
        MealPrep entity = new MealPrep(mealPrepDTO);
        when(mealPrepRepository.findById(TestConstants.VALID_L_ID)).thenReturn(Optional.of(entity));
        when(mealPrepRepository.save(any())).thenReturn(entity);
        when(recipeService.getById(TestConstants.VALID_S_ID)).thenReturn(TestConstants.generateRecipeDTO());
        RecipeDTO replacement = TestConstants.generateRecipeDTO();
        String noDinnerId = IdConverter.convertId(Constants.NO_RECIPE_DINNER_ID);
        replacement.setId(noDinnerId);
        when(recipeService.getById(noDinnerId)).thenReturn(replacement);

        mealPrepDTO = mealPrepService.updateWeeklyMealPrep(mealPrepDTO);

        assertNotNull(mealPrepDTO);
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getMonDinner().getId());
        assertEquals(noDinnerId, mealPrepDTO.getTuesDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getWedDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getThursDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getFriDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatBreak().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunBreak().getId());
        verify(mealPrepRepository, atLeast(1)).save(any());
    }

    @Test
    void updateWed_pass() throws RestException {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        mealPrepDTO.setWedDinner(null);
        MealPrep entity = new MealPrep(mealPrepDTO);
        when(mealPrepRepository.findById(TestConstants.VALID_L_ID)).thenReturn(Optional.of(entity));
        when(mealPrepRepository.save(any())).thenReturn(entity);
        when(recipeService.getById(TestConstants.VALID_S_ID)).thenReturn(TestConstants.generateRecipeDTO());
        RecipeDTO replacement = TestConstants.generateRecipeDTO();
        String noDinnerId = IdConverter.convertId(Constants.NO_RECIPE_DINNER_ID);
        replacement.setId(noDinnerId);
        when(recipeService.getById(noDinnerId)).thenReturn(replacement);

        mealPrepDTO = mealPrepService.updateWeeklyMealPrep(mealPrepDTO);

        assertNotNull(mealPrepDTO);
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getMonDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getTuesDinner().getId());
        assertEquals(noDinnerId, mealPrepDTO.getWedDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getThursDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getFriDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatBreak().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunBreak().getId());
        verify(mealPrepRepository, atLeast(1)).save(any());
    }

    @Test
    void updateThurs_pass() throws RestException {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        mealPrepDTO.setThursDinner(null);
        MealPrep entity = new MealPrep(mealPrepDTO);
        when(mealPrepRepository.findById(TestConstants.VALID_L_ID)).thenReturn(Optional.of(entity));
        when(mealPrepRepository.save(any())).thenReturn(entity);
        when(recipeService.getById(TestConstants.VALID_S_ID)).thenReturn(TestConstants.generateRecipeDTO());
        RecipeDTO replacement = TestConstants.generateRecipeDTO();
        String noDinnerId = IdConverter.convertId(Constants.NO_RECIPE_DINNER_ID);
        replacement.setId(noDinnerId);
        when(recipeService.getById(noDinnerId)).thenReturn(replacement);

        mealPrepDTO = mealPrepService.updateWeeklyMealPrep(mealPrepDTO);

        assertNotNull(mealPrepDTO);
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getMonDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getTuesDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getWedDinner().getId());
        assertEquals(noDinnerId, mealPrepDTO.getThursDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getFriDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatBreak().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunBreak().getId());
        verify(mealPrepRepository, atLeast(1)).save(any());
    }

    @Test
    void updateFri_pass() throws RestException {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        mealPrepDTO.setFriDinner(null);
        MealPrep entity = new MealPrep(mealPrepDTO);
        when(mealPrepRepository.findById(TestConstants.VALID_L_ID)).thenReturn(Optional.of(entity));
        when(mealPrepRepository.save(any())).thenReturn(entity);
        when(recipeService.getById(TestConstants.VALID_S_ID)).thenReturn(TestConstants.generateRecipeDTO());
        RecipeDTO replacement = TestConstants.generateRecipeDTO();
        String noDinnerId = IdConverter.convertId(Constants.NO_RECIPE_DINNER_ID);
        replacement.setId(noDinnerId);
        when(recipeService.getById(noDinnerId)).thenReturn(replacement);

        mealPrepDTO = mealPrepService.updateWeeklyMealPrep(mealPrepDTO);

        assertNotNull(mealPrepDTO);
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getMonDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getTuesDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getWedDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getThursDinner().getId());
        assertEquals(noDinnerId, mealPrepDTO.getFriDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatBreak().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunBreak().getId());
        verify(mealPrepRepository, atLeast(1)).save(any());
    }

    @Test
    void updateSat_pass() throws RestException {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        mealPrepDTO.setSatBreak(null);
        mealPrepDTO.setSatDinner(null);
        MealPrep entity = new MealPrep(mealPrepDTO);
        when(mealPrepRepository.findById(TestConstants.VALID_L_ID)).thenReturn(Optional.of(entity));
        when(mealPrepRepository.save(any())).thenReturn(entity);
        when(recipeService.getById(TestConstants.VALID_S_ID)).thenReturn(TestConstants.generateRecipeDTO());
        RecipeDTO replacement = TestConstants.generateRecipeDTO();
        RecipeDTO breakReplacement = TestConstants.generateRecipeDTO();
        String noDinnerId = IdConverter.convertId(Constants.NO_RECIPE_DINNER_ID);
        String noBreakId = IdConverter.convertId(Constants.NO_RECIPE_BREAKFAST_ID);
        replacement.setId(noDinnerId);
        breakReplacement.setId(noBreakId);
        when(recipeService.getById(noDinnerId)).thenReturn(replacement);
        when(recipeService.getById(noBreakId)).thenReturn(breakReplacement);

        mealPrepDTO = mealPrepService.updateWeeklyMealPrep(mealPrepDTO);

        assertNotNull(mealPrepDTO);
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getMonDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getTuesDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getWedDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getThursDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getFriDinner().getId());
        assertEquals(noDinnerId, mealPrepDTO.getSatDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunDinner().getId());
        assertEquals(noBreakId, mealPrepDTO.getSatBreak().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSunBreak().getId());
        verify(mealPrepRepository, atLeast(1)).save(any());
    }

    @Test
    void updateSun_pass() throws RestException {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        mealPrepDTO.setSunBreak(null);
        mealPrepDTO.setSunDinner(null);
        MealPrep entity = new MealPrep(mealPrepDTO);
        when(mealPrepRepository.findById(TestConstants.VALID_L_ID)).thenReturn(Optional.of(entity));
        when(mealPrepRepository.save(any())).thenReturn(entity);
        when(recipeService.getById(TestConstants.VALID_S_ID)).thenReturn(TestConstants.generateRecipeDTO());
        RecipeDTO replacement = TestConstants.generateRecipeDTO();
        RecipeDTO breakReplacement = TestConstants.generateRecipeDTO();
        String noDinnerId = IdConverter.convertId(Constants.NO_RECIPE_DINNER_ID);
        String noBreakId = IdConverter.convertId(Constants.NO_RECIPE_BREAKFAST_ID);
        replacement.setId(noDinnerId);
        breakReplacement.setId(noBreakId);
        when(recipeService.getById(noDinnerId)).thenReturn(replacement);
        when(recipeService.getById(noBreakId)).thenReturn(breakReplacement);

        mealPrepDTO = mealPrepService.updateWeeklyMealPrep(mealPrepDTO);

        assertNotNull(mealPrepDTO);
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getMonDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getTuesDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getWedDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getThursDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getFriDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatDinner().getId());
        assertEquals(noDinnerId, mealPrepDTO.getSunDinner().getId());
        assertEquals(TestConstants.VALID_S_ID, mealPrepDTO.getSatBreak().getId());
        assertEquals(noBreakId, mealPrepDTO.getSunBreak().getId());
        verify(mealPrepRepository, atLeast(1)).save(any());
    }

    @Test
    void updateNoId_fail() {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        mealPrepDTO.setId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> mealPrepService.updateWeeklyMealPrep(mealPrepDTO),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
        verify(mealPrepRepository, never()).save(any());
    }

    @Test
    void updateNotExist_fail() {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        when(mealPrepRepository.findById(any())).thenReturn(Optional.empty());

        RestException thrown = assertThrows(
                RestException.class,
                () -> mealPrepService.updateWeeklyMealPrep(mealPrepDTO),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
        verify(mealPrepRepository, never()).save(any());
    }

    @Test
    void generateGenericShoppingList_Pass() throws RestException {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        when(mealPrepRepository.findById(any())).thenReturn(Optional.of(new MealPrep(mealPrepDTO)));
        when(recipeService.getById(any())).thenReturn(TestConstants.generateRecipeDTO());

        String shoppinglist = mealPrepService.generateShoppingList(TestConstants.VALID_S_ID);
        assertNotNull(shoppinglist);
        //generic ingredients with generic quantity for each of the 9 meals
        String totalQuantity = "" + (TestConstants.GENERIC_QUANTITY * TestConstants.GENERIC_QUANTITY * 9);
        String metric = mealPrepDTO.getMonDinner().getIngredients().get(0).getMetric().toString();
        String totalIngredents = String.format(Constants.LIST_TEMPLATE, totalQuantity, metric, TestConstants.GENERIC_NAME);
        assertTrue(shoppinglist.contains(totalIngredents));
        assertTrue(shoppinglist.contains(Constants.SHOPPING_LIST_TITLE));
    }

    @Test
    void generateShoppingListDontCalculateEmpty_Pass() throws RestException {
        MealPrep mealPrep = new MealPrep();
        mealPrep.setMonDinnerId(Constants.NO_RECIPE_DINNER_ID);
        mealPrep.setTuesDinnerId(Constants.NO_RECIPE_DINNER_ID);
        mealPrep.setWedDinnerId(Constants.NO_RECIPE_DINNER_ID);
        mealPrep.setThursDinnerId(Constants.NO_RECIPE_DINNER_ID);
        mealPrep.setFriDinnerId(Constants.NO_RECIPE_DINNER_ID);
        mealPrep.setSatDinnerId(Constants.NO_RECIPE_DINNER_ID);
        mealPrep.setSunDinnerId(Constants.NO_RECIPE_DINNER_ID);
        mealPrep.setSatBreakId(Constants.NO_RECIPE_DINNER_ID);
        mealPrep.setSunBreakId(Constants.NO_RECIPE_DINNER_ID);
        RecipeDTO emptyRecipe = TestConstants.generateNoMealRecipeDto();
        when(recipeService.getById(IdConverter.convertId(Constants.NO_RECIPE_DINNER_ID))).thenReturn(emptyRecipe);
        when(mealPrepRepository.findById(any())).thenReturn(Optional.of(mealPrep));

        String shoppinglist = mealPrepService.generateShoppingList(TestConstants.VALID_S_ID);
        assertEquals(Constants.SHOPPING_LIST_TITLE.trim(), shoppinglist.trim());
    }

    @Test
    void generateGenericShoppingListExtra_Pass() throws RestException {
        MealPrepDTO mealPrepDTO = TestConstants.generateMealPrepDTO();
        RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
        IngredientDTO ingredientDTO = TestConstants.generateIngredientDTO();
        ingredientDTO.setQuantity(TestConstants.GENERIC_QUANTITY * 2);
        String otherFood = "Other";
        String otherId = IdConverter.convertId(TestConstants.VALID_L_ID * 2);
        ingredientDTO.getFoodTypeDTO().setName(otherFood);
        ingredientDTO.getFoodTypeDTO().setId(otherId);
        recipeDTO.getIngredients().add(ingredientDTO);
        when(mealPrepRepository.findById(any())).thenReturn(Optional.of(new MealPrep(mealPrepDTO)));
        when(recipeService.getById(any())).thenReturn(recipeDTO);

        String shoppinglist = mealPrepService.generateShoppingList(TestConstants.VALID_S_ID);
        assertNotNull(shoppinglist);
        String totalQuantity = "" + (TestConstants.GENERIC_QUANTITY * TestConstants.GENERIC_QUANTITY * 9);
        String metric = mealPrepDTO.getMonDinner().getIngredients().get(0).getMetric().toString();
        String totalIngredents = String.format(Constants.LIST_TEMPLATE, totalQuantity, metric, TestConstants.GENERIC_NAME);

        String totalOtherQuantity = "" + (TestConstants.GENERIC_QUANTITY * TestConstants.GENERIC_QUANTITY * 9) / 2;
        String totalOtherIngredents = String.format(Constants.LIST_TEMPLATE, totalOtherQuantity, metric, otherFood);

        assertTrue(shoppinglist.contains(totalIngredents));
        assertTrue(shoppinglist.contains(totalOtherIngredents));
        assertTrue(shoppinglist.contains(Constants.SHOPPING_LIST_TITLE));
    }
}