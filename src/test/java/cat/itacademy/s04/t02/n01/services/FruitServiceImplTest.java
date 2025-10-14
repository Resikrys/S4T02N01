package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitRequest;
import cat.itacademy.s04.t02.n01.dto.FruitResponse;
import cat.itacademy.s04.t02.n01.exception.ResourceAlreadyExistsException;
import cat.itacademy.s04.t02.n01.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FruitServiceImplTest {

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private FruitServiceImpl fruitService;

    private final FruitRequest validRequest = FruitRequest.builder().name("Banana").quantityKilos(15).build();

    private Fruit createFruitEntity(Long id, String name, Integer kilos) {
        return Fruit.builder().id(id).name(name).quantityKilos(kilos).build();
    }

    @Test
    public void testCreateFruit_shouldSaveAndReturnFruitResponse() {
        Fruit fruitToSave = createFruitEntity(null, "Banana", 15);
        Fruit savedFruit = createFruitEntity(1L, "Banana", 15);

        when(fruitRepository.save(any(Fruit.class))).thenReturn(savedFruit);

        FruitResponse createdResponse = fruitService.createFruit(validRequest);
        assertNotNull(createdResponse);
        assertEquals(1L, createdResponse.getId());
        assertEquals("Banana", createdResponse.getName());

        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    public void testCreateFruit_shouldThrowException_whenFruitNameAlreadyExists() {
        Fruit existingFruit = createFruitEntity(1L, "Banana", 10);

        when(fruitRepository.findByName(validRequest.getName())).thenReturn(Optional.of(existingFruit));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> fruitService.createFruit(validRequest));

        verify(fruitRepository, times(1)).findByName(validRequest.getName());
        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    public void testGetAllFruits_shouldReturnPageOfFruitResponse() {
        Fruit fruit1 = createFruitEntity(1L, "Banana", 15);
        Fruit fruit2 = createFruitEntity(2L, "Watermelon", 47);
        List<Fruit> fruits = List.of(fruit1, fruit2);
        Page<Fruit> fruitPage = new PageImpl<>(fruits);

        Pageable pageable = Pageable.unpaged();

        when(fruitRepository.findAll(pageable)).thenReturn(fruitPage);

        Page<FruitResponse> foundResponses = fruitService.getAllFruits(pageable);
        assertFalse(foundResponses.isEmpty());
        assertEquals(2, foundResponses.getTotalElements());
        assertEquals("Banana", foundResponses.getContent().get(0).getName());

        verify(fruitRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetFruitById_shouldReturnFruitResponseIfExists() {
        Long id = 1L;
        Fruit fruit = createFruitEntity(id, "Banana", 15);

        when(fruitRepository.findById(id)).thenReturn(Optional.of(fruit));

        FruitResponse foundResponse = fruitService.getFruitById(id);
        assertNotNull(foundResponse);
        assertEquals(fruit.getName(), foundResponse.getName());
        verify(fruitRepository, times(1)).findById(id);
    }

    @Test
    public void testGetFruitById_shouldThrowResourceNotFoundIfNotFound() {
        Long id = 99L;

        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fruitService.getFruitById(id));
        verify(fruitRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdateFruit_shouldReturnUpdatedFruitResponse_whenFruitExists() {
        Long id = 1L;
        Fruit existingFruit = createFruitEntity(id, "Banana", 10);
        FruitRequest updateRequest = FruitRequest.builder().name("Mango").quantityKilos(20).build();
        Fruit updatedFruit = createFruitEntity(id, "Mango", 20);

        when(fruitRepository.findById(id)).thenReturn(Optional.of(existingFruit));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(updatedFruit);

        FruitResponse result = fruitService.updateFruit(id, updateRequest);
        assertNotNull(result);
        assertEquals("Mango", result.getName());
        assertEquals(20, result.getQuantityKilos());

        verify(fruitRepository, times(1)).findById(id);
        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    public void testUpdateFruit_shouldThrowResourceNotFound_whenFruitDoesNotExist() {
        Long id = 99L;
        FruitRequest updateRequest = FruitRequest.builder().name("Watermelon").quantityKilos(50).build();

        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fruitService.updateFruit(id, updateRequest));
        verify(fruitRepository, times(1)).findById(id);
        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    public void testDeleteFruit_shouldDelete_whenFruitExists() {
        Long id = 1L;
        Fruit existingFruit = createFruitEntity(id, "Watermelon", 50);

        when(fruitRepository.findById(id)).thenReturn(Optional.of(existingFruit));
        doNothing().when(fruitRepository).delete(existingFruit);

        fruitService.deleteFruit(id);

        verify(fruitRepository, times(1)).findById(id);
        verify(fruitRepository, times(1)).delete(existingFruit);
    }

    @Test
    public void testDeleteFruit_shouldThrowResourceNotFound_whenFruitDoesNotExist() {
        Long id = 99L;

        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fruitService.deleteFruit(id));
        verify(fruitRepository, times(1)).findById(id);
        verify(fruitRepository, never()).delete(any(Fruit.class));
    }
}