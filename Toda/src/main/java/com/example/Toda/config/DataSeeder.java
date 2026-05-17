package com.example.Toda.config;

import com.example.Toda.Entity.Landmark;
import com.example.Toda.Entity.LandmarkType;
import com.example.Toda.Entity.Role;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.repo.LandmarkRepository;
import com.example.Toda.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepo userRepo;
    private final LandmarkRepository landmarkRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepo userRepo, LandmarkRepository landmarkRepository, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.landmarkRepository = landmarkRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedLandmarks();
    }

    private void seedUsers() {
        // Check if admin user already exists
        if (userRepo.findByEmail("admin@toda.com").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmail("admin@toda.com");
            admin.setPassword(passwordEncoder.encode("0000"));
            admin.setRole(Role.ADMIN);
            userRepo.save(admin);
            logger.info("Admin user created successfully! Email: admin@toda.com | Password: 0000");
        } else {
            logger.info("Admin user already exists. Skipping creation.");
        }

        if (userRepo.findByEmail("tourist@toda.com").isEmpty()) {
            UserEntity tourist = new UserEntity();
            tourist.setUsername("tourist");
            tourist.setEmail("tourist@toda.com");
            tourist.setPassword(passwordEncoder.encode("0000"));
            tourist.setRole(Role.TOURIST);
            userRepo.save(tourist);
            logger.info("Tourist user created successfully! Email: tourist@toda.com | Password: 0000");
        } else {
            logger.info("Tourist user already exists. Skipping creation.");
        }

        if (userRepo.findByEmail("tourguide@toda.com").isEmpty()) {
            UserEntity tourguide = new UserEntity();
            tourguide.setUsername("tourguide");
            tourguide.setEmail("tourguide@toda.com");
            tourguide.setPassword(passwordEncoder.encode("0000"));
            tourguide.setRole(Role.TOURGUIDE);
            userRepo.save(tourguide);
            logger.info("Tourguide user created successfully! Email: tourguide@toda.com | Password: 0000");
        } else {
            logger.info("Tourguide user already exists. Skipping creation.");
        }
    }

    private void seedLandmarks() {
        if (landmarkRepository.count() > 0) {
            logger.info("Landmarks already exist (count={}). Skipping seeding.", landmarkRepository.count());
            return;
        }

        logger.info("Seeding 30 Egyptian landmarks...");

        List<Landmark> landmarks = List.of(
                // ===== HISTORICAL (6) =====
                new Landmark(null, "Pyramids of Giza",
                        "The last surviving wonder of the ancient world, the Great Pyramids were built around 2560 BC as royal tombs for pharaohs Khufu, Khafre, and Menkaure.",
                        "Giza", "Al Haram, Nazlet El-Semman, Giza Governorate", LandmarkType.HISTORICAL, null, null),
                new Landmark(null, "Valley of the Kings",
                        "A royal necropolis where pharaohs including Tutankhamun were buried. Over 60 tombs have been discovered in this UNESCO World Heritage Site.",
                        "Luxor", "West Bank, Luxor, Luxor Governorate", LandmarkType.HISTORICAL, null, null),
                new Landmark(null, "Karnak Temple",
                        "One of the largest temple complexes in the world, Karnak was built over 2,000 years and dedicated to the Theban gods Amun, Mut, and Khonsu.",
                        "Luxor", "Karnak, East Bank, Luxor", LandmarkType.HISTORICAL, null, null),
                new Landmark(null, "Philae Temple",
                        "An island temple complex dedicated to the goddess Isis, relocated to Agilkia Island to save it from the Aswan Dam's rising waters.",
                        "Aswan", "Agilkia Island, Aswan", LandmarkType.HISTORICAL, null, null),
                new Landmark(null, "Abu Simbel Temples",
                        "Twin rock temples carved out of a mountainside by Ramesses II in the 13th century BC, relocated in a massive UNESCO rescue campaign.",
                        "Aswan", "Abu Simbel, Aswan Governorate", LandmarkType.HISTORICAL, null, null),
                new Landmark(null, "Colossi of Memnon",
                        "Two massive stone statues of Pharaoh Amenhotep III, standing 18 meters tall on the West Bank of the Nile since 1350 BC.",
                        "Luxor", "West Bank, Luxor", LandmarkType.HISTORICAL, null, null),

                // ===== MONUMENT (4) =====
                new Landmark(null, "The Great Sphinx",
                        "A limestone statue of a mythical creature with a lion's body and human head, believed to represent Pharaoh Khafre. It is one of the oldest and largest sculptures in the world.",
                        "Giza", "Al Haram, Giza Plateau", LandmarkType.MONUMENT, null, null),
                new Landmark(null, "Cairo Tower",
                        "A 187-meter free-standing tower in Zamalek offering panoramic views of Cairo. Built from 1956 to 1961, it resembles a lotus plant.",
                        "Cairo", "Zamalek, Cairo Governorate", LandmarkType.MONUMENT, null, null),
                new Landmark(null, "Pompey's Pillar",
                        "A Roman triumphal column standing 27 meters tall, built in honor of Emperor Diocletian around 300 AD in ancient Alexandria.",
                        "Alexandria", "Karmouz, Alexandria", LandmarkType.MONUMENT, null, null),
                new Landmark(null, "Unknown Soldier Memorial",
                        "A pyramid-shaped monument in Cairo honoring Egyptian soldiers who died in the 1973 October War and other conflicts.",
                        "Cairo", "Nasr City, Cairo", LandmarkType.MONUMENT, null, null),

                // ===== MUSEUM (4) =====
                new Landmark(null, "The Egyptian Museum",
                        "Home to an extensive collection of ancient Egyptian antiquities including Tutankhamun's treasures and royal mummies.",
                        "Cairo", "Tahrir Square, Downtown Cairo", LandmarkType.MUSEUM, null, null),
                new Landmark(null, "Grand Egyptian Museum",
                        "The largest archaeological museum in the world, housing over 100,000 artifacts including the complete Tutankhamun collection.",
                        "Giza", "Al Remaya Square, Giza", LandmarkType.MUSEUM, null, null),
                new Landmark(null, "Nubian Museum",
                        "A UNESCO-awarded museum showcasing Nubian culture, history, and art, preserving the heritage of the region affected by the Aswan Dam.",
                        "Aswan", "Elephantine Island Road, Aswan", LandmarkType.MUSEUM, null, null),
                new Landmark(null, "Alexandria National Museum",
                        "Houses over 1,800 artifacts spanning Egyptian history from the Pharaonic era through the Greco-Roman and Coptic-Islamic periods.",
                        "Alexandria", "110 Horreya Avenue, Alexandria", LandmarkType.MUSEUM, null, null),

                // ===== RELIGIOUS (4) =====
                new Landmark(null, "Al-Azhar Mosque",
                        "One of Cairo's oldest mosques and a leading center of Islamic learning, founded in 970 AD by the Fatimid dynasty.",
                        "Cairo", "Al Azhar Street, Islamic Cairo", LandmarkType.RELIGIOUS, null, null),
                new Landmark(null, "The Hanging Church",
                        "One of the oldest Coptic churches in Egypt dating to the 3rd century, suspended above the gatehouse of an old Roman fortress.",
                        "Cairo", "Old Cairo, Coptic Cairo", LandmarkType.RELIGIOUS, null, null),
                new Landmark(null, "Mosque of Ibn Tulun",
                        "The oldest mosque in Cairo and the largest in terms of area, built in 876-879 AD with a unique spiral minaret.",
                        "Cairo", "Saliba Street, Cairo", LandmarkType.RELIGIOUS, null, null),
                new Landmark(null, "Saint Catherine's Monastery",
                        "A UNESCO World Heritage Site at the foot of Mount Sinai, one of the oldest continuously functioning Christian monasteries since 565 AD.",
                        "South Sinai", "Saint Catherine, South Sinai Governorate", LandmarkType.RELIGIOUS, null, null),

                // ===== NATURAL (4) =====
                new Landmark(null, "White Desert",
                        "A surreal desert landscape with chalk rock formations shaped by wind erosion into mushroom-like pillars and other fantastical shapes.",
                        "Farafra", "Farafra Depression, New Valley Governorate", LandmarkType.NATURAL, null, null),
                new Landmark(null, "Siwa Oasis",
                        "A remote oasis in the Western Desert famous for its salt lakes, ancient oracle temple of Amun, and unique Berber-influenced culture.",
                        "Siwa", "Siwa Oasis, Matrouh Governorate", LandmarkType.NATURAL, null, null),
                new Landmark(null, "Ras Muhammad National Park",
                        "Egypt's first national park, renowned for its vibrant coral reefs, diverse marine life, and crystal-clear waters at the tip of the Sinai Peninsula.",
                        "Sharm El Sheikh", "Ras Muhammad, South Sinai", LandmarkType.NATURAL, null, null),
                new Landmark(null, "Wadi Al-Hitan (Whale Valley)",
                        "A UNESCO World Heritage Site containing invaluable fossil remains of the earliest extinct whales, showing their transition from land to sea.",
                        "Fayoum", "Wadi El-Rayan, Fayoum Governorate", LandmarkType.NATURAL, null, null),

                // ===== ENTERTAINMENT (3) =====
                new Landmark(null, "Sound and Light Show at Giza",
                        "An evening spectacle narrating ancient Egyptian history with dramatic lighting and sound effects against the backdrop of the Pyramids and Sphinx.",
                        "Giza", "Giza Plateau, Pyramids Road", LandmarkType.ENTERTAINMENT, null, null),
                new Landmark(null, "Al-Azhar Park",
                        "A beautifully landscaped 30-hectare park offering stunning views of Cairo's historic skyline, gardens, restaurants, and walking paths.",
                        "Cairo", "Salah Salem Road, Cairo", LandmarkType.ENTERTAINMENT, null, null),
                new Landmark(null, "Dahab Blue Hole",
                        "A world-famous diving site on the Sinai coast, known for its deep blue sinkhole attracting snorkelers and scuba divers from around the globe.",
                        "Dahab", "Dahab, South Sinai Governorate", LandmarkType.ENTERTAINMENT, null, null),

                // ===== RESTAURANT (2) =====
                new Landmark(null, "Abou Tarek (Koshary)",
                        "Cairo's most iconic koshary restaurant serving Egypt's beloved national dish since 1950. A must-visit culinary experience.",
                        "Cairo", "17 El Maarad Street, Downtown Cairo", LandmarkType.RESTAURANT, null, null),
                new Landmark(null, "Naguib Mahfouz Café",
                        "An elegant café in the heart of Khan El Khalili, named after Egypt's Nobel Prize-winning author, offering traditional Egyptian cuisine and tea.",
                        "Cairo", "Khan El Khalili, Islamic Cairo", LandmarkType.RESTAURANT, null, null),

                // ===== SHOPPING (2) =====
                new Landmark(null, "Khan El Khalili Bazaar",
                        "A major souk in Islamic Cairo dating to the 14th century, famous for its vibrant shops selling jewelry, spices, textiles, and souvenirs.",
                        "Cairo", "Al Azhar Street, Islamic Cairo", LandmarkType.SHOPPING, null, null),
                new Landmark(null, "Aswan Souk",
                        "A colorful traditional market along the Nile corniche, known for spices, Nubian crafts, perfumes, and fresh produce.",
                        "Aswan", "Corniche El Nil, Aswan", LandmarkType.SHOPPING, null, null),

                // ===== OTHER (1) =====
                new Landmark(null, "Bibliotheca Alexandrina",
                        "A modern tribute to the ancient Library of Alexandria, housing millions of books, museums, planetarium, and a stunning tilted disc-shaped architecture.",
                        "Alexandria", "Corniche, El Shatby, Alexandria", LandmarkType.OTHER, null, null)
        );

        landmarkRepository.saveAll(landmarks);
        logger.info("Successfully seeded {} Egyptian landmarks!", landmarks.size());
    }
}