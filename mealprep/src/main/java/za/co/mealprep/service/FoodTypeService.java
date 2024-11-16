package za.co.mealprep.service;

import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.exception.RestException;

import java.util.List;

public interface FoodTypeService {

    FoodTypeDTO create(FoodTypeDTO foodTypeDTO);

    List<FoodTypeDTO> checkForExisting(String name) throws RestException;
    FoodTypeDTO findById(String id) throws RestException;

}
