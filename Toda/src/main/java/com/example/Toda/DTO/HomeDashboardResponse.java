package com.example.Toda.DTO;

import java.util.List;

public record HomeDashboardResponse(
        String guideName,
        String guideLocation,
        String profilePhoto,
        List<TripResponse> upcomingTrips,
        List<BookingRequestResponse> recentRequests,
        MonthlyStats stats
) {
    public record MonthlyStats(long completedTrips, double rating, double earnings) {}
    public record TripResponse(Long id, String title, String image, String date, String location) {}
}
