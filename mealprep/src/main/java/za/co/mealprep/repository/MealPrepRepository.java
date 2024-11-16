package za.co.mealprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.mealprep.entities.MealPrep;

import java.util.Optional;

@Repository
public interface MealPrepRepository extends JpaRepository<MealPrep, Long> {

    Optional<MealPrep> findFirstByOrderByIdDesc();
}
