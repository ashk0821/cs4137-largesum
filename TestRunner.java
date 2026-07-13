/*
 * TestRunner.java - compiles/runs LargeSum against input1.txt..input10.txt
 * and compares each result to the matching output1.txt..output10.txt.
 *
 * This is a development convenience, NOT a graded deliverable.
 *
 * Usage:
 *   javac TestRunner.java LargeSum.java
 *   java TestRunner          # run all cases that exist (1..10)
 *   java TestRunner 5        # run only case 5
 *
 * LargeSum always reads a file literally named "input.txt", so for each case
 * this runner copies inputN.txt -> input.txt before invoking LargeSum, then
 * restores any pre-existing input.txt when finished.
 */
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class TestRunner
{
  // ANSI colors (harmless plain text on terminals that don't support them)
  static final String GREEN = "\033[0;32m";
  static final String RED   = "\033[0;31m";
  static final String YELLOW= "\033[0;33m";
  static final String RESET = "\033[0m";

  public static void main(String[] args) throws Exception
  {
    // Decide which cases to run
    List<Integer> cases = new ArrayList<>();
    if (args.length >= 1)
    {
      cases.add(Integer.parseInt(args[0].trim()));
    }
    else
    {
      for (int i = 1; i <= 10; i++) cases.add(i);
    }

    Path input = Paths.get("input.txt");

    // Back up any existing input.txt so we don't clobber the user's file
    Path backup = Paths.get(".input.txt.bak");
    boolean hadInput = Files.exists(input);
    if (hadInput)
    {
      Files.copy(input, backup, StandardCopyOption.REPLACE_EXISTING);
    }

    int pass = 0, fail = 0;
    try
    {
      for (int i : cases)
      {
        Path in  = Paths.get("input" + i + ".txt");
        Path exp = Paths.get("output" + i + ".txt");

        if (!Files.exists(in))
        {
          System.out.println(YELLOW + "Skipping case " + i + ": " + in + " not found." + RESET);
          continue;
        }

        // Copy inputN.txt -> input.txt, then run LargeSum
        Files.copy(in, input, StandardCopyOption.REPLACE_EXISTING);
        String actual = runLargeSum().strip();

        if (Files.exists(exp))
        {
          String expected = new String(Files.readAllBytes(exp)).strip();
          if (actual.equals(expected))
          {
            System.out.println(GREEN + "PASS" + RESET + " case " + i);
            pass++;
          }
          else
          {
            System.out.println(RED + "FAIL" + RESET + " case " + i);
            System.out.println("  expected:");
            indent(expected);
            System.out.println("  actual:");
            indent(actual);
            fail++;
          }
        }
        else
        {
          System.out.println("case " + i + " (no " + exp + " to compare):");
          indent(actual);
        }
      }
    }
    finally
    {
      // Restore or remove input.txt
      if (hadInput)
      {
        Files.move(backup, input, StandardCopyOption.REPLACE_EXISTING);
      }
      else
      {
        Files.deleteIfExists(input);
      }
    }

    System.out.println();
    System.out.println("-----------------------------------------");
    System.out.println("Passed: " + pass + "   Failed: " + fail);
    if (fail != 0) System.exit(1);
  }

  /**
   * Runs LargeSum.main in-process and captures everything it prints to stdout.
   * This avoids spawning a separate JVM and works as long as LargeSum.class is
   * on the classpath (compile both files together).
   */
  static String runLargeSum() throws Exception
  {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    System.setOut(new PrintStream(buffer, true, "UTF-8"));
    try
    {
      LargeSum.main(new String[0]);
    }
    finally
    {
      System.setOut(originalOut);
    }
    return buffer.toString("UTF-8");
  }

  static void indent(String text)
  {
    for (String line : text.split("\n", -1))
    {
      System.out.println("    " + line);
    }
  }
}