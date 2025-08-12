package com.project.demo.logic.entity.question;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.deepseek.DeepSeekService;
import com.project.demo.logic.entity.option.Option;
import com.project.demo.logic.entity.option.OptionRepository;
import com.project.demo.logic.entity.quiz.Quiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para la generación automática de preguntas y opciones usando IA.
 */
@Service
public class QuestionGenerationService {

    @Autowired
    private DeepSeekService deepSeekService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    private static final Logger logger = LoggerFactory.getLogger(QuestionGenerationService.class);

    /**
     * Genera preguntas y opciones para un cuestionario usando IA.
     * @param quiz cuestionario al que se agregarán las preguntas
     * @param numberOfQuestions número de preguntas a generar
     */
    public void generateQuestionsWithAI(Quiz quiz, int numberOfQuestions) {
        try {
            String storyContent = quiz.getStory().getContent();
            String aiResponse = deepSeekService.generateQuizQuestions(storyContent, numberOfQuestions);

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
            logger.error("Error generando preguntas con IA: {}", e.getMessage(), e);
        }
    }
}
