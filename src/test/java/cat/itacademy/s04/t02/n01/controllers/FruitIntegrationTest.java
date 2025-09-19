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
        // GIVEN: Un objeto Fruit para crear
        Fruit newFruit = new Fruit("Apple", 15);

        // WHEN: Se realiza la petición POST
        ResponseEntity<Fruit> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/fruits-rest", //Fruits-rest es el endpoint de la API (@RequestMapping)
                newFruit,
                Fruit.class);

        // THEN: Se valida la respuesta 201 Created y que la fruta se ha guardado en la base de datos
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Apple");
        assertThat(response.getBody().getId()).isGreaterThan(0);
        assertThat(fruitRepository.count()).isEqualTo(1);
    }

    @Test
    void testGetAllFruits_shouldReturnListOfFruits() {
        // GIVEN: Dos frutas en la base de datos
        fruitRepository.save(new Fruit("Banana", 10));
        fruitRepository.save(new Fruit("Orange", 20));

        // WHEN: Se realiza la petición GET
        ResponseEntity<Fruit[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/fruits-rest",
                Fruit[].class);

        // THEN: Se valida la respuesta 200 OK y que la lista tiene dos elementos
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }
}
