package dietrixapp.dietrix;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

class WtHisRecyclerAdapter extends RecyclerView.Adapter<WtHisRecyclerAdapter.MyHolderHistory> {

    private Context ctx;
    private ArrayList<WeightHistoryInfo> weightHistoryInfo;

    WtHisRecyclerAdapter(Context ctx, ArrayList<WeightHistoryInfo> weightHistoryInfo){
        this.ctx=ctx;
        this.weightHistoryInfo=weightHistoryInfo;

    }
    @NonNull
    @Override
    public WtHisRecyclerAdapter.MyHolderHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(ctx).inflate(R.layout.custom_row_weight_history,parent,false);
        return new WtHisRecyclerAdapter.MyHolderHistory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WtHisRecyclerAdapter.MyHolderHistory holder, int position) {
        holder.WtHisCrntWt.setText(weightHistoryInfo.get(position).getWT());
        holder.WtHisWtDt.setText(weightHistoryInfo.get(position).getWtDT());
        holder.WtHisName.setText(weightHistoryInfo.get(position).getWtName());
        holder.WtHisCrBmi.setText(weightHistoryInfo.get(position).getWtHisCrBMI());
        holder.WtHisRemove.setVisibility(View.INVISIBLE);

        holder.WtHisCrntWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.WtHisRemove.setVisibility(View.VISIBLE);

                holder.WtHisRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                        alert.setTitle("Remove Record?");
                        alert.setMessage("This weight record will be removed!");
                        alert.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyDb.TblWeights.deleteWtInfo(holder.WtHisName.getText().toString(),
                                        holder.WtHisWtDt.getText().toString(),holder.WtHisCrntWt.getText().toString(),
                                        holder.WtHisWtDt.getText().toString());
                                weightHistoryInfo.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(),getItemCount());
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.WtHisRemove.setVisibility(View.INVISIBLE);
                            }
                        });
                        alert.show();
                    }
                });
            }
        });

        holder.WtHisCrntWt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                   holder.WtHisRemove.setVisibility(View.VISIBLE);
                else
                    holder.WtHisRemove.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(weightHistoryInfo != null)
        return weightHistoryInfo.size();
        else return 0;
    }

    class MyHolderHistory extends RecyclerView.ViewHolder {
        TextView WtHisCrntWt, WtHisWtDt, WtHisName, WtHisCrBmi;
        ImageButton WtHisRemove;
        MyHolderHistory(View itemView) {
            super(itemView);
            WtHisCrntWt = itemView.findViewById(R.id.WtHisCrntWtTxt);
            WtHisWtDt = itemView.findViewById(R.id.WtHisDateTxt);
            WtHisName = itemView.findViewById(R.id.WtHisName);
            WtHisCrBmi = itemView.findViewById((R.id.WtHisCrBMI));
            WtHisRemove = itemView.findViewById(R.id.WtHisRemoveBtn);
        }
    }
}
