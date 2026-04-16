package com.example.Toda.service;

import com.example.Toda.DTO.BookingRequestResponse;
import com.example.Toda.Entity.BookingRequest;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.repo.BookingRequestRepository;
import com.example.Toda.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRequestRepository repository;
    private final UserRepo userRepo;

    public BookingService(BookingRequestRepository repository, UserRepo userRepo) {
        this.repository = repository;
        this.userRepo = userRepo;
    }


    public List<BookingRequestResponse> getRequestsByStatus(String guideEmail, BookingRequest.RequestStatus status) {

        UserEntity user = userRepo.findByEmail(guideEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long guideId = user.getTourGuide().getId();

        List<BookingRequest> requests = repository.findByTourGuideIdAndStatus(guideId, status);

        return requests.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public void updateStatus(Long requestId, BookingRequest.RequestStatus newStatus, String guideEmail) {
        BookingRequest request = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));


        String ownerEmail = request.getTourGuide().getUser().getEmail();
        if (!ownerEmail.equals(guideEmail)) {
            throw new RuntimeException("Unauthorized: You do not own this request");
        }

        request.setStatus(newStatus);
        repository.save(request);
    }


    private BookingRequestResponse convertToResponse(BookingRequest entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, h:mm a");
        String formattedDate = entity.getDate() != null ? entity.getDate().format(formatter) : "";

        return new BookingRequestResponse(
                entity.getId(),
                entity.getTourist().getUser().getUsername(),
                entity.getTourist().getProfilePhoto(),
                entity.getCategory(),
                formattedDate,
                entity.getTouristCount(),
                entity.getStatus().name()
        );
    }
}
