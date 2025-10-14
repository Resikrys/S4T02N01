package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.dto.FruitRequest;
import cat.itacademy.s04.t02.n01.dto.FruitResponse;
import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.services.FruitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final FruitService service;

    public FruitController(FruitService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FruitResponse> create(@Valid @RequestBody FruitRequest request) {
        FruitResponse created = service.createFruit(request);

        // 💡 MEJORA: Devolver 201 Created con la URI de la nueva fruta
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitResponse> getOne(@PathVariable Long id) {
        FruitResponse fruit = service.getFruitById(id);
        return ResponseEntity.ok(fruit);
    }

    @GetMapping // 💡 MEJORA: Se usa la ruta base para listar/paginar
    public ResponseEntity<Page<FruitResponse>> getAllFruits(Pageable pageable) {
        Page<FruitResponse> page = service.getAllFruits(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitResponse> update(@PathVariable Long id, @Valid @RequestBody FruitRequest request) {
        FruitResponse updated = service.updateFruit(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteFruit(id);
        return ResponseEntity.noContent().build();
    }

//    private FruitResponse toResponseDTO(Fruit fruit) {
//        return new FruitResponse(fruit.getId(), fruit.getName(), fruit.getQuantityKilos());
//    }
//
//    @PostMapping
//    public ResponseEntity<FruitResponse> createFruit(@Valid @RequestBody FruitRequest fruitRequest) {
//        Fruit createdFruit = fruitService.createFruit(fruitRequest);
//
//        FruitResponse responseDTO = toResponseDTO(createdFruit);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(responseDTO.getId())
//                .toUri();
//
//        return ResponseEntity.created(location).body(responseDTO);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<FruitResponse>> getAllFruits() {
//        List<Fruit> fruits = fruitService.list();
//
//        List<FruitResponse> responseList = fruits.stream()
//                .map(this::toResponseDTO)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(responseList);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<FruitResponse> getFruitById(@PathVariable Integer id) {
//        Optional<FruitResponse> response = fruitService.getFruitById(id)
//                .map(this::toResponseDTO);
//
//        return response.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<FruitResponse> updateFruit(@PathVariable Integer id, @Valid @RequestBody FruitRequest fruitRequest) {
//        Optional<FruitResponse> response = fruitService.updateFruit(id, fruitRequest)
//                .map(this::toResponseDTO);
//
//        return response.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteFruit(@PathVariable Integer id) {
//        fruitService.deleteFruit(id);
//        return ResponseEntity.noContent().build();
//    }

}
