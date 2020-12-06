package edu.ucucite.serverhard.model.EventBus;

import java.util.List;

import edu.ucucite.serverhard.model.AddonModel;

public class UpdateAddonModel {
    private List<AddonModel> addonModels;

    public UpdateAddonModel() {

    }

    public List<AddonModel> getAddonModels() {
        return addonModels;
    }

    public void setAddonModels(List<AddonModel> addonModels) {
        this.addonModels = addonModels;
    }
}
