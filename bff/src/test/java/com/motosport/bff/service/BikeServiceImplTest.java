package com.motosport.bff.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import com.motosport.bff.dto.BikeDto;
import com.motosport.bff.dto.ResponseDto;

@ExtendWith(MockitoExtension.class)
public class BikeServiceImplTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient restClient;

    @Test
    void getBike_shouldCallRestClientAndReturnBike() {
        BikeServiceImpl bikeService = new BikeServiceImpl(restClient);
        BikeDto mockBike = new BikeDto(1L, "Suzuki", "Gixxer", "JKL012", 2100000, 2019, "Negro", 20000, true);

        when(restClient.get()
                .uri(anyString(), eq(1L))
                .retrieve()
                .body(BikeDto.class))
                .thenReturn(mockBike);

        BikeDto result = bikeService.getBike(1L);

        assertNotNull(result);
        assertEquals("Suzuki", result.marca());
    }

    @Test
    void addBike_shouldPostAndReturnSavedBike() {
        BikeServiceImpl bikeService = new BikeServiceImpl(restClient);
        BikeDto inputDto = new BikeDto(null, "Ducati", "Monster", "MNO345", 9500000, 2023, "Rojo", 500, true);
        BikeDto savedDto = new BikeDto(99L, "Ducati", "Monster", "MNO345", 9500000, 2023, "Rojo", 500, true);

        when(restClient.post()
                .uri(anyString())
                .body(any(BikeDto.class))
                .retrieve()
                .body(BikeDto.class))
                .thenReturn(savedDto);

        BikeDto result = bikeService.addBike(inputDto);

        assertNotNull(result);
        assertEquals(99L, result.id());
    }

    @Test
    void deleteBike_shouldReturnSuccessMessageOnNoContent() {
        BikeServiceImpl bikeService = new BikeServiceImpl(restClient);

        when(restClient.delete()
                .uri(anyString(), eq(10L))
                .retrieve()
                .toBodilessEntity())
                .thenReturn(null);

        ResponseDto response = bikeService.deleteBike(10L);

        assertNotNull(response);
        assertEquals("Id eliminada correctamente.", response.message());
    }
}