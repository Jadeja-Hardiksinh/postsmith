package com.learn.postsmith.controller.api.integrations.x;

import com.learn.postsmith.entity.Platform;
import com.learn.postsmith.entity.UserDetail;
import com.learn.postsmith.enums.PlatformName;
import com.learn.postsmith.service.PlatformService;
import com.learn.postsmith.service.UserDetailService;
import com.learn.postsmith.util.Oauth1Utils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/v1/user/x")
public class XIntegration {
    @Autowired
    UserDetailService userDetailService;
    @Autowired
    PlatformService platformService;


    @GetMapping("/callback")
    public Mono<ResponseEntity<String>> callbackimpl(@RequestParam(value = "oauth_token") String token, @RequestParam(name = "oauth_verifier") String verifier) {
        String accessTokenURl = "https://api.x.com/oauth/access_token";
        TreeMap<String, String> queryParam = new TreeMap<>();
        queryParam.put("oauth_token", token);
        queryParam.put("oauth_verifier", verifier);
        WebClient webClient = WebClient.builder().baseUrl(accessTokenURl).defaultHeaders((headers) -> {
            headers.add("oauth_token", token);
            headers.add("oauth_verifier", verifier);
        }).build();
//        String response1 = webClient.post().uri(Oauth1Utils.createQueryStrings(queryParam)).retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
//            return clientResponse.createException();
//        }).bodyToMono(String.class).block();

        return webClient.post().uri(Oauth1Utils.createQueryStrings(queryParam))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is4xxClientError()) {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.just(ResponseEntity.status(401).body("UNAUTHORIZED: " + error)));

                    } else if (clientResponse.statusCode().is5xxServerError()) {
                        return clientResponse.bodyToMono(String.class).flatMap(error -> Mono.just(ResponseEntity.status(500).body("Internal Server Error: " + error)));
                    } else {
                        return clientResponse.bodyToMono(String.class).flatMap(response -> {
                            System.out.println(response);
                            HashMap<String, String> hashMap = new HashMap<>();
                            if (response != null) {
                                String[] responseParts = response.split("&");
                                String[] pairs;
                                for (String part : responseParts) {
                                    pairs = part.split("=");
                                    hashMap.put(pairs[0], pairs[1]);
                                }
                            }
                            Platform platform = platformService.findPlatformByRequestToken(token);
                            if (platform != null) {
                                System.out.println(platform.getId());
                                platform.setAccessToken(hashMap.get("oauth_token"));
                                platform.setTokenSecret(hashMap.get("oauth_token_secret"));
                                platformService.addPlatformRecord(platform);
                            }
                            return Mono.just(ResponseEntity.status(302).header("Location", "/dashboard").body(""));
                        });
                    }
                }).onErrorResume(e -> Mono.just(ResponseEntity.status(500).body(e.getMessage())));


    }

    @GetMapping("/token")
    public ResponseEntity<String> getXAuth(HttpSession session) throws NoSuchAlgorithmException, InvalidKeyException {
        SecurityContext context = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (context != null) {
            Authentication authentication = context.getAuthentication();
            UserDetail user = userDetailService.findByEmail(authentication.getName());
            System.out.println(user.getId());
            String requestTokenUrl = "https://api.twitter.com/oauth/request_token";
            String getOauthUrl = "https://api.x.com/oauth/authorize";
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
            String request_token = "";
            if (response != null) {
                String[] responseParts = response.split("&");
                String[] pairs;
                for (String part : responseParts) {
                    pairs = part.split("=");
                    if (Objects.equals(pairs[0], "oauth_token")) {
                        request_token = pairs[1];
                        break;
                    }
                }
            }
            Platform platform = new Platform(null, user.getId(), request_token, null, null, PlatformName.X);

            if (platformService.addPlatformRecord(platform)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


    }
}
