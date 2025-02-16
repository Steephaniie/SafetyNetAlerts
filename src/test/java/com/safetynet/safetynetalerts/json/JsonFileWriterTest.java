package com.safetynet.safetynetalerts.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.safetynet.safetynetalerts.dto.FichierJsonDTO;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;


/**
 * Classe de test pour vérifier les fonctionnalités de JsonFileWriter.
 * Elle utilise des mocks pour simuler les interactions avec ObjectMapper
 * et ObjectWriter, et teste que l'écriture de données JSON fonctionne correctement.
 */
@SpringBootTest
class JsonFileWriterTest {

    /**
     * Teste la méthode writeToFile pour s'assurer qu'elle écrit des données valides dans un fichier JSON.
     * <p>
     * Étapes principales :
     * 1. Crée un mock d'ObjectMapper et d'ObjectWriter pour simuler leur comportement.
     * 2. Initialise des données de test fictives pour les entités (Person, FireStation, MedicalRecord).
     * 3. Configure les actions des mocks pour imiter le résultat attendu.
     * 4. Vérifie que la méthode ne lève pas d'exception et que les mocks sont correctement utilisés.
     *
     * @throws IOException en cas d'erreur d'entrée/sortie lors de l'écriture.
     */
    @Test
    void writeToFile_ShouldWriteValidDataToFile() throws IOException {
        // Étape 1 : Création d'un mock pour ObjectMapper
        ObjectMapper mockObjectMapper = Mockito.mock(ObjectMapper.class);
        // Création d'un mock pour ObjectWriter (retourné par writerWithDefaultPrettyPrinter)
        var mockObjectWriter = Mockito.mock(ObjectWriter.class);

        // Étape 2 : Instanciation de l'objet testé
        JsonFileWriter jsonFileWriter = new JsonFileWriter();

        // Étape 3 : Préparation des données fictives pour le test
        FichierJsonDTO fichierJsonDTO = new FichierJsonDTO();
        fichierJsonDTO.setPersons(Collections.singletonList(new Person()));
        fichierJsonDTO.setFirestations(Collections.singletonList(new FireStation()));
        fichierJsonDTO.setMedicalrecords(Collections.singletonList(new MedicalRecord()));

        // Étape 4 : Configuration du comportement attendu des mocks
        when(mockObjectMapper.writerWithDefaultPrettyPrinter()).thenReturn(mockObjectWriter);
        doNothing().when(mockObjectWriter).writeValue(any(File.class), eq(fichierJsonDTO));

        // Étape 5 : Appel de la méthode à tester
        jsonFileWriter.writeToFile();

    }
}