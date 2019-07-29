package org.yomigae.output;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import heronarts.lx.LX;
import heronarts.lx.output.LXOutput;

public class BufferSaveOutput extends LXOutput {
  public static final String DEFAULT_PATH = ".saved-buffer";

  private String filePath;

  public BufferSaveOutput(LX lx) {
    this(lx, DEFAULT_PATH);
  }

  public BufferSaveOutput(LX lx, String filePath) {
    super(lx, "BufferSaveOutput");

    this.filePath = filePath;

    framesPerSecond.setValue(1);
  }

  @Override
  protected void onSend(int[] colors, byte[] glut) {
    String tempFilePath = filePath + ".pending";
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFilePath))) {
      out.writeObject(colors);
      out.flush();
    }
    catch (Exception e) {
      System.err.println("Error saving output buffer: " + e.getMessage());
    }

    try {
      Files.move(Paths.get(tempFilePath), Paths.get(filePath), StandardCopyOption.ATOMIC_MOVE);
    }
    catch (Exception e) {
      System.err.println("Error renaming saved buffer: " + e.getMessage());
    }
  }
}
