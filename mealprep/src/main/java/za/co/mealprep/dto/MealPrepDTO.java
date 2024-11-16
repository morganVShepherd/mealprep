package za.co.mealprep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealPrepDTO {
    private String Id;
    private Map<String, RecipeDTO> weeksRecipes = new HashMap<>();
}
