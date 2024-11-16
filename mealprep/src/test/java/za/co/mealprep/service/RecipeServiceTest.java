package za.co.mealprep.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.entities.Recipe;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.pojo.MealRotation;
import za.co.mealprep.pojo.MealType;
import za.co.mealprep.repository.RecipeRepository;
import za.co.mealprep.service.impl.RecipeServiceImpl;
import za.co.mealprep.utils.IdConverter;
import za.co.mealprep.utils.TestConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("Unit-Test")
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private IngredientService ingredientService;
    @Mock
    private StepService stepService;
    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    void create_pass() throws RestException {
        RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
        Recipe entity = new Recipe(recipeDTO);
        when(recipeRepository.save(any())).thenReturn(entity);
        when(stepService.create(any())).thenReturn(TestConstants.generateStepDTO(1));
        when(ingredientService.create(any())).thenReturn(TestConstants.generateIngredientDTO());

        RecipeDTO created = recipeService.create(recipeDTO);

        assertNotNull(created);
        assertNotNull(created.getIngredients());
        assertNotNull(created.getSteps());
        assertEquals(TestConstants.VALID_S_ID, created.getId());
        assertEquals(TestConstants.GENERIC_QUANTITY, created.getKcal());
        assertEquals(TestConstants.GENERIC_QUANTITY, created.getServingSize());
        assertEquals(TestConstants.GENERIC_QUANTITY, created.getRating());
        assertEquals(TestConstants.GENERIC_DETAILS, created.getNotes());
        assertEquals(TestConstants.GENERIC_NAME, created.getName());

        verify(recipeRepository, atLeast(1)).save(any());
    }

    @Test
    void getRandomMealsForPrep_pass() throws RestException{
        List<Recipe> recipes = new ArrayList<>();
        RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
        for (int a = 0; a< TestConstants.GENERIC_QUANTITY; a++){
            recipes.add(new Recipe(recipeDTO));
        }
        when(recipeRepository.findAllByInRotationAndMealType(MealRotation.IN,MealType.DINNER)).thenReturn(recipes);
        when(stepService.getAllForRecipe(any())).thenReturn(recipeDTO.getSteps());
        when(ingredientService.getAllForRecipe(any())).thenReturn(recipeDTO.getIngredients());

        List<RecipeDTO> recipeDTOS = recipeService.getRandomMealsForPrep(TestConstants.GENERIC_QUANTITY, MealType.DINNER);

        assertNotNull(recipeDTOS);
        assertEquals(TestConstants.GENERIC_QUANTITY, recipeDTOS.size());
    }

    @Test
    void getRandomMealsForNotEnough_fail()  {
        List<Recipe> recipes = new ArrayList<>();
        RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
        for (int a = 0; a< TestConstants.GENERIC_QUANTITY-1; a++){
            recipes.add(new Recipe(recipeDTO));
        }
        when(recipeRepository.findAllByInRotationAndMealType(MealRotation.IN,MealType.DINNER)).thenReturn(recipes);

        RestException thrown = assertThrows(
                RestException.class,
                () -> recipeService.getRandomMealsForPrep(TestConstants.GENERIC_QUANTITY,MealType.DINNER),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.NEED_DATA, thrown.getError());
    }

    @Test
    void getRandomMealsShuffle_pass() throws RestException{
        List<Recipe> recipes = new ArrayList<>();
        RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
        for (int a = 0; a< TestConstants.GENERIC_QUANTITY*2; a++){
            Recipe recipe = new Recipe(recipeDTO);
            recipe.setId(Long.valueOf(a));
            recipes.add(recipe);
        }
        when(recipeRepository.findAllByInRotationAndMealType(MealRotation.IN,MealType.DINNER)).thenReturn(recipes);
        when(stepService.getAllForRecipe(any())).thenReturn(recipeDTO.getSteps());
        when(ingredientService.getAllForRecipe(any())).thenReturn(recipeDTO.getIngredients());

        List<RecipeDTO> recipeDTOS = recipeService.getRandomMealsForPrep(TestConstants.GENERIC_QUANTITY, MealType.DINNER);

        assertNotNull(recipeDTOS);
        assertEquals(TestConstants.GENERIC_QUANTITY, recipeDTOS.size());
        //the chances of the first being shuffle dot teh same order is super low
        if(IdConverter.convertId(recipeDTOS.get(0).getId())==0L
            && IdConverter.convertId(recipeDTOS.get(1).getId())==1L
                && IdConverter.convertId(recipeDTOS.get(2).getId())==2L ){
            fail();
        }
    }

    @Test
    void findAll_pass() throws RestException {
        List<Recipe> recipes = new ArrayList<>();
        RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
        for (int a = 0; a< TestConstants.GENERIC_QUANTITY; a++){
            recipes.add(new Recipe(recipeDTO));
        }
        when(recipeRepository.findAll()).thenReturn(recipes);
        when(stepService.getAllForRecipe(any())).thenReturn(recipeDTO.getSteps());
        when(ingredientService.getAllForRecipe(any())).thenReturn(recipeDTO.getIngredients());

        List<RecipeDTO> foundDtos = recipeService.getAll();

        assertEquals(TestConstants.GENERIC_QUANTITY, foundDtos.size());
        for (int a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            assertEquals(TestConstants.VALID_S_ID, foundDtos.get(a).getId(), "Iteration " + a + "Is incorrect");
            assertEquals(TestConstants.GENERIC_QUANTITY, foundDtos.get(a).getKcal(), "Iteration " + a + " Is incorrect");
            assertEquals(TestConstants.GENERIC_QUANTITY, foundDtos.get(a).getServingSize(), "Iteration " + a + "Is incorrect");
            assertEquals(TestConstants.GENERIC_QUANTITY, foundDtos.get(a).getRating(), "Iteration " + a + "Is incorrect");
            assertEquals(TestConstants.GENERIC_DETAILS, foundDtos.get(a).getNotes(), "Iteration " + a + "Is incorrect");
            assertEquals(TestConstants.GENERIC_NAME, foundDtos.get(a).getName(), "Iteration " + a + "Is incorrect");
        }
    }

    @Test
    void findAllEmpty_pass() throws RestException {
        List<Recipe> entites = new ArrayList<>();
        when(recipeRepository.findAll()).thenReturn(entites);

        List<RecipeDTO> foundDtos = recipeService.getAll();

        assertEquals(0, foundDtos.size());
    }

    @Test
    void findAllById_pass() throws RestException{
        RecipeDTO recipeDTO = TestConstants.generateRecipeDTO();
        Recipe recipe = new Recipe(recipeDTO);
        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
        when(stepService.getAllForRecipe(any())).thenReturn(recipeDTO.getSteps());
        when(ingredientService.getAllForRecipe(any())).thenReturn(recipeDTO.getIngredients());

        RecipeDTO found = recipeService.getById(TestConstants.VALID_S_ID);

        assertNotNull(found);
    }

    @Test
    void findAllById_empty(){
        when(recipeRepository.findById(any())).thenReturn(Optional.empty());

        RestException thrown = assertThrows(
                RestException.class,
                () -> recipeService.getById(TestConstants.VALID_S_ID),
                "Expecting exception"
        );
        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
    }
}