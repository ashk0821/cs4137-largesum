===============================================================================
 LargeSum - Project Euler #13 (modified)
===============================================================================

Group Number 8
Group Members:
  Ayal Yakobe    (amy2127)
  Leen Alshorafa (UNI: ?)
  Aashir Khan    (UNI: ?)

-------------------------------------------------------------------------------
 OVERVIEW
-------------------------------------------------------------------------------
This program reads up to 200 non-negative integers (1 to 50 digits each) from
a file named "input.txt" located in the same directory as the source, sums
them using manual grade-school addition (no BigInteger or arbitrary-precision
builtins), and prints two lines:

  Full sum: <the complete sum, leading zeros removed>
  First 10 digits: <the first 10 digits of the full sum>

If the sum has fewer than 10 digits, the second line prints as many digits as
exist (so both lines show the same number). An empty file or a sum of 0 prints
0 on both lines.

-------------------------------------------------------------------------------
 FILES
-------------------------------------------------------------------------------
  LargeSum.java              Source code.
  input1.txt  - input10.txt  Test case inputs.
  output1.txt - output10.txt Expected (and verified actual) program output for
                             the matching input file.
  README.txt                 This file.

-------------------------------------------------------------------------------
 HOW TO COMPILE AND RUN
-------------------------------------------------------------------------------
  javac LargeSum.java

To run a given test case, copy that test's input into input.txt first, since
the program always reads a file literally named "input.txt":

  cp input5.txt input.txt
  java LargeSum

Expected result for that run is in output5.txt.

-------------------------------------------------------------------------------
 ALGORITHM
-------------------------------------------------------------------------------
1. Read every non-empty line into a list of digit strings.
2. Add column by column from the least significant digit. For each column,
   index each number from its right end; numbers shorter than the current
   column contribute nothing (this avoids explicit zero-padding).
3. Track a running carry. Since there can be up to 200 numbers, a single
   column's total can exceed 9, and the final leftover carry can be more than
   one digit, so the carry is flushed digit by digit at the end.
4. Reverse the accumulated result to most-significant-first order.
5. Strip leading zeros. An all-zero or empty input collapses to "0".

No BigInteger, no arbitrary-precision library - all arithmetic is done on
individual char digits.

-------------------------------------------------------------------------------
 TEST CASES
-------------------------------------------------------------------------------
Each case lists: input -> expected full sum (and the reasoning it checks).

 1. Empty file (0 numbers)
    -> Full sum: 0 / First 10 digits: 0
    Checks the empty-file edge case (no crash, prints 0).

 2. Single short number: 42
    -> Full sum: 42 / First 10 digits: 42
    Checks a sum with fewer than 10 digits (both lines identical, no
    substring out-of-bounds).

 3. Leading zeros: 0001 + 0001
    -> Full sum: 2 / First 10 digits: 2
    Checks that leading zeros in the input are ignored / stripped from output.

 4. All zeros: 0 + 0 + 0
    -> Full sum: 0 / First 10 digits: 0
    Checks that a genuine zero sum collapses to a single 0.

 5. Carry propagation: 99 + 99
    -> Full sum: 198 / First 10 digits: 198
    Checks carry across columns and the leftover carry producing a new digit.

 6. Small three-number sum: 123 + 456 + 789
    -> Full sum: 1368 / First 10 digits: 1368
    Basic multi-number sanity check, still under 10 digits.

 7. Single 50-digit number:
    12345678901234567890123456789012345678901234567890
    -> Full sum: (same 50-digit value) / First 10 digits: 1234567890
    Checks a result longer than 10 digits so the two output lines differ, and
    confirms max digit-length handling.

 8. Mixed lengths: 1, 22, 333, 4444, 55555, 999999999999, 1, 7
    -> Full sum: 1000000060362 / First 10 digits: 1000000060
    Checks numbers of differing lengths added correctly without padding bugs.

 9. Stress case: 200 copies of a fifty-digit number of all 9s
    -> Full sum: 199999...99800 (53 digits) / First 10 digits: 1999999999
    Maximum count and maximum width together; verifies the multi-digit final
    carry. Result independently confirmed to equal 200 * (10^50 - 1).

10. Ten-digit values plus a zero line: 9999999999, 1, 0, 8888888888, 2222222222
    -> Full sum: 21111111110 / First 10 digits: 2111111111
    Checks a sum that crosses the 10-digit boundary and that a 0 line among
    real numbers is handled correctly.
