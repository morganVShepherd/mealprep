package za.co.mealprep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.service.MealPrepService;

@RestController
public class PrepareMealController {

    @Autowired
    private MealPrepService mealPrepService;

    @PostMapping("/prepare-weeks-meals")
    public ResponseEntity<MealPrepDTO> prepareMealPrep() {
        try {
            return new ResponseEntity<>(mealPrepService.generateWeeklyMealPrep(), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @PatchMapping("/update-weeks-meals")
    public ResponseEntity<MealPrepDTO> updateMealPRep() {
        try {
            return new ResponseEntity<>(mealPrepService.generateWeeklyMealPrep(), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @GetMapping("/get-shopping-list")
    public ResponseEntity<String> getShoppingList(@RequestParam(name = "mealPrepId") String mealPrepId) {
        try {
            return new ResponseEntity<>(mealPrepService.generateShoppingList(mealPrepId), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }
}