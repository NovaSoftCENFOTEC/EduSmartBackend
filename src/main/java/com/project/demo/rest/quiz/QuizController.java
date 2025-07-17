package com.project.demo.rest.quiz;

import com.project.demo.logic.entity.gemini.GeminiService;
import com.project.demo.logic.entity.quiz.Quiz;
import com.project.demo.logic.entity.quiz.QuizRepository;
import com.project.demo.logic.entity.story.Story;
import com.project.demo.logic.entity.story.StoryRepository;
import com.project.demo.logic.entity.question.Question;
import com.project.demo.logic.entity.question.QuestionRepository;
import com.project.demo.logic.entity.option.Option;
import com.project.demo.logic.entity.option.OptionRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private GeminiService geminiService;

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAllQuizzes(HttpServletRequest request) {
        List<Quiz> quizzes = quizRepository.findAll();
        return new GlobalResponseHandler().handleResponse("Quizzes obtenidos correctamente", quizzes, HttpStatus.OK,
                request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getQuizById(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Quiz obtenido correctamente", quiz.get(), HttpStatus.OK,
                    request);
        } else {
            return new GlobalResponseHandler().handleResponse("Quiz " + id + " no encontrado", HttpStatus.NOT_FOUND,
                    request);
        }
    }

    @PostMapping("/story/{storyId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createQuiz(@PathVariable Integer storyId, @RequestBody Quiz quiz,
                                        HttpServletRequest request) {
        try {
            Optional<Story> foundStory = storyRepository.findById(Long.valueOf(storyId));
            if (foundStory.isPresent()) {
                quiz.setStory(foundStory.get());
                Quiz savedQuiz = quizRepository.save(quiz);

                if (quiz.isGenerateWithAI() && quiz.getNumberOfQuestions() > 0) {
                    generateQuestionsWithAI(savedQuiz, quiz.getNumberOfQuestions());
                }

                return new GlobalResponseHandler().handleResponse("Quiz creado con éxito", savedQuiz, HttpStatus.OK, request);
            } else {
                return new GlobalResponseHandler().handleResponse("Historia " + storyId + " no encontrada",
                        HttpStatus.NOT_FOUND, request);
            }
        } catch (Exception e) {
            return new GlobalResponseHandler().handleResponse("Error al crear el quiz: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @PostMapping("/{quizId}/generate-questions")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> generateQuestionsForQuiz(
            @PathVariable Integer quizId,
            @RequestParam int numberOfQuestions,
            HttpServletRequest request) {

        Optional<Quiz> quizOpt = quizRepository.findById(quizId);
        if (!quizOpt.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Quiz no encontrado", HttpStatus.NOT_FOUND, request);
        }

        Quiz quiz = quizOpt.get();
        generateQuestionsWithAI(quiz, numberOfQuestions);

        return new GlobalResponseHandler().handleResponse("Preguntas generadas con éxito", HttpStatus.OK, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateQuiz(@PathVariable Integer id, @RequestBody Quiz quizDetails,
                                        HttpServletRequest request) {
        Optional<Quiz> foundQuiz = quizRepository.findById(id);
        if (foundQuiz.isPresent()) {
            Quiz quiz = foundQuiz.get();
            quiz.setTitle(quizDetails.getTitle());
            quiz.setDescription(quizDetails.getDescription());
            quiz.setDueDate(quizDetails.getDueDate());
            quizRepository.save(quiz);
            return new GlobalResponseHandler().handleResponse("Quiz actualizado con éxito", quiz, HttpStatus.OK,
                    request);
        } else {
            return new GlobalResponseHandler().handleResponse("Quiz " + id + " no encontrado", HttpStatus.NOT_FOUND,
                    request);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteQuiz(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Quiz> foundQuiz = quizRepository.findById(id);
        if (foundQuiz.isPresent()) {
            quizRepository.delete(foundQuiz.get());
            return new GlobalResponseHandler().handleResponse("Quiz eliminado con éxito", foundQuiz.get(),
                    HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Quiz " + id + " no encontrado", HttpStatus.NOT_FOUND,
                    request);
        }
    }

    private void generateQuestionsWithAI(Quiz quiz, int numberOfQuestions) {
        try {
            String storyContent = quiz.getStory().getContent();

            String aiResponse = geminiService.generateQuizQuestions(storyContent, numberOfQuestions);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);
            JsonNode questionsNode = rootNode.get("questions");

            if (questionsNode != null && questionsNode.isArray()) {
                for (JsonNode questionNode : questionsNode) {
                    Question question = new Question();
                    question.setText(questionNode.get("question").asText());
                    question.setQuiz(quiz);
                    Question savedQuestion = questionRepository.save(question);

                    JsonNode optionsNode = questionNode.get("options");
                    if (optionsNode != null && optionsNode.isArray()) {
                        for (JsonNode optionNode : optionsNode) {
                            Option option = new Option();
                            option.setText(optionNode.get("text").asText());
                            option.setCorrect(optionNode.get("correct").asBoolean());
                            option.setQuestion(savedQuestion);
                            optionRepository.save(option);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error generando preguntas con IA: " + e.getMessage());
            e.printStackTrace();
        }
    }
}