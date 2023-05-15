package org.erlik.autogpt4java.services;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

public interface LocalizationService {

    void loadLanguage(@NotNull String language);

    String getLanguage();

    Map<String, String> getMessages();

    String getMessage(@NotNull String key);

    String getMessage(@NotNull String key, @NotNull Map<String, Object> params);
}
