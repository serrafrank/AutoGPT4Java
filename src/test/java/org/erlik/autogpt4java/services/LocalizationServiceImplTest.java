package org.erlik.autogpt4java.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.erlik.autogpt4java.exceptions.LocalizationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LocalizationServiceImplTest {

    @Autowired
    private LocalizationService localizationService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("load a language from the default file")
    void testGetLanguage() {
        // Given an existing language
        final var expectedLanguage = "en";
        // When I get the language
        // Then the language is returned
        assertEquals(expectedLanguage, localizationService.getLanguage());
        assertNotNull(localizationService.getMessages());
    }

    @Test
    @DisplayName("load a language from an existing file")
    void testLoadLanguage() {
        // Given an existing language
        final var language = "en";
        // When I load the language
        final var localizationService = new LocalizationServiceImpl(objectMapper, language);
        // Then the language is loaded
        assertEquals(language, localizationService.getLanguage());
    }

    @Test
    @DisplayName("load a language from a non existing file throws an exception")
    void testLoadLanguageWithNonExistingFile() {
        // Given a non existing language
        final var language = "xx";
        // When I load the language
        // Then an exception is thrown
        assertThrows(LocalizationException.class, () -> new LocalizationServiceImpl(objectMapper, language));
    }

    @Test
    @DisplayName("get a message with an existing key returns the message")
    void getMessageWithKeyReturnMessage() {
        // Given an existing key
        final var key = "hello_world";
        // When I get the message
        final var message = localizationService.getMessage(key);
        // Then the message is returned
        assertThat(message).isNotEmpty()
            .isEqualTo("Hello World!");
    }

    @Test
    @DisplayName("get a message with an invalid key throws an exception")
    void getMessageWithInvalidKeyThrowsException() {
        // Given an invalid key
        final var key = "invalid_key";
        // When I get the message
        // Then an exception is thrown
        assertThrows(LocalizationException.class, () -> localizationService.getMessage(key));
    }

    @Test
    @DisplayName("get a message with a subkey returns the message")
    void getMessageWithSubkeyReturnMessage() {
        // Given an existing key
        final var key = "key.subKey";
        // When I get the message
        final var message = localizationService.getMessage(key);
        // Then the message is returned
        assertThat(message).isNotEmpty()
            .isEqualTo("It works!");
    }

    @Test
    @DisplayName("get a message with a null key throws an exception")
    void getMessageWithNullKeyThrowsException() {
        // Given a null key
        final String key = null;
        // When I get the message
        // Then an exception is thrown
        assertThrows(IllegalArgumentException.class, () -> localizationService.getMessage(key));
    }

    @Test
    @DisplayName("get a message with an existing key and parameters returns the message")
    void getMessageWithKeyAndParametersReturnMessage() {
        // Given an existing key
        final var key = "hello_param";
        // And some parameters
        final Map<String, Object> params = Map.of("name", "World");
        // When I get the message
        final var message = localizationService.getMessage(key, params);
        // Then the message is returned
        assertThat(message).isNotEmpty()
            .isEqualTo("Hello World!");
    }

    @Test
    @DisplayName("get a message with an existing key and empty parameters returns the message")
    void getMessageWithKeyAndEmptyParametersReturnMessage() {
        // Given an existing key
        final var key = "hello_param";
        // And empty parameters
        final Map<String, Object> params = Map.of();
        // When I get the message
        final var message = localizationService.getMessage(key, params);
        // Then the message is returned
        assertThat(message).isNotEmpty()
            .isEqualTo("Hello ${name}!");
    }

    @Test
    @DisplayName("get a message with an invalid key and parameters throws an exception")
    void getMessageWithInvalidKeyAndParametersThrowException() {
        // Given an invalid key
        final var key = "invalid_key";
        // And some parameters
        final Map<String, Object> params = Map.of("name", "World");
        // When I get the message
        // Then an exception is thrown
        assertThrows(LocalizationException.class, () -> localizationService.getMessage(key, params));
    }

    @Test
    @DisplayName("get a message with a key and null parameters throws an exception")
    void getMessageWithKeyAndNullParametersThrowException(){
        // Given a key
        final var key = "hello_param";
        // And null parameters
        final Map<String, Object> params = null;
        // When I get the message
        // Then an exception is thrown
        assertThrows(IllegalArgumentException.class, () -> localizationService.getMessage(key, params));
    }

}