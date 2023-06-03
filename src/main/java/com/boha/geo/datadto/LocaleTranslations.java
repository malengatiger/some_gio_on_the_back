package com.boha.geo.datadto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("localeTranslations")
public class LocaleTranslations {
    String localeTranslationsId;
    String locale;
    String translations;
    String date;
}
