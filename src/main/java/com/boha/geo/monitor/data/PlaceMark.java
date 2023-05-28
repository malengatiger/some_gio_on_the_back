package com.boha.geo.monitor.data;

import lombok.Data;

@Data
public class PlaceMark {
    String name;
    String street;
    String isoCountryCode;
    String country;
    String postalCode;
    String administrativeArea;
    String locality;
    String subLocality;
    String subThoroughfare;
    String thoroughfare;


}
