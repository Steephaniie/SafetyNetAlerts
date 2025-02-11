package com.safetynet.safetynetalerts;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;


/**
 * Classe Handler Globale pour intercepter les exceptions dans l'application.
 * Elle permet de fournir une gestion centralisée des erreurs et
 * de renvoyer des réponses adaptées aux clients.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les erreurs de validation pour les objets annotés avec {@code @Valid}.
     * Cette méthode extrait les erreurs de validation et retourne un objet JSON contenant
     * les champs invalides et leurs messages d'erreur.
     *
     * @param ex l'exception contenant les détails des erreurs de validation.
     * @return une réponse HTTP avec le statut BAD_REQUEST (400) et un corps contenant
     * les erreurs de validation sous forme de clé (champ) et valeur (message d'erreur).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}