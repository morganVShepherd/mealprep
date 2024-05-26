package za.co.mealprep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.mealprep.dto.StepDTO;
import za.co.mealprep.entities.Step;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.repository.StepRepository;
import za.co.mealprep.service.StepService;
import za.co.mealprep.utils.IdConverter;

import java.util.ArrayList;
import java.util.List;

@Service
public class StepServiceImpl implements StepService {

    @Autowired
    private StepRepository stepRepository;

    @Override
    public StepDTO create(StepDTO stepDTO) throws RestException {
        try {
            if (stepDTO.getRecipeId() == null) {
                throw new RestException(ErrorConstants.NEEDS_PARENT);
            }
            if (stepDTO.getId() != null) {
                throw new RestException(ErrorConstants.DOES_EXIST);
            }
            Step step = stepRepository.save(new Step(stepDTO));
            return new StepDTO(step);

        } catch (RestException re) {
            throw re;
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public StepDTO update(StepDTO stepDTO) throws RestException {
        try {
            if (stepDTO.getId() == null) {
                throw new RestException(ErrorConstants.DOES_NOT_EXIST);
            }
            if (stepDTO.getRecipeId() == null) {
                throw new RestException(ErrorConstants.NEEDS_PARENT);
            }
            Step step = stepRepository.save(new Step(stepDTO));
            return new StepDTO(step);

        } catch (RestException re) {
            throw re;
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void delete(StepDTO stepDTO) throws RestException {
        try {
            if (stepDTO.getId() == null) {
                throw new RestException(ErrorConstants.DOES_NOT_EXIST);
            }
            stepRepository.deleteById(IdConverter.convertId(stepDTO.getId()));
        } catch (RestException re) {
            throw re;
        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void deleteByRecipeId(String recipeId) throws RestException{
        try {
            if ( recipeId== null) {
                throw new RestException(ErrorConstants.DOES_NOT_EXIST);
            }
            stepRepository.deleteAllByRecipeId(IdConverter.convertId(recipeId));
        }
        catch (RestException re){
            throw re;
        }
        catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public List<StepDTO> getAllForRecipe(String recipeId) throws RestException {
        try {
            List<Step> steps = stepRepository.findAllByRecipeIdOrderByStepNoAsc(IdConverter.convertId(recipeId));
            return mapList(steps);

        } catch (Exception e) {
            throw new RestException(e.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION);
        }
    }

    private List<StepDTO> mapList(List<Step> entities) {
        List<StepDTO> dtos = new ArrayList<>();
        for (Step entity : entities) {
            StepDTO dto = new StepDTO(entity);
            dtos.add(dto);
        }
        return dtos;
    }
}