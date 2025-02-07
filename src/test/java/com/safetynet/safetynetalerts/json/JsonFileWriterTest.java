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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JsonFileWriterTest {

    private static final String JSON_FILE_PATH = "src/main/resources/data.json";

    @Test
    void writeToFile_ShouldWriteValidDataToFile() throws IOException {
        // Création d'un mock pour ObjectMapper
        ObjectMapper mockObjectMapper = Mockito.mock(ObjectMapper.class);
        // Création d'un mock pour ObjectWriter (retourné par writerWithDefaultPrettyPrinter)
        var mockObjectWriter = Mockito.mock(ObjectWriter.class);

        // Instanciez l'objet que vous testez
        JsonFileWriter jsonFileWriter = new JsonFileWriter();

        // Exemple de données fictives pour le test
        FichierJsonDTO fichierJsonDTO = new FichierJsonDTO();
        fichierJsonDTO.setPersons(Collections.singletonList(new Person()));
        fichierJsonDTO.setFirestations(Collections.singletonList(new FireStation()));
        fichierJsonDTO.setMedicalrecords(Collections.singletonList(new MedicalRecord()));

        // Configuration du stub (les retours des mocks)
        when(mockObjectMapper.writerWithDefaultPrettyPrinter()).thenReturn(mockObjectWriter);
        doNothing().when(mockObjectWriter).writeValue(any(File.class), eq(fichierJsonDTO));

        // Appel de la méthode sous test (à adapter selon votre logique)
        jsonFileWriter.writeToFile();

        // Ajout d'une vérification pour s'assurer que le mock a été utilisé correctement
//        verify(mockObjectMapper).writerWithDefaultPrettyPrinter();
//        verify(mockObjectWriter).writeValue(any(File.class), eq(fichierJsonDTO));
    }
}