package za.co.mealprep.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.junit.jupiter.MockitoExtension;
import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.entities.FoodType;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.repository.FoodTypeRepository;
import za.co.mealprep.service.impl.FoodTypeServiceImpl;
import za.co.mealprep.utils.IdConverter;
import za.co.mealprep.utils.TestConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Tag("Unit-Test")
public class FoodTypeServiceTest {

    @Mock
    private FoodTypeRepository foodTypeRepository;
    @InjectMocks
    private FoodTypeServiceImpl foodTypeService;

    @Test
    void create_pass() throws RestException {
        FoodTypeDTO dto = TestConstants.generateFoodTypeDTO();
        dto.setName(TestConstants.GENERIC_NAME);
        FoodType entity = new FoodType(dto);
        entity.setId(TestConstants.VALID_L_ID);
        when(foodTypeRepository.findByName(any())).thenReturn(null);
        when(foodTypeRepository.save(any())).thenReturn(entity);

        FoodTypeDTO created = foodTypeService.create(dto);

        assertNotNull(created);
        assertEquals(TestConstants.VALID_S_ID, created.getId());
        assertEquals(TestConstants.GENERIC_NAME, created.getName());
        verify(foodTypeRepository, atLeast(1)).save(any());
    }

    @Test
    void createExists_fail() {
        FoodTypeDTO dto = TestConstants.generateFoodTypeDTO();
        List<FoodType> list = new ArrayList<>();
        list.add(new FoodType(dto));
        when(foodTypeRepository.findByName(any())).thenReturn(list);

        RestException thrown = assertThrows(
                RestException.class,
                () -> foodTypeService.create(dto),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.ALREADY_EXISTS, thrown.getError());
        verify(foodTypeRepository, never()).save(any());
    }

    @Test
    void update_pass() throws RestException {
        FoodTypeDTO dto = TestConstants.generateFoodTypeDTO();
        dto.setName(TestConstants.GENERIC_NAME);
        dto.setId(TestConstants.VALID_S_ID);
        FoodType entity = new FoodType(dto);
        when(foodTypeRepository.save(any())).thenReturn(entity);

        FoodTypeDTO created = foodTypeService.update(dto);

        assertNotNull(created);
        assertEquals(TestConstants.VALID_S_ID, created.getId());
        assertEquals(TestConstants.GENERIC_NAME, created.getName());
        verify(foodTypeRepository, atLeast(1)).save(any());
    }

    @Test
    void updateDoesNotExist_fail() {
        FoodTypeDTO dto = TestConstants.generateFoodTypeDTO();
        dto.setName(TestConstants.GENERIC_NAME);
        dto.setId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> foodTypeService.update(dto),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
        verify(foodTypeRepository, never()).save(any());
    }

    @Test
    void updateDuplicate_fail() {
        FoodTypeDTO dto = TestConstants.generateFoodTypeDTO();
        List<FoodType> list = new ArrayList<>();
        list.add(new FoodType(dto));
        when(foodTypeRepository.findByName(any())).thenReturn(list);

        RestException thrown = assertThrows(
                RestException.class,
                () -> foodTypeService.update(dto),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.DOES_EXIST, thrown.getError());
        verify(foodTypeRepository, never()).save(any());
    }

    @Test
    void delete_pass() throws RestException {
        FoodTypeDTO dto = new FoodTypeDTO();
        dto.setId(TestConstants.VALID_S_ID);
        doNothing().when(foodTypeRepository).deleteById(any());

        foodTypeService.delete(dto);

        verify(foodTypeRepository, atLeast(1)).deleteById(TestConstants.VALID_L_ID);
    }

    @Test
    void deleteDoesNotExist_fail() {
        FoodTypeDTO dto = TestConstants.generateFoodTypeDTO();
        dto.setName(TestConstants.GENERIC_NAME);
        dto.setId(null);

        RestException thrown = assertThrows(
                RestException.class,
                () -> foodTypeService.delete(dto),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.DOES_NOT_EXIST, thrown.getError());
        verify(foodTypeRepository, never()).save(any());
    }

