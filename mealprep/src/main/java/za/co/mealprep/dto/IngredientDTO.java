package za.co.mealprep.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.mealprep.entities.Ingredient;
import za.co.mealprep.pojo.IngredientType;
import za.co.mealprep.pojo.Metric;
import za.co.mealprep.utils.IdConverter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    private String id;
    private String recipeId;
    @NotNull(message = "notNull")
    @NotEmpty(message = "notEmpty")
    @Positive(message = "positive")
    private long quantity;
    @NotNull(message = "notNull")
    @NotEmpty(message = "notEmpty")
    @Size(min = 0, max = 255, message = "size")
    private Metric metric;
    @Valid
    private FoodTypeDTO foodTypeDTO;
    @NotNull(message = "notNull")
    @NotEmpty(message = "notEmpty")
    @Size(min = 0, max = 255, message = "size")
    private IngredientType ingredientType;

    public IngredientDTO(Ingredient ingredient, FoodTypeDTO foodTypeDTO) {
        this.id = IdConverter.convertId(ingredient.getId());
        this.recipeId = IdConverter.convertId(ingredient.getRecipeId());
        this.quantity = ingredient.getQuantity();
        this.metric = ingredient.getMetric();
        this.foodTypeDTO = foodTypeDTO;
        this.ingredientType = ingredient.getIngredientType();
    }
}