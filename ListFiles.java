package TreeImpl;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListFiles {

  private static final String ANSI_BLUE = "\033[1;34m";
  private static final String ANSI_RESET = "\033[0m";

  public static void listFIles(String directory, List<File> files) {
    File current = new File(directory);

    if (files.isEmpty()) {
      System.out.println(current.getName());
    } else {
      StringBuilder tree = new StringBuilder();

      for (int i = 1; i < files.size(); i++) {
        File file = files.get(i);
        File parent = files.get(i - 1);

        List<File> parentFiles = Arrays.asList(parent.listFiles());
        Collections.sort(parentFiles);

        if (parentFiles.get(parentFiles.size() - 1).equals(file)) {
          tree.append("   ");
        } else {
          tree.append("│  ");
        }
      }

      String append = "├";

      File parent = files.get(files.size() - 1);
      List<File> parentfiles = Arrays.asList(parent.listFiles());
      Collections.sort(parentfiles);

      if (
        parentfiles.size() == 1 ||
        parentfiles.get(parentfiles.size() - 1).equals(current)
      ) {
        append = "└";
      }

      tree.append(append);
      tree.append("─ ");
      if (current.isDirectory()) {
        tree.append(ANSI_BLUE + current.getName() + ANSI_RESET);
      } else {
        tree.append(current.getName());
      }

      System.out.println(tree.toString());
    }
    if (current.isDirectory()) {
      files.add(current);
      List<String> parentStrings = Arrays.asList(current.list());
      Collections.sort(parentStrings);
      for (String child : parentStrings) {
        listFIles(directory + "/" + child, files);
      }
      files.remove(files.size() - 1);
    }
  }

  public static int[] countFiles(String directory) {
    int[] result = new int[2];
    int drcCount = 0;
    int fileCount = 0;
    File temp = new File(directory);
    File[] countFiles = temp.listFiles();
    if (countFiles != null) {
      for (File file : countFiles) {
        if (file.isDirectory()) {
          drcCount++;
          int[] tempCount = countFiles(file.getAbsolutePath());
          drcCount += tempCount[0];
          fileCount += tempCount[1];
        } else {
          fileCount++;
        }
      }
      result[0] += drcCount;
      result[1] += fileCount;
    }
    return result;
  }

  public static void listDirectoryFiles(String directory) {
    try {
      Paths.get(directory);

      List<File> files = new ArrayList<>();

      int[] countedFiles = countFiles(directory);
      listFIles(directory, files);
      System.out.println(
        "\n" + countedFiles[0] + " directories, " + countedFiles[1] + "files"
      );
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void listDirectoryFiles() {
    String directory = (System.getProperty("user.dir"));
    List<File> files = new ArrayList<>();
    int[] countedFiles = countFiles(directory);

    listFIles(directory, files);
    System.out.println(
      "\n" + countedFiles[0] + " directories, " + countedFiles[1] + " files"
    );
  }

  public static void main(String[] args) {
    if (args.length != 0) {
      listDirectoryFiles(args[0]);
    } else {
      listDirectoryFiles();
    }
  }
}
