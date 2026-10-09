
package com.team.karyera.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CareerAdviceRequest(

        @NotBlank(message = "Maraqlarını qeyd et.")
        @Size(max = 2000, message = "Mətn 2000 simvoldan çox ola bilməz.")
        String interests,

        @Size(max = 1000, message = "Fənlər 1000 simvoldan çox ola bilməz.")
        String favoriteSubjects,

        @Size(max = 1000, message = "Bacarıqlar 1000 simvoldan çox ola bilməz.")
        String skills,

        @Size(max = 1000, message = "Məqsəd 1000 simvoldan çox ola bilməz.")
        String goals
) {
}