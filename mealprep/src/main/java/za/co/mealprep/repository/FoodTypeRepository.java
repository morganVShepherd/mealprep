package za.co.mealprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.mealprep.entities.FoodType;

import java.util.List;

@Repository
public interface FoodTypeRepository extends JpaRepository<FoodType, Long> {

    List<FoodType> findByName(String name);
    List<FoodType> findByNameContaining(String name);
}
