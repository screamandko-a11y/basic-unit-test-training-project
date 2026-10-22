package lv.bootcamp.shelter.task5;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Task 5: Nested test classes for CSV parsing
 * <p>
 * Practice:
 * - @Nested to organize by scenario
 * - @DisplayName for readable output
 * - Testing Optional results (isPresent, isEmpty)
 * - Testing file I/O with temp files
 * <p>
 * Instructions:
 * Write tests for AnimalCsvParser. Use @Nested classes to group tests by scenario.
 * For file-based tests, use Files.createTempFile() and Files.writeString() to create test data.
 */
@DisplayName("AnimalCsvParser")
class AnimalCsvParserTest {

    private AnimalCsvParser parser;

    @BeforeEach
    void setUp() {
        parser = new AnimalCsvParser();
    }

    // ==================== parseRow tests ====================

    @Nested
    @DisplayName("When parsing valid rows")
    class ValidRows {

        @Test
        @DisplayName("parses a complete row into an Animal")
        void shouldParseCompleteRow() {

            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,true,2026-01-15");
            assertTrue(result.isPresent());
            assertEquals("Buddy", result.get().getName());

        }

        @Test
        @DisplayName("trims whitespace from fields")
        void shouldTrimWhitespace() {

            Optional<Animal> result = parser.parseRow("  Buddy , Dog , 3 , true , 2026-01-15 ");
            assertEquals("Buddy", result.get().getName());

        }

        @Test
        @DisplayName("parses vaccinated=false correctly")
        void shouldParseFalseVaccination() {

            Optional<Animal> result = parser.parseRow("Buddy, Dog, 3, false, 2026-01-15");
            assertFalse(result.get().isVaccinated());

        }
    }

    @Nested
    @DisplayName("When parsing malformed rows")
    class MalformedRows {

        @Test
        @DisplayName("returns empty for null input")
        void shouldReturnEmptyForNull() {

            Optional<Animal> result = parser.parseRow(null);
            assertTrue(result.isEmpty());

        }

        @Test
        @DisplayName("returns empty for blank input")
        void shouldReturnEmptyForBlank() {

            Optional<Animal> result = parser.parseRow("  ");
            assertTrue(result.isEmpty());

        }

        @Test
        @DisplayName("returns empty when row has fewer than 5 fields")
        void shouldReturnEmptyForTooFewFields() {

            Optional<Animal> result = parser.parseRow("Buddy,Dog,3");
            assertTrue(result.isEmpty());

        }

        @Test
        @DisplayName("returns empty when name is missing")
        void shouldReturnEmptyForMissingName() {

            Optional<Animal> result = parser.parseRow(",Dog,3,true,2026-01-15");
            assertTrue(result.isEmpty());

        }

        @Test
        @DisplayName("returns empty when age is not a number")
        void shouldReturnEmptyForBadAge() {

            Optional<Animal> result = parser.parseRow("Buddy,Dog,thisNotNumber,true,2026-01-15");
            assertTrue(result.isEmpty());

        }

        @Test
        @DisplayName("returns empty when age is negative")
        void shouldReturnEmptyForNegativeAge() {

            Optional<Animal> result = parser.parseRow("Buddy,Dog,-1,true,2026-01-15");
            assertTrue(result.isEmpty());

        }

        @Test
        @DisplayName("returns empty when date is invalid")
        void shouldReturnEmptyForBadDate() {

            Optional<Animal> result = parser.parseRow("Buddy,Dog,thisNotNumber,true,not-a-date");
            assertTrue(result.isEmpty());

        }
    }

    @Nested
    @DisplayName("When handling edge cases")
    class EdgeCases {

        @Test
        @DisplayName("handles vaccinated field as any non-true string → false")
        void shouldTreatNonTrueAsFalse() {

            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,maybe,2026-01-15");

            // check that an animal is present before getting it
            assertTrue(result.isPresent());
            assertFalse(result.get().isVaccinated());

        }

        @Test
        @DisplayName("handles age 0 as valid")
        void shouldAcceptAgeZero() {

            Optional<Animal> result = parser.parseRow("Buddy,Dog,0,true,2026-01-15");
            assertTrue(result.isPresent());
            assertEquals(0, result.get().getAge());
        }
    }

    // ==================== parseFile tests ====================

    @Nested
    @DisplayName("When parsing a CSV file")
    class ParseFile {

        @Test
        @DisplayName("parses valid rows and counts skipped rows")
        void shouldParseFileAndCountSkipped() throws IOException {
            // TODO: Create a temp file with a header + 3 valid rows + 1 malformed row
            //   Hint: Path tempFile = Files.createTempFile("test-intake", ".csv");
            //         Files.writeString(tempFile, content, StandardCharsets.UTF_8);
            // TODO: Call parser.parseFile(tempFile)
            // TODO: Assert result.animals() has size 3
            // TODO: Assert result.skippedRows() == 1
            // TODO: Clean up: Files.deleteIfExists(tempFile)
        }

        @Test
        @DisplayName("returns empty result for file with only a header")
        void shouldReturnEmptyForHeaderOnly() throws IOException {
            // TODO: Create a temp file with just "name,species,age,vaccinated,intakeDate"
            // TODO: Call parser.parseFile(tempFile)
            // TODO: Assert result.animals() is empty and skippedRows == 0
        }

        @Test
        @DisplayName("throws IOException for non-existent file")
        void shouldThrowForMissingFile() {
            // TODO: Call parser.parseFile(Path.of("does-not-exist.csv"))
            // TODO: Assert it throws IOException
        }
    }
}
