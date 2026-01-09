//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam.service;

import com.accenture.java.mock.exam.model.Question;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CsvReaderService {
    public List<Question> readQuestionsFromCsv(String csvFilePath) throws IOException {
        List<Question> questions = new ArrayList();

        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            while((line = reader.readLine()) != null) {
                Question question = this.parseQuestionFromCsvLine(line);
                if (question != null) {
                    questions.add(question);
                }
            }
        }

        return questions;
    }

    public List<Question> readQuestionsFromClasspath(String resourcePath) throws IOException {
        List<Question> questions = new ArrayList();

        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }

            String line;
            while((line = reader.readLine()) != null) {
                Question question = this.parseQuestionFromCsvLine(line);
                if (question != null) {
                    questions.add(question);
                }
            }
        }

        return questions;
    }

    private Question parseQuestionFromCsvLine(String csvLine) {
        try {
            String[] fields = this.parseCsvLine(csvLine);
            if (fields.length < 9) {
                System.err.println("Invalid CSV line (insufficient fields): " + csvLine);
                return null;
            } else {
                int id = Integer.parseInt(fields[0].trim());
                String questionText = fields[1].trim();
                List<String> options = Arrays.asList("A. " + fields[2].trim(), "B. " + fields[3].trim(), "C. " + fields[4].trim(), "D. " + fields[5].trim());
                char correctAnswer = fields[6].trim().toUpperCase().charAt(0);
                String explanation = fields[7].trim();
                String category = fields[8].trim();
                String questionType = "text";
                String codeImagePath = null;
                if (fields.length >= 10 && !fields[9].trim().isEmpty()) {
                    questionType = fields[9].trim();
                }

                if (fields.length >= 11 && !fields[10].trim().isEmpty()) {
                    codeImagePath = fields[10].trim();
                }

                return new Question(id, questionText, options, correctAnswer, explanation, category, questionType, codeImagePath);
            }
        } catch (Exception e) {
            System.err.println("Error parsing CSV line: " + csvLine + " - " + e.getMessage());
            return null;
        }
    }

    private String[] parseCsvLine(String csvLine) {
        List<String> fields = new ArrayList();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for(int i = 0; i < csvLine.length(); ++i) {
            char c = csvLine.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        fields.add(currentField.toString());
        return (String[])fields.toArray(new String[0]);
    }
}
