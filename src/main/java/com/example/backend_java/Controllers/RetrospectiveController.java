package com.example.backend_java.Controllers;

import com.example.backend_java.Models.Feedback;
import com.example.backend_java.Models.Retrospective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/retrospectives")
public class RetrospectiveController {
    private final List<Retrospective> retrospectiveRepo = new ArrayList();

    Logger logger = LoggerFactory.getLogger(RetrospectiveController.class);

    @PostMapping
    public ResponseEntity<String> createRetrospective(@RequestBody Retrospective retrospective) {
            try {
                if (retrospective.getDate() == null || retrospective.getParticipants() == null
                        || retrospective.getParticipants().isEmpty()) {
                    logger.info("Date or participants list was empty");
                    return ResponseEntity.badRequest().body("Date and participants are required.");
                }
                retrospectiveRepo.add(retrospective);

                return ResponseEntity.ok("Retrospective created successfully.");

            } catch (Exception ex){
                logger.error(ex.getMessage());
                return ResponseEntity.internalServerError().body("There was an error in the server");
        }
    }

    @PostMapping("/{name}/feedback")
    public ResponseEntity<String> addFeedback(@PathVariable String name, @RequestBody Feedback feedback) {
        try {
            var optionalRetrospective = retrospectiveRepo.stream()
                    .filter(r -> r.getName().equals(name))
                    .findFirst();

            if (optionalRetrospective.isPresent()) {
                Retrospective retrospective = optionalRetrospective.get();
                if( retrospective.getFeedbacks() == null){
                    retrospective.setFeedbacks(new ArrayList<>());
                }
                retrospective.getFeedbacks().add(feedback);
                return ResponseEntity.ok("Feedback added successfully.");
            } else {
                logger.info("The retrospective with name: " + name + "was not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError().body("There was an error in the server");
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity<String> updateFeedback(@PathVariable String name, @RequestBody Feedback feedback){
        try{
            var updated = false;
            var optionalRetrospective = retrospectiveRepo.stream()
                    .filter(r -> r.getName().equals(name)).findFirst();
            if(optionalRetrospective.isPresent()){
                var feedbacks = optionalRetrospective.get().getFeedbacks();
                for(Feedback f : feedbacks){
                    if(f.getName().equals(feedback.getName())){
                        f.setBody(feedback.getBody());
                        f.setFeedback(feedback.getFeedback());
                        logger.info("the feedback by "
                                +feedback.getName()+" for retrospective: "
                                + name + "was successfully updated");
                        updated=true;
                    }
                }
                if(!updated){
                    logger.warn("the feedback by "
                            +feedback.getName()+" for retrospective: "
                            + name + "was not found");
                    return ResponseEntity.notFound().build();
                }
            } else {
                logger.warn("The retrospective with name: " + name + "was not found");
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok("Feedback updated successfully.");
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError().body("There was an error in the server");
        }
    }

    @GetMapping
    public ResponseEntity<List<Retrospective>> getRetrospectives(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            var paginatedRetrospectives = retrospectiveRepo
                    .stream().skip(page).limit(pageSize)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(paginatedRetrospectives);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Retrospective>> searchRetrospectivesByDate(
            @RequestParam Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            var matchedRetrospectives = retrospectiveRepo.stream()
                    .filter(r -> r.getDate().equals(date))
                    .collect(Collectors.toList());

            var paginatedRetrospectives = matchedRetrospectives
                    .stream().skip(page).limit(pageSize)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(paginatedRetrospectives);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
