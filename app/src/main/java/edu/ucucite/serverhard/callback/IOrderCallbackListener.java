package edu.ucucite.serverhard.callback;

import java.util.List;

import edu.ucucite.serverhard.model.OrderModel;

public interface IOrderCallbackListener {
    void onOrderLoadSuccess(List<OrderModel> orderModelList);
    void onOrderLoadFailed(String message);
}
