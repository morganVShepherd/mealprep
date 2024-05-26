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
import za.co.mealprep.dto.StepDTO;
import za.co.mealprep.utils.IdConverter;

@Entity(name = "step")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "step_seq")
    @SequenceGenerator(name = "step_seq", sequenceName = "step_seq", allocationSize = 1)
    private Long id;
    @Column
    private int stepNo;

    @Column
    private Long recipeId;

    @Column
    private String details;

    public Step(StepDTO stepDTO){
        this.id = IdConverter.convertId(stepDTO.getId());
        this.stepNo = stepDTO.getStepNo();
        this.recipeId = IdConverter.convertId(stepDTO.getRecipeId());
        this.setDetails(stepDTO.getDetails());
    }
}