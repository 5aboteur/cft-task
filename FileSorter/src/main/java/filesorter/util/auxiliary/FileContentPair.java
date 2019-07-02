package filesorter.util.auxiliary;

/**
 * Represents a [FILE <=> CONTENT] pair, where FILE is the name of a file you
 * want to sort, and CONTENT is the file data.
 */
public class FileContentPair
{
    private String fileName;
    private String[] content;

    public FileContentPair(String fileName, String[] content)
    {
        this.fileName = fileName;
        this.content = content;
    }

    public FileContentPair(FileContentPair pair)
    {
        this.fileName = pair.fileName;
        this.content = pair.content;
    }

    public String getFileName() { return fileName; }
    public String[] getRawContent() { return content; }

    public String[] getContentInStringRepr() { return content; }
    public Integer[] getContentInIntegerRepr() throws NumberFormatException
    {
        Integer[] integers = new Integer[content.length];
        int i = 0;
        for (String s: content) {
            try {
                integers[i++] = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.err.println("Not a number: " + s + " at index " + i);
                throw new NumberFormatException();
            }
        }
        return integers;
    }

    public void setContentFromIntegerRepr(Integer[] integers)
    {
        String[] strings = new String[integers.length];
        for (int i = 0; i < integers.length; i++) {
            strings[i] = integers[i].toString();
        }
        this.content = strings;
    }

    public String toString()
    {
        return "PAIR {\n" + " file: " + fileName + "\n"
            + " content: " + content.toString() + "\n"
            + "}\n";
    }
}