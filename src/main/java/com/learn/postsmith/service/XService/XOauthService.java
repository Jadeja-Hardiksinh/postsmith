package com.learn.postsmith.service.XService;

import com.learn.postsmith.dto.GeneralResponseDTO;
import com.learn.postsmith.entity.Platform;
import com.learn.postsmith.entity.UserDetail;
import com.learn.postsmith.enums.PlatformName;
import com.learn.postsmith.service.PlatformService;
import com.learn.postsmith.util.Oauth1Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.TreeMap;

@Service
public class XOauthService {
    @Autowired
    PlatformService platformService;

    public static HashMap<String, String> getMapFromQueryString(String queryParam) {
        HashMap<String, String> map = new HashMap<>();
        String[] parts = queryParam.split("&");
        for (String part : parts) {
            String[] pair = part.split("=");
            map.put(pair[0], pair[1]);
        }
        return map;
    }

    public ResponseEntity<GeneralResponseDTO> generateRequestToken(UserDetail user) throws NoSuchAlgorithmException, InvalidKeyException {
        String requestTokenUrl = "https://api.twitter.com/oauth/request_token";
        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("oauth_consumer_key", "lYQoUTNgsrtSWoL2IMWce1TN1");
        headers.put("oauth_nonce", "qwertyui123456");
        headers.put("oauth_signature_method", "HMAC-SHA1");
        headers.put("oauth_timestamp", String.valueOf(Instant.now().getEpochSecond()));
        headers.put("oauth_version", "1.0");
        String signature = Oauth1Utils.generateSignature("post", requestTokenUrl, "gQvFsHsX2jj7RHvmpPtUbbKkg84h2yToftgrVCUBS6ULQ0pBYX", null, headers);
        headers.put("oauth_signature", signature);
        String authHeader = Oauth1Utils.generateAuthHeaderString(headers);
        WebClient webClient = WebClient.builder().baseUrl(requestTokenUrl).defaultHeader(HttpHeaders.AUTHORIZATION, authHeader).build();
        String response = webClient.post().uri("").retrieve().bodyToMono(String.class).block();
        String request_token = getMapFromQueryString(response).get("oauth_token");
        Platform platform = platformService.findPlatformByUserIdAndPlatformName(user.getId(), PlatformName.X);
        if (platform != null) {
            platform.setRequestToken(request_token);
            if (platformService.addPlatformRecord(platform)) {
                String authWebUrl = "https://api.twitter.com/oauth/authorize?" + response;
                return new ResponseEntity<>(new GeneralResponseDTO("success", "request token generated", authWebUrl), HttpStatus.OK);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


        } else {
            platform = new Platform(null, user.getId(), request_token, null, null, PlatformName.X);
            String authWebUrl = "https://api.twitter.com/oauth/authorize?" + response;
            if (platformService.addPlatformRecord(platform)) {

                return new ResponseEntity<>(new GeneralResponseDTO("success", "request token generated", authWebUrl), HttpStatus.OK);
            }

        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    public Mono<ResponseEntity<String>> generateAccessToken(String oauthToken, String oauthVerifier) {
        String accessTokenURl = "https://api.x.com/oauth/access_token";
        TreeMap<String, String> queryParam = new TreeMap<>();
        queryParam.put("oauth_token", oauthToken);
        queryParam.put("oauth_verifier", oauthVerifier);
        WebClient webClient = WebClient.builder().baseUrl(accessTokenURl).defaultHeaders((headers) -> {
            headers.add("oauth_token", oauthToken);
            headers.add("oauth_verifier", oauthVerifier);
        }).build();

        return webClient.post().uri(Oauth1Utils.createQueryStrings(queryParam))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is4xxClientError()) {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.just(ResponseEntity.status(401).body("UNAUTHORIZED: " + error)));

                    } else if (clientResponse.statusCode().is5xxServerError()) {
                        return clientResponse.bodyToMono(String.class).flatMap(error -> Mono.just(ResponseEntity.status(500).body("Internal Server Error: " + error)));
                    } else {
                        return clientResponse.bodyToMono(String.class).flatMap(response -> {
                            HashMap<String, String> hashMap = new HashMap<>();
                            if (response != null) {
                                String[] responseParts = response.split("&");
                                String[] pairs;
                                for (String part : responseParts) {
                                    pairs = part.split("=");
                                    hashMap.put(pairs[0], pairs[1]);
                                }
                            }
                            Platform platform = platformService.findPlatformByRequestToken(oauthToken);
                            if (platform != null) {
                                platform.setAccessToken(hashMap.get("oauth_token"));
                                platform.setTokenSecret(hashMap.get("oauth_token_secret"));
                                platformService.addPlatformRecord(platform);
                            }
                            return Mono.just(ResponseEntity.status(302).header("Location", "/dashboard").body(""));
                        });
                    }
                }).onErrorResume(e -> Mono.just(ResponseEntity.status(500).body(e.getMessage())));

    }

}



