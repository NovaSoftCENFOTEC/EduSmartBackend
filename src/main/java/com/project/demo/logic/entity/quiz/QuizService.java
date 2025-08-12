package com.project.demo.logic.entity.quiz;

import com.project.demo.logic.entity.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de cuestionarios.
 * Permite crear, actualizar, eliminar y consultar cuestionarios.
 */
@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private StoryRepository storyRepository;

    /**
     * Obtiene todos los cuestionarios.
     * @return lista de cuestionarios
     */
    public List<Quiz> getAllQuizzes(){
        return quizRepository.findAll();
    }

    /**
     * Obtiene un cuestionario por su identificador.
     * @param id identificador del cuestionario
     * @return cuestionario (opcional)
     */
    public Optional<Quiz> getQuizById(Integer id) {
        return quizRepository.findById(id);
    }

    /**
     * Crea un nuevo cuestionario asociado a una historia.
     * @param storyId identificador de la historia
     * @param quiz datos del cuestionario
     * @return cuestionario creado (opcional)
     */
    public Optional<Quiz> createQuiz(Integer storyId, Quiz quiz) {
        return storyRepository.findById(Long.valueOf(storyId)).map(story -> {
            quiz.setStory(story);
            return quizRepository.save(quiz);
        });
    }

    /**
     * Actualiza los datos de un cuestionario existente.
     * @param id identificador del cuestionario
     * @param quizDetails datos actualizados
     * @return cuestionario actualizado (opcional)
     */
    public Optional<Quiz> updateQuiz(Integer id, Quiz quizDetails) {
        return quizRepository.findById(id).map(quiz -> {
            quiz.setTitle(quizDetails.getTitle());
            quiz.setDescription(quizDetails.getDescription());
            quiz.setDueDate(quizDetails.getDueDate());
            return quizRepository.save(quiz);
        });
    }

    /**
     * Elimina un cuestionario por su identificador.
     * @param id identificador del cuestionario
     * @return cuestionario eliminado (opcional)
     */
    public Optional<Quiz> deleteQuiz(Integer id) {
        return quizRepository.findById(id).map(quiz -> {
            quizRepository.delete(quiz);
            return quiz;
        });
    }

    /**
     * Obtiene los cuestionarios asociados a una historia.
     * @param storyId identificador de la historia
     * @return lista de cuestionarios
     */
    public List<Quiz> getQuizzesByStoryId(Integer storyId) {
        return quizRepository.findByStoryId(storyId);
    }
}
