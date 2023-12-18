package main.java.com;

public class FSFile extends FSEntry{
    private String fileContent;

    public FSFile (String fileContent, String name){
        super(name);
        this.fileContent = fileContent;
    }

    public String getFileContent() {
        return fileContent;
    }
}