    @Test
    void findExistingWithData_pass() throws RestException {
        List<FoodTypeDTO> dtos = new ArrayList<>();
        List<FoodType> entites = new ArrayList<>();
        for (long a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            FoodTypeDTO dto = new FoodTypeDTO(IdConverter.convertId(Long.valueOf(a)), "" + a);
            dtos.add(dto);
            entites.add(new FoodType(dto));
        }
        when(foodTypeRepository.findByNameContaining(any())).thenReturn(entites);

        List<FoodTypeDTO> foundDtos = foodTypeService.checkForExisting(TestConstants.GENERIC_NAME);

        assertEquals(TestConstants.GENERIC_QUANTITY, foundDtos.size());
        for (int a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            assertEquals(dtos.get(a).getId(), foundDtos.get(a).getId(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getName(), foundDtos.get(a).getName(), "Iteration " + a + "Is incorrect");
        }
        verify(foodTypeRepository, atLeast(1)).findByNameContaining(TestConstants.GENERIC_NAME);
    }

    @Test
    void findExistingWithoutDataNull_pass() throws RestException {
        when(foodTypeRepository.findByNameContaining(any())).thenReturn(null);

        List<FoodTypeDTO> foundDtos = foodTypeService.checkForExisting(TestConstants.GENERIC_NAME);

        assertEquals(null, foundDtos);
        verify(foodTypeRepository, atLeast(1)).findByNameContaining(TestConstants.GENERIC_NAME);
    }

    @Test
    void findExistingWithoutDataEmpty_pass() throws RestException {
        List<FoodType> entites = new ArrayList<>();
        when(foodTypeRepository.findByNameContaining(any())).thenReturn(entites);

        List<FoodTypeDTO> foundDtos = foodTypeService.checkForExisting(TestConstants.GENERIC_NAME);

        assertEquals(0, foundDtos.size());
        verify(foodTypeRepository, atLeast(1)).findByNameContaining(TestConstants.GENERIC_NAME);
    }

    @Test
    void findAll_pass() throws RestException {
        List<FoodTypeDTO> dtos = new ArrayList<>();
        List<FoodType> entites = new ArrayList<>();
        for (long a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            FoodTypeDTO dto = new FoodTypeDTO(IdConverter.convertId(Long.valueOf(a)), "" + a);
            dtos.add(dto);
            entites.add(new FoodType(dto));
        }
        when(foodTypeRepository.findAll()).thenReturn(entites);

        List<FoodTypeDTO> foundDtos = foodTypeService.getAll();

        assertEquals(TestConstants.GENERIC_QUANTITY, foundDtos.size());
        for (int a = 0; a < TestConstants.GENERIC_QUANTITY; a++) {
            assertEquals(dtos.get(a).getId(), foundDtos.get(a).getId(), "Iteration " + a + "Is incorrect");
            assertEquals(dtos.get(a).getName(), foundDtos.get(a).getName(), "Iteration " + a + "Is incorrect");
        }
    }

    @Test
    void findAllEmpty_pass() throws RestException {
        List<FoodType> entites = new ArrayList<>();
        when(foodTypeRepository.findAll()).thenReturn(entites);

        List<FoodTypeDTO> foundDtos = foodTypeService.getAll();

        assertEquals(0, foundDtos.size());
    }

    @Test
    void findAllNull_pass() throws RestException {
        when(foodTypeRepository.findAll()).thenReturn(null);

        List<FoodTypeDTO> foundDtos = foodTypeService.getAll();

        assertEquals(null, foundDtos);
    }

    @Test
    void findById_pass() throws RestException {
        Optional optional = Optional.of(new FoodType(TestConstants.generateFoodTypeDTO()));
        when(foodTypeRepository.findById(any())).thenReturn(optional);

        FoodTypeDTO foundDto = foodTypeService.findById(TestConstants.VALID_S_ID);

        assertEquals(TestConstants.VALID_S_ID, foundDto.getId());
        assertEquals(TestConstants.GENERIC_NAME, foundDto.getName());
        verify(foodTypeRepository, atLeast(1)).findById(TestConstants.VALID_L_ID);
    }

    @Test
    void findbyIdNotFound_pass() {
        Optional optional = Optional.empty();
        when(foodTypeRepository.findById(any())).thenReturn(optional);

        RestException thrown = assertThrows(
                RestException.class,
                () -> foodTypeService.findById(TestConstants.VALID_S_ID),
                "Expecting exception"
        );

        assertEquals(ErrorConstants.EMPTY_OPTIONAL, thrown.getError());
        verify(foodTypeRepository, never()).save(any());
    }
}