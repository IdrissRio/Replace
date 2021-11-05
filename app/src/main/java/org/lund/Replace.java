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
import CLI.src.Format;
import CLI.src.Table;
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
  public static String replacement;
  public static boolean printHelp = false;
  public static boolean fix = false;
  public static String collectcode = "";
  public static Format readformat = Format.FANCY;
  public static Table table;

  private void setEnv(String[] args) {
    for (int i = 0; i < args.length; ++i) {
      String opt = args[i];
      if (opt.equals("-help")) {

      } else if (opt.startsWith(
                     "-replacewith=")) { // Getting the value to replace
        replacement = opt.substring(13, opt.length());
        continue;
      } else if (opt.startsWith("-fix")) { // Getting the value to replace
        fix = true;
        continue;
      } else if (opt.startsWith("-read=")) {
        opt = opt.substring(6, opt.length());
        switch (opt) {
        case "csv":
          readformat = Format.CSV;
          continue;
        case "tab":
          readformat = Format.TAB;
          continue;
        case "fancy":
          readformat = Format.FANCY;
          continue;

        default:
          System.err.println("error");
          System.exit(1);
          break;
        }
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

    table = new Table(System.in, Format.FANCY, readformat);
    for (int i = 1; i < table.getNumberRow(); ++i) {
      String filename = table.getElementRowColumn(i, "rel_path");
      FileManager fm = new FileManager(filename, fix);
      Integer line_start =
          Integer.parseInt(table.getElementRowColumn(i, "line_start"));
      Integer line_end =
          Integer.parseInt(table.getElementRowColumn(i, "line_end"));
      Integer column_start =
          Integer.parseInt(table.getElementRowColumn(i, "column_start"));
      Integer column_end =
          Integer.parseInt(table.getElementRowColumn(i, "column_end"));
      fm.replace(line_start, line_end, column_start, column_end, replacement);
      fm.print(i);
      fm.close();
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
