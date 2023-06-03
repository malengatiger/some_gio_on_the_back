package com.boha.geo.datadto;

import com.boha.geo.monitor.data.Photo;
import lombok.Data;

@Data
public class UploadBag {
    private byte[] fileBytes;
    private byte[] thumbnailBytes;
    private Photo photo;
}
