package com.example.courseservice.courseservice.client;

import com.example.courseservice.courseservice.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
@Slf4j
public class UserClient {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;

    public UserClient(RestTemplate restTemplate,
                        @Value("${user.service.url}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    public void addCourseToInstructor(Long courseId, Long instructorId, String token) {
        String url = userServiceUrl + "/" + instructorId + "/courses/"+courseId;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            // Build the entity with headers (body is null in this case)
            HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

            log.info("Calling User Service at URL: {} with request entity: {}", url, requestEntity);

            // Use exchange instead of postForEntity
            restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );

            log.info("Successfully added courseId: {} to instructorId: {}", courseId, instructorId);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Failed to call User Service for instructorId: " + instructorId);
        }
    }
}
