package za.co.mealprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.mealprep.entities.Recipe;
import za.co.mealprep.pojo.MealRotation;
import za.co.mealprep.pojo.MealType;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByName(String name);
    List<Recipe> findAllByInRotationAndMealType(MealRotation mealRotation, MealType mealType);

    //@Modifying
    //@Query("update Recipe r set r.mealRotation = :mealRotationTo where r.mealRotation = :mealRotationFrom")
    //void changeMealRotation(@Param("mealRotationTo") MealRotation mealRotationTo,@Param("mealRotationFrom") MealRotation mealRotationFrom);

}
