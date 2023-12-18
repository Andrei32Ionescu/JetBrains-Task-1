import main.java.com.FSCreator;
import main.java.com.FSEntry;
import main.java.com.FSFile;
import main.java.com.FSFolder;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FSCreatorTest {

    public void deleteContents(String pathname) {
        File folder = new File(pathname);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Recursive call to delete contents of subdirectories
                        deleteContents(pathname + "//" + file.getName());
                        // Delete the folder
                        if (!file.delete()) {
                            System.err.println("Failed to delete folder: " + file.getAbsolutePath());
                        }
                    } else {
                        // Delete the file
                        if (!file.delete()) {
                            System.err.println("Failed to delete file: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    public String readFileContent(File createdFile){
        try {
            // Read the text from the file
            String readContent = new String(Files.readAllBytes(Paths.get(createdFile.getAbsolutePath())));
            return readContent;
        } catch (IOException e) {
            System.err.println("Couldn't read text from the file!");
        }
        return null;
    }

    @After
    public void restoreTestingFolder() {
        deleteContents("testingFolder");
    }

    @Test
    public void testCreateFile() {
        String fileContent = "This is a text file.";
        FSEntry file = new FSFile(fileContent, "file");
        FSCreator.create(file, "testingFolder");
        // Add assertions to check if the file is created at the specified destination
        File createdFile = new File("testingFolder\\file.txt");
        assertTrue(createdFile.exists());
        assertEquals(fileContent, readFileContent(createdFile));
    }

    @Test
    public void testCreateFolderNoContent() {
        FSEntry folder = new FSFolder(new ArrayList<>(), "folder");
        FSCreator.create(folder, "testingFolder");
        // Add assertions to check if the folder is created at the specified destination
        File createdFolder = new File("testingFolder\\folder");
        assertTrue(createdFolder.exists());
        assertTrue(createdFolder.isDirectory());
        assertEquals(0, Arrays.stream(createdFolder.listFiles()).count());
    }

    @Test
    public void testCreateFolderNullContent() {
        FSEntry folder = new FSFolder(null, "folder");
        FSCreator.create(folder, "testingFolder");
        // Add assertions to check if the folder is created at the specified destination
        File createdFolder = new File("testingFolder\\folder");
        assertTrue(createdFolder.exists());
        assertTrue(createdFolder.isDirectory());
        assertEquals(0, Arrays.stream(createdFolder.listFiles()).count());
    }

    @Test
    public void testCreateNestedFolderStructure() {
        FSFolder subFolder = new FSFolder(new ArrayList<FSEntry>(),"subFolder");
        FSFolder rootFolder = new FSFolder(List.of(subFolder),"rootFolder");
        FSCreator.create(rootFolder, "testingFolder");
        // Add assertions to check if the nested folder structure is created at the specified destination
        File createdFolder = new File("testingFolder\\rootFolder");
        assertTrue(createdFolder.exists());
        assertTrue(createdFolder.isDirectory());
        assertEquals(1, Arrays.stream(createdFolder.listFiles()).count());
        File createdSubFolder = new File("testingFolder\\rootFolder\\subFolder");
        assertTrue(createdSubFolder.exists());
        assertTrue(createdSubFolder.isDirectory());
        assertEquals(0, Arrays.stream(createdSubFolder.listFiles()).count());
    }

    @Test
    public void testCreateFilesInNestedFolder() {
        String fileContent = "This is a text file. It contains text, cool!";
        FSEntry file = new FSFile(fileContent, "nestedTextFile");
        FSFolder subFolder = new FSFolder(List.of(file),"subFolder");
        FSFolder rootFolder = new FSFolder(List.of(subFolder, file),"rootFolder");
        FSCreator.create(rootFolder, "testingFolder");
        // Add assertions to check if the nested folder structure is created at the specified destination
        File createdFolder = new File("testingFolder\\rootFolder");
        assertTrue(createdFolder.exists());
        assertTrue(createdFolder.isDirectory());
        assertEquals(2, Arrays.stream(createdFolder.listFiles()).count());
        File createdRootFolderFile = new File("testingFolder\\rootFolder\\nestedTextFile.txt");
        assertTrue(createdRootFolderFile.exists());
        assertFalse(createdRootFolderFile.isDirectory());
        assertEquals(fileContent, readFileContent(createdRootFolderFile));
        File createdSubFolder = new File("testingFolder\\rootFolder\\subFolder");
        assertTrue(createdSubFolder.exists());
        assertTrue(createdSubFolder.isDirectory());
        assertEquals(1, Arrays.stream(createdSubFolder.listFiles()).count());
        File createdSubFolderFile = new File("testingFolder\\rootFolder\\subFolder\\nestedTextFile.txt");
        assertTrue(createdSubFolderFile.exists());
        assertFalse(createdSubFolderFile.isDirectory());
        assertEquals(fileContent, readFileContent(createdSubFolderFile));
    }


    @Test
    public void testCreateNotADirectory() {
        String fileContent = "This is a text file.";
        FSEntry file1 = new FSFile(fileContent, "file1");
        FSEntry file2 = new FSFile(fileContent, "file2");
        FSCreator.create(file1, "testingFolder");

        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            FSCreator.create(file2, "testingFolder\\file1");
        });

    }

    @Test
    public void testCreateDuplicateFile() {
        String fileContent = "This is a text file.";
        FSEntry file = new FSFile(fileContent, "file");
        FSCreator.create(file, "testingFolder");

        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            FSCreator.create(file, "testingFolder");
        });
        assertTrue(expected.getMessage().startsWith("Text file already exists:"));
    }

    @Test
    public void testCreateDuplicateFolder() {
        FSEntry folder = new FSFolder(new ArrayList<>(), "folder");
        FSCreator.create(folder, "testingFolder");
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            FSCreator.create(folder, "testingFolder");
        });
        assertTrue(expected.getMessage().startsWith("Directory already exists:"));
    }


    @Test
    public void testCreateNullEntry() {
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            FSCreator.create(null, "testingFolder");
        });
        assertTrue(expected.getMessage().startsWith("The entry supplied can't be null!"));
    }

    @Test
    public void testCreateNullDestination() {
        FSEntry file = new FSFile("This is a text file.", "file");
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            FSCreator.create(file, null);
        });
        assertTrue(expected.getMessage().startsWith("The destination supplied can't be null!"));
    }

    @Test
    public void testCreateNonExistingDestination() {
        FSEntry file = new FSFile("This is a text file.", "file");
        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            FSCreator.create(file, "testingFolder\\nonExistingFolder");
        });
        assertTrue(expected.getMessage().startsWith("No such destination directory for the entry!"));

    }
}