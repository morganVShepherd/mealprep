package za.co.mealprep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.entities.FoodType;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.repository.FoodTypeRepository;
import za.co.mealprep.service.FoodTypeService;
import za.co.mealprep.utils.IdConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FoodTypeServiceImpl implements FoodTypeService {

    @Autowired
    public FoodTypeRepository foodTypeRepository;

    @Override
    public FoodTypeDTO create(FoodTypeDTO foodTypeDTO) throws RestException {
        try {
            if (foodTypeRepository.findByName(foodTypeDTO.getName())!=null
                    && foodTypeRepository.findByName(foodTypeDTO.getName()).size()>0) {
                throw new RestException(ErrorConstants.ALREADY_EXISTS);
            }
            FoodType foodType = foodTypeRepository.save(new FoodType(foodTypeDTO));
            return new FoodTypeDTO(foodType);
        } catch (RestException re) {
            throw re;
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public FoodTypeDTO update(FoodTypeDTO foodTypeDTO) throws RestException {
        try {
            if (foodTypeDTO.getId() == null) {
                throw new RestException(ErrorConstants.DOES_NOT_EXIST);
            }
            if (foodTypeRepository.findByName(foodTypeDTO.getName()).size() > 0) {
                throw new RestException(ErrorConstants.DOES_EXIST);
            }
            FoodType foodType = foodTypeRepository.save(new FoodType(foodTypeDTO));
            return new FoodTypeDTO(foodType);
        } catch (RestException re) {
            throw re;
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void delete(FoodTypeDTO foodTypeDTO) throws RestException {
        try {
            if (foodTypeDTO.getId() == null) {
                throw new RestException(ErrorConstants.DOES_NOT_EXIST);
            }
            foodTypeRepository.deleteById(IdConverter.convertId(foodTypeDTO.getId()));
        }
        catch (RestException re) {
            throw re;
        }
        catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public List<FoodTypeDTO> checkForExisting(String name) throws RestException {
        try {
            List<FoodType> foodTypes = foodTypeRepository.findByNameContaining(name);

            return mapList(foodTypes);
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public List<FoodTypeDTO> getAll() throws RestException {
        try {
            List<FoodType> foodTypes = foodTypeRepository.findAll();
            return mapList(foodTypes);
        } catch (Exception e) {
            throw new RestException(e.getMessage(),ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public FoodTypeDTO findById(String id) throws RestException {
        try {
            Optional<FoodType> foodType = foodTypeRepository.findById(IdConverter.convertId(id));
            if(foodType.isEmpty()){
                throw new RestException(ErrorConstants.EMPTY_OPTIONAL);
            }
            return new FoodTypeDTO(foodType.get());
        }catch (RestException re){
            throw re;
        }
        catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    private List<FoodTypeDTO> mapList(List<FoodType> foodTypes){
        if(foodTypes == null){
            return null;
        }
        List<FoodTypeDTO> foodTypeDTOS = new ArrayList<>();
        for (FoodType foodType : foodTypes) {
            foodTypeDTOS.add(new FoodTypeDTO(foodType));
        }
        return foodTypeDTOS;
    }
}