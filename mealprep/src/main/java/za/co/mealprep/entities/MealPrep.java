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
    @Column
    private Long satBreakId;
    @Column
    private Long sunBreakId;

    public MealPrep (MealPrepDTO mealPrepDTO) {
        this.id = IdConverter.convertId(mealPrepDTO.getId());
        if(mealPrepDTO.getMonDinner()!=null)
        this.monDinnerId = IdConverter.convertId(mealPrepDTO.getMonDinner().getId());
        if(mealPrepDTO.getTuesDinner()!=null)
        this.tuesDinnerId = IdConverter.convertId(mealPrepDTO.getTuesDinner().getId());
        if(mealPrepDTO.getWedDinner()!=null)
        this.wedDinnerId = IdConverter.convertId(mealPrepDTO.getWedDinner().getId());
        if(mealPrepDTO.getThursDinner()!=null)
        this.thursDinnerId = IdConverter.convertId(mealPrepDTO.getThursDinner().getId());
        if(mealPrepDTO.getFriDinner()!=null)
        this.friDinnerId = IdConverter.convertId(mealPrepDTO.getFriDinner().getId());
        if(mealPrepDTO.getSatDinner()!=null)
        this.satDinnerId = IdConverter.convertId(mealPrepDTO.getSatDinner().getId());
        if(mealPrepDTO.getSunDinner()!=null)
        this.sunDinnerId = IdConverter.convertId(mealPrepDTO.getSunDinner().getId());
        if(mealPrepDTO.getSatBreak()!=null)
        this.satBreakId = IdConverter.convertId(mealPrepDTO.getSatBreak().getId());
        if(mealPrepDTO.getSunBreak()!=null)
        this.sunBreakId = IdConverter.convertId(mealPrepDTO.getSunBreak().getId());
    }
}