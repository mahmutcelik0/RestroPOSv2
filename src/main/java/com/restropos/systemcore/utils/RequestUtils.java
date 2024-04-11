package com.restropos.systemcore.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtils {
    public static String getDomainFromOrigin(String origin){
        String subdomain = "";
        Pattern pattern = Pattern.compile("^https?://([^./]+)\\.");
        Matcher matcher = pattern.matcher(origin);
        if (matcher.find()) {
            subdomain = matcher.group(1);
        }
        return subdomain;
    }
}
