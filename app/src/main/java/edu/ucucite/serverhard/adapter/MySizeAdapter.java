package edu.ucucite.serverhard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import edu.ucucite.serverhard.R;
import edu.ucucite.serverhard.callback.IRecyclerClickListener;
import edu.ucucite.serverhard.model.EventBus.SelectSizeModel;
import edu.ucucite.serverhard.model.EventBus.UpdateSizeModel;
import edu.ucucite.serverhard.model.SizeModel;

public class MySizeAdapter extends RecyclerView.Adapter<MySizeAdapter.MyViewHolder> {

    Context context;
    List<SizeModel> sizeModelList;
    UpdateSizeModel updateSizeModel;
    int editPos;

    public MySizeAdapter(Context context, List<SizeModel> sizeModelList) {
        this.context = context;
        this.sizeModelList = sizeModelList;
        editPos=-1;
        updateSizeModel = new UpdateSizeModel();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new  MyViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.layout_size_addon_display,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.txt_name.setText(sizeModelList.get(position).getName());
            holder.txt_price.setText(String.valueOf(sizeModelList.get(position).getPrice()));

            //Event
        holder.img_delete.setOnClickListener(v -> {
            //Delete item
            sizeModelList.remove(position);
            notifyItemRemoved(position);
            updateSizeModel.setSizeModelList(sizeModelList); //Set for event
            EventBus.getDefault().postSticky(updateSizeModel); //Send event
        });

        holder.setListener((view, pos) -> {
            editPos =position;
            EventBus.getDefault().postSticky(new SelectSizeModel(sizeModelList.get(pos)));

        });
    }

    @Override
    public int getItemCount() {

        return sizeModelList.size();
    }

    public void addNewSize(SizeModel sizeModel) {
        sizeModelList.add(sizeModel);
        notifyItemInserted(sizeModelList.size()-1);
        updateSizeModel.setSizeModelList(sizeModelList);
        EventBus.getDefault().postSticky(updateSizeModel);

    }

    public void editSize(SizeModel sizeModel) {
        if (editPos != -1)
        {
            sizeModelList.set(editPos,sizeModel);
            notifyItemChanged(editPos);
            editPos = -1; //Reset Variable

            //Send update
            updateSizeModel.setSizeModelList(sizeModelList);
            EventBus.getDefault().postSticky(updateSizeModel);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.img_delete)
        ImageView img_delete;

        Unbinder unbinder;

        IRecyclerClickListener listener;

        public void setListener (IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view ) {
                    listener.onItemClickListener(view, getAdapterPosition());
                }
            });
        }
    }
}
