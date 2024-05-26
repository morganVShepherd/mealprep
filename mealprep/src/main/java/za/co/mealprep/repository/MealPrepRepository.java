package za.co.mealprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.mealprep.entities.MealPrep;
import za.co.mealprep.entities.Recipe;
import za.co.mealprep.pojo.MealRotation;
import za.co.mealprep.pojo.MealType;

import java.util.List;

@Repository
public interface MealPrepRepository extends JpaRepository<MealPrep, Long> {

}
