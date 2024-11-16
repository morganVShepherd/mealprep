package za.co.mealprep.controllers;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import za.co.mealprep.service.MealPrepService;
import za.co.mealprep.utils.TestConstants;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .andExpect(jsonPath("$.weeksRecipes.Monday").exists())
                .andExpect(jsonPath("$.weeksRecipes.Tuesday").exists())
                .andExpect(jsonPath("$.weeksRecipes.Wednesday").exists())
                .andExpect(jsonPath("$.weeksRecipes.Thursday").exists())
                .andExpect(jsonPath("$.weeksRecipes.Friday").exists())
                .andExpect(jsonPath("$.weeksRecipes.Saturday").exists())
                .andExpect(jsonPath("$.weeksRecipes.Sunday").exists())
                .andReturn();
    }

    @Test
    public void getShoppingListByNoParam() throws Exception {
        int items = 10;
        when(mealPrepService.generateShoppingList()).thenReturn(TestConstants.generateShoppingList(items));
        mvc.perform(get("/get-shopping-list")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getWhatsAppMessage() throws Exception {
        when(mealPrepService.generateWhatsAppList()).thenReturn("whatsapp");
        mvc.perform(get("/get-whastapp-message")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getWeesData() throws Exception {
        int items = 10;
        when(mealPrepService.generateWeekView()).thenReturn(TestConstants.generateWeekView(items));
        mvc.perform(get("/get-weeks-data")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isOk())
                .andReturn();
    }
}