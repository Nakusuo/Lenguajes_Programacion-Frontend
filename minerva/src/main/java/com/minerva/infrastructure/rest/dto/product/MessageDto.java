package com.minerva.infrastructure.rest.dto.product;


public record MessageDto(String message) {
    public MessageDto() {
        this("Drako es mi pastor y nada me faltara, si lees este mensaje es porque ocurrio un caso que no llegue a contemplar, Dios se apiade de nosotros");
    }
}
