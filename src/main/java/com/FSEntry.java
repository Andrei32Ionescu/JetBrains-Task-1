package main.java.com;

public abstract class FSEntry {
    private String name;


    public FSEntry(String name){
        checkNameForValidity(name);
        // The entry name should have all leading and trailing whitespaces removed
        this.name = name.trim();
    }

     public String getName() {
          return name;
     }

    /*
   The following characters can't be assigned as the names of files/directories:
    - Forbidden printable ASCII characters in Windows or Linux:
           < (less than)
           > (greater than)
           : (colon - sometimes works, but is actually NTFS Alternate Data Streams)
           " (double quote)
           / (forward slash)
           \ (backslash)
           | (vertical bar or pipe)
           ? (question mark)
           * (asterisk)
    - Forbidden non-printable characters in Windows or Linux:
           0 (NULL byte) for Linux
           0-31 (ASCII control characters)
    - The following filenames are reserved for Windows:
            {"CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3",
                 "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
                 "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6",
                 "LPT7", "LPT8", "LPT9"}
    */
     private void checkNameForValidity(String name){
         // Check for forbidden printable characters
         if (name.matches(".*[\\\\\\\\/:*?\\\"<>|].*")) {
             throw new IllegalArgumentException("The entry name contains forbidden characters!");
         }

         // Check for forbidden non-printable characters
         for (char c : name.toCharArray()){
             for(int i = 0; i <=31; i++) {
                 if (c == (char) i) {
                     throw new IllegalArgumentException("The entry name contains forbidden characters!");
                 }
             }
         }

         // Check for reserved words
         String[] reservedWords = {"CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3",
                 "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
                 "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6",
                 "LPT7", "LPT8", "LPT9"};
         for (String reservedWord : reservedWords) {
             if (name.equalsIgnoreCase(reservedWord)) {
                 throw new IllegalArgumentException("The entry name can't be part of the reserved words!");
             }
         }

         // Check for name length
         if (name.length() > 255 ) {
             throw new IllegalArgumentException("The entry name can't be longer than 255 characters!");
         }

         if(name.trim().isEmpty()){
             throw new IllegalArgumentException("The entry name can't be made up of only whitespaces!");
         }
     }
}
