package org.yomigae;

public class MathUtils {

  static public final float map(float value,
                                float istart,
                                float istop,
                                float ostart,
                                float ostop) {
      return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
  }

}