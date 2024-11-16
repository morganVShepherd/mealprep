package za.co.mealprep.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.entities.MealPrep;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.pojo.Constants;
import za.co.mealprep.pojo.DayOfWeekRef;
import za.co.mealprep.pojo.MealType;
import za.co.mealprep.repository.MealPrepRepository;
import za.co.mealprep.service.impl.MealPrepServiceImpl;
import za.co.mealprep.utils.TestConstants;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        for (int a = 0; a < Constants.GENERAL_MEAL_PREP_DINNER_TOTAL; a++) {
            RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
            recipeDTO.setId(TestConstants.addToStringId(recipeDTO.getId(), a));
            dinnerDTOS.add(recipeDTO);
        }
        when(recipeService.getRandomMealsForPrep(Constants.GENERAL_MEAL_PREP_DINNER_TOTAL, MealType.DINNER)).thenReturn(dinnerDTOS);
        MealPrep entity = new MealPrep(TestConstants.generateMealPrepDTO());
        entity.setId(TestConstants.VALID_L_ID);
        when(mealPrepRepository.save(any())).thenReturn(entity);

        MealPrepDTO mealPrepDTO = mealPrepService.generateWeeklyMealPrep();

        assertNotNull(mealPrepDTO);
        assertEquals(TestConstants.VALID_S_ID,mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY1.getDayOfWeekName()).getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 1), mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY2.getDayOfWeekName()).getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 2),mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY3.getDayOfWeekName()).getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 3), mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY4.getDayOfWeekName()).getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 4), mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY5.getDayOfWeekName()).getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 5), mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY6.getDayOfWeekName()).getId());
        assertEquals(TestConstants.addToStringId(TestConstants.VALID_S_ID, 6), mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY7.getDayOfWeekName()).getId());
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

}