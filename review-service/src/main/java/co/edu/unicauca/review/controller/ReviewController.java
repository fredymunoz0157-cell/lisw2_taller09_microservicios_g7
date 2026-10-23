package co.edu.unicauca.review.controller;

import co.edu.unicauca.review.dto.AssignReviewerRequestDTO;
import co.edu.unicauca.review.dto.EvaluateQuestionRequestDTO;
import co.edu.unicauca.review.model.Review;
import co.edu.unicauca.review.model.Revision;
import co.edu.unicauca.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Escenario 1.7: Asignar un revisor a una pregunta elegible (POST /reviews/assign).
     */
    @PostMapping("/assign")
    public ResponseEntity<Map<String, Object>> assignReviewer(@RequestBody AssignReviewerRequestDTO request) {
        Map<String, Object> result = reviewService.assignReviewer(request);
        return ResponseEntity.ok(result);
    }

    /**
     * Evalua una pregunta en estado IN_REVIEW (aprobar o rechazar con motivo registrado).
     */
    @PostMapping("/evaluate")
    public ResponseEntity<Map<String, Object>> evaluateQuestion(@RequestBody EvaluateQuestionRequestDTO request) {
        Map<String, Object> result = reviewService.evaluateQuestion(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Revision> getRevisionByQuestion(@PathVariable Long questionId) {
        return reviewService.getRevisionByQuestion(questionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Review>> listReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleReviewError(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "REVIEW_OPERATION_REJECTED",
                "message", ex.getMessage()
        ));
    }
}
