package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.services.FruitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FruitController.class)
public class FruitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FruitService fruitService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateFruit_shouldReturn201Created_whenFruitIsValid() throws Exception {

        Fruit fruit = new Fruit("Apple", 20);

        when(fruitService.createFruit(any(Fruit.class))).thenReturn(fruit);

        mockMvc.perform(post("/api/v1/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.quantityKilos").value(20));

        verify(fruitService, times(1)).createFruit(any(Fruit.class));
    }

    @Test
    void testCreateFruit_shouldReturn400BadRequest_whenFruitIsInvalid() throws Exception {

        Fruit invalidFruit = new Fruit("", 0);

        mockMvc.perform(post("/api/v1/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFruit)))
                .andExpect(status().isBadRequest());

        verify(fruitService, never()).createFruit(any(Fruit.class));
    }

    @Test
    void testGetAllFruits_shouldReturn200Ok_whenListIsNotEmpty() throws Exception {

        List<Fruit> fruitList = Arrays.asList(new Fruit("Apple", 10), new Fruit("Banana", 15));

        when(fruitService.list()).thenReturn(fruitList);

        mockMvc.perform(get("/api/v1/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].quantityKilos").value(15));
    }

    @Test
    void testGetFruitById_shouldReturn200Ok_whenFruitExists() throws Exception {

        Fruit fruit = new Fruit("Apple", 10);
        fruit.setId(1);

        when(fruitService.getFruitById(1)).thenReturn(fruit);

        mockMvc.perform(get("/api/v1/fruits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void testGetFruitById_shouldReturn404NotFound_whenFruitDoesNotExist() throws Exception {

        int nonExistentId = 99;

        when(fruitService.getFruitById(nonExistentId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/fruits/" + nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFruit_shouldReturn200Ok_whenFruitExistsAndIsValid() throws Exception {

        int id = 1;
        Fruit updatedFruit = new Fruit("Kiwi", 5);
        updatedFruit.setId(id);

        when(fruitService.updateFruit(eq(id), any(Fruit.class))).thenReturn(updatedFruit);

        mockMvc.perform(put("/api/v1/fruits/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFruit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kiwi"));

        verify(fruitService, times(1)).updateFruit(eq(id), any(Fruit.class));
    }

    @Test
    void testUpdateFruit_shouldReturn404NotFound_whenFruitDoesNotExist() throws Exception {

        int nonExistentId = 99;
        Fruit fruitToUpdate = new Fruit("Kiwi", 5);

        when(fruitService.updateFruit(eq(nonExistentId), any(Fruit.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/fruits/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruitToUpdate)))
                .andExpect(status().isNotFound());

        verify(fruitService, times(1)).updateFruit(eq(nonExistentId), any(Fruit.class));
    }

    @Test
    void testDeleteFruit_shouldReturn204NoContent() throws Exception {

        doNothing().when(fruitService).deleteFruit(1);

        mockMvc.perform(delete("/api/v1/fruits/1"))
                .andExpect(status().isNoContent());

        verify(fruitService, times(1)).deleteFruit(1);
    }
}
