package org.erlik.autogpt4java.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.erlik.autogpt4java.exceptions.LocalizationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LocalizationServiceImpl implements LocalizationService {

    private final ObjectMapper objectMapper;

    @Getter
    private final HashMap<String, String> messages = new HashMap<>();

    @Getter
    private String language;

    public LocalizationServiceImpl(ObjectMapper objectMapper, @Value("${language:en}") String language) {
        this.objectMapper = objectMapper;
        loadLanguage(language);
    }

    @Override
    public void loadLanguage(@NotNull String language) {
        log.debug("Loading language " + language);
        this.language = language;
        File file = getFile(language);

        try {
            Map<String, Object> map = this.objectMapper.readValue(file, new TypeReference<HashMap<String, Object>>() {});
            this.messages.clear();
            this.messages.putAll(flattenMap(map));
            log.debug("Loaded " + messages.size() + " translations");
        } catch (IOException e) {
            throw new LocalizationException("Error loading language " + language, e);
        }

    }

    public Map<String, String> getMessage() {
        return messages;
    }

    @NotNull
    private static File getFile(String language) {
        String languageFileName = "i18n/" + language + ".json";
        try {
            final File file = new ClassPathResource(languageFileName).getFile();
            log.debug("File address = " + file.getAbsolutePath());
            return file;
        } catch (IOException e) {
            throw new LocalizationException("File not found in classpath: " + languageFileName);
        }
    }

    @Override
    public String getMessage(@NotNull String key) {
        return Optional.ofNullable(this.messages.get(key)).orElseThrow(() -> new LocalizationException("Key " + key + " not found"));

    }

    @Override
    public String getMessage(@NotNull String key, @NotNull Map<String, Object> params) {
        log.debug("Getting message for key " + key + " with params " + params);
        String message = getMessage(key);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            message = message.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }
        return message;
    }

    public static Map<String, String> transformJsonToFlatMap(JsonNode node, String prefix){

            Map<String,String> jsonMap = new HashMap<>();

            if(node.isArray()) {
                //Iterate over all array nodes
                int i = 0;
                for (JsonNode arrayElement : node) {
                    jsonMap.putAll(transformJsonToFlatMap(arrayElement, prefix+"[" + i + "]"));
                    i++;
                }
            }else if(node.isObject()) {
                Iterator<String> fieldNames = node.fieldNames();
                String curPrefixWithDot = (prefix == null || prefix.trim().length() == 0)
                                          ? ""
                                          : prefix + ".";
                //list all keys and values
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    JsonNode fieldValue = node.get(fieldName);
                    jsonMap.putAll(transformJsonToFlatMap(fieldValue, curPrefixWithDot + fieldName));
                }

            }else {
                //basic type
                jsonMap.put(prefix,node.asText());
                System.out.println(prefix+"="+node.asText());
            }

            return jsonMap;
    }

    /*
        public static Map<String, String> transformJsonToMap(JsonNode node, String prefix){

            Map<String,String> jsonMap = new HashMap<>();

            if(node.isArray()) {
                //Iterate over all array nodes
                int i = 0;
                for (JsonNode arrayElement : node) {
                    jsonMap.putAll(transformJsonToMap(arrayElement, prefix+"[" + i + "]"));
                    i++;
                }
            }else if(node.isObject()){
                Iterator<String> fieldNames = node.fieldNames();
                String curPrefixWithDot = (prefix==null || prefix.trim().length()==0) ? "" : prefix+".";
                //list all keys and values
                while(fieldNames.hasNext()){
                    String fieldName = fieldNames.next();
                    JsonNode fieldValue = node.get(fieldName);
                    jsonMap.putAll(transformJsonToMap(fieldValue, curPrefixWithDot+fieldName));
                }
            }else {
                //basic type
                jsonMap.put(prefix,node.asText());
                System.out.println(prefix+"="+node.asText());
            }

            return jsonMap;
        }
        */

    public Map<String, String> flattenMap(Map<?, ?> source) {
        Map<String, String> converted = new HashMap<>();

        for (Entry<?, ?> entry : source.entrySet()) {
            if (entry.getValue() instanceof Map) {
                flattenMap((Map<String, Object>) entry.getValue())
                    .forEach((key, value) -> converted.put(entry.getKey() + "." + key, value));
            } else {
                converted.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return converted;
    }
}
