/*
 * Group Members:
 *   Ayal Yakobe (amy2127)
 *   Leen Alshorafa (?)
 *   Aashir Khan (?)
 *
 * Group Number 8
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class LargeSum
{
  public static void main(String[] args) throws FileNotFoundException
  {
    File myObj = new File("input.txt");
    ArrayList<String> numbers = readNumbers(myObj);
    String fullSum = addStrings(numbers);

    int cut = Math.min(10, fullSum.length());
    String first10 = fullSum.substring(0, cut);

    System.out.println("Full sum: " + fullSum);
    System.out.println("First 10 digits: " + first10);
  }

  public static ArrayList<String> readNumbers(File myObj) throws FileNotFoundException
  {
    ArrayList<String> lines = new ArrayList<>();
    try (Scanner myReader = new Scanner(myObj))
    {
      while (myReader.hasNextLine())
      {
        String data = myReader.nextLine().trim();
        if (data.length() > 0)
        {
          lines.add(data);
        }
      }
    }
    return lines;
  }

  public static String addStrings(ArrayList<String> numbers)
  {
    // Empty file / no numbers -> sum is 0
    if (numbers.isEmpty())
    {
      return "0";
    }

    // Find the longest length so we know how many digit columns to process
    int longestLength = 0;
    for (String s : numbers)
    {
      if (s.length() > longestLength)
      {
        longestLength = s.length();
      }
    }

    StringBuilder finalSum = new StringBuilder();
    int carry = 0;

    // Add column by column, from the least significant digit.
    // We index from the right of each string so we don't need to pad.
    for (int col = 0; col < longestLength; col++)
    {
      int sum = carry;
      for (String value : numbers)
      {
        int idx = value.length() - 1 - col;
        if (idx >= 0)
        {
          sum += value.charAt(idx) - '0';
        }
      }
      carry = sum / 10;
      finalSum.append(sum % 10);
    }

    // Remaining carry can be multiple digits (e.g. 200 numbers of 50 nines)
    while (carry > 0)
    {
      finalSum.append(carry % 10);
      carry /= 10;
    }

    // Reverse to most-significant-first
    String result = finalSum.reverse().toString();

    // Strip leading zeros (e.g. 0001 + 0001 -> 2, and all-zeros -> 0)
    int start = 0;
    while (start < result.length() - 1 && result.charAt(start) == '0')
    {
      start++;
    }
    return result.substring(start);
  }
}
