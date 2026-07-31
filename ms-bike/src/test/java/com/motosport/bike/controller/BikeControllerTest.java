package com.motosport.bike.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import com.motosport.bike.dto.BikeDto;
import com.motosport.bike.dto.ResponseDto;
import com.motosport.bike.service.BikeService;

@ExtendWith(MockitoExtension.class)
public class BikeControllerTest {

    @Mock
    private BikeService bikeService;

    @Test
    void getAllBikes_shouldReturnListOfBikes() {
        BikeController bikeController = new BikeController(bikeService);
        BikeDto kawasaki = new BikeDto(1L, "Kawasaki", "Ninja 400", "ABC123", 4500000, 2022, "Verde", 12000, true);
        when(bikeService.getAllBike()).thenReturn(List.of(kawasaki));

        ResponseEntity<List<BikeDto>> result = bikeController.getAllBikes();

        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("Ninja 400", result.getBody().get(0).modelo());
    }

    @Test
    void getBikeById_shouldReturnBikeDto() {
        BikeController bikeController = new BikeController(bikeService);
        BikeDto yamaha = new BikeDto(2L, "Yamaha", "R6", "DEF456", 8900000, 2021, "Azul", 8000, true);
        when(bikeService.getBike(2L)).thenReturn(yamaha);

        ResponseEntity<BikeDto> result = bikeController.getBikeById(2L);

        assertNotNull(result.getBody());
        assertEquals("Yamaha", result.getBody().marca());
    }

    @Test
    void createBike_shouldReturnCreatedBike() {
        BikeController bikeController = new BikeController(bikeService);
        BikeDto honda = new BikeDto(null, "Honda", "CBR", "GHI789", 3200000, 2020, "Rojo", 15000, true);
        BikeDto hondaSaved = new BikeDto(3L, "Honda", "CBR", "GHI789", 3200000, 2020, "Rojo", 15000, true);
        when(bikeService.addBike(any(BikeDto.class))).thenReturn(hondaSaved);

        ResponseEntity<BikeDto> result = bikeController.createBike(honda);

        assertNotNull(result.getBody());
        assertEquals(3L, result.getBody().id());
    }

    @Test
    void updateBike_shouldReturnUpdatedBike() {
        BikeController bikeController = new BikeController(bikeService);
        BikeDto updated = new BikeDto(4L, "Suzuki", "GSX-R", "JKL012", 5200000, 2023, "Negro", 500, true);
        when(bikeService.updateBike(eq(4L), any(BikeDto.class))).thenReturn(updated);

        ResponseEntity<BikeDto> result = bikeController.updateBike(4L, updated);

        assertNotNull(result.getBody());
        assertEquals("Suzuki", result.getBody().marca());
    }

    @Test
    void deleteBike_shouldReturnResponseDto() {
        BikeController bikeController = new BikeController(bikeService);
        ResponseDto response = new ResponseDto("Moto eliminada correctamente");
        when(bikeService.deleteBike(5L)).thenReturn(response);

        ResponseEntity<ResponseDto> result = bikeController.deleteBike(5L);

        assertNotNull(result.getBody());
        assertEquals("Moto eliminada correctamente", result.getBody().message());
        verify(bikeService).deleteBike(5L);
    }
}
