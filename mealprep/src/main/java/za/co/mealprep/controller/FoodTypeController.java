package za.co.mealprep.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.exception.RestException;
import za.co.mealprep.service.FoodTypeService;

import java.util.List;

@RestController
@RequestMapping("food-type")
public class FoodTypeController extends RootController {
    @Autowired
    private FoodTypeService foodTypeService;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<FoodTypeDTO>> getFoods() {
        try {
            return new ResponseEntity<>(foodTypeService.getAll(), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @GetMapping("/check-exists")
    @ResponseBody
    public ResponseEntity<List<FoodTypeDTO>> checkExists(@Valid @RequestParam(name = "name")
                                                             @Size(min = 0, max = 255,
                                                                     message = "size") String name) {
        try {
            return new ResponseEntity<>(foodTypeService.checkForExisting(name), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<FoodTypeDTO> create(@Valid @RequestBody FoodTypeDTO foodTypeDTO) {
        try {
            return new ResponseEntity<>(foodTypeService.create(foodTypeDTO), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<FoodTypeDTO> update(@Valid @RequestBody FoodTypeDTO foodTypeDTO) {
        try {
            return new ResponseEntity<>(foodTypeService.update(foodTypeDTO), HttpStatus.OK);
        } catch (RestException e) {
            return e.getRestfulResponse();
        }
    }
}