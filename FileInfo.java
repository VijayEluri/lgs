import java.io.File;
import java.util.Vector;

class FileInfo {

    public File getFile() {
        return this.file;
    }

    private final File file;
    private final String matchName;

    public String getMatchName() {
        return this.matchName;
    }

    public FileInfo(File aFile) {
        this.file = aFile;
        this.matchName = GetMatchName(aFile);
    }

    public static String GetMatchName(File aFile) {
        String name = aFile.getName();
        return GetMatchName(name);
    }

    public static String GetMatchName(String name) {
        int index = name.lastIndexOf('.');
        if (index > 0 && index <= name.length() - 2) {
            return name.substring(0, index);
        }
        return "";
    }

    /*
      * search for files with same matchname in the same directory as itself.
      * this is usually used for finding a xmp file to a nef file
      * we want the file itself returned also
      */
    public Vector<File> GetSiblings(File otherDir) {
        Vector<File> ret = new Vector<File>();
        File[] files;
        if (otherDir.isDirectory()) {
            files = otherDir.listFiles();
        } else {
            files = otherDir.getParentFile().listFiles();
        }
        for (File aFile : files) {
            if (this.IsMatch(aFile)) {
                ret.add(aFile);
            }
        }
        return ret;
    }

    public Boolean IsMatch(FileInfo other) {
        return this.matchName.equals(other.matchName);
    }

    public Boolean IsMatch(File other) {
        return this.matchName.equals(GetMatchName(other));
    }

    public String GetString() {
        return this.file.getName() + " >> " + this.matchName;
    }
}