package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.dto.FruitRequest;
import cat.itacademy.s04.t02.n01.dto.FruitResponse;
import cat.itacademy.s04.t02.n01.services.FruitService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

    @GetMapping
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
}
