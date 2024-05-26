package za.co.mealprep.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.mealprep.entities.FoodType;
import za.co.mealprep.utils.IdConverter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodTypeDTO {
    String id;
    @NotNull(message = "notNull")
    @NotEmpty(message = "notEmpty")
    @Size(min = 0, max = 255, message = "size")
    String name;

    public FoodTypeDTO(FoodType foodType) {
        this.id = IdConverter.convertId(foodType.getId());
        this.name = foodType.getName().toLowerCase();
    }
}