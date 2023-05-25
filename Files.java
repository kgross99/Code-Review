import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Files {
    //Method to create a new file with the provided path.
    public static File createFile(String path) {
        //Create a new File.
        File file = new File(path);
        try {
            //Try to create the file.
            if (file.createNewFile()) {
                //If the file was created, return the File.
                return file;
            } else {
                //Print a message if this file already exists.
                System.out.println("File \"" + path + "\" already exists.");
            }
        } catch (IOException e) {
            //Print if an error occurred while creating the file.
            System.out.println("An error occurred while creating file \"" + path + "\"");
        }
        //Return null if the file was not able to be created.
        return null;
    }

    //Method to return whether a file with the provided path exists.
    public static boolean fileExists(String path) {
        //Create a new File and return whether this file exists.
        return new File(path).exists();
    }

    //Method to create a BufferedWriter for the provided file.
    public static BufferedWriter getFileWriter(File file) {
        try {
            //Create and return the BufferedWriter.
            return new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            //Print if there was an error while creating the BufferedWriter.
            System.out.println("Error creating BufferedWriter");
        }
        //Return null if the BufferedWriter was not able to be created.
        return null;
    }

    //Method to create a BufferedReader for the provided file.
    public static BufferedReader getFileReader(File file) {
        try {
            //Create and return the BufferedReader.
            return new BufferedReader(new FileReader(file));
        } catch (IOException e) {
            //Print if there was an error while creating the BufferedReader.
            System.out.println("Error creating BufferedWriter");
        }
        //Return null if the BufferedReader was not able to be created.
        return null;
    }
}
