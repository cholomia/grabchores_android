package com.tip.theboss.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cholo Mia on 12/22/2016.
 */

public class StringUtils {

    public static Map<String, String> getParamsFromUrl(String baseUrl) {
        String[] url = baseUrl.split("\\?");
        Map<String, String> parameters = new HashMap<>();
        if (url.length > 1) {
            String[] params = url[1].split("&");
            for (String keyValue : params) {
                String[] p = keyValue.split("=");
                if (p.length > 1)
                    parameters.put(p[0], p[1]);
            }
        }
        return parameters;
    }
}
