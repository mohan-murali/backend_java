package com.example.backend_java;

import com.example.backend_java.Controllers.RetrospectiveController;
import com.example.backend_java.Models.Feedback;
import com.example.backend_java.Models.Retrospective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetrospectiveControllerTest {

    private RetrospectiveController retrospectiveController;

    @Mock
    private Logger logger;

    @BeforeEach
    public void setUp() throws Exception {
        retrospectiveController = new RetrospectiveController();
    }

    @Test
    public void testCreateRetrospective_Success() {
        var retrospective = new Retrospective("Retrospective1", "Good Retrospective", new Date());
        retrospective.setDate(new Date());
        retrospective.setParticipants(Arrays.asList("John", "Jane"));

        var response = retrospectiveController.createRetrospective(retrospective);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Retrospective created successfully.", response.getBody());
    }

    @Test
    public void testCreateRetrospective_BadRequest() {
        var retrospective = new Retrospective("Retrospective1", "Good Retrospective", new Date());
        var response = retrospectiveController.createRetrospective(retrospective);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Date and participants are required.", response.getBody());
    }

    @Test
    public void testAddFeedback_Success() {
        var retrospective = new Retrospective("Retrospective", "Good Retrospective", new Date());
        retrospective.setDate(new Date());
        retrospective.setParticipants(Arrays.asList("John", "Jane"));

        retrospectiveController.createRetrospective(retrospective);

        var feedback = new Feedback("John", "Good feedback", Feedback.Type.POSITIVE);

        var response = retrospectiveController.addFeedback("Retrospective", feedback);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Feedback added successfully.", response.getBody());
    }

    @Test
    public void testAddFeedback_RetrospectiveNotFound() {
        var feedback = new Feedback("John", "Good feedback", Feedback.Type.IDEA);

        var response = retrospectiveController.addFeedback("NonExistentRetrospective", feedback);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateFeedback_Success() {
        var retrospective = new Retrospective("Retrospective2", "Good Retrospective", new Date());
        retrospective.setDate(new Date());
        retrospective.setParticipants(Arrays.asList("John", "Jane"));

        retrospectiveController.createRetrospective(retrospective);

        var feedback = new Feedback("John", "Initial feedback", Feedback.Type.POSITIVE);
        retrospective.setFeedbacks(Collections.singletonList(feedback));
        retrospectiveController.createRetrospective(retrospective);

        var updatedFeedback = new Feedback("John", "Updated feedback", Feedback.Type.PRAISE);

        var response = retrospectiveController.updateFeedback("Retrospective2", updatedFeedback);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Feedback updated successfully.", response.getBody());
    }

    @Test
    public void testUpdateFeedback_RetrospectiveNotFound() {
        var feedback = new Feedback("John", "Updated feedback", Feedback.Type.PRAISE);

        var response = retrospectiveController.updateFeedback("NonExistentRetrospective", feedback);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
