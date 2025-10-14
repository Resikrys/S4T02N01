package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.dto.FruitRequest;
import cat.itacademy.s04.t02.n01.dto.FruitResponse;
import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponse<T> {
    private T[] content;
    public T[] getContent() { return content; }
    public void setContent(T[] content) { this.content = content; }
}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FruitIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FruitRepository fruitRepository;

    @AfterEach
    public void cleanup() {
        fruitRepository.deleteAll();
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1/fruits";
    }

    @Test
    void testCreateFruit_shouldReturn201Created() {
        FruitRequest newRequest = new FruitRequest("Apple", 15);

        ResponseEntity<FruitResponse> response = restTemplate.postForEntity(
                getBaseUrl(),
                newRequest,
                FruitResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Apple");
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(fruitRepository.count()).isEqualTo(1);
    }

    @Test
    void testCreateFruit_shouldReturn400BadRequest_whenFruitIsInvalid() {
        FruitRequest invalidRequest = new FruitRequest("", 0);

        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl(),
                invalidRequest,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testCreateFruit_shouldReturn400BadRequest_andDetailedErrorMessage_whenFruitIsInvalid() {
        FruitRequest invalidRequest = new FruitRequest("", 0);
        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl(),
                invalidRequest,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Name cannot be empty");
        assertThat(response.getBody()).contains("Quantity must be at least 1");
    }

    @Test
    void testGetAllFruits_shouldReturn200Ok_withAllFruits() {
        fruitRepository.save(Fruit.builder().name("Banana").quantityKilos(10).build());
        fruitRepository.save(Fruit.builder().name("Orange").quantityKilos(20).build());

        ResponseEntity<PageResponse<FruitResponse>> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(2);
        assertThat(response.getBody().getContent()[0].getName()).isEqualTo("Banana");
    }

    @Test
    void testGetFruitById_shouldReturn200Ok_whenFruitExists() {
        Fruit existingFruit = fruitRepository.save(Fruit.builder().name("Cherry").quantityKilos(5).build());
        Long id = existingFruit.getId();

        ResponseEntity<FruitResponse> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + id,
                FruitResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Cherry");
    }

    @Test
    void testGetFruitById_shouldReturn404NotFound_whenFruitDoesNotExist() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUpdateFruit_shouldReturn200Ok_whenFruitExists() {
        Fruit existingFruit = fruitRepository.save(Fruit.builder().name("Cherry").quantityKilos(5).build());
        Long id = existingFruit.getId();

        FruitRequest updatedRequest = new FruitRequest("Strawberry", 12);

        ResponseEntity<FruitResponse> response = restTemplate.exchange(
                getBaseUrl() + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updatedRequest),
                FruitResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Strawberry");

        Fruit fruitFromDb = fruitRepository.findById(id).orElse(null);
        assertThat(fruitFromDb).isNotNull();
        assertThat(fruitFromDb.getName()).isEqualTo("Strawberry");
        assertThat(fruitFromDb.getQuantityKilos()).isEqualTo(12);
    }

    @Test
    void testUpdateFruit_shouldReturn404NotFound_whenFruitDoesNotExist() {
        FruitRequest updatedRequest = new FruitRequest("Strawberry", 12);
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/999",
                HttpMethod.PUT,
                new HttpEntity<>(updatedRequest),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteFruit_shouldReturn204NoContent_whenFruitExists() {
        Fruit existingFruit = fruitRepository.save(
                Fruit.builder()
                        .name("Watermelon")
                        .quantityKilos(50)
                        .build()
        );
        Long id = existingFruit.getId();

        restTemplate.delete(getBaseUrl() + "/" + id);

        assertThat(fruitRepository.findById(id)).isEmpty();
    }
}