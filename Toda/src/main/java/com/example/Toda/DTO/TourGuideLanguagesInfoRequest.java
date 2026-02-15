package com.example.Toda.DTO;

import com.example.Toda.Entity.Language;

import java.util.List;

public record TourGuideLanguagesInfoRequest(
       List<Language>languages
) {
}
