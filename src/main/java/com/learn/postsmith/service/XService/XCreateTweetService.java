package com.learn.postsmith.service.XService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learn.postsmith.entity.Post;
import com.learn.postsmith.enums.PostStatus;
import com.learn.postsmith.service.HandlePostService;
import com.learn.postsmith.util.Oauth1Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.TreeMap;

@Service
public class XCreateTweetService {

    private final String cKey;
    private final String csKey;
    private final String baseUrl;
    @Autowired
    HandlePostService handlePostService;

    public XCreateTweetService(@Value("${x.consumer.key}") String cKey, @Value("${x.consumer.secret.key}") String csKey, @Value("${x.api.base.url}") String baseUrl) {
        this.cKey = cKey;
        this.csKey = csKey;
        this.baseUrl = baseUrl;
    }

    public boolean createTextTweet(Post post) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        final String accessToken = post.getPlatform().getAccessToken();
        final String tokenSecret = post.getPlatform().getTokenSecret();
        String path = "/tweets";
        String method = "POST";
        String content = post.getContent();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("text", content);
        String reqBody = mapper.writeValueAsString(node);
        String authHeaderStr = getAuthHeader(method, baseUrl + path, accessToken, tokenSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeaderStr);
        headers.set("Content-Type", "application/json");
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).defaultHeaders(httpHeaders -> httpHeaders.addAll(headers)).build();
        return webClient.post().uri(path).bodyValue(reqBody).exchangeToMono(clientResponse -> {
            if (clientResponse.statusCode().is2xxSuccessful()) {
                return clientResponse.bodyToMono(ObjectNode.class).flatMap(res -> {
                    Long referenceId = res.get("data").get("id").asLong();
                    post.setReferenceId(referenceId);
                    post.setPostStatus(PostStatus.POSTED);
                    handlePostService.addPost(post);
                    return Mono.just(true);
                });

            }
            post.setPostStatus(PostStatus.FAILED);
            handlePostService.addPost(post);
            return Mono.just(false);
        }).block();
    }

    private String getAuthHeader(String method, String endPoint, String accessToken, String tokenSecret) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        TreeMap<String, String> authParams = new TreeMap<>();
        authParams.put("oauth_consumer_key", cKey);
        authParams.put("oauth_token", accessToken);
        authParams.put("oauth_nonce", "iuytr34567");
        authParams.put("oauth_signature_method", "HMAC-SHA1");
        authParams.put("oauth_timestamp", String.valueOf(Instant.now().getEpochSecond()));
        authParams.put("oauth_version", "1.0");
        String oauthSignature = Oauth1Utils.generateSignature(method, endPoint, csKey, tokenSecret, authParams);
        authParams.put("oauth_signature", oauthSignature);
        return Oauth1Utils.generateAuthHeaderString(authParams);
    }
}
