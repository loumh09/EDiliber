package edu.ucucite.serverhard.model.EventBus;

import edu.ucucite.serverhard.model.AddonModel;

public class SelectAddonModel {
    private AddonModel addonModel;

    public SelectAddonModel(AddonModel addonModel) {
        this.addonModel = addonModel;
    }

    public AddonModel getAddonModel() {
        return addonModel;
    }

    public void setAddonModel(AddonModel addonModel) {
        this.addonModel = addonModel;
    }
}
