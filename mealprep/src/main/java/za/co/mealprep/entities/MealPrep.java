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
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.pojo.DayOfWeekRef;
import za.co.mealprep.utils.IdConverter;

@Entity(name = "meal_prep")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealPrep {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meal_prep_seq")
    @SequenceGenerator(name = "meal_prep_seq", sequenceName = "meal_prep_seq", allocationSize = 1)
    private Long id;
    @Column
    private Long monDinnerId;
    @Column
    private Long tuesDinnerId;
    @Column
    private Long wedDinnerId;
    @Column
    private Long thursDinnerId;
    @Column
    private Long friDinnerId;
    @Column
    private Long satDinnerId;
    @Column
    private Long sunDinnerId;

    public MealPrep(MealPrepDTO mealPrepDTO) {
        this.id = IdConverter.convertId(mealPrepDTO.getId());
        if (mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY1.getDayOfWeekName()) != null)
            this.monDinnerId = IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY1.getDayOfWeekName()).getId());
        if (mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY2.getDayOfWeekName()) != null)
            this.tuesDinnerId = IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY2.getDayOfWeekName()).getId());
        if (mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY3.getDayOfWeekName()) != null)
            this.wedDinnerId = IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY3.getDayOfWeekName()).getId());
        if (mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY4.getDayOfWeekName()) != null)
            this.thursDinnerId = IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY4.getDayOfWeekName()).getId());
        if (mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY5.getDayOfWeekName()) != null)
            this.friDinnerId = IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY5.getDayOfWeekName()).getId());
        if (mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY6.getDayOfWeekName()) != null)
            this.satDinnerId = IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY6.getDayOfWeekName()).getId());
        if (mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY7.getDayOfWeekName()) != null)
            this.sunDinnerId = IdConverter.convertId(mealPrepDTO.getWeeksRecipes().get(DayOfWeekRef.DAY7.getDayOfWeekName()).getId());
    }
}