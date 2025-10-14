package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.dto.FruitRequest;
import cat.itacademy.s04.t02.n01.dto.FruitResponse;
import cat.itacademy.s04.t02.n01.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.services.FruitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    private final FruitRequest validRequest = new FruitRequest("Apple", 20);
    private final FruitResponse createdResponse = new FruitResponse(1L, "Apple", 20);

    @Test
    void testCreateFruit_shouldReturn201Created_whenFruitIsValid() throws Exception {
        when(fruitService.createFruit(any(FruitRequest.class))).thenReturn(createdResponse);

        mockMvc.perform(post("/api/v1/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.quantityKilos").value(20));

        verify(fruitService, times(1)).createFruit(any(FruitRequest.class));
    }

    @Test
    void testCreateFruit_shouldReturn400BadRequest_whenFruitIsInvalid() throws Exception {
        FruitRequest invalidRequest = new FruitRequest("", 0);

        mockMvc.perform(post("/api/v1/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(fruitService, never()).createFruit(any(FruitRequest.class));
    }

    @Test
    void testGetAllFruits_shouldReturn200Ok_whenListIsNotEmpty() throws Exception {
        FruitResponse res1 = new FruitResponse(1L, "Apple", 10);
        FruitResponse res2 = new FruitResponse(2L, "Banana", 15);
        List<FruitResponse> content = List.of(res1, res2);
        PageImpl<FruitResponse> fruitPage = new PageImpl<>(content);

        when(fruitService.getAllFruits(any(Pageable.class))).thenReturn(fruitPage);

        mockMvc.perform(get("/api/v1/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Apple"))
                .andExpect(jsonPath("$.content[1].quantityKilos").value(15));

        verify(fruitService, times(1)).getAllFruits(any(Pageable.class));
    }

    @Test
    void testGetFruitById_shouldReturn200Ok_whenFruitExists() throws Exception {
        Long id = 1L;
        FruitResponse response = new FruitResponse(id, "Apple", 10);

        when(fruitService.getFruitById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/fruits/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));

        verify(fruitService, times(1)).getFruitById(id);
    }

    @Test
    void testGetFruitById_shouldReturn404NotFound_whenFruitDoesNotExist() throws Exception {
        Long nonExistentId = 99L;

        doThrow(new ResourceNotFoundException("Fruit not found")).when(fruitService).getFruitById(nonExistentId);

        mockMvc.perform(get("/api/v1/fruits/{id}", nonExistentId))
                .andExpect(status().isNotFound());

        verify(fruitService, times(1)).getFruitById(nonExistentId);
    }

    @Test
    void testUpdateFruit_shouldReturn200Ok_whenFruitExistsAndIsValid() throws Exception {
        Long id = 1L;
        FruitRequest updateRequest = new FruitRequest("Kiwi", 5);
        FruitResponse updatedResponse = new FruitResponse(id, "Kiwi", 5);

        when(fruitService.updateFruit(eq(id), any(FruitRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/fruits/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kiwi"));

        verify(fruitService, times(1)).updateFruit(eq(id), any(FruitRequest.class));
    }

    @Test
    void testUpdateFruit_shouldReturn404NotFound_whenFruitDoesNotExist() throws Exception {
        Long nonExistentId = 99L;
        FruitRequest updateRequest = new FruitRequest("Kiwi", 5);

        doThrow(new ResourceNotFoundException("Fruit not found")).when(fruitService).updateFruit(eq(nonExistentId), any(FruitRequest.class));

        mockMvc.perform(put("/api/v1/fruits/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(fruitService, times(1)).updateFruit(eq(nonExistentId), any(FruitRequest.class));
    }

    @Test
    void testDeleteFruit_shouldReturn204NoContent() throws Exception {
        Long id = 1L;

        doNothing().when(fruitService).deleteFruit(id);

        mockMvc.perform(delete("/api/v1/fruits/{id}", id))
                .andExpect(status().isNoContent());

        verify(fruitService, times(1)).deleteFruit(id);
    }
}