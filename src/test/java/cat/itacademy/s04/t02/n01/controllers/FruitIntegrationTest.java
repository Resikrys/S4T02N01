package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

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
        // Limpia la base de datos después de cada test para asegurar la independencia
        fruitRepository.deleteAll();
    }

    @Test
    void testCreateFruit_shouldReturn201Created() {
        Fruit newFruit = new Fruit("Apple", 15);
        ResponseEntity<Fruit> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/fruits-rest",
                newFruit,
                Fruit.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Apple");
        assertThat(fruitRepository.count()).isEqualTo(1);
    }

    @Test
    void testCreateFruit_shouldReturn400BadRequest_whenFruitIsInvalid() {
        // La validación del controlador (@Valid) debería devolver un 400
        Fruit invalidFruit = new Fruit("", 0);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/fruits-rest",
                invalidFruit,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testGetAllFruits_shouldReturn200Ok_withAllFruits() {
        fruitRepository.save(new Fruit("Banana", 10));
        fruitRepository.save(new Fruit("Orange", 20));

        ResponseEntity<Fruit[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/fruits-rest",
                Fruit[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void testGetFruitById_shouldReturn200Ok_whenFruitExists() {
        Fruit existingFruit = fruitRepository.save(new Fruit("Cherry", 5));
        ResponseEntity<Fruit> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/fruits-rest/" + existingFruit.getId(),
                Fruit.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Cherry");
    }

    @Test
    void testGetFruitById_shouldReturn404NotFound_whenFruitDoesNotExist() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/fruits-rest/999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUpdateFruit_shouldReturn200Ok_whenFruitExists() {
        Fruit existingFruit = fruitRepository.save(new Fruit("Cherry", 5));
        Fruit updatedFruit = new Fruit("Strawberry", 12);

        // La entidad debe tener el ID para que el @PathVariable lo reciba correctamente
        updatedFruit.setId(existingFruit.getId());

        restTemplate.put(
                "http://localhost:" + port + "/fruits-rest/" + existingFruit.getId(),
                updatedFruit
        );

        Fruit fruitFromDb = fruitRepository.findById(existingFruit.getId()).orElse(null);
        assertThat(fruitFromDb).isNotNull();
        assertThat(fruitFromDb.getName()).isEqualTo("Strawberry");
        assertThat(fruitFromDb.getQuantityKilos()).isEqualTo(12);
    }

    @Test
    void testUpdateFruit_shouldReturn404NotFound_whenFruitDoesNotExist() {
        Fruit updatedFruit = new Fruit("Strawberry", 12);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/fruits-rest/999",
                org.springframework.http.HttpMethod.PUT,
                new HttpEntity<>(updatedFruit),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteFruit_shouldReturn204NoContent_whenFruitExists() {
        Fruit existingFruit = fruitRepository.save(new Fruit("Watermelon", 50));
        restTemplate.delete("http://localhost:" + port + "/fruits-rest/" + existingFruit.getId());
        assertThat(fruitRepository.findById(existingFruit.getId())).isEmpty();
    }
}
