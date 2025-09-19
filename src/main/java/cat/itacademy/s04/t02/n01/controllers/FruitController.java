package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.services.FruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("fruits-rest")
public class FruitController {

    @Autowired
    private FruitService fruitService;

    @PostMapping
    public ResponseEntity<Fruit> createFruit(@RequestBody Fruit fruit) {
        Fruit newFruit = fruitService.createFruit(fruit);
        return new ResponseEntity<>(newFruit, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Fruit>> getAllFruits() {
        List<Fruit> fruits = fruitService.list();
        return new ResponseEntity<>(fruits, HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Fruit> getFruitById(@PathVariable int id) {
//        Fruit fruit = fruitService.getFruitById(id);
//        if (fruit == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(fruit, HttpStatus.OK);
//    }
    @GetMapping("/{id}")
    public ResponseEntity<Fruit> getFruitById(@PathVariable int id) {
        Optional<Fruit> fruit = Optional.ofNullable(fruitService.getFruitById(id));
        return fruit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Fruit> updateFruit(@PathVariable int id, @RequestBody Fruit fruit) {
//        Fruit existingFruit = fruitService.getFruitById(id);
//        if (existingFruit == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        Fruit updatedFruit = fruitService.updateFruit(id, fruit);
//        return new ResponseEntity<>(updatedFruit, HttpStatus.OK);
//    }
    @PutMapping("/{id}")
    public ResponseEntity<Fruit> updateFruit(@PathVariable int id, @Valid @RequestBody Fruit fruit) {
        Fruit updatedFruit = fruitService.updateFruit(id, fruit);
        if (updatedFruit != null) {
            return ResponseEntity.ok(updatedFruit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteFruit(@PathVariable int id) {
//        Fruit fruit = fruitService.getFruitById(id);
//        if (fruit == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        fruitService.deleteFruit(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFruit(@PathVariable int id) {
        fruitService.deleteFruit(id);
        return ResponseEntity.noContent().build();
    }

}
