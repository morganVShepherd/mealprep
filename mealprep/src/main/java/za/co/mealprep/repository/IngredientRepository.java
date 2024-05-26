package za.co.mealprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.mealprep.entities.FoodType;
import za.co.mealprep.entities.Ingredient;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    void deleteAllByRecipeId(Long recipeId);
    List<Ingredient> findAllByRecipeId(Long recipeId);
}
