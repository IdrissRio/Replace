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
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.Scanner;
public class FileManager {
  String fileName;
  Scanner file;
  String code = "";
  Boolean onFile = false;
  public FileManager(String filename, Boolean onFile) {
    try {
      this.fileName = filename;
      this.file = new Scanner(new File(filename));
      this.onFile = onFile;
    } catch (Throwable t) {
      System.out.println("Error when opening the file: " + filename);
    }
  }
  public void replace(Integer line_start, Integer line_end,
                      Integer column_start, Integer column_end,
                      String replacement) {
    if (!onFile) {
      replacement = "\033[1;33m" + replacement + "\u001b[0m";
    }
    String newline = "\n";
    StringBuilder b = new StringBuilder(newline);
    for (int i = 0; i < line_end - line_start; i++) {
      b.append("\n");
    }
    for (int i = 0; i < line_start - 1; i++) {
      code += file.nextLine() + newline;
    }
    String target_line = file.nextLine();
    code += target_line.substring(0, column_start - 1) + replacement;
    for (int i = line_start - 1; i < line_end - 1; ++i) {
      file.nextLine(); // ignoring the other lines
      System.out.println("here");
    }
    if (line_start != line_end)
      target_line = file.nextLine();
    code += target_line.substring(column_end, target_line.length()) +
            b; // TODO: this doesn't work when we have more \n
    while (file.hasNextLine()) {
      code += file.nextLine() + newline;
    }
  }

  public void close() { file.close(); }

  public void print(Integer iter) {
    if (onFile) {
      try {
        FileWriter of = new FileWriter(fileName);
        of.write(code);
        of.close();
      } catch (Exception e) {
        System.err.println("Error. Cannot open/override file " + fileName);
      }
    } else {
      System.out.println(
          "\n----------\u001b[33m[INFO]\u001b[0m: Preview replacement #" +
          iter + "----------");
      System.out.print(code);
    }
  }
}
