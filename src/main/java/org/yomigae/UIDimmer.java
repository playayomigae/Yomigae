package org.yomigae;

import heronarts.lx.parameter.LXParameter;
import heronarts.lx.studio.LXStudio;

public class UIDimmer extends UIConfig {
  public static final String PAR_DIMMER = "Par";
  public static final String WALLWASHER_DIMMER = "Washer";

  public String filename;

  public int parDimmer = 255;
  public int wallWasherDimmer = 255;

  public UIDimmer(final LXStudio.UI ui, String title, String filenameBase) {
    super(ui, title, filenameBase + ".json");
    filename = filenameBase + ".json";

    parDimmer = (int)registerCompoundParameter(PAR_DIMMER, 255f, 0f, 255f).getValuef();
    wallWasherDimmer = (int)registerCompoundParameter(WALLWASHER_DIMMER, 255f, 0f, 255f).getValuef();
    save();
    buildUI(ui);
  }

  @Override
  public void onParameterChanged(LXParameter p) {
    super.onParameterChanged(p);
    if (PAR_DIMMER.equals(p.getLabel())) {
      parDimmer = (int)p.getValuef();
    } else if (WALLWASHER_DIMMER.equals(p.getLabel())) {
      wallWasherDimmer = (int)p.getValuef();
    }
  }
}
