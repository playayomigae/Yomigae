package org.yomigae.patterns;

import java.io.ObjectInputStream;
import java.io.FileInputStream;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.StringParameter;

import org.yomigae.output.BufferSaveOutput;

public class LoadBufferPattern extends LXPattern {

  public final BooleanParameter loadBufferParam = new BooleanParameter("Load Buffer")
      .setDescription("Load buffer from path.")
      .setMode(BooleanParameter.Mode.MOMENTARY);

  public final StringParameter filePathParam = new StringParameter("File Path")
      .setDescription("Path to buffer file.");

  private int[] buffer;

  public LoadBufferPattern(LX lx) {
    this(lx, BufferSaveOutput.DEFAULT_PATH);
  }

  public LoadBufferPattern(LX lx, String filePath) {
    super(lx);

    addParameter("load-buffer", loadBufferParam);
    addParameter("file-path", filePathParam);

    filePathParam.setValue(filePath);
    loadBufferParam.setValue(true);
  }

  @Override
  public void onParameterChanged(LXParameter parameter) {
    if (parameter == loadBufferParam) {
      loadBufferParam.setValue(false);

      loadBuffer();
    }
  }

  private void loadBuffer() {
    String filePath = filePathParam.getString();

    if (filePath == null || filePath.isEmpty())
      return;

    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
      buffer = (int[])in.readObject();
    }
    catch (Exception e) {
      System.err.println("Error loading color buffer: " + e.getMessage());
    }
  }

  @Override
  public synchronized void run(double deltaMs) {
    int[] buffer = this.buffer;

    if (buffer == null || buffer.length != colors.length)
      return;

    for (int i = 0; i < colors.length; ++i) {
      colors[i] = buffer[i];
    }
  }
}
