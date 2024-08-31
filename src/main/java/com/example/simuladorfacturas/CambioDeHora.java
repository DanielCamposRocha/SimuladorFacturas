package com.example.simuladorfacturas;

import java.time.LocalDateTime;

public enum CambioDeHora  {
    VERANO_2021(LocalDateTime.of(2021, 3, 28,2,0)),
    INVIERNO_2021(LocalDateTime.of(2021, 10, 31,2,0)),
    VERANO_2022(LocalDateTime.of(2022, 3, 27,2,0)),
    INVIERNO_2022(LocalDateTime.of(2022, 10, 30,2,0)),
    VERANO_2023(LocalDateTime.of(2023, 3, 26,2,0)),
    INVIERNO_2023(LocalDateTime.of(2023, 10, 29,2,0)),
    VERANO_2024(LocalDateTime.of(2024, 3, 31,2,0)),
    INVIERNO_2024(LocalDateTime.of(2024, 10, 27,2,0)),
    VERANO_2025(LocalDateTime.of(2025,3,30,2,0)),
    INVIERNO_2025(LocalDateTime.of(2025, 10, 26,2,0)),
    VERANO_2026(LocalDateTime.of(2026,3,29,2,0)),
    INVIERNO_2026(LocalDateTime.of(2026, 10, 25,2,0));


    private final LocalDateTime fecha;

    CambioDeHora(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

}
