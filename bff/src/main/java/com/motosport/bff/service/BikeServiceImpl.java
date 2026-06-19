package com.motosport.bff.service;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.motosport.bff.dto.BikeDto;
import com.motosport.bff.dto.ResponseDto;

@Service
public class BikeServiceImpl implements BikeService {

    private final RestClient restClient;
    // Definimos la URL base del microservicio de bicicletas como una constante
    private static final String BIKE_MS_URL = "http://localhost:4003/api/bikes";

    // Inyectamos el restClient que configuró tu profesor en AppConfig
    public BikeServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public BikeDto addBike(BikeDto dto) {
        return restClient.post()
                .uri(BIKE_MS_URL)
                .body(dto)
                .retrieve()
                .body(BikeDto.class);
    }

    @Override
    public BikeDto getBike(Long id) {
        return restClient.get()
                .uri(BIKE_MS_URL + "/{id}", id)
                .retrieve()
                .body(BikeDto.class);
    }

    @Override
    public List<BikeDto> getAllBike() {
        return restClient.get()
                .uri(BIKE_MS_URL)
                .retrieve()
                .body(new ParameterizedTypeReference<List<BikeDto>>() {
                });
    }

    @Override
    public BikeDto updateBike(Long id, BikeDto dto) {
        return restClient.put()
                .uri(BIKE_MS_URL + "/{id}", id)
                .body(dto)
                .retrieve()
                .body(BikeDto.class);
    }

    @Override
    public ResponseDto deleteBike(Long id) {
        try {
            restClient.delete()
                    .uri(BIKE_MS_URL + "/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
            return new ResponseDto("Id eliminada correctamente.");
        } catch (Exception e) {
            return new ResponseDto("Error al eliminar o Id no encontrada.");
        }
    }
}