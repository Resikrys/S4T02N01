package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitRequest;
import cat.itacademy.s04.t02.n01.dto.FruitResponse;
import cat.itacademy.s04.t02.n01.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.exception.ResourceAlreadyExistsException;
import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FruitServiceImpl implements FruitService {

    private final FruitRepository repository;

    public FruitServiceImpl(FruitRepository repository ) {
        this.repository  = repository ;
    }

    public FruitResponse createFruit(FruitRequest request) {
        repository.findByName(request.getName())
                .ifPresent(fruit -> {
                    throw new ResourceAlreadyExistsException("Fruit with name " + request.getName() + " already exists.");
                });

        Fruit fruit = Fruit.builder()
                .name(request.getName())
                .quantityKilos(request.getQuantityKilos())
                .build();
        Fruit saved = repository.save(fruit);
        return toResponse(saved);
    }

    public FruitResponse getFruitById(Long id) {
        Fruit fruit = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit not found with id " + id));
        return toResponse(fruit);
    }

    public Page<FruitResponse> getAllFruits(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::toResponse);
    }

    public FruitResponse updateFruit(Long id, @Valid FruitRequest request) {
        Fruit existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit not found with id " + id));

        existing.setName(request.getName());
        existing.setQuantityKilos(request.getQuantityKilos());
        Fruit updated = repository.save(existing);

        return toResponse(updated);
    }

    public void deleteFruit(Long id) {
        Fruit existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit not found with id " + id));
        repository.delete(existing);
    }

    private FruitResponse toResponse(Fruit fruit) {
        return FruitResponse.builder()
                .id(fruit.getId())
                .name(fruit.getName())
                .quantityKilos(fruit.getQuantityKilos())
                .build();
    }
}
