package com.motosport.bike.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.motosport.bike.dto.BikeDto;
import com.motosport.bike.dto.ResponseDto;
import com.motosport.bike.model.Bike;
import com.motosport.bike.repository.BikeRepository;

@ExtendWith(MockitoExtension.class)
public class BikeServiceImplTest {

    @Mock
    private BikeRepository repository;

    private BikeServiceImpl bikeService;

    private Bike buildBike() {
        return new Bike(1L, "Kawasaki", "Ninja 400", "ABC123", 4500000, 2022, "Verde", 12000, true);
    }

    private BikeDto buildDto() {
        return new BikeDto(1L, "Kawasaki", "Ninja 400", "ABC123", 4500000, 2022, "Verde", 12000, true);
    }

    @Test
    void addBike_shouldSaveAndReturnDto() {
        bikeService = new BikeServiceImpl(repository);
        when(repository.save(any(Bike.class))).thenReturn(buildBike());

        BikeDto result = bikeService.addBike(buildDto());

        assertNotNull(result);
        assertEquals("Kawasaki", result.marca());
        verify(repository).save(any(Bike.class));
    }

    @Test
    void getBike_shouldReturnDtoWhenExists() {
        bikeService = new BikeServiceImpl(repository);
        when(repository.findById(1L)).thenReturn(Optional.of(buildBike()));

        BikeDto result = bikeService.getBike(1L);

        assertNotNull(result);
        assertEquals("Ninja 400", result.modelo());
    }

    @Test
    void getBike_shouldThrowWhenNotFound() {
        bikeService = new BikeServiceImpl(repository);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bikeService.getBike(99L));
    }

    @Test
    void getAllBike_shouldReturnListOfDtos() {
        bikeService = new BikeServiceImpl(repository);
        when(repository.findAll()).thenReturn(List.of(buildBike()));

        List<BikeDto> result = bikeService.getAllBike();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ABC123", result.get(0).patente());
    }

    @Test
    void updateBike_shouldModifyAndReturnDto() {
        bikeService = new BikeServiceImpl(repository);
        Bike existing = buildBike();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        BikeDto updatedDto = new BikeDto(1L, "Yamaha", "R6", "DEF456", 8900000, 2021, "Azul", 8000, false);

        BikeDto result = bikeService.updateBike(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Yamaha", result.marca());
        assertEquals("R6", result.modelo());
        verify(repository).save(existing);
    }

    @Test
    void updateBike_shouldThrowWhenNotFound() {
        bikeService = new BikeServiceImpl(repository);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bikeService.updateBike(99L, buildDto()));
    }

    @Test
    void deleteBike_shouldReturnSuccessMessageWhenExists() {
        bikeService = new BikeServiceImpl(repository);
        when(repository.existsById(1L)).thenReturn(true);

        ResponseDto result = bikeService.deleteBike(1L);

        assertEquals("Id eliminada correctamente.", result.message());
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteBike_shouldReturnNotFoundMessageWhenMissing() {
        bikeService = new BikeServiceImpl(repository);
        when(repository.existsById(99L)).thenReturn(false);

        ResponseDto result = bikeService.deleteBike(99L);

        assertEquals("Id no encontrada.", result.message());
    }
}
