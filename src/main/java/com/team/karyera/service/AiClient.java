
package com.team.karyera.service;

import com.team.karyera.exception.AiUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class AiClient {

    @Value("${ai.service.url:http://localhost:8000}")
    private String serviceUrl;

    @Value("${ai.mock:true}")
    private boolean mock;

    @Value("${ai.connect-timeout:5000}")
    private long connectTimeout;

    @Value("${ai.read-timeout:15000}")
    private long readTimeout;

    private final HttpClient httpClient;

    public AiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String ask(String question) {
        if (mock) {
            return "Sualınız qəbul edildi: " + question
                    + "\n\nBu, test rejimində yaradılmış nümunə cavabdır. "
                    + "Real AI cavabı üçün AI servisi qoşulmalıdır.";
        }

        try {
            String escapedQuestion = question
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");

            String jsonBody = "{\"question\":\"" + escapedQuestion + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serviceUrl + "/ask"))
                    .timeout(Duration.ofMillis(readTimeout))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new AiUnavailableException(
                        "AI servisi sorğuya uğursuz cavab verdi."
                );
            }

            return response.body();

        } catch (AiUnavailableException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new AiUnavailableException(
                    "AI servisinə qoşulmaq mümkün olmadı.",
                    exception
            );
        }
    }
}
