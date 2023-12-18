package main.java.com;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FSCreator {

    public static void create(FSEntry entryToCreate, String destination){
        if(destination == null){
            throw new IllegalArgumentException("The destination supplied can't be null!");
        }

        File destinationFolder = new File(destination);

        if(!destinationFolder.exists() || !destinationFolder.isDirectory()){
            throw new IllegalArgumentException("No such destination directory for the entry!");
        }

        if(entryToCreate == null){
            throw new IllegalArgumentException("The entry supplied can't be null!");
        }

        if(entryToCreate.getClass() == FSFile.class){
            try {
                FSFile fileToCreate = (FSFile) entryToCreate;

                // Create a File object representing the text file
                // Information about file extensions is not specified, so I add the ".txt" extension to the entry's name
                // This can be easily changed, but clarifications are needed
                File file = new File(destination + "//" + fileToCreate.getName() + ".txt");

                if(!file.exists()) {
                    // Create a BufferedWriter to write to the file
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                    // Write the content to the file
                    writer.write(fileToCreate.getFileContent());

                    // Close the writer to release resources
                    writer.close();

                    System.out.println("Text file created successfully: " + file.getAbsolutePath());
                }
                else {
                   throw new IllegalArgumentException("Text file already exists: " + file.getAbsolutePath());
                }

            } catch (IOException e) {
                    /* Transform exception from checked to unchecked since we want to clearly know when file creation
                        has failed. Easily modifiable if this is not the intended functionality. */
                   throw new RuntimeException("Error creating the text file: " + e.getMessage());
            }
        }
        else if(entryToCreate.getClass() == FSFolder.class){
            FSFolder folderToCreate = (FSFolder) entryToCreate;
            // Create a File object representing the text file
            File directory = new File(destination + "//" + folderToCreate.getName());

            // Check if the directory already exists
            if (!directory.exists()) {
                // Attempt to create the directory
                boolean created = directory.mkdir();

                // Check if the directory creation was successful
                if (created) {
                    System.out.println("Directory created successfully: " + directory.getAbsolutePath());
                    if(folderToCreate.getFolderContent() != null){
                        for (FSEntry entry : folderToCreate.getFolderContent()){
                            create(entry, destination + "//" + folderToCreate.getName());
                        }
                    }
                } else {
                    throw new RuntimeException("Failed to create directory!");
                }
            } else {
                throw new IllegalArgumentException("Directory already exists: " + directory.getAbsolutePath());
            }
        }
        else{
            throw new RuntimeException("No creation method specified for this entry type.");
        }
    }
}
