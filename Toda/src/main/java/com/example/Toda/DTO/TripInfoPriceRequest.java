package com.example.Toda.DTO;

import java.util.List;

public record TripInfoPriceRequest(
        Double pricePerTourist,
        List<String> inclusions
) {
}
