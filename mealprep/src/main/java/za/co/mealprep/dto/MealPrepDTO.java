package za.co.mealprep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealPrepDTO {
    private String Id;
    private RecipeDTO monDinner;
    private RecipeDTO tuesDinner;
    private RecipeDTO wedDinner;
    private RecipeDTO thursDinner;
    private RecipeDTO friDinner;
    private RecipeDTO satDinner;
    private RecipeDTO sunDinner;
    private RecipeDTO weekBreakfast;
    private RecipeDTO satBreak;
    private RecipeDTO sunBreak;
}
