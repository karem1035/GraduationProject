package com.example.Toda.mapper;

import com.example.Toda.DTO.TripBasicInfoRequest;
import com.example.Toda.DTO.TripCardResponse;
import com.example.Toda.DTO.TripInfoPriceRequest;
import com.example.Toda.DTO.TripInfoTimeRequest;
import com.example.Toda.Entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(target = "categories", source = "category")
     Trip toEntity(TripBasicInfoRequest dto);
     Trip toTrip(TripInfoTimeRequest dto);
     Trip toTrip(TripInfoPriceRequest dto);
     TripBasicInfoRequest toDto(Trip entity);
    @Mapping(source = "tripCoverImage", target = "coverImageUrl")
    @Mapping(target = "category", expression = "java(getFirstCategory(trip))")
    @Mapping(target = "status", expression = "java(formatStatus(trip))")
    TripCardResponse toCardResponse(Trip trip);

    List<TripCardResponse> toCardResponseList(List<Trip> trips);

    default String getFirstCategory(Trip trip) {
        if (trip.getCategories() == null || trip.getCategories().isEmpty()) {
            return "General";
        }
        return trip.getCategories().get(0);
    }

    default String formatStatus(Trip trip) {
        return trip.getStatus() != null ? trip.getStatus().name() : "NEW";
    }
}
