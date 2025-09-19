package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.services.FruitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FruitController.class)
public class FruitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FruitService fruitService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- Tests para crear una fruta (POST) ---
    @Test
    void testCreateFruit_shouldReturn201Created_whenFruitIsValid() throws Exception {
        // GIVEN: Un objeto Fruit válido
        Fruit fruit = new Fruit("Apple", 20);

        // WHEN: El servicio mockeado guarda la fruta
        when(fruitService.createFruit(any(Fruit.class))).thenReturn(fruit);

        // THEN: Se realiza la petición POST y se valida la respuesta
        mockMvc.perform(post("/fruits-rest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.quantityKilos").value(20));

        verify(fruitService, times(1)).createFruit(any(Fruit.class));
    }

    @Test
    void testCreateFruit_shouldReturn400BadRequest_whenFruitIsInvalid() throws Exception {
        // GIVEN: Un objeto Fruit inválido (nombre en blanco)
        Fruit invalidFruit = new Fruit("", 0);

        // WHEN: Se realiza la petición POST con el objeto inválido
        // THEN: Se valida la respuesta 400 Bad Request
        mockMvc.perform(post("/fruits-rest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFruit)))
                .andExpect(status().isBadRequest());

        // Verificamos que el servicio nunca fue llamado
        verify(fruitService, never()).createFruit(any(Fruit.class));
    }

    // --- Tests para obtener todas las frutas (GET) ---
    @Test
    void testGetAllFruits_shouldReturn200Ok_whenListIsNotEmpty() throws Exception {
        // GIVEN: Una lista de frutas
        List<Fruit> fruitList = Arrays.asList(new Fruit("Apple", 10), new Fruit("Banana", 15));

        // WHEN: El servicio mockeado devuelve la lista
        when(fruitService.list()).thenReturn(fruitList);

        // THEN: Se realiza la petición GET y se valida la respuesta
        mockMvc.perform(get("/fruits-rest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].quantityKilos").value(15));
    }

    // --- Tests para obtener una fruta por ID (GET) ---
    @Test
    void testGetFruitById_shouldReturn200Ok_whenFruitExists() throws Exception {
        // GIVEN: Una fruta existente con ID 1
        Fruit fruit = new Fruit("Apple", 10);
        fruit.setId(1);

        // WHEN: El servicio mockeado devuelve un Optional con la fruta
        when(fruitService.getFruitById(1)).thenReturn(fruit);

        // THEN: Se realiza la petición GET y se valida la respuesta
        mockMvc.perform(get("/fruits-rest/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void testGetFruitById_shouldReturn404NotFound_whenFruitDoesNotExist() throws Exception {
        // GIVEN: Un ID que no existe
        int nonExistentId = 99;

        // WHEN: El servicio mockeado devuelve null
        when(fruitService.getFruitById(nonExistentId)).thenReturn(null);

        // THEN: Se realiza la petición GET y se valida la respuesta
        mockMvc.perform(get("/fruits-rest/" + nonExistentId))
                .andExpect(status().isNotFound());
    }

    // --- Tests para actualizar una fruta (PUT) ---
    @Test
    void testUpdateFruit_shouldReturn200Ok_whenFruitExistsAndIsValid() throws Exception {
        // GIVEN: Una fruta existente a actualizar
        int id = 1;
        Fruit updatedFruit = new Fruit("Kiwi", 5);
        updatedFruit.setId(id);

        // WHEN: El servicio mockeado devuelve la fruta actualizada
        when(fruitService.updateFruit(eq(id), any(Fruit.class))).thenReturn(updatedFruit);

        // THEN: Se realiza la petición PUT y se valida la respuesta
        mockMvc.perform(put("/fruits-rest/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFruit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kiwi"));

        verify(fruitService, times(1)).updateFruit(eq(id), any(Fruit.class));
    }

    @Test
    void testUpdateFruit_shouldReturn404NotFound_whenFruitDoesNotExist() throws Exception {
        // GIVEN: Una fruta con un ID que no existe
        int nonExistentId = 99;
        Fruit fruitToUpdate = new Fruit("Kiwi", 5);

        // WHEN: El servicio mockeado devuelve null (fruta no encontrada)
        when(fruitService.updateFruit(eq(nonExistentId), any(Fruit.class))).thenReturn(null);

        // THEN: Se realiza la petición PUT y se valida la respuesta
        mockMvc.perform(put("/fruits-rest/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruitToUpdate)))
                .andExpect(status().isNotFound());

        verify(fruitService, times(1)).updateFruit(eq(nonExistentId), any(Fruit.class));
    }

    // --- Tests para borrar una fruta (DELETE) ---
    @Test
    void testDeleteFruit_shouldReturn204NoContent() throws Exception {
        // WHEN: Se realiza la petición DELETE
        doNothing().when(fruitService).deleteFruit(1);

        // THEN: Se valida la respuesta 204 No Content
        mockMvc.perform(delete("/fruits-rest/1"))
                .andExpect(status().isNoContent());

        verify(fruitService, times(1)).deleteFruit(1);
    }
}
