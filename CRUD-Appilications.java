import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * A class that performs CRUD (Create, Read, Update, Delete) operations on a file.
 */
public class FileCRUD {

    private Path filePath;

    /**
     * Constructor that initializes the local Path variable.
     * It also creates the file if it does not exist.
     *
     * @param filePath the file path
     */
    public FileCRUD(String filePath) {
        this.filePath = Paths.get(filePath);
        createFileIfNotExists();
    }

    /**
     * Checks if the file does not exist and creates it if it doesn't.
     */
    private void createFileIfNotExists() {
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                System.out.printf("Unable to create file: %s\n", e.getMessage());
            }
        }
    }

    /**
     * Gets all the lines in the file as a list of strings and returns a string with everything joined by a line break.
     *
     * @return the entire file content as a string
     */
    public String getEntireFileContent() {
        try {
            return String.join("\n", Files.readAllLines(filePath));
        } catch (IOException e) {
            System.out.printf("Unable to read file content: %s\n", e.getMessage());
            return null;
        }
    }

    /**
     * Returns a string of the file content at the given line number.
     * Note: line number is 0 indexed.
     *
     * @param lineNumber the line number
     * @return the file content at the given line number as a string
     */
    public String getFileContentAtLine(int lineNumber) {
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines.skip(lineNumber).findFirst().get();
        } catch (NoSuchElementException e) {
            return "* Nothing present *";
        } catch (IOException e) {
            System.out.printf("Unable to read file content at line %d: %s\n", lineNumber, e.getMessage());
            return null;
        }
    }

    /**
     * Replaces the contents of the file with the string passed.
     *
     * @param newFileContent the new file content
     */
    public void updateEntireFile(String newFileContent) {
        try {
            Files.write(filePath, List.of(newFileContent.split("\n")));
        } catch (IOException e) {
            System.out.printf("Unable to write to the file content: %s\n", e.getMessage());
        }
    }

    /**
     * Replaces the content at the given line number with the string passed.
     * Note: line number is 0 indexed.
     *
     * @param newFileContent the new file content
     * @param lineNumber     the line number
     */
    public void updateFileContentAtLine(String newFileContent, int lineNumber) {
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            lines.set(lineNumber, newFileContent);
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.printf("Unable to write to the file: %s\n", e.getMessage());
        }
    }

    /**
     * Deletes the file.
     */
    public void deleteFile() {
        try {
            Files.delete(filePath);
        } catch (IOException e)
