package com.example.Toda.mapper;

import com.example.Toda.DTO.*;
import com.example.Toda.Entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TripMapper {

    @Mapping(target = "categories", source = "category")
    @Mapping(target = "status", constant = "NEW")
    Trip toEntity(TripBasicInfoRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "meetingPoint", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "tourGuide", ignore = true)
    @Mapping(target = "tripCoverImage", ignore = true)
    @Mapping(target = "pricePerTourist", ignore = true)
    @Mapping(target = "inclusions", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateTripFromTimeRequest(TripInfoTimeRequest dto, @MappingTarget Trip trip);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "meetingPoint", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "tourGuide", ignore = true)
    @Mapping(target = "tripCoverImage", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "minGroupSize", ignore = true)
    @Mapping(target = "maxGroupSize", ignore = true)
    @Mapping(target = "tourDuration", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateTripFromPriceRequest(TripInfoPriceRequest dto, @MappingTarget Trip trip);

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