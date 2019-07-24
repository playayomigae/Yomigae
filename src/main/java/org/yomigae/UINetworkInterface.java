package org.yomigae;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

import static java.lang.System.out;

import heronarts.lx.LX;
import heronarts.lx.parameter.DiscreteParameter;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.parameter.StringParameter;
import heronarts.lx.studio.LXStudio;
import heronarts.p3lx.ui.component.*;

public class UINetworkInterface extends UIConfig {
  public static final String NETWORK_INTERFACE = "Interface";

  public String filename;

  boolean parameterChanged = false;
  LX lx;

  private InetAddress[] addresses;
  private UIToggleSet toggle;

  public UINetworkInterface(final LXStudio.UI ui, LX lx, String title, String filenameBase) {
    super(ui, title, filenameBase + ".json");
    filename = filenameBase + ".json";

    this.lx = lx;

    List<InetAddress> addressList = loadNetworkInterfaceList();
    String[] addressesStr = addressList.stream().map(InetAddress::toString).toArray(String[]::new);
    addresses = addressList.toArray(new InetAddress[addressList.size()]);

    registerDropdownParameter(NETWORK_INTERFACE, 0, addresses);
    save();

    // selectedAddress = new UITextBox(0, 0, ui.leftPane.global.getContentWidth() - 10, 20);
    // selectedAddress.setValue("interface");
    // selectedAddress.addToContainer(this);

    toggle = new UIToggleSet(0, 0, ui.leftPane.global.getContentWidth() - 8, 20);
    toggle.setOptions(addressesStr);
    toggle.addToContainer(this);

    buildUI(ui);
  }

  @Override
  public void onParameterChanged(LXParameter p) {
    super.onParameterChanged(p);
    if (NETWORK_INTERFACE.equals(p.getLabel())) {
      int interfaceIndex = (int)p.getValuef();
      toggle.setValue(interfaceIndex);
    }

    parameterChanged = true;
  }

  @Override
  public void onSave() {
    if (parameterChanged) {
      boolean originalEnabled = lx.engine.output.enabled.getValueb();
      lx.engine.output.enabled.setValue(false);

      // TODO: Change output interface to new value

      parameterChanged = false;
      lx.engine.output.enabled.setValue(originalEnabled);
    }
  }

  private static List<InetAddress> loadNetworkInterfaceList() {
    List<InetAddress> ret = new ArrayList<InetAddress>();
    try {
      Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
      for (NetworkInterface netint : Collections.list(nets)) {
        if (!netint.isUp()
          || netint.isLoopback()
          || netint.isPointToPoint()
          || netint.isVirtual()) {
            continue;
          }

        List<InetAddress> inetAddresses = Collections.list(netint.getInetAddresses());
        inetAddresses.removeIf(addr -> !(addr instanceof Inet4Address));
        if (!inetAddresses.isEmpty()) {
          ret.add(inetAddresses.get(0));
        }
      }
    } catch (SocketException e) {
      throw new Error("Could not list network interfaces", e);
    }
    return ret;
  }
}
