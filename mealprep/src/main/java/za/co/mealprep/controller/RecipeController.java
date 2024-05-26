package za.co.mealprep.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("recipe")
public class RecipeController {

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
    public ResponseEntity<RecipeDTO> checkExists(@RequestParam(name = "id") String recipeId) {
        try {
            return new ResponseEntity<>(recipeService.getById(recipeId), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<RecipeDTO> create(@RequestBody RecipeDTO recipeDTO) {
        try {
            return new ResponseEntity<>(recipeService.create(recipeDTO), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<RecipeDTO> update(@Valid @RequestBody RecipeDTO recipeDTO) {
        try {
            return new ResponseEntity<>(recipeService.update(recipeDTO), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }
}