package za.co.mealprep.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.mealprep.entities.Recipe;
import za.co.mealprep.pojo.MealRotation;
import za.co.mealprep.pojo.MealType;
import za.co.mealprep.utils.IdConverter;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {
    private String id;
    @Valid
    private List<IngredientDTO> ingredients;
    @Valid
    private List<StepDTO> steps;
    @Positive(message = "positive")
    private int kcal;
    @Positive(message = "positive")
    private int servingSize;
    @NotNull(message = "notNull")
    @NotEmpty(message = "notEmpty")
    @Size(min = 0, max = 255, message = "size")
    private String notes;
    @NotNull(message = "notNull")
    @NotEmpty(message = "notEmpty")
    @Size(min = 0, max = 255, message = "size")
    private String name;
    @Positive(message = "positive")
    private int rating;
    @NotNull(message = "notNull")
    private MealType mealType;
    @NotNull(message = "notNull")
    private MealRotation mealRotation;

    public RecipeDTO(Recipe recipe){
        this.id = IdConverter.convertId(recipe.getId());
        this.servingSize = recipe.getServingSize();
        this.notes= recipe.getNotes();
        this.name = recipe.getName();
        this.rating = recipe.getRating();
        this.mealType = recipe.getMealType();
        this.mealRotation = recipe.getInRotation();
        this.kcal = recipe.getKcal();
    }

    public RecipeDTO(Recipe recipe, List<StepDTO> steps, List<IngredientDTO> ingredients){
        this.id = IdConverter.convertId(recipe.getId());
        this.servingSize = recipe.getServingSize();
        this.notes= recipe.getNotes();
        this.name = recipe.getName();
        this.rating = recipe.getRating();
        this.mealType = recipe.getMealType();
        this.mealRotation = recipe.getInRotation();
        this.setSteps(steps);
        this.setIngredients(ingredients);
        this.kcal = recipe.getKcal();
    }
}
