
package com.team.karyera.service;

import com.team.karyera.dto.MajorRecommendation;
import com.team.karyera.dto.MentorRecommendation;
import com.team.karyera.dto.ProfileRequest;
import com.team.karyera.dto.RankRequest;
import com.team.karyera.dto.RankResponse;
import com.team.karyera.dto.RecommendResponse;
import com.team.karyera.model.Major;
import com.team.karyera.model.Mentor;
import com.team.karyera.repository.MajorRepository;
import com.team.karyera.repository.MentorRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RecommendService {

    private final MajorRepository majorRepository;
    private final MentorRepository mentorRepository;

    public RecommendService(
            MajorRepository majorRepository,
            MentorRepository mentorRepository
    ) {
        this.majorRepository = majorRepository;
        this.mentorRepository = mentorRepository;
    }

    public RecommendResponse recommend(ProfileRequest request) {
        String interests = safe(request.getInterests());
        String skills = safe(request.getSkills());
        String category = safe(request.getPreferredCategory());

        List<MajorRecommendation> majors = majorRepository.findAll()
                .stream()
                .map(major -> toMajorRecommendation(
                        major,
                        calculateScore(
                                interests + " " + skills,
                                major.getName() + " "
                                        + major.getCategory() + " "
                                        + major.getDescription() + " "
                                        + major.getCareers()
                        )
                ))
                .filter(item -> category.isBlank()
                        || item.getCategory() == null
                        || item.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparingDouble(
                        MajorRecommendation::getScore
                ).reversed())
                .limit(5)
                .toList();

        List<MentorRecommendation> mentors = mentorRepository.findAll()
                .stream()
                .map(mentor -> toMentorRecommendation(
                        mentor,
                        calculateScore(
                                interests + " " + skills,
                                mentor.getProfession() + " "
                                        + mentor.getCategory() + " "
                                        + mentor.getBio()
                        )
                ))
                .filter(item -> category.isBlank()
                        || item.getCategory() == null
                        || item.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparingDouble(
                        MentorRecommendation::getScore
                ).reversed())
                .limit(5)
                .toList();

        return new RecommendResponse(
                majors,
                mentors,
                "Tövsiyələr maraqlar, bacarıqlar və kateqoriya üzrə "
                        + "sadə mətn uyğunluğu ilə hazırlanıb."
        );
    }

    public RankResponse rankMajors(RankRequest request) {
        String searchText = safe(request.getInterests())
                + " " + safe(request.getSkills());

        List<MajorRecommendation> recommendations =
                majorRepository.findByCategoryIgnoreCase(
                                request.getCategory()
                        )
                        .stream()
                        .map(major -> toMajorRecommendation(
                                major,
                                calculateScore(
                                        searchText,
                                        major.getName() + " "
                                                + major.getDescription() + " "
                                                + major.getCareers()
                                )
                        ))
                        .sorted(Comparator.comparingDouble(
                                MajorRecommendation::getScore
                        ).reversed())
                        .toList();

        return new RankResponse(
                request.getCategory(),
                recommendations
        );
    }

    private MajorRecommendation toMajorRecommendation(
            Major major,
            double score
    ) {
        return new MajorRecommendation(
                major.getId(),
                major.getName(),
                major.getCategory(),
                major.getDescription(),
                major.getCareers(),
                score
        );
    }

    private MentorRecommendation toMentorRecommendation(
            Mentor mentor,
            double score
    ) {
        return new MentorRecommendation(
                mentor.getId(),
                mentor.getFullName(),
                mentor.getProfession(),
                mentor.getCategory(),
                mentor.getBio(),
                score
        );
    }

    private double calculateScore(String keywords, String content) {
        String normalizedKeywords = safe(keywords).toLowerCase();
        String normalizedContent = safe(content).toLowerCase();

        if (normalizedKeywords.isBlank()
                || normalizedContent.isBlank()) {
            return 0.0;
        }

        String[] words = normalizedKeywords.split("\\s+");
        int matches = 0;
        int total = 0;

        for (String word : words) {
            String cleaned = word.replaceAll("[^\\p{L}\\p{N}]", "");

            if (cleaned.length() < 3) {
                continue;
            }

            total++;

            if (normalizedContent.contains(cleaned)) {
                matches++;
            }
        }

        if (total == 0) {
            return 0.0;
        }

        return Math.round((double) matches / total * 100.0 * 100.0)
                / 100.0;
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
