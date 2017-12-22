package Shared;

import java.io.Serializable;

/**
 * InternetBankieren Created by Sven de Vries on 22-12-2017
 */
public class TempBank implements Serializable {
    private String name;
    private String shortcut;

    public String getName() {
        return name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public TempBank(String name, String shortcut) {
        this.name = name;
        this.shortcut = shortcut;
    }
}
