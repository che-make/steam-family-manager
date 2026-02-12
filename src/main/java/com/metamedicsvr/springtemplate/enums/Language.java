package com.metamedicsvr.springtemplate.enums;

import com.metamedicsvr.springtemplate.error.exception.LanguageNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Language {
    ENGLISH("EN"),
    SPANISH("ES"),
    SOMALI("SO");

    private final String initial;

    public static Language fromInitial(String initial) {
        return Arrays.stream(Language.values())
                .filter(language -> language.getInitial().equals(initial))
                .findFirst()
                .orElseThrow(() -> new LanguageNotFoundException("Language not found"));
    }


}
