package za.co.mealprep.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.dto.StepDTO;
import za.co.mealprep.entities.FoodType;
import za.co.mealprep.entities.Step;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.repository.FoodTypeRepository;
import za.co.mealprep.repository.StepRepository;
import za.co.mealprep.service.impl.FoodTypeServiceImpl;
import za.co.mealprep.service.impl.StepServiceImpl;
import za.co.mealprep.utils.IdConverter;
import za.co.mealprep.utils.TestConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class StepServiceTest {

    @Mock
    private StepRepository stepRepository;
    @InjectMocks
    private StepServiceImpl stepService;

    @Test
    void create_pass() throws RestException {
        StepDTO stepDTO = TestConstants.generateStepDTO(1);
        Step entity = new Step(stepDTO);
        stepDTO.setId(null);
        when(stepRepository.save(any())).thenReturn(entity);

        stepDTO = stepService.create(stepDTO);

        assertNotNull(stepDTO);
        verify(stepRepository, atLeast(1)).save(any());
    }

    @Test
    void createExists_fail() throws RestException {
        StepDTO stepDTO = TestConstants.generateStepDTO(1);

        RestException thrown = assertThrows(
                RestException.class,
                () -> stepService.create(stepDTO),
                "Expecting exception"
        );
        assertEquals(ErrorConstants.DOES_EXIST, thrown.getError());
        verify(stepRepository,never()).save(any());
    }

    @Test
    void createNoRecipe_fail() throws RestException {
        StepDTO stepDTO = TestConstants.generateStepDTO(1);
        stepDTO.setRecipeId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> stepService.create(stepDTO),
                "Expecting exception"
        );
        assertEquals(ErrorConstants.NEEDS_PARENT, thrown.getError());
        verify(stepRepository,never()).save(any());
    }

    @Test
    void update_pass() throws RestException {
        StepDTO stepDTO = TestConstants.generateStepDTO(1);
        Step entity = new Step(stepDTO);
        when(stepRepository.save(any())).thenReturn(entity);

        stepDTO = stepService.update(stepDTO);

        assertNotNull(stepDTO);
        verify(stepRepository, atLeast(1)).save(any());
    }

    @Test
    void updateDoeNotExist_pass(){
        StepDTO stepDTO = TestConstants.generateStepDTO(1);
        stepDTO.setId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> stepService.update(stepDTO),
                "Expecting exception"
        );
        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
        verify(stepRepository,never()).save(any());
    }

    @Test
    void updateNoRecipe_pass(){
        StepDTO stepDTO = TestConstants.generateStepDTO(1);
        stepDTO.setRecipeId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> stepService.update(stepDTO),
                "Expecting exception"
        );
        assertEquals(ErrorConstants.NEEDS_PARENT, thrown.getError());
        verify(stepRepository,never()).save(any());
    }

    @Test
    void delete_pass() throws RestException {
        StepDTO stepDTO = TestConstants.generateStepDTO(1);
        doNothing().when(stepRepository).deleteById(any());

        stepService.delete(stepDTO);

        verify(stepRepository, atLeast(1)).deleteById(TestConstants.VALID_L_ID);
    }

    @Test
    void deleteNoId_fail() {
        StepDTO stepDTO = TestConstants.generateStepDTO(1);
        stepDTO.setId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> stepService.delete(stepDTO),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
        verify(stepRepository, never()).deleteById(TestConstants.VALID_L_ID);
    }

    @Test
    void deleteByRecipeId_pass() throws RestException {
        doNothing().when(stepRepository).deleteAllByRecipeId(TestConstants.VALID_L_ID);

        stepService.deleteByRecipeId(TestConstants.VALID_S_ID);

        verify(stepRepository, atLeast(1)).deleteAllByRecipeId(TestConstants.VALID_L_ID);
    }

    @Test
    void deleteByRecipeId_fail() {
        RestException thrown = assertThrows(
                RestException.class,
                () -> stepService.deleteByRecipeId(null),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
        verify(stepRepository, never()).deleteById(any());
    }

    @Test
    void findAll_pass() throws RestException {
        List<StepDTO> dtos = new ArrayList<>();
        List<Step> entites = new ArrayList<>();
        for (long a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            StepDTO dto = TestConstants.generateStepDTO((int)a);
            dtos.add(dto);
            entites.add(new Step(dto));
        }
        when(stepRepository.findAllByRecipeIdOrderByStepNoAsc(any())).thenReturn(entites);

        List<StepDTO> foundDtos = stepService.getAllForRecipe(TestConstants.VALID_S_ID);

        assertEquals(TestConstants.GENERIC_QUANTITY, foundDtos.size());
        for (int a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            assertEquals(dtos.get(a).getId(), foundDtos.get(a).getId(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getStepNo(), foundDtos.get(a).getStepNo(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getDetails(), foundDtos.get(a).getDetails(), "Iteration " + a + "Is incorrect");
        }
    }

    @Test
    void findAllEmpty_pass() throws RestException {
        List<Step> entites = new ArrayList<>();
        when(stepRepository.findAllByRecipeIdOrderByStepNoAsc(any())).thenReturn(entites);

        List<StepDTO> foundDtos = stepService.getAllForRecipe(TestConstants.VALID_S_ID);

        assertEquals(0, foundDtos.size());
    }
}