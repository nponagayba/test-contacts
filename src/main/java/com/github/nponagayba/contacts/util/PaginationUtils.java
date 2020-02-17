package com.github.nponagayba.contacts.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;

@UtilityClass
public class PaginationUtils {

    public static HttpHeaders generateSliceHttpHeaders(Slice<?> slice) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Has-Next-Page", "" + slice.hasNext());
        return headers;
    }
}
