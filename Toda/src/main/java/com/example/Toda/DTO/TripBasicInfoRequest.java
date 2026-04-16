package com.example.Toda.DTO;

import java.util.List;

public record TripBasicInfoRequest(
        String title,
        List<String> category,
        String city,
        String meetingPoint
) {
}
