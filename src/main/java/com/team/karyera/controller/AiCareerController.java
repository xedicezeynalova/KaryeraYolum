
package com.team.karyera.controller;

import com.team.karyera.dto.CareerAdviceRequest;
import com.team.karyera.dto.CareerAdviceResponse;
import com.team.karyera.service.AiCareerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiCareerController {

    private final AiCareerService aiCareerService;

    public AiCareerController(AiCareerService aiCareerService) {
        this.aiCareerService = aiCareerService;
    }

    @PostMapping("/career-advice")
    public CareerAdviceResponse getCareerAdvice(
            @Valid @RequestBody CareerAdviceRequest request
    ) {
        String advice = aiCareerService.getCareerAdvice(request);

        return new CareerAdviceResponse(advice);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleAiError(
            IllegalStateException ex
    ) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(
            org.springframework.web.bind.MethodArgumentNotValidException.class
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationError() {
        return Map.of(
                "error",
                "Məlumatları yoxla. Maraqlar sahəsi boş qala bilməz."
        );
    }
}
