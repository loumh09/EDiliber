package edu.ucucite.serverhard.model.EventBus;

import java.util.List;

import edu.ucucite.serverhard.model.SizeModel;

public class UpdateSizeModel {
    private List<SizeModel> sizeModelList;

    public UpdateSizeModel() {

    }

    public UpdateSizeModel(List<SizeModel> sizeModelList) {
        this.sizeModelList = sizeModelList;
    }

    public List<SizeModel> getSizeModelList() {
        return sizeModelList;
    }

    public void setSizeModelList(List<SizeModel> sizeModelList) {
        this.sizeModelList = sizeModelList;
    }
}
