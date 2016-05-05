package pubprocess;
import pubprocess.ReplacedItem;

import java.io.*;
import java.util.*;
import java.util.regex.*;

class SrcFile {
    String name;
    String ext;

    SrcFile(String fileName) {
        String regex = "(.+)(.txt)\\z";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(fileName);

        if (m.find()) {
            name = m.group(1);
            ext = m.group(2);
        } else {
            name = "";
            ext = "";
        }
    }

    String getOrgName() {
        return (name + ext);
    }

    String getOutName() {
        return (name + ".html");
    }
}

class ReplacePair {
    String before;
    String after;

    ReplacePair(String before, String after) {
        this.before = before;
        this.after = after;
    }
}

class TextProcess {
    HashSet<ReplacePair> replaceSet = new HashSet<ReplacePair>();

    boolean includedText(String line, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        return m.find();
    }

    void setReplacedWord(String before, String after) {
        ReplacePair pair = new ReplacePair(before, after);
        replaceSet.add(pair);
    }

    void clearReplaceWord() {
        replaceSet.clear();
    }

    void editAndCopy(SrcFile src, String readFile, boolean needBr) {
        try {
            /* postscript */
            PrintWriter write = new PrintWriter(new BufferedWriter(new FileWriter(src.getOutName(), true)));

            try {
                BufferedReader rdBuf = new BufferedReader(new FileReader(readFile));

                String line;
                while ((line = rdBuf.readLine()) != null) {
                    Iterator itr = replaceSet.iterator();
                    while(itr.hasNext()) {
                        ReplacePair pair = (ReplacePair)itr.next();
                        line = line.replaceAll(pair.before, pair.after);
                    }

                    if (needBr) {
                        line += "<br>";
                    }

                    write.println(line);
                }
                rdBuf.close();
            }
            catch (FileNotFoundException errInfo) {
                System.out.println("File Error: " + errInfo);
            }
            catch (IOException errInfo) {
                System.out.println("Read Error: " + errInfo);
            }
            write.close();
        }
        catch (IOException errInfo) {
            System.out.println("Write Error: " + errInfo);
        }
    }
}

class OutText extends TextProcess {
    private SrcFile src;
    private String nextPage = "test";

    OutText(SrcFile src) {
        this.src = src;
    }

    protected void setHeader() {
        initOutFile();
        clearReplaceWord();
        /* set title */
        setReplacedWord("<title>", ("<title>" + src.name));
        /* write header */
        editAndCopy(src, "header.xhtml", false);
    }

    protected void setFooter() {
        try {
            /* postscript */
            PrintWriter write = new PrintWriter(new BufferedWriter(new FileWriter(src.getOutName(), true)));
            write.println("</body>");
            write.println("</html>");
            write.close();
        }
        catch (IOException errInfo) {
            System.out.println("Write Error: " + errInfo);
        }
    }

    protected void setBodyText(ReplacedItem replacedItem) {
        clearReplaceWord();
        /* prepare to replace */
        for (String word: replacedItem.getItems()) {
            setReplacedWord(word, (nextPage + word));
        }
        /* ruby */
        setReplacedWord("｜", "<ruby>");
        setReplacedWord("《", "<rt>");
        setReplacedWord("》", "</rt></ruby>");
        /* tcy */
        setReplacedWord("!\\?", "<span class='tcy'>!\\?</span>");
        setReplacedWord("!!", "<span class='tcy'>!!</span>");
        /* write file */
        editAndCopy(src, src.getOrgName(), true);
    }

    private void initOutFile() {
        try {
            /* over write */
            PrintWriter write = new PrintWriter(new BufferedWriter(new FileWriter(src.getOutName())));
            write.print("");
            write.close();
        }
        catch (IOException errInfo) {
            System.out.println("Write Error: " + errInfo);
        }
    }
}

public class PubProcess {
    private String fileName;

    public PubProcess(String fileName) {
        this.fileName = fileName;
    }

    public void convertFile(ReplacedItem replacedItem) {
        SrcFile srcFile = new SrcFile(fileName);
        if (!srcFile.ext.equals(".txt")) {
            System.out.println("Supporting text file only.");
            return;
        }

        String outName = srcFile.getOutName();
        System.out.println("Output: " + outName);

        OutText out = new OutText(srcFile);

        out.setHeader();
        out.setBodyText(replacedItem);
        out.setFooter();
    }
}

