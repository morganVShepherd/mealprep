package za.co.mealprep.controllers;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import za.co.mealprep.dto.RecipeDTO;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.pojo.MealRotation;
import za.co.mealprep.pojo.MealType;
import za.co.mealprep.service.RecipeService;
import za.co.mealprep.utils.TestConstants;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("Contract-Test")
public class RecipeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecipeService recipeService;

    @Test
    public void getAll_success() throws Exception {
        mvc.perform(get("/recipe/all")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }

    @Test
    public void getById_success() throws Exception {
        when(recipeService.getById(any())).thenReturn(TestConstants.generateRecipeDTO());
        mvc.perform(get("/recipe/by-id?id=test")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }

    @Test
    public void getByNoParam_fail() throws Exception {
        mvc.perform(get("/recipe/by-id")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getByWrongParamName_fail() throws Exception {
        mvc.perform(get("/recipe/by-id?value=test")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getByParamTooLong_fail() throws Exception {
        mvc.perform(get("/recipe/by-id?id=" + TestConstants.LONG_STRING(255))
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void create_success() throws Exception {
        RecipeDTO reqObj = TestConstants.generateRecipeDTO();
        String jsonString = TestConstants.asJsonString(reqObj);
        when(recipeService.create(any())).thenReturn(TestConstants.generateRecipeDTO());
        mvc.perform(post("/recipe/create")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(TestConstants.GENERIC_NAME))
                .andExpect(jsonPath("$.id").value(TestConstants.VALID_S_ID))
                .andExpect(jsonPath("$.kcal").value(TestConstants.GENERIC_QUANTITY))
                .andExpect(jsonPath("$.servingSize").value(TestConstants.GENERIC_QUANTITY))
                .andExpect(jsonPath("$.rating").value(TestConstants.GENERIC_QUANTITY))
                .andExpect(jsonPath("$.notes").value(TestConstants.GENERIC_DETAILS))
                .andExpect(jsonPath("$.mealType").value(MealType.DINNER.toString()))
                .andExpect(jsonPath("$.mealRotation").value(MealRotation.IN.toString()))
                .andExpect(jsonPath("$.ingredients").exists())
                .andExpect(jsonPath("$.ingredients").isNotEmpty())
                .andExpect(jsonPath("$.steps").exists())
                .andExpect(jsonPath("$.steps").isNotEmpty())
                .andReturn();
    }

    @Test
    public void createMissingFields_fail() throws Exception {
        RecipeDTO reqObj = TestConstants.generateRecipeDTO();
        reqObj.setName(null);
        reqObj.setNotes(null);
        reqObj.setMealType(null);
        reqObj.setMealRotation(null);
        String jsonString = TestConstants.asJsonString(reqObj);
        when(recipeService.create(any())).thenReturn(TestConstants.generateRecipeDTO());
        mvc.perform(post("/recipe/create")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(ErrorConstants.CANNOT_BE_EMPTY))
                .andExpect(jsonPath("$.notes").value(ErrorConstants.CANNOT_BE_EMPTY))
                .andExpect(jsonPath("$.mealType").value(ErrorConstants.CANNOT_BE_EMPTY))
                .andExpect(jsonPath("$.mealRotation").value(ErrorConstants.CANNOT_BE_EMPTY))
                .andReturn();
    }
}