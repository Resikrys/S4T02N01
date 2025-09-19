package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.services.FruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("fruits-rest")
public class FruitController {

    // Inyección de la capa de servicio
    @Autowired
    private FruitService fruitService;

    // Método para crear una nueva fruta
    @PostMapping
    public ResponseEntity<Fruit> createFruit(@RequestBody Fruit fruit) {
        Fruit newFruit = fruitService.createFruit(fruit);
        return new ResponseEntity<>(newFruit, HttpStatus.CREATED);
    }

    // Método para obtener una fruta por ID --> pathVariable ideal per trobar algo específic (id)
    // RequestParams --> params (mètodes per filtrar, ordenar o proporcionar info opcional) --> signo ?
    @GetMapping("/{id}")
    public ResponseEntity<Fruit> getFruitById(@PathVariable int id) {
        Fruit fruit = fruitService.getFruitById(id);
        if (fruit != null) {
            return new ResponseEntity<>(fruit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
