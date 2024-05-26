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
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.pojo.MealRotation;
import za.co.mealprep.pojo.MealType;
import za.co.mealprep.utils.IdConverter;

@Entity(name = "recipe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_seq")
    @SequenceGenerator(name = "recipe_seq", sequenceName = "recipe_seq", allocationSize = 1)
    private Long id;
    @Column
    private int kcal;
    @Column
    private int servingSize;
    @Column
    private String notes;
    @Column
    private String name;
    @Column
    private int rating;
    @Column
    @Enumerated(EnumType.STRING)
    private MealType mealType;
    @Column(name = "IN_ROTATION")
    @Enumerated(EnumType.STRING)
    private MealRotation inRotation;

    public Recipe(RecipeDTO recipeDTO){
        this.id = IdConverter.convertId(recipeDTO.getId());
        this.servingSize = recipeDTO.getServingSize();
        this.notes= recipeDTO.getNotes();
        this.name = recipeDTO.getName();
        this.rating = recipeDTO.getRating();
        this.mealType = recipeDTO.getMealType();
        this.inRotation = recipeDTO.getMealRotation();
        this.kcal = recipeDTO.getKcal();
    }
}