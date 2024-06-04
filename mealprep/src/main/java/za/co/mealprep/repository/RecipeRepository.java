package za.co.mealprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.mealprep.entities.Recipe;
import za.co.mealprep.pojo.MealRotation;
import za.co.mealprep.pojo.MealType;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByName(String name);
    List<Recipe> findAllByInRotationAndMealType(MealRotation mealRotation, MealType mealType);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Recipe r set r.inRotation = :rotationTo where r.inRotation = :rotationFrom")
    int updateRotation(@Param(value = "rotationTo") MealRotation rotationFrom, @Param(value = "rotationFrom") MealRotation rotationTo);
}
