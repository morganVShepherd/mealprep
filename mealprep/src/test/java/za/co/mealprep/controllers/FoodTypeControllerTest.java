package za.co.mealprep.controllers;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import za.co.mealprep.dto.FoodTypeDTO;
import za.co.mealprep.exception.ErrorConstants;
import za.co.mealprep.service.FoodTypeService;
import za.co.mealprep.utils.TestConstants;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("Contract-Test")
public class FoodTypeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FoodTypeService foodTypeService;

    @Test
    public void getAll_success() throws Exception {
        mvc.perform(get("/food-type/all")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }

    @Test
    public void getByExisting_success() throws Exception {
        mvc.perform(get("/food-type/check-exists?name=test")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }

    @Test
    public void getByNoParam_fail() throws Exception {
        mvc.perform(get("/food-type/check-exists")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getByWrongParamName_fail() throws Exception {
        mvc.perform(get("/food-type/check-exists?id=test")
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getByParamTooLong_fail() throws Exception {
        mvc.perform(get("/food-type/check-exists?id=" + TestConstants.LONG_STRING(255))
                        .headers(TestConstants.defaultHeaders()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void create_success() throws Exception {
        FoodTypeDTO reqObj = TestConstants.generateFoodTypeDTO();
        reqObj.setId(null);
        String jsonString = TestConstants.asJsonString(reqObj);
        when(foodTypeService.create(any())).thenReturn(TestConstants.generateFoodTypeDTO());
        mvc.perform(post("/food-type/create")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(TestConstants.GENERIC_NAME))
                .andExpect(jsonPath("$.id").value(TestConstants.VALID_S_ID))
                .andReturn();
    }

    @Test
    public void createNullName_fail() throws Exception {
        FoodTypeDTO reqObj = TestConstants.generateFoodTypeDTO();
        reqObj.setName(null);
        String jsonString = TestConstants.asJsonString(reqObj);
        mvc.perform(post("/food-type/create")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(ErrorConstants.CANNOT_BE_EMPTY))
                .andReturn();
    }

    @Test
    public void createEmptyName_fail() throws Exception {
        FoodTypeDTO reqObj = TestConstants.generateFoodTypeDTO();
        reqObj.setName("");
        String jsonString = TestConstants.asJsonString(reqObj);
        mvc.perform(post("/food-type/create")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(ErrorConstants.CANNOT_BE_EMPTY))
                .andReturn();
    }

    @Test
    public void createLongName_fail() throws Exception {
        FoodTypeDTO reqObj = TestConstants.generateFoodTypeDTO();
        reqObj.setName(TestConstants.LONG_STRING(255));
        String jsonString = TestConstants.asJsonString(reqObj);
        mvc.perform(post("/food-type/create")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(ErrorConstants.INCORRECT_LENGTH))
                .andReturn();
    }

    @Test
    public void update_success() throws Exception {
        FoodTypeDTO reqObj = TestConstants.generateFoodTypeDTO();
        String jsonString = TestConstants.asJsonString(reqObj);
        when(foodTypeService.update(any())).thenReturn(TestConstants.generateFoodTypeDTO());
        mvc.perform(put("/food-type/update")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(TestConstants.GENERIC_NAME))
                .andExpect(jsonPath("$.id").value(TestConstants.VALID_S_ID))
                .andReturn();
    }

    @Test
    public void updateNullName_fail() throws Exception {
        FoodTypeDTO reqObj = TestConstants.generateFoodTypeDTO();
        reqObj.setName(null);
        String jsonString = TestConstants.asJsonString(reqObj);
        mvc.perform(put("/food-type/update")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(ErrorConstants.CANNOT_BE_EMPTY))
                .andReturn();
    }

    @Test
    public void updateEmptyName_fail() throws Exception {
        FoodTypeDTO reqObj = TestConstants.generateFoodTypeDTO();
        reqObj.setName("");
        String jsonString = TestConstants.asJsonString(reqObj);
        mvc.perform(put("/food-type/update")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(ErrorConstants.CANNOT_BE_EMPTY))
                .andReturn();
    }

    @Test
    public void updateLongName_fail() throws Exception {
        FoodTypeDTO reqObj = TestConstants.generateFoodTypeDTO();
        reqObj.setName(TestConstants.LONG_STRING(255));
        String jsonString = TestConstants.asJsonString(reqObj);
        mvc.perform(put("/food-type/update")
                        .headers(TestConstants.defaultHeaders())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(ErrorConstants.INCORRECT_LENGTH))
                .andReturn();
    }
}