package org.yomigae;

import java.util.List;

// import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.p3lx.P3LX;
import heronarts.p3lx.ui.UI;
import heronarts.p3lx.ui.UI3dComponent;
import processing.core.PConstants;
import processing.core.PGraphics;

public class TempleVisualizer extends UI3dComponent {
  private static final float TOWER_H = 14 * 12;
  private static final float TOP_PLANE = 5 * 3;

  private static final int TOWER_COLOR = 0xFF555555;

  private final P3LX lx;
  // private final LXModel model;

  public TempleVisualizer(P3LX lx) {
    this.lx = lx;
    // this.model = null;
  }

  @Override
  protected void onDraw(UI ui, PGraphics pg) {
    // int[] colors = lx.getColors();
    pg.lights();

    pg.pushStyle();
    {
      // X == red
      pg.stroke(255, 0, 0);
      pg.line(0, 0, 100, 0);
    }
    pg.popStyle();

    pg.pushStyle();
    pg.pushMatrix();
    {
      // Y == green
      pg.rotateZ((float) Math.PI / 2);
      pg.stroke(0, 255, 0);
      pg.line(0, 0, 100, 0);
    }
    pg.popMatrix();
    pg.popStyle();

    pg.pushStyle();
    pg.pushMatrix();
    {
      // Z == blue
      pg.rotateY((float) Math.PI / 2);
      pg.stroke(0, 0, 255);
      pg.line(0, 0, 100, 0);
    }
    pg.popMatrix();
    pg.popStyle();

    pg.noStroke();
    pg.fill(TOWER_COLOR);

    List<Tori> toris = Tori.CreateGates();
    List<LXPoint> lights = Tori.createLXPoints(true);

    int count = 0;

    for (Tori tori : toris) {
      pg.pushMatrix();
      // nudging with magic numbers for now until I understand how the torii positions
      // were calculated
      pg.translate(tori.gateX + 2, tori.gateY + 4, tori.gateZ - 5);
      pg.box(2, 10, 1);
      pg.translate(0, 0, 2);

      if (count < 6) {
        pg.pointLight(255, 255, 255, 0, 0, 0);
        count++;
      }

      pg.popMatrix();
    }

    // for (LXPoint light : lights) {
    // // for (int i = 0; i < 4; i++) {
    // // LXPoint light = lights.get(i);
    // pg.pushMatrix();
    // pg.translate(light.xn, light.yn, light.zn);
    // pg.pointLight(255, 255, 255, 0, 0, 0);
    // pg.popMatrix();
    // }
    // }

    // for (int i = 0; i < 6; i++) {
    // pg.pushMatrix();
    // pg.translate(2 * i, -(TOWER_H / 2 - TOP_PLANE), 3 * (5 - i));
    // pg.box(4, TOWER_H, 4);
    // pg.popMatrix();
    // }

    // for (LXPoint p : model.points) {
    // int c = colors[p.index];
    // pg.pushMatrix();
    // pg.translate(p.x, p.y, p.z);
    // // drop alpha channel (mimics what output does)
    // pg.fill(0xFF000000 | c);
    // pg.box(StreetlampModel.ELEMENT_X_SIZE, StreetlampModel.ELEMENT_Y_DELTA,
    // StreetlampModel.ELEMENT_Z_SIZE);
    // pg.fill(TOWER_COLOR);
    // float fillTowerHeight = TOP_PLANE - p.y - StreetlampModel.ELEMENT_Y_DELTA /
    // 2f;
    // pg.translate(0, fillTowerHeight / 2f + StreetlampModel.ELEMENT_Y_DELTA / 2f,
    // 0);
    // pg.box(StreetlampModel.ELEMENT_X_SIZE, fillTowerHeight,
    // StreetlampModel.ELEMENT_Z_SIZE);
    // pg.popMatrix();
    // }
  }
}