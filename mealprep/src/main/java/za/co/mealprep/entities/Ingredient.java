package za.co.mealprep.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.mealprep.dto.IngredientDTO;
import za.co.mealprep.pojo.IngredientType;
import za.co.mealprep.pojo.Metric;
import za.co.mealprep.utils.IdConverter;
@Entity(name = "ingredient")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_seq")
    @SequenceGenerator(name = "ingredient_seq", sequenceName = "ingredient_seq", allocationSize = 1)
    private Long id;
    @Column
    private long quantity;
    @Column
    @Enumerated(EnumType.STRING)
    private Metric metric;
    @Column
    private Long foodTypeId;
    @Column
    private Long recipeId;
    @Column
    @Enumerated(EnumType.STRING)
    private IngredientType ingredientType;

    public Ingredient(IngredientDTO ingredient){
        this.id = IdConverter.convertId(ingredient.getId());
        this.recipeId = IdConverter.convertId(ingredient.getRecipeId());
        this.quantity = ingredient.getQuantity();
        this.metric= ingredient.getMetric();
        this.foodTypeId = IdConverter.convertId(ingredient.getFoodTypeDTO().getId());
        this.ingredientType = ingredient.getIngredientType();
    }
}