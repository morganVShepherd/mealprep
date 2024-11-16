package za.co.mealprep.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.mealprep.dto.StepDTO;
import za.co.mealprep.entities.Step;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.repository.StepRepository;
import za.co.mealprep.service.impl.StepServiceImpl;
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
    void createExists_fail() {
        StepDTO stepDTO = TestConstants.generateStepDTO(1);

        RestException thrown = assertThrows(
                RestException.class,
                () -> stepService.create(stepDTO),
                "Expecting exception"
        );
        assertEquals(ErrorConstants.DOES_EXIST, thrown.getError());
        verify(stepRepository, never()).save(any());
    }

    @Test
    void createNoRecipe_fail() {
        StepDTO stepDTO = TestConstants.generateStepDTO(1);
        stepDTO.setRecipeId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> stepService.create(stepDTO),
                "Expecting exception"
        );
        assertEquals(ErrorConstants.NEEDS_PARENT, thrown.getError());
        verify(stepRepository, never()).save(any());
    }

    @Test
    void findAll_pass() throws RestException {
        List<StepDTO> dtos = new ArrayList<>();
        List<Step> entites = new ArrayList<>();
        for (long a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            StepDTO dto = TestConstants.generateStepDTO((int) a);
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