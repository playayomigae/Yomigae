package org.yomigae;

public class ColorTemp {
  static public void convertKToRGB(float k, int[] rgb)
  {
    int red, green, blue;

    if (k < 1000)
      k = 1000;
    else if (k > 40000)
      k = 40000;
    float tmp = k / 100.0f;

    if (tmp <= 66.0f) {
      red = 255;
    } else {
      float tmpRed = 329.698727446f * (float)Math.pow(tmp - 60.0f, -0.1332047592f);
      if (tmpRed < 0) {
        red = 0;
      } else if (tmpRed > 255) {
        red = 255;
      } else {
        red = (int)tmpRed;
      }
    }

    float tmpGreen;
    if (tmp <= 66.0f) {
      tmpGreen = 99.4708025861f * (float)Math.log(tmp) - 161.1195681661f;
    } else {
      tmpGreen = 288.1221695283f *(float)Math.pow(tmp - 60.0f, -0.0755148492f);
    }
    if (tmpGreen < 0.0f) {
      green = 0;
    } else if (tmpGreen > 255.0f) {
      green = 255;
    } else {
      green = (int)tmpGreen;
    }

    if (tmp > 66.0f) {
      blue = 255;
    } else if (tmp < 19.0f) {
      blue = 0;
    } else {
      float tmpBlue = 138.5177312231f * (float)Math.log(tmp - 10.0f) - 305.0447927307f;
      if (tmpBlue < 0.0f) {
        blue = 0;
      } else if (tmpBlue > 255.0f) {
        blue = 255;
      } else {
        blue = (int)tmpBlue;
      }
    }
    rgb[0] = red;
    rgb[1] = green;
    rgb[2] = blue;
  }

  /*
    https://stackoverflow.com/questions/45433647/how-to-get-color-temperature-from-color-correction-gain

    Correlated Color Temperature (CCT) which is measured in degrees Kelvin (K) on a scale from 1,000 to 10,000.

    Calculating Color Temperature from RGB value

    1. Find out CIE tristimulus values (XYZ) as follows:
    X=(−0.14282)(R)+(1.54924)(G)+(−0.95641)(B)
    Y=(−0.32466)(R)+(1.57837)(G)+(−0.73191)(B)=Illuminance
    Z=(−0.68202)(R)+(0.77073)(G)+(0.56332)(B)

    2. Calculate the normalized chromaticity values:
    x=X/(X+Y+Z)
    y=Y/(X+Y+Z)

    3. Compute the CCT value from:
    CCT=449n3+3525n2+6823.3n+5520.33

    where n=(x−0.3320)/(0.1858−y)

    Consolidated Formula (CCT From RGB)

    CCT=449n3+3525n2+6823.3n+5520.33
    where n=((0.23881)R+(0.25499)G+(−0.58291)B)/((0.11109)R+(−0.85406)G+(0.52289)B)
  */
  static public float convertRGBtoKConsolidated(int[] rgb) {
    int r = rgb[0];
    int g = rgb[1];
    int b = rgb[2];

    float n = (0.23881f * r) + (0.25499f * g) + (-0.58291f * b) / ((0.11109f * r) + (-0.85406f * g) + (0.52289f * b));
    return 449f * (float)Math.pow(n, 3) + 3525f * (float)Math.pow(n, 2) + 6823.3f * n + 5520.33f;
  }

  static public float convertRGBtoK(int[] rgb) {
    int r = rgb[0];
    int g = rgb[1];
    int b = rgb[2];

    float x = (-0.14282f * r) + (1.54924f * g) + (-0.95641f * b);
    float y = (-0.32466f * r) + (1.57837f * g) + (-0.73191f * b);
    float z = (-0.68202f * r) + (0.77073f * g) + (0.56332f * b);

    float chx = x / (x + y + z);
    float chy = y / (x + y + z);

    float n = (chx - 0.3320f) / (0.1858f - chy);

    return 449f * (float)Math.pow(n, 3) + 3525f * (float)Math.pow(n, 2) + 6823.3f * n + 5520.33f;
  }


  public static void main(String [] args) {
    int[] rgb = new int[3];
    System.out.println("k\tk(rgb(k))\tr\tg\tb");
    for (int k = 1000; k <= 7000; k += 100) {
      convertKToRGB(k, rgb);
      System.out.println(String.format("%d\t%.4f\t%d\t%d\t%d", k, convertRGBtoK(rgb), rgb[0], rgb[1], rgb[2]));
    }
  }
}
