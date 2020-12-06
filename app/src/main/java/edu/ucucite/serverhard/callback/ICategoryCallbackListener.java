package edu.ucucite.serverhard.callback;

import java.util.List;

import edu.ucucite.serverhard.model.CategoryModel;

public interface ICategoryCallbackListener {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModelList);
    void onCategoryLoadFailed(String message);
}
