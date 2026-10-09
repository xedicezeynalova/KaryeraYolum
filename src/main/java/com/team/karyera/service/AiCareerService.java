
package com.team.karyera.service;

import com.team.karyera.dto.CareerAdviceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class AiCareerService {

    private final RestClient restClient;
    private final String model;

    public AiCareerService(
            @Value("${ai.service.url:http://localhost:11434}")
            String aiUrl,
            @Value("${openai.model:qwen2.5:3b}")
            String model
    ) {
        this.model = model;

        this.restClient = RestClient.builder()
                .baseUrl(aiUrl.replaceAll("/+$", ""))
                .build();
    }

    public String getCareerAdvice(CareerAdviceRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Sorğu məlumatları boş ola bilməz."
            );
        }

        String prompt = """
                Sən KaryeraYolum platformasının peşəkar
                karyera məsləhətçisisən.

                İstifadəçinin məlumatları:
                Maraqlar: %s
                Sevdiyi fənlər: %s
                Bacarıqlar: %s
                Məqsədlər: %s

                Bu məlumatlara əsasən fərdi karyera planı hazırla.

                CAVABIN QURULUŞU:

                ÜÇ UYĞUN İXTİSAS

                1. Üç real və bir-birindən fərqli ixtisas seç.
                Hər biri üçün:
                - İxtisasın düzgün adını yaz.
                - Uyğunluğunu iki aydın cümlə ilə izah et.
                - Üç konkret öyrənilməli bacarıq göstər.

                İNKİŞAF PLANI

                - Öyrənilməli texnologiyaları və bacarıqları yaz.
                - Başlamaq üçün üç praktik addım göstər.
                - Bir kiçik layihə ideyası ver.

                YEKUN TÖVSİYƏ

                - Ən uyğun bir ixtisası seç.
                - Seçimin səbəbini iki cümlə ilə izah et.

                QAYDALAR:

                - Yalnız Azərbaycan dilində yaz.
                - Azərbaycan hərflərindən düzgün istifadə et.
                - Sadə, aydın və qrammatik cümlələr qur.
                - Real ixtisas adlarından istifadə et.
                - Eyni fikri təkrarlama.
                - İstifadəçinin qeyd etmədiyi bacarıqları
                  ona aid etmə.
                - Məlumat çatışmırsa, bunu açıq bildir.
                - Konkret texnologiya, bacarıq və addımlar göstər.
                - İxtisas adlarını uydurma.
                - Təlimatı təkrarlama, birbaşa cavab ver.
                - Markdown başlıqlarından və siyahılardan istifadə et.
                - Cavabı yarımçıq qoyma.
                """.formatted(
                safeValue(request.interests()),
                safeValue(request.favoriteSubjects()),
                safeValue(request.skills()),
                safeValue(request.goals())
        );

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content",
                                "Sən təcrübəli karyera məsləhətçisisən. "
                                        + "Azərbaycan dilində təbii, aydın və "
                                        + "məntiqli cavab ver. "
                                        + "İxtisas adlarını düzgün yaz."
                        ),
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "stream", false,
                "keep_alive", "10m",
                "options", Map.of(
                        "temperature", 0.2,
                        "num_predict", 700
                )
        );

        try {
            Map<?, ?> response = restClient.post()
                    .uri("/api/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            return extractAnswer(response);

        } catch (RestClientResponseException ex) {
            System.err.println(
                    "Ollama HTTP xətası: "
                            + ex.getStatusCode().value()
            );
            System.err.println(
                    "Ollama cavabı: "
                            + ex.getResponseBodyAsString()
            );

            throw new IllegalStateException(
                    "AI sorğusu uğursuz oldu. HTTP status: "
                            + ex.getStatusCode().value()
            );

        } catch (RestClientException ex) {
            System.err.println(
                    "Ollama bağlantı xətası: " + ex.getMessage()
            );

            throw new IllegalStateException(
                    "Ollama ilə əlaqə qurulmadı. "
                            + "Ollama xidmətini yoxla."
            );
        }
    }

    private String extractAnswer(Map<?, ?> response) {

        if (response == null) {
            throw new IllegalStateException(
                    "Ollama boş cavab qaytardı."
            );
        }

        Object messageObject = response.get("message");

        if (!(messageObject instanceof Map<?, ?> message)) {
            throw new IllegalStateException(
                    "AI mesajı tapılmadı."
            );
        }

        Object contentObject = message.get("content");

        if (!(contentObject instanceof String content)
                || content.isBlank()) {
            throw new IllegalStateException(
                    "AI boş mətn qaytardı."
            );
        }

        return content.trim();
    }

    private String safeValue(String value) {
        if (value == null || value.isBlank()) {
            return "Qeyd edilməyib";
        }

        return value.trim();
    }
}
