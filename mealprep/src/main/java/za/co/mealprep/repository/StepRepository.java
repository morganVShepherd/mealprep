package za.co.mealprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.mealprep.entities.Step;

import java.util.List;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {

    List<Step> findAllByRecipeIdOrderByStepNoAsc(Long recipeId);

    void deleteAllByRecipeId(long recipeId);
}
