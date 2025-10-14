package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitRequest;
import cat.itacademy.s04.t02.n01.dto.FruitResponse;
import cat.itacademy.s04.t02.n01.model.Fruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FruitService {
    FruitResponse  createFruit(FruitRequest request);
    FruitResponse getFruitById(Long id);
    Page<FruitResponse> getAllFruits(Pageable pageable);
    FruitResponse updateFruit(Long id, FruitRequest request);
    void deleteFruit(Long id);
}
