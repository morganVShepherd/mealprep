package za.co.mealprep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekDataItem {

    private String day;
    private String name;
    private String id;
}
