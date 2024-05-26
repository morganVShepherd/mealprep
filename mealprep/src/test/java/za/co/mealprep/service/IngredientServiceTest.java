package za.co.mealprep.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.entities.Ingredient;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.pojo.IngredientType;
import za.co.mealprep.pojo.Metric;
import za.co.mealprep.repository.IngredientRepository;
import za.co.mealprep.service.impl.IngredientServiceImpl;
import za.co.mealprep.utils.TestConstants;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("Unit-Test")
public class IngredientServiceTest {
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private FoodTypeService foodTypeService;
    @InjectMocks
    private IngredientServiceImpl ingredientService;

    @Test
    void create_pass() throws RestException {
        IngredientDTO ingredientDTO = TestConstants.generateIngredientDTO();
        Ingredient entity = new Ingredient(ingredientDTO);
        when(ingredientRepository.save(any())).thenReturn(entity);

        IngredientDTO created = ingredientService.create(ingredientDTO);

        assertNotNull(created);
        assertEquals(TestConstants.VALID_S_ID, created.getId());
        assertEquals(TestConstants.GENERIC_NAME, created.getFoodTypeDTO().getName());
        assertEquals(IngredientType.OTHER, created.getIngredientType());
        assertEquals(Metric.CUP, created.getMetric());
        assertEquals(TestConstants.GENERIC_QUANTITY, created.getQuantity());
        verify(ingredientRepository, atLeast(1)).save(any());
    }

    @Test
    void createNoRecipe_fail() {
        IngredientDTO ingredientDTO = TestConstants.generateIngredientDTO();
        ingredientDTO.setRecipeId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> ingredientService.create(ingredientDTO),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.NEEDS_PARENT, thrown.getError());
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void update_pass() throws RestException {
        IngredientDTO ingredientDTO = TestConstants.generateIngredientDTO();
        Ingredient entity = new Ingredient(ingredientDTO);
        when(ingredientRepository.save(any())).thenReturn(entity);

        IngredientDTO created = ingredientService.update(ingredientDTO);

        assertNotNull(created);
        assertEquals(TestConstants.VALID_S_ID, created.getId());
        assertEquals(TestConstants.GENERIC_NAME, created.getFoodTypeDTO().getName());
        assertEquals(IngredientType.OTHER, created.getIngredientType());
        assertEquals(Metric.CUP, created.getMetric());
        assertEquals(TestConstants.GENERIC_QUANTITY, created.getQuantity());
        verify(ingredientRepository, atLeast(1)).save(any());
    }

    @Test
    void updateDoesNotExist_fail() {
        IngredientDTO ingredientDTO = TestConstants.generateIngredientDTO();
        ingredientDTO.setId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> ingredientService.update(ingredientDTO),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void updateDoesNotHaveRecipe_fail() {
        IngredientDTO ingredientDTO = TestConstants.generateIngredientDTO();
        ingredientDTO.setRecipeId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> ingredientService.update(ingredientDTO),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.NEEDS_PARENT, thrown.getError());
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void deleteIngredient_pass() throws RestException {
        IngredientDTO ingredientDTO = TestConstants.generateIngredientDTO();
        doNothing().when(ingredientRepository).deleteById(any());

        ingredientService.delete(ingredientDTO);

        verify(ingredientRepository, atLeast(1)).deleteById(TestConstants.VALID_L_ID);
    }

    @Test
    void deleteIngredientNoId_fail() {
        IngredientDTO ingredientDTO = TestConstants.generateIngredientDTO();
        ingredientDTO.setId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> ingredientService.delete(ingredientDTO),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
        verify(ingredientRepository, never()).deleteById(TestConstants.VALID_L_ID);
    }

    @Test
    void deleteByRecipeId_pass() throws RestException {
        doNothing().when(ingredientRepository).deleteAllByRecipeId(any());

        ingredientService.deleteByRecipeId(TestConstants.VALID_S_ID);

        verify(ingredientRepository, atLeast(1)).deleteAllByRecipeId(TestConstants.VALID_L_ID);
    }

    @Test
    void deleteByRecipeId_fail() {
        RestException thrown = assertThrows(
                RestException.class,
                () -> ingredientService.deleteByRecipeId(null),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
        verify(ingredientRepository, never()).deleteById(any());
    }

    @Test
    void findAll_pass() throws RestException {
        List<IngredientDTO> dtos = new ArrayList<>();
        List<Ingredient> entites = new ArrayList<>();
        for (long a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            IngredientDTO dto = TestConstants.generateIngredientDTO();
            dtos.add(dto);
            entites.add(new Ingredient(dto));

        }
        when(foodTypeService.findById(any())).thenReturn(TestConstants.generateFoodTypeDTO());
        when(ingredientRepository.findAll()).thenReturn(entites);

        List<IngredientDTO> foundDtos = ingredientService.getAll();

        assertEquals(TestConstants.GENERIC_QUANTITY, foundDtos.size());
        for (int a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            assertEquals(dtos.get(a).getId(), foundDtos.get(a).getId(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getMetric(), foundDtos.get(a).getMetric(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getQuantity(), foundDtos.get(a).getQuantity(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getFoodTypeDTO().getId(), foundDtos.get(a).getFoodTypeDTO().getId(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getIngredientType(), foundDtos.get(a).getIngredientType(), "Iteration " + a + "Is incorrect");
        }
    }

    @Test
    void findAllEmpty_pass() throws RestException {
        List<Ingredient> entites = new ArrayList<>();
        when(ingredientRepository.findAll()).thenReturn(entites);

        List<IngredientDTO> foundDtos = ingredientService.getAll();

        assertEquals(0, foundDtos.size());
    }

    @Test
    void findAllByRecipeId_pass() throws RestException {
        List<IngredientDTO> dtos = new ArrayList<>();
        List<Ingredient> entites = new ArrayList<>();
        for (long a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            IngredientDTO dto = TestConstants.generateIngredientDTO();
            dtos.add(dto);
            entites.add(new Ingredient(dto));
        }
        when(foodTypeService.findById(any())).thenReturn(TestConstants.generateFoodTypeDTO());
        when(ingredientRepository.findAllByRecipeId(TestConstants.VALID_L_ID)).thenReturn(entites);

        List<IngredientDTO> foundDtos = ingredientService.getAllForRecipe(TestConstants.VALID_S_ID);

        assertEquals(TestConstants.GENERIC_QUANTITY, foundDtos.size());
        for (int a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            assertEquals(dtos.get(a).getId(), foundDtos.get(a).getId(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getMetric(), foundDtos.get(a).getMetric(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getQuantity(), foundDtos.get(a).getQuantity(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getFoodTypeDTO().getId(), foundDtos.get(a).getFoodTypeDTO().getId(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getIngredientType(), foundDtos.get(a).getIngredientType(), "Iteration " + a + "Is incorrect");
        }
    }

    @Test
    void findAllByRecipeIdEmpty_pass() throws RestException {
        List<Ingredient> entites = new ArrayList<>();
        when(ingredientRepository.findAllByRecipeId(TestConstants.VALID_L_ID)).thenReturn(entites);

        List<IngredientDTO> foundDtos = ingredientService.getAllForRecipe(TestConstants.VALID_S_ID);

        assertEquals(0, foundDtos.size());
    }
}