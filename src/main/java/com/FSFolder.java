package main.java.com;

import java.util.List;

public class FSFolder extends FSEntry{
    private List<FSEntry> folderContent;

    public FSFolder(List<FSEntry> folderContent, String name){
        super(name);
        this.folderContent = folderContent;
    }

    public List<FSEntry> getFolderContent() {
        return folderContent;
    }

}



