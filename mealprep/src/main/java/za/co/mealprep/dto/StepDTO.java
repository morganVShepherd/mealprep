package za.co.mealprep.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.mealprep.entities.Step;
import za.co.mealprep.utils.IdConverter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepDTO {
    private String id;
    @Positive(message = "positive")
    private int stepNo;
    @NotNull(message = "notNull")
    @NotEmpty(message = "notEmpty")
    @Size(min = 0, max = 1024, message = "size")
    private String details;
    private String recipeId;

    public StepDTO(Step step){
        this.id = IdConverter.convertId(step.getId());
        this.stepNo = step.getStepNo();
        this.recipeId = IdConverter.convertId(step.getRecipeId());
        this.setDetails(step.getDetails());
    }
}
