package com.learn.postsmith.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;


public class Oauth1Utils {
    public static String urlEncode(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    public static String generateAuthHeaderString(TreeMap<String, String> header) {
        StringBuilder headerStr = new StringBuilder("OAuth ");
        for (Map.Entry<String, String> map : header.entrySet()) {
            headerStr.append(urlEncode(map.getKey())).append("=\"").append(urlEncode(map.getValue())).append("\"").append(", ");
        }

        headerStr.delete(headerStr.length() - 2, headerStr.length());

        generateBaseString("post", "https://api.twitter.com/oauth/request_token", header);
        return headerStr.toString();
    }

    public static String generateBaseString(String apiMethod, String apiURL, TreeMap<String, String> headers) {
        StringBuilder baseStr = new StringBuilder();
        StringBuilder headerStr = new StringBuilder();

        for (Map.Entry<String, String> map : headers.entrySet()) {
            headerStr.append(map.getKey()).append("=").append(map.getValue()).append("&");
        }
        if (!headerStr.isEmpty()) {
            headerStr.deleteCharAt(headerStr.length() - 1);
        }
        baseStr.append(apiMethod.toUpperCase()).append("&").append(urlEncode(apiURL)).append("&").append(urlEncode(headerStr.toString()));

        return baseStr.toString();
    }

    public static String generateSigningKey(String csKey, String accessToken) {
        StringBuilder str = new StringBuilder();
        str.append(urlEncode(csKey)).append("&");
        if (accessToken != null && !accessToken.isEmpty()) {
            str.append(urlEncode(accessToken));
        }
        return str.toString();
    }

    public static String generateSignature(String apiMethod, String apiURL, String csKey, String accessToken, TreeMap<String, String> headers) throws NoSuchAlgorithmException, InvalidKeyException {
        String baseString = generateBaseString(apiMethod, apiURL, headers);
        String signingKey = generateSigningKey(csKey, accessToken);
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKeySpec = new SecretKeySpec(signingKey.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        mac.init(secretKeySpec);
        byte[] signature = mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature);

    }

    public static String createQueryStrings(TreeMap<String, String> params) {
        StringBuilder queryString = new StringBuilder("?");
        for (Map.Entry<String, String> map : params.entrySet()) {
            queryString.append(map.getKey()).append("=").append(map.getValue()).append("&");
        }
        queryString.deleteCharAt(queryString.length() - 1);
        return queryString.toString();
    }
}
