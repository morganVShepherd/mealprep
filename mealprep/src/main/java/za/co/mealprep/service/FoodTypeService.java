package za.co.mealprep.service;

import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.exception.RestException;

import java.util.List;

public interface FoodTypeService {

    FoodTypeDTO create(FoodTypeDTO foodTypeDTO) throws RestException;
    FoodTypeDTO update(FoodTypeDTO foodTypeDTO) throws RestException;
    void delete (FoodTypeDTO foodTypeDTO) throws RestException;
    List<FoodTypeDTO> checkForExisting(String name) throws RestException;
    List<FoodTypeDTO> getAll() throws RestException;
    FoodTypeDTO findById(String id) throws RestException;

}
