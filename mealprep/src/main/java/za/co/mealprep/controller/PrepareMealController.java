package za.co.mealprep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.dto.WeekDataItem;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.pojo.ShoppingListItem;
import za.co.mealprep.service.MealPrepService;

import java.util.List;

@RestController
public class PrepareMealController extends RootController{

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

    @GetMapping("/get-shopping-list")
    public ResponseEntity<List<ShoppingListItem>> getShoppingList() {
        try {
            return new ResponseEntity<>(mealPrepService.generateShoppingList(), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @GetMapping("/get-whastapp-message")
    public ResponseEntity<String> getWhatsAppMessage() {
        try {
            return new ResponseEntity<>(mealPrepService.generateWhatsAppList(), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @GetMapping("/get-weeks-data")
    public ResponseEntity<List<WeekDataItem>> getWeeksData() {
        try {
            return new ResponseEntity<>(mealPrepService.generateWeekView(), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }
}