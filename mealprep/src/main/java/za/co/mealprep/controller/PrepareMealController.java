package za.co.mealprep.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.service.MealPrepService;

@RestController
public class PrepareMealController {

    @Autowired
    private MealPrepService mealPrepService;

    @GetMapping("/prepare-weeks-meals")
    public ResponseEntity<MealPrepDTO> prepareMealPrep() {
        try {
            return new ResponseEntity<>(mealPrepService.generateWeeklyMealPrep(), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @PutMapping("/update-weeks-meals")
    public ResponseEntity<MealPrepDTO> updateMealPRep(@Valid @RequestBody MealPrepDTO mealPrepDTO) {
        try {
            return new ResponseEntity<>(mealPrepService.updateWeeklyMealPrep(mealPrepDTO), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @GetMapping("/get-shopping-list")
    public ResponseEntity<String> getShoppingList(@Valid @RequestParam(name = "mealPrepId")
                                                  @Size(min = 0, max = 255,
                                                          message = "size") String mealPrepId) {
        try {
            return new ResponseEntity<>(mealPrepService.generateShoppingList(mealPrepId), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @GetMapping("/get-whastapp-message")
    public ResponseEntity<String> getWhatsAppMessage(@Valid @RequestParam(name = "mealPrepId")
                                                     @Size(min = 0, max = 255,
                                                             message = "size") String mealPrepId) {
        try {
            return new ResponseEntity<>(mealPrepService.generateWhatsAppList(mealPrepId), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }
}