package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitRequest;
import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;

    public FruitServiceImpl(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    private Fruit toEntity(FruitRequest request) {
        Fruit fruit = new Fruit();
        fruit.setName(request.getName());
        fruit.setQuantityKilos(request.getQuantityKilos());
        return fruit;
    }

    private Fruit updateEntity(Fruit existingFruit, FruitRequest request) {
        existingFruit.setName(request.getName());
        existingFruit.setQuantityKilos(request.getQuantityKilos());
        return existingFruit;
    }

    @Override
    public Fruit createFruit(FruitRequest fruitRequest) {
        Fruit fruitToSave = toEntity(fruitRequest);
        return fruitRepository.save(fruitToSave);
    }

    @Override
    public Optional<Fruit> getFruitById(Integer id) {
        return fruitRepository.findById(id);
    }

    @Override
    public List<Fruit> list() {
        return fruitRepository.findAll();
    }

    @Override
    public Optional<Fruit> updateFruit(Integer id, FruitRequest fruitRequest) {
        return fruitRepository.findById(id)
                .map(existingFruit -> {
                    Fruit updatedFruit = updateEntity(existingFruit, fruitRequest);
                    return fruitRepository.save(updatedFruit);
                });
    }

    @Override
    public void deleteFruit(Integer id) {
        fruitRepository.deleteById(id);

    }
}
