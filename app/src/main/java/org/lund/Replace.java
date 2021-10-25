/* Copyright (c) 2021, Idriss Riouak <idriss.riouak@cs.lth.se>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.lund;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Perform static semantic checks on a Java program.
 */
public class Replace {

  public static String[] tokens;
  public static String specified_replacement;
  public static boolean printHelp = false;
  public static boolean fix = false;
  public static String collectcode = "";

  private void setEnv(String[] args) {
    for (int i = 0; i < args.length; ++i) {
      String opt = args[i];
      if (opt.equals("-help")) {

      } else if (opt.startsWith(
                     "-replacewith=")) { // Getting the value to replace
        specified_replacement = opt.substring(13, opt.length());
        continue;
      } else if (opt.startsWith("-fix")) { // Getting the value to replace
        fix = true;
        continue;
      } else {
        System.err.println("Unrecognized parameter:" + opt);
        printOptionsUsage();
      }
    }
  }

  /**
   * Entry point for the Java checker.
   * @param args command-line arguments
   */
  public static void main(String args[]) {
    Replace replace = new Replace();
    replace.setEnv(args);

    // Reading the table values from the command line.

    Scanner stdin = new Scanner(System.in);

    try {
      while (stdin.hasNextLine()) {
        stdin.nextLine();
        stdin.nextLine();
        stdin.nextLine();
        String line = stdin.nextLine();
        tokens = line.split("\\|");
        for (int i = 0; i < tokens.length; i++) {
          tokens[i] = tokens[i].trim();
        }
      }
    } catch (Exception e) {
      System.err.println("");
    }

    // Reading the file at the specified location

    String fileName = tokens[5];

    if (fix) {
      // Write code here to fix the existing file
      try {
        Scanner scanner = new Scanner(new File(fileName));
        int line_start = Integer.parseInt(tokens[1]);
        int line_end = Integer.parseInt(
            tokens[2]); // TODO: handle chagnes on more than one line
        int column_start = Integer.parseInt(tokens[3]);
        int column_end = Integer.parseInt(tokens[4]);

        while (scanner.hasNextLine()) {
          for (int i = 0; i < line_start - 1; i++) {
            collectcode += scanner.nextLine() + "\n";
          }
          String target_line = scanner.nextLine();
          collectcode += target_line.substring(0, column_start - 1) +
                         specified_replacement +
                         target_line.substring(column_end - 1) + "\n";
          while (scanner.hasNextLine()) {
            collectcode += scanner.nextLine() + "\n";
          }
        }
      } catch (Exception e) {
        System.err.println("");
      }
      try {
        FileWriter newfile = new FileWriter(fileName);
        newfile.write(collectcode);
        newfile.close();
      } catch (Exception e) {
        System.err.println("");
      }

    } else {
      try {
        Scanner scanner = new Scanner(new File(fileName));
        int line_start = Integer.parseInt(tokens[1]);
        int line_end = Integer.parseInt(tokens[2]);
        int column_start = Integer.parseInt(tokens[3]);
        int column_end = Integer.parseInt(tokens[4]);
        while (scanner.hasNextLine()) {
          for (int i = 0; i < line_start - 1; i++) {
            System.out.println(scanner.nextLine());
          }
          String target_line = scanner.nextLine();
          System.out.println(target_line.substring(0, column_start - 1) +
                             specified_replacement +
                             target_line.substring(column_end - 1));
          while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
          }
        }
      } catch (Exception e) {
        System.err.println("");
      }
    }
  }

  protected String name() { return "Replace"; }

  protected String version() { return "ProjectCourse2021"; }

  void printOptionsUsage() {
    System.out.println(name() + " - Version:" + version());
    System.out.println("Authors: Idriss Riouak & Momina Rizwan");
    System.out.println("\n");
    System.out.println("./replace *.java -find=. -replacewith=. [-fix]");
    System.out.println("\n");
    System.out.println("Available options:");
    System.out.println(
        "  -help: prints the usage and all the available options.\n");
    System.out.println(
        "  -find: specify the name of the column you want to replace. \n");
    System.out.println(
        "  -replacewith: specify the value you want to replace the literal with. \n");
    System.exit(1);
  }
}
