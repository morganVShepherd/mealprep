package za.co.mealprep.controllers;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import za.co.mealprep.dto.MealPrepDTO;
import za.co.mealprep.service.MealPrepService;
import za.co.mealprep.utils.TestConstants;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("Contract-Test")
public class PrepareMealControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MealPrepService mealPrepService;

    @Test
    public void prepareMeal_success() throws Exception {
        when(mealPrepService.generateWeeklyMealPrep()).thenReturn(TestConstants.generateMealPrepDTO());
        mvc.perform(get("/prepare-weeks-meals")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.monDinner").exists())
                .andExpect(jsonPath("$.tuesDinner").exists())
                .andExpect(jsonPath("$.wedDinner").exists())
                .andExpect(jsonPath("$.thursDinner").exists())
                .andExpect(jsonPath("$.friDinner").exists())
                .andExpect(jsonPath("$.satDinner").exists())
                .andExpect(jsonPath("$.sunDinner").exists())
                .andExpect(jsonPath("$.satBreak").exists())
                .andExpect(jsonPath("$.sunBreak").exists())
                .andReturn();
    }

    @Test
    public void getShoppingList_success() throws Exception {
        when(mealPrepService.generateShoppingList(any())).thenReturn("list");
        mvc.perform(get("/get-shopping-list?mealPrepId=test")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getShoppingListByNoParam_fail() throws Exception {
        mvc.perform(get("/get-shopping-list")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getShoppingListWrongParamName_fail() throws Exception {
        mvc.perform(get("/get-shopping-list?id=test")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getShoppingListParamTooLong_fail() throws Exception {
        mvc.perform(get("/get-shopping-list?mealPrepId=" + TestConstants.LONG_STRING(255))
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updateAll_success() throws Exception {
        MealPrepDTO reqObj = TestConstants.generateMealPrepDTO();
        String jsonString = TestConstants.asJsonString(reqObj);
        when(mealPrepService.updateWeeklyMealPrep(any())).thenReturn(TestConstants.generateMealPrepDTO());
        mvc.perform(put("/update-weeks-meals")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.monDinner").exists())
                .andExpect(jsonPath("$.tuesDinner").exists())
                .andExpect(jsonPath("$.wedDinner").exists())
                .andExpect(jsonPath("$.thursDinner").exists())
                .andExpect(jsonPath("$.friDinner").exists())
                .andExpect(jsonPath("$.satDinner").exists())
                .andExpect(jsonPath("$.sunDinner").exists())
                .andExpect(jsonPath("$.satBreak").exists())
                .andExpect(jsonPath("$.sunBreak").exists())
                .andReturn();
    }

    @Test
    public void updateAllSomeNulls_success() throws Exception {
        MealPrepDTO reqObj = TestConstants.generateMealPrepDTO();
        reqObj.setMonDinner(null);
        reqObj.setTuesDinner(null);
        reqObj.setWedDinner(null);
        reqObj.setThursDinner(null);
        reqObj.setSunBreak(null);
        String jsonString = TestConstants.asJsonString(reqObj);
        when(mealPrepService.updateWeeklyMealPrep(any())).thenReturn(TestConstants.generateMealPrepDTO());
        mvc.perform(put("/update-weeks-meals")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.monDinner").exists())
                .andExpect(jsonPath("$.tuesDinner").exists())
                .andExpect(jsonPath("$.wedDinner").exists())
                .andExpect(jsonPath("$.thursDinner").exists())
                .andExpect(jsonPath("$.friDinner").exists())
                .andExpect(jsonPath("$.satDinner").exists())
                .andExpect(jsonPath("$.sunDinner").exists())
                .andExpect(jsonPath("$.satBreak").exists())
                .andExpect(jsonPath("$.sunBreak").exists())
                .andReturn();
    }
}