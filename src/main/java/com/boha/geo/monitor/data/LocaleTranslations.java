package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("LocaleTranslations")
public class LocaleTranslations {
    String localeTranslationsId;
    String locale;
    String translations;
    String date;
}
