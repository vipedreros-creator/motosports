package com.motosport.bff.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.motosport.bff.dto.BikeDto;
import com.motosport.bff.service.BikeService;

@ExtendWith(MockitoExtension.class)
class BikeControllerTest {

    @Mock
    private BikeService bikeService;

    @Test
    void getAll_shouldReturnListOfBikes() {
        BikeController bikeController = new BikeController(bikeService);
        BikeDto kawasaki = new BikeDto(1L, "Kawasaki", "Ninja 400", "Verde", 400);
        when(bikeService.getAllBike()).thenReturn(List.of(kawasaki));

        List<BikeDto> result = bikeController.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ninja 400", result.get(0).model()); // Cambia .model() por la forma de tu DTO (getter o record)
    }

    @Test
    void getById_shouldReturnBikeDto() {
        BikeController bikeController = new BikeController(bikeService);
        BikeDto yamaha = new BikeDto(2L, "Yamaha", "R6", "Azul", 600);
        when(bikeService.getBike(2L)).thenReturn(yamaha);

        BikeDto result = bikeController.getById(2L);

        assertNotNull(result);
        assertEquals("Yamaha", result.brand()); // Cambia .brand() si usas getters tradicionales como .getBrand()
    }

    @Test
    void create_shouldReturnCreatedBike() {
        BikeController bikeController = new BikeController(bikeService);
        BikeDto honda = new BikeDto(null, "Honda", "CBR", "Rojo", 250);
        BikeDto hondaSaved = new BikeDto(3L, "Honda", "CBR", "Rojo", 250);
        when(bikeService.addBike(any(BikeDto.class))).thenReturn(hondaSaved);

        BikeDto result = bikeController.create(honda);

        assertNotNull(result);
        assertEquals(3L, result.id());
    }

    @Test
    void delete_shouldCallServiceDelete() {
        BikeController bikeController = new BikeController(bikeService);

        bikeController.delete(4L);

        verify(bikeService).deleteBike(4L);
    }
}
