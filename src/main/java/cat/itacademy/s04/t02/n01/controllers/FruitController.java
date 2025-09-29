package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.dto.FruitRequest;
import cat.itacademy.s04.t02.n01.dto.FruitResponse;
import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.services.FruitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fruits")
public class FruitController {

    @Autowired
    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    private FruitResponse toResponseDTO(Fruit fruit) {
        return new FruitResponse(fruit.getId(), fruit.getName(), fruit.getQuantityKilos());
    }

    @PostMapping
    public ResponseEntity<FruitResponse> createFruit(@Valid @RequestBody FruitRequest fruitRequest) {
        Fruit createdFruit = fruitService.createFruit(fruitRequest);

        FruitResponse responseDTO = toResponseDTO(createdFruit);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<FruitResponse>> getAllFruits() {
        List<Fruit> fruits = fruitService.list();

        List<FruitResponse> responseList = fruits.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitResponse> getFruitById(@PathVariable Integer id) {
        Optional<FruitResponse> response = fruitService.getFruitById(id)
                .map(this::toResponseDTO);

        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitResponse> updateFruit(@PathVariable Integer id, @Valid @RequestBody FruitRequest fruitRequest) {
        Optional<FruitResponse> response = fruitService.updateFruit(id, fruitRequest)
                .map(this::toResponseDTO);

        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFruit(@PathVariable Integer id) {
        fruitService.deleteFruit(id);
        return ResponseEntity.noContent().build();
    }

}
