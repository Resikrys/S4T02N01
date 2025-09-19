package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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
        fruitRepository.deleteAll();
    }

    @Test
    void testCreateFruit_shouldReturn201Created() {

        Fruit newFruit = new Fruit("Apple", 15);

        ResponseEntity<Fruit> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/fruits-rest", //Fruits-rest es el endpoint de la API (@RequestMapping)
                newFruit,
                Fruit.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Apple");
        assertThat(response.getBody().getId()).isGreaterThan(0);
        assertThat(fruitRepository.count()).isEqualTo(1);
    }

    @Test
    void testGetAllFruits_shouldReturnListOfFruits() {

        fruitRepository.save(new Fruit("Banana", 10));
        fruitRepository.save(new Fruit("Orange", 20));

        ResponseEntity<Fruit[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/fruits-rest",
                Fruit[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void testUpdateFruit_shouldReturn200Ok_whenFruitExists() {

        Fruit existingFruit = fruitRepository.save(new Fruit("Cherry", 5));

        Fruit updatedFruit = new Fruit("Strawberry", 12);

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
    void testDeleteFruit_shouldReturn204NoContent_whenFruitExists() {

        Fruit existingFruit = fruitRepository.save(new Fruit("Watermelon", 50));

        restTemplate.delete("http://localhost:" + port + "/fruits-rest/" + existingFruit.getId());

        assertThat(fruitRepository.findById(existingFruit.getId())).isEmpty();
        assertThat(fruitRepository.count()).isEqualTo(0);
    }
}
