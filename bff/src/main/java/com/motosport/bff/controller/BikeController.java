package com.motosport.bff.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.motosport.bff.dto.BikeDto;
import com.motosport.bff.service.BikeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bikes")
public class BikeController {

    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping
    public List<BikeDto> getAll() {
        return bikeService.getAllBike();
    }

    @GetMapping("/{id}")
    public BikeDto getById(@PathVariable Long id) {
        return bikeService.getBike(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BikeDto create(@Valid @RequestBody BikeDto request) {
        return bikeService.addBike(request);
    }

    @PutMapping("/{id}")
    public BikeDto update(
            @PathVariable Long id,
            @Valid @RequestBody BikeDto request) {

        return bikeService.updateBike(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bikeService.deleteBike(id);
    }
}