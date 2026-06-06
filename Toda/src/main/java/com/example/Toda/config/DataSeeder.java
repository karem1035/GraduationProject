package com.example.Toda.config;

import com.example.Toda.Entity.*;
import com.example.Toda.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepo userRepo;
    private final LandmarkRepository landmarkRepository;
    private final TourGuideRepo tourGuideRepo;
    private final TripRepository tripRepository;
    private final StaticTripRepository staticTripRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepo userRepo, LandmarkRepository landmarkRepository,
                      TourGuideRepo tourGuideRepo, TripRepository tripRepository,
                      StaticTripRepository staticTripRepository, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.landmarkRepository = landmarkRepository;
        this.tourGuideRepo = tourGuideRepo;
        this.tripRepository = tripRepository;
        this.staticTripRepository = staticTripRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedTourGuides();
        seedLandmarks();
        seedTrips();
        seedStaticTrips();
    }

    private void seedUsers() {
        if (userRepo.findByEmail("admin@toda.com").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmail("admin@toda.com");
            admin.setPassword(passwordEncoder.encode("0000"));
            admin.setRole(Role.ADMIN);
            userRepo.save(admin);
            logger.info("Admin user created: admin@toda.com | Password: 0000");
        }

        if (userRepo.findByEmail("tourist@toda.com").isEmpty()) {
            UserEntity tourist = new UserEntity();
            tourist.setUsername("tourist");
            tourist.setEmail("tourist@toda.com");
            tourist.setPassword(passwordEncoder.encode("0000"));
            tourist.setRole(Role.TOURIST);
            userRepo.save(tourist);
            logger.info("Tourist user created: tourist@toda.com | Password: 0000");
        }

        String[][] guideUsers = {
                {"ahmed@toda.com", "ahmedhassan"},
                {"fatima@toda.com", "fatimamahmoud"},
                {"omar@toda.com", "omarelsayed"},
                {"noura@toda.com", "nouraali"},
                {"mohamed@toda.com", "mohamedibrahim"}
        };
        for (String[] data : guideUsers) {
            if (userRepo.findByEmail(data[0]).isEmpty()) {
                UserEntity u = new UserEntity();
                u.setUsername(data[1]);
                u.setEmail(data[0]);
                u.setPassword(passwordEncoder.encode("0000"));
                u.setRole(Role.TOURGUIDE);
                userRepo.save(u);
                logger.info("Guide user created: {}", data[0]);
            }
        }

        String[][] extraTourists = {{"sara@toda.com", "sarajohnson"}, {"liam@toda.com", "liamsmith"}};
        for (String[] data : extraTourists) {
            if (userRepo.findByEmail(data[0]).isEmpty()) {
                UserEntity u = new UserEntity();
                u.setUsername(data[1]);
                u.setEmail(data[0]);
                u.setPassword(passwordEncoder.encode("0000"));
                u.setRole(Role.TOURIST);
                userRepo.save(u);
            }
        }
    }

    private void seedTourGuides() {
        if (tourGuideRepo.count() > 0) {
            logger.info("Tour guides already exist. Skipping.");
            return;
        }
        logger.info("Seeding tour guide profiles...");

        List<TourGuideEntity> guides = new ArrayList<>();
        UserEntity ahmed = userRepo.findByEmail("ahmed@toda.com").orElse(null);
        UserEntity fatima = userRepo.findByEmail("fatima@toda.com").orElse(null);
        UserEntity omar = userRepo.findByEmail("omar@toda.com").orElse(null);
        UserEntity noura = userRepo.findByEmail("noura@toda.com").orElse(null);
        UserEntity mohamed = userRepo.findByEmail("mohamed@toda.com").orElse(null);

        if (ahmed != null) {
            TourGuideEntity g = new TourGuideEntity();
            g.setUser(ahmed); g.setName("Ahmed Hassan"); g.setEmail("ahmed@toda.com");
            g.setType(TourGuideEntity.GuideType.MALE); g.setPhone("+201012345678"); g.setCity("Cairo");
            g.setGuideType(TourGuideEntity.GuideTypeCategory.LICENSED_GUIDE);
            g.setLicensedNumber("EG-LIC-2024-001"); g.setYearsOfExperience(12);
            g.setSpecialization(List.of("Historical Tours", "Islamic Cairo", "Museum Tours"));
            g.setLanguages(List.of(new Language("Arabic", "Native"), new Language("English", "Fluent")));
            g.setTourType(TourGuideEntity.TourType.GROUP);
            g.setProfilePhoto("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=400&fit=crop");
            guides.add(g);
        }
        if (fatima != null) {
            TourGuideEntity g = new TourGuideEntity();
            g.setUser(fatima); g.setName("Fatima Mahmoud"); g.setEmail("fatima@toda.com");
            g.setType(TourGuideEntity.GuideType.FEMALE); g.setPhone("+201098765432"); g.setCity("Luxor");
            g.setGuideType(TourGuideEntity.GuideTypeCategory.LICENSED_GUIDE);
            g.setLicensedNumber("EG-LIC-2024-002"); g.setYearsOfExperience(8);
            g.setSpecialization(List.of("Pharaonic History", "Nile Cruises", "Temple Tours"));
            g.setLanguages(List.of(new Language("Arabic", "Native"), new Language("English", "Fluent"), new Language("German", "Intermediate")));
            g.setTourType(TourGuideEntity.TourType.PRIVATE);
            g.setProfilePhoto("https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&h=400&fit=crop");
            guides.add(g);
        }
        if (omar != null) {
            TourGuideEntity g = new TourGuideEntity();
            g.setUser(omar); g.setName("Omar El-Sayed"); g.setEmail("omar@toda.com");
            g.setType(TourGuideEntity.GuideType.MALE); g.setPhone("+201111223344"); g.setCity("Aswan");
            g.setGuideType(TourGuideEntity.GuideTypeCategory.LOCAL_GUIDE);
            g.setYearsOfExperience(5);
            g.setSpecialization(List.of("Nubian Culture", "Desert Safaris", "Nile Tours"));
            g.setLanguages(List.of(new Language("Arabic", "Native"), new Language("English", "Fluent")));
            g.setTourType(TourGuideEntity.TourType.GROUP);
            g.setProfilePhoto("https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&h=400&fit=crop");
            guides.add(g);
        }
        if (noura != null) {
            TourGuideEntity g = new TourGuideEntity();
            g.setUser(noura); g.setName("Noura Ali"); g.setEmail("noura@toda.com");
            g.setType(TourGuideEntity.GuideType.FEMALE); g.setPhone("+201234567890"); g.setCity("Alexandria");
            g.setGuideType(TourGuideEntity.GuideTypeCategory.LICENSED_GUIDE);
            g.setLicensedNumber("EG-LIC-2024-003"); g.setYearsOfExperience(10);
            g.setSpecialization(List.of("Greco-Roman History", "Coastal Tours", "Food Tours"));
            g.setLanguages(List.of(new Language("Arabic", "Native"), new Language("English", "Fluent"), new Language("Italian", "Fluent")));
            g.setTourType(TourGuideEntity.TourType.PRIVATE);
            g.setProfilePhoto("https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400&h=400&fit=crop");
            guides.add(g);
        }
        if (mohamed != null) {
            TourGuideEntity g = new TourGuideEntity();
            g.setUser(mohamed); g.setName("Mohamed Ibrahim"); g.setEmail("mohamed@toda.com");
            g.setType(TourGuideEntity.GuideType.MALE); g.setPhone("+201556677889"); g.setCity("Sharm El Sheikh");
            g.setGuideType(TourGuideEntity.GuideTypeCategory.LOCAL_GUIDE);
            g.setYearsOfExperience(7);
            g.setSpecialization(List.of("Diving Tours", "Desert Adventures", "Marine Life"));
            g.setLanguages(List.of(new Language("Arabic", "Native"), new Language("English", "Fluent"), new Language("Russian", "Intermediate")));
            g.setTourType(TourGuideEntity.TourType.GROUP);
            g.setProfilePhoto("https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&h=400&fit=crop");
            guides.add(g);
        }

        tourGuideRepo.saveAll(guides);
        logger.info("Seeded {} tour guide profiles!", guides.size());
    }

    private void seedLandmarks() {
        if (landmarkRepository.count() > 0) {
            logger.info("Landmarks already exist ({}). Skipping.", landmarkRepository.count());
            return;
        }
        logger.info("Seeding 30 Egyptian landmarks with images...");

        List<Landmark> landmarks = List.of(
                new Landmark(null, "Pyramids of Giza", "The last surviving wonder of the ancient world, built around 2560 BC as royal tombs.", "Giza", "Al Haram, Giza", LandmarkType.HISTORICAL, "https://images.unsplash.com/photo-1503177119275-0aa32b3a9368?w=800&h=600&fit=crop", null),
                new Landmark(null, "Valley of the Kings", "Royal necropolis where pharaohs including Tutankhamun were buried.", "Luxor", "West Bank, Luxor", LandmarkType.HISTORICAL, "https://images.unsplash.com/photo-1553913861-c0fddf2619ee?w=800&h=600&fit=crop", null),
                new Landmark(null, "Karnak Temple", "One of the largest temple complexes in the world, built over 2,000 years.", "Luxor", "East Bank, Luxor", LandmarkType.HISTORICAL, "https://images.unsplash.com/photo-1539650116574-8efeb43e2750?w=800&h=600&fit=crop", null),
                new Landmark(null, "Philae Temple", "Island temple dedicated to goddess Isis, relocated to Agilkia Island.", "Aswan", "Agilkia Island", LandmarkType.HISTORICAL, "https://images.unsplash.com/photo-1598950750694-7e0dd0dd9e9e?w=800&h=600&fit=crop", null),
                new Landmark(null, "Abu Simbel Temples", "Twin rock temples carved by Ramesses II in 13th century BC.", "Aswan", "Abu Simbel", LandmarkType.HISTORICAL, "https://images.unsplash.com/photo-1608148895985-6e0c6884dd42?w=800&h=600&fit=crop", null),
                new Landmark(null, "Colossi of Memnon", "Two massive stone statues of Pharaoh Amenhotep III, 18 meters tall.", "Luxor", "West Bank, Luxor", LandmarkType.HISTORICAL, "https://images.unsplash.com/photo-1568322445389-f64e1cf1aab8?w=800&h=600&fit=crop", null),
                new Landmark(null, "The Great Sphinx", "Limestone statue with a lion's body and human head.", "Giza", "Giza Plateau", LandmarkType.MONUMENT, "https://images.unsplash.com/photo-1553913861-c0fddf2619ee?w=800&h=600&fit=crop", null),
                new Landmark(null, "Cairo Tower", "187-meter tower in Zamalek offering panoramic views of Cairo.", "Cairo", "Zamalek, Cairo", LandmarkType.MONUMENT, "https://images.unsplash.com/photo-1572252009286-268acec5ca0a?w=800&h=600&fit=crop", null),
                new Landmark(null, "Pompey's Pillar", "Roman triumphal column 27 meters tall, built around 300 AD.", "Alexandria", "Karmouz, Alexandria", LandmarkType.MONUMENT, "https://images.unsplash.com/photo-1568322445389-f64e1cf1aab8?w=800&h=600&fit=crop", null),
                new Landmark(null, "Unknown Soldier Memorial", "Pyramid-shaped monument honoring Egyptian soldiers.", "Cairo", "Nasr City, Cairo", LandmarkType.MONUMENT, "https://images.unsplash.com/photo-1539650116574-8efeb43e2750?w=800&h=600&fit=crop", null),
                new Landmark(null, "The Egyptian Museum", "Home to Tutankhamun's treasures and royal mummies.", "Cairo", "Tahrir Square, Cairo", LandmarkType.MUSEUM, "https://images.unsplash.com/photo-1599832327686-baebc9c56d7d?w=800&h=600&fit=crop", null),
                new Landmark(null, "Grand Egyptian Museum", "Largest archaeological museum, housing 100,000+ artifacts.", "Giza", "Al Remaya Square, Giza", LandmarkType.MUSEUM, "https://images.unsplash.com/photo-1503177119275-0aa32b3a9368?w=800&h=600&fit=crop", null),
                new Landmark(null, "Nubian Museum", "Showcasing Nubian culture, history, and art.", "Aswan", "Elephantine Island Road", LandmarkType.MUSEUM, "https://images.unsplash.com/photo-1598950750694-7e0dd0dd9e9e?w=800&h=600&fit=crop", null),
                new Landmark(null, "Alexandria National Museum", "1,800 artifacts spanning Egyptian history.", "Alexandria", "Horreya Avenue", LandmarkType.MUSEUM, "https://images.unsplash.com/photo-1553913861-c0fddf2619ee?w=800&h=600&fit=crop", null),
                new Landmark(null, "Al-Azhar Mosque", "One of Cairo's oldest mosques, founded 970 AD.", "Cairo", "Islamic Cairo", LandmarkType.RELIGIOUS, "https://images.unsplash.com/photo-1599832327686-baebc9c56d7d?w=800&h=600&fit=crop", null),
                new Landmark(null, "The Hanging Church", "Coptic church dating to 3rd century.", "Cairo", "Coptic Cairo", LandmarkType.RELIGIOUS, "https://images.unsplash.com/photo-1572252009286-268acec5ca0a?w=800&h=600&fit=crop", null),
                new Landmark(null, "Mosque of Ibn Tulun", "Oldest mosque in Cairo, built 876-879 AD.", "Cairo", "Saliba Street, Cairo", LandmarkType.RELIGIOUS, "https://images.unsplash.com/photo-1539650116574-8efeb43e2750?w=800&h=600&fit=crop", null),
                new Landmark(null, "Saint Catherine's Monastery", "UNESCO site at foot of Mount Sinai, since 565 AD.", "South Sinai", "Saint Catherine", LandmarkType.RELIGIOUS, "https://images.unsplash.com/photo-1608148895985-6e0c6884dd42?w=800&h=600&fit=crop", null),
                new Landmark(null, "White Desert", "Surreal chalk rock formations shaped by wind erosion.", "Farafra", "New Valley Governorate", LandmarkType.NATURAL, "https://images.unsplash.com/photo-1509316785289-025f5b846b35?w=800&h=600&fit=crop", null),
                new Landmark(null, "Siwa Oasis", "Remote oasis with salt lakes and ancient oracle temple.", "Siwa", "Matrouh Governorate", LandmarkType.NATURAL, "https://images.unsplash.com/photo-1598950750694-7e0dd0dd9e9e?w=800&h=600&fit=crop", null),
                new Landmark(null, "Ras Muhammad National Park", "Vibrant coral reefs and crystal-clear waters.", "Sharm El Sheikh", "South Sinai", LandmarkType.NATURAL, "https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=800&h=600&fit=crop", null),
                new Landmark(null, "Wadi Al-Hitan", "UNESCO site with fossil remains of earliest whales.", "Fayoum", "Wadi El-Rayan", LandmarkType.NATURAL, "https://images.unsplash.com/photo-1509316785289-025f5b846b35?w=800&h=600&fit=crop", null),
                new Landmark(null, "Sound and Light Show at Giza", "Evening spectacle with dramatic lighting at the Pyramids.", "Giza", "Giza Plateau", LandmarkType.ENTERTAINMENT, "https://images.unsplash.com/photo-1503177119275-0aa32b3a9368?w=800&h=600&fit=crop", null),
                new Landmark(null, "Al-Azhar Park", "30-hectare park with views of Cairo's skyline.", "Cairo", "Salah Salem Road", LandmarkType.ENTERTAINMENT, "https://images.unsplash.com/photo-1572252009286-268acec5ca0a?w=800&h=600&fit=crop", null),
                new Landmark(null, "Dahab Blue Hole", "World-famous diving site on Sinai coast.", "Dahab", "South Sinai", LandmarkType.ENTERTAINMENT, "https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=800&h=600&fit=crop", null),
                new Landmark(null, "Abou Tarek (Koshary)", "Cairo's most iconic koshary restaurant since 1950.", "Cairo", "Downtown Cairo", LandmarkType.RESTAURANT, "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&h=600&fit=crop", null),
                new Landmark(null, "Naguib Mahfouz Café", "Elegant café in Khan El Khalili.", "Cairo", "Islamic Cairo", LandmarkType.RESTAURANT, "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&h=600&fit=crop", null),
                new Landmark(null, "Khan El Khalili Bazaar", "Major souk dating to 14th century.", "Cairo", "Islamic Cairo", LandmarkType.SHOPPING, "https://images.unsplash.com/photo-1599832327686-baebc9c56d7d?w=800&h=600&fit=crop", null),
                new Landmark(null, "Aswan Souk", "Colorful traditional market along Nile corniche.", "Aswan", "Corniche El Nil", LandmarkType.SHOPPING, "https://images.unsplash.com/photo-1598950750694-7e0dd0dd9e9e?w=800&h=600&fit=crop", null),
                new Landmark(null, "Bibliotheca Alexandrina", "Modern tribute to ancient Library of Alexandria.", "Alexandria", "El Shatby", LandmarkType.OTHER, "https://images.unsplash.com/photo-1568322445389-f64e1cf1aab8?w=800&h=600&fit=crop", null)
        );

        landmarkRepository.saveAll(landmarks);
        logger.info("Seeded {} landmarks with images!", landmarks.size());
    }

    private void seedTrips() {
        if (tripRepository.count() > 0) {
            logger.info("Trips already exist. Skipping.");
            return;
        }
        logger.info("Seeding trips...");

        List<TourGuideEntity> guides = tourGuideRepo.findAll();
        List<Landmark> allLandmarks = landmarkRepository.findAll();
        if (guides.isEmpty()) { logger.warn("No guides. Skip trips."); return; }

        List<Trip> trips = new ArrayList<>();

        Trip t1 = new Trip();
        t1.setTitle("Pyramids & Sphinx Full Day Adventure");
        t1.setDescription("Full-day tour of the Giza Pyramid Complex, Sphinx, and Grand Egyptian Museum with lunch.");
        t1.setCity("Giza"); t1.setMeetingPoint("Giza Plateau Main Entrance");
        t1.setCategories(List.of("Historical", "Adventure", "Museum"));
        t1.setInclusions(List.of("Transport", "Lunch", "Entrance Fees", "Guide"));
        t1.setStartDate(LocalDate.of(2026, 7, 1)); t1.setEndDate(LocalDate.of(2026, 7, 1));
        t1.setPricePerTourist(75.0); t1.setMinGroupSize(2); t1.setMaxGroupSize(15);
        t1.setTourDuration("8 hours"); t1.setStatus(TripStatus.UPCOMING);
        t1.setTripCoverImage("https://images.unsplash.com/photo-1503177119275-0aa32b3a9368?w=800&h=500&fit=crop");
        t1.setTourGuide(guides.get(0));
        if (allLandmarks.size() >= 3) t1.setLandmarks(allLandmarks.subList(0, 3));
        trips.add(t1);

        Trip t2 = new Trip();
        t2.setTitle("Luxor: Valley of the Kings & Karnak");
        t2.setDescription("Explore the Valley of the Kings, Hatshepsut Temple, Colossi of Memnon, and Karnak Temple.");
        t2.setCity("Luxor"); t2.setMeetingPoint("Luxor Temple, East Bank");
        t2.setCategories(List.of("Historical", "Cultural", "Temple"));
        t2.setInclusions(List.of("Transport", "Guide", "Water", "Entrance Fees"));
        t2.setStartDate(LocalDate.of(2026, 7, 5)); t2.setEndDate(LocalDate.of(2026, 7, 6));
        t2.setPricePerTourist(120.0); t2.setMinGroupSize(1); t2.setMaxGroupSize(8);
        t2.setTourDuration("2 days"); t2.setStatus(TripStatus.UPCOMING);
        t2.setTripCoverImage("https://images.unsplash.com/photo-1553913861-c0fddf2619ee?w=800&h=500&fit=crop");
        t2.setTourGuide(guides.size() > 1 ? guides.get(1) : guides.get(0));
        if (allLandmarks.size() >= 6) t2.setLandmarks(allLandmarks.subList(1, 5));
        trips.add(t2);

        Trip t3 = new Trip();
        t3.setTitle("Aswan Nubian Village & Philae Temple");
        t3.setDescription("Felucca ride to Nubian Village, Philae Temple, and traditional Nubian dinner.");
        t3.setCity("Aswan"); t3.setMeetingPoint("Aswan Corniche");
        t3.setCategories(List.of("Cultural", "Nubian", "Nile"));
        t3.setInclusions(List.of("Felucca Ride", "Dinner", "Guide"));
        t3.setStartDate(LocalDate.of(2026, 7, 10)); t3.setEndDate(LocalDate.of(2026, 7, 10));
        t3.setPricePerTourist(90.0); t3.setMinGroupSize(2); t3.setMaxGroupSize(12);
        t3.setTourDuration("6 hours"); t3.setStatus(TripStatus.UPCOMING);
        t3.setTripCoverImage("https://images.unsplash.com/photo-1598950750694-7e0dd0dd9e9e?w=800&h=500&fit=crop");
        t3.setTourGuide(guides.size() > 2 ? guides.get(2) : guides.get(0));
        trips.add(t3);

        Trip t4 = new Trip();
        t4.setTitle("Alexandria: Pearl of the Mediterranean");
        t4.setDescription("Explore Catacombs, Pompey's Pillar, Qaitbay Citadel, and Bibliotheca Alexandrina.");
        t4.setCity("Alexandria"); t4.setMeetingPoint("Alexandria Main Train Station");
        t4.setCategories(List.of("Historical", "Coastal", "Museum"));
        t4.setInclusions(List.of("Transport", "Lunch", "Entrance Fees", "Guide"));
        t4.setStartDate(LocalDate.of(2026, 7, 15)); t4.setEndDate(LocalDate.of(2026, 7, 15));
        t4.setPricePerTourist(85.0); t4.setMinGroupSize(1); t4.setMaxGroupSize(10);
        t4.setTourDuration("10 hours"); t4.setStatus(TripStatus.UPCOMING);
        t4.setTripCoverImage("https://images.unsplash.com/photo-1568322445389-f64e1cf1aab8?w=800&h=500&fit=crop");
        t4.setTourGuide(guides.size() > 3 ? guides.get(3) : guides.get(0));
        trips.add(t4);

        Trip t5 = new Trip();
        t5.setTitle("Sharm El Sheikh: Ras Mohammed Diving");
        t5.setDescription("World-class diving at Ras Mohammed National Park with equipment and instructor.");
        t5.setCity("Sharm El Sheikh"); t5.setMeetingPoint("Hotel Lobby, Naama Bay");
        t5.setCategories(List.of("Diving", "Adventure", "Marine Life"));
        t5.setInclusions(List.of("Diving Equipment", "Boat Trip", "Lunch", "Instructor"));
        t5.setStartDate(LocalDate.of(2026, 7, 20)); t5.setEndDate(LocalDate.of(2026, 7, 20));
        t5.setPricePerTourist(110.0); t5.setMinGroupSize(1); t5.setMaxGroupSize(6);
        t5.setTourDuration("7 hours"); t5.setStatus(TripStatus.UPCOMING);
        t5.setTripCoverImage("https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=800&h=500&fit=crop");
        t5.setTourGuide(guides.size() > 4 ? guides.get(4) : guides.get(0));
        trips.add(t5);

        Trip t6 = new Trip();
        t6.setTitle("Islamic Cairo Walking Tour");
        t6.setDescription("Walk through centuries of Islamic history visiting Al-Azhar Mosque, Ibn Tulun, and Khan El Khalili.");
        t6.setCity("Cairo"); t6.setMeetingPoint("Al-Azhar Square");
        t6.setCategories(List.of("Historical", "Religious", "Walking"));
        t6.setInclusions(List.of("Guide", "Snacks", "Tea"));
        t6.setStartDate(LocalDate.of(2026, 7, 25)); t6.setEndDate(LocalDate.of(2026, 7, 25));
        t6.setPricePerTourist(45.0); t6.setMinGroupSize(2); t6.setMaxGroupSize(20);
        t6.setTourDuration("4 hours"); t6.setStatus(TripStatus.UPCOMING);
        t6.setTripCoverImage("https://images.unsplash.com/photo-1599832327686-baebc9c56d7d?w=800&h=500&fit=crop");
        t6.setTourGuide(guides.get(0));
        trips.add(t6);

        Trip t7 = new Trip();
        t7.setTitle("Nile Sunset Felucca Cruise");
        t7.setDescription("Relaxing felucca sail on the Nile at sunset with drinks and city views.");
        t7.setCity("Cairo"); t7.setMeetingPoint("Maadi Corniche");
        t7.setCategories(List.of("Relaxation", "Nile", "Sunset"));
        t7.setInclusions(List.of("Felucca", "Drinks", "Snacks"));
        t7.setStartDate(LocalDate.of(2026, 8, 1)); t7.setEndDate(LocalDate.of(2026, 8, 1));
        t7.setPricePerTourist(35.0); t7.setMinGroupSize(2); t7.setMaxGroupSize(10);
        t7.setTourDuration("2 hours"); t7.setStatus(TripStatus.UPCOMING);
        t7.setTripCoverImage("https://images.unsplash.com/photo-1539650116574-8efeb43e2750?w=800&h=500&fit=crop");
        t7.setTourGuide(guides.size() > 1 ? guides.get(1) : guides.get(0));
        trips.add(t7);

        Trip t8 = new Trip();
        t8.setTitle("White Desert Overnight Camping");
        t8.setDescription("Camp under the stars in the surreal White Desert with Bedouin guides and BBQ dinner.");
        t8.setCity("Farafra"); t8.setMeetingPoint("Bahariya Oasis Meeting Point");
        t8.setCategories(List.of("Adventure", "Desert", "Camping"));
        t8.setInclusions(List.of("4x4 Transport", "BBQ Dinner", "Camping Gear", "Breakfast"));
        t8.setStartDate(LocalDate.of(2026, 8, 5)); t8.setEndDate(LocalDate.of(2026, 8, 6));
        t8.setPricePerTourist(150.0); t8.setMinGroupSize(4); t8.setMaxGroupSize(8);
        t8.setTourDuration("2 days"); t8.setStatus(TripStatus.UPCOMING);
        t8.setTripCoverImage("https://images.unsplash.com/photo-1509316785289-025f5b846b35?w=800&h=500&fit=crop");
        t8.setTourGuide(guides.size() > 2 ? guides.get(2) : guides.get(0));
        trips.add(t8);

        tripRepository.saveAll(trips);
        logger.info("Seeded {} trips!", trips.size());
    }

    private void seedStaticTrips() {
        if (staticTripRepository.count() > 0) {
            logger.info("Static trips already exist. Skipping.");
            return;
        }
        logger.info("Seeding static trips...");

        List<UserEntity> users = userRepo.findAll();
        if (users.isEmpty()) { logger.warn("No users. Skip static trips."); return; }

        List<StaticTrip> trips = new ArrayList<>();

        StaticTrip s1 = new StaticTrip();
        s1.setTitle("Best Koshary in Cairo Food Tour");
        s1.setDescription("Join a local foodie to explore Cairo's best koshary spots, fresh juice bars, and traditional dessert places.");
        s1.setCity("Cairo"); s1.setMeetingPoint("Tahrir Square");
        s1.setStartDate(LocalDate.of(2026, 7, 12)); s1.setEndDate(LocalDate.of(2026, 7, 12));
        s1.setPrice(30.0); s1.setDuration("3 hours"); s1.setGroupSize(8);
        s1.setCategories(List.of("Food", "Cultural", "Walking"));
        s1.setInclusions(List.of("Food Tasting", "Drinks", "Guide"));
        s1.setImageUrl("https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&h=500&fit=crop");
        s1.setCreatedBy(users.get(0));
        trips.add(s1);

        StaticTrip s2 = new StaticTrip();
        s2.setTitle("Siwa Oasis Weekend Escape");
        s2.setDescription("3-day escape to Siwa Oasis featuring salt lakes, oracle temple, desert safari, and hot springs.");
        s2.setCity("Siwa"); s2.setMeetingPoint("Siwa Town Center");
        s2.setStartDate(LocalDate.of(2026, 8, 1)); s2.setEndDate(LocalDate.of(2026, 8, 3));
        s2.setPrice(200.0); s2.setDuration("3 days"); s2.setGroupSize(6);
        s2.setCategories(List.of("Nature", "Adventure", "Relaxation"));
        s2.setInclusions(List.of("Accommodation", "Transport", "Meals"));
        s2.setImageUrl("https://images.unsplash.com/photo-1598950750694-7e0dd0dd9e9e?w=800&h=500&fit=crop");
        s2.setCreatedBy(users.size() > 1 ? users.get(1) : users.get(0));
        trips.add(s2);

        StaticTrip s3 = new StaticTrip();
        s3.setTitle("Dahab Snorkeling & Beach Day");
        s3.setDescription("Snorkel at the famous Blue Hole and relax on Dahab's beautiful beaches.");
        s3.setCity("Dahab"); s3.setMeetingPoint("Dahab Promenade");
        s3.setStartDate(LocalDate.of(2026, 7, 18)); s3.setEndDate(LocalDate.of(2026, 7, 18));
        s3.setPrice(55.0); s3.setDuration("5 hours"); s3.setGroupSize(12);
        s3.setCategories(List.of("Beach", "Snorkeling", "Adventure"));
        s3.setInclusions(List.of("Snorkel Gear", "Lunch", "Transport"));
        s3.setImageUrl("https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=800&h=500&fit=crop");
        s3.setCreatedBy(users.size() > 2 ? users.get(2) : users.get(0));
        trips.add(s3);

        StaticTrip s4 = new StaticTrip();
        s4.setTitle("Coptic Cairo Heritage Walk");
        s4.setDescription("Explore the ancient Coptic quarter including the Hanging Church, Babylon Fortress, and Coptic Museum.");
        s4.setCity("Cairo"); s4.setMeetingPoint("Mar Girgis Metro Station");
        s4.setStartDate(LocalDate.of(2026, 7, 22)); s4.setEndDate(LocalDate.of(2026, 7, 22));
        s4.setPrice(25.0); s4.setDuration("3 hours"); s4.setGroupSize(15);
        s4.setCategories(List.of("Historical", "Religious", "Walking"));
        s4.setInclusions(List.of("Guide", "Museum Tickets"));
        s4.setImageUrl("https://images.unsplash.com/photo-1572252009286-268acec5ca0a?w=800&h=500&fit=crop");
        s4.setCreatedBy(users.size() > 3 ? users.get(3) : users.get(0));
        trips.add(s4);

        StaticTrip s5 = new StaticTrip();
        s5.setTitle("Mount Sinai Sunrise Trek");
        s5.setDescription("Night trek up Mount Sinai to watch an unforgettable sunrise, followed by St. Catherine's Monastery visit.");
        s5.setCity("South Sinai"); s5.setMeetingPoint("St. Catherine's Village");
        s5.setStartDate(LocalDate.of(2026, 8, 10)); s5.setEndDate(LocalDate.of(2026, 8, 10));
        s5.setPrice(65.0); s5.setDuration("8 hours"); s5.setGroupSize(10);
        s5.setCategories(List.of("Adventure", "Hiking", "Spiritual"));
        s5.setInclusions(List.of("Guide", "Flashlight", "Breakfast"));
        s5.setImageUrl("https://images.unsplash.com/photo-1608148895985-6e0c6884dd42?w=800&h=500&fit=crop");
        s5.setCreatedBy(users.size() > 4 ? users.get(4) : users.get(0));
        trips.add(s5);

        staticTripRepository.saveAll(trips);
        logger.info("Seeded {} static trips!", trips.size());
    }
}