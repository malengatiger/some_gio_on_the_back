package com.boha.geo.datadto.mcountry;
import com.boha.geo.monitor.data.Position;
import com.boha.geo.monitor.data.mcountry.Timezone;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document("Country")
public class Country {
    private String countryId;
    private String name;
    private String iso3;
    private String iso2;
    @JsonProperty("phone_code")
    private String phoneCode;
    private String capital;
    private String currency;
    @JsonProperty("currency_name")
    private String currencyName;
    @JsonProperty("currency_symbol")
    private String currencySymbol;
    private String tld;
    private String region;
    private String subregion;
    private List<Timezone> timezones;
    private double latitude;
    private double longitude;
    private String emoji;
    private String emojiU;
    private Position position;
}


