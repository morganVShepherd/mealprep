package za.co.mealprep.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.utils.IdConverter;

@Entity(name = "food_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_type_seq")
    @SequenceGenerator(name = "food_type_seq", sequenceName = "food_type_seq", allocationSize = 1)
    private Long id;
    @Column
    private String name;

    public FoodType(FoodTypeDTO foodType){
        this.id = IdConverter.convertId(foodType.getId());
        this.name = foodType.getName().toLowerCase();
    }
}