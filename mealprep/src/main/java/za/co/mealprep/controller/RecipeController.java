package za.co.mealprep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.service.RecipeService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController extends RootController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/all")
    public ResponseEntity<List<RecipeDTO>> getRecipes() {
        try {
            return new ResponseEntity<>(recipeService.getAll(), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @GetMapping("/by-id")
    public ResponseEntity<List<RecipeDTO>> getRecipe(@RequestParam(name = "id")
                                                     @Size(min = 0, max = 255,
                                                             message = "size") String recipeId) {
        try {
            List<RecipeDTO> recipeDTOS = new ArrayList<>();
            recipeDTOS.add(recipeService.getById(recipeId));
            return new ResponseEntity<>(recipeDTOS, HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    /**
     * TODO
     * for some reason I cannot seem to have the mapped Java script Json object searlizied into my DTO,
     * so decided to leverage Jackson mapper for now
     *
     * @param recipeDTO
     * @return
     */
    @PostMapping("/createJavaScript")
    public ResponseEntity<RecipeDTO> create(@Valid @RequestBody String recipeDTO) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            RecipeDTO recipeDTO1 = mapper.readValue(recipeDTO, RecipeDTO.class);
            return new ResponseEntity<>(recipeService.create(recipeDTO1), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        } catch (Exception ex) {
            return new RestException(ex.getMessage(), ErrorConstants.UNEXPECTED_EXCEPTION).getRestfulResponse();
        }
    }
}