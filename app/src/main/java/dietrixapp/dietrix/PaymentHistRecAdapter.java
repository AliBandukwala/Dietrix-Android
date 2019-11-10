package dietrixapp.dietrix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PaymentHistRecAdapter extends RecyclerView.Adapter<PaymentHistRecAdapter.MyHolder> {

    private Context ctx;
    private ArrayList<PaymentHistRecInfo> arrayList;

    PaymentHistRecAdapter(Context ctx, ArrayList<PaymentHistRecInfo> arrayList)
    {
        this.ctx = ctx;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public PaymentHistRecAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(ctx).inflate(R.layout.custom_payhist_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHistRecAdapter.MyHolder holder, int position) {
        holder.payHistMonth.setText(arrayList.get(position).getPayHistMonth());
        holder.payHistAmt.setText(arrayList.get(position).getPayHisAmt());
        holder.payHistDue.append(arrayList.get(position).getPayHistDue());
    }

    @Override
    public int getItemCount() {
        if(arrayList != null)
            return  arrayList.size();
        else
            return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView payHistMonth, payHistAmt, payHistDue;

        MyHolder(View itemView) {
            super(itemView);
            payHistMonth = itemView.findViewById(R.id.PayHistMonthTxt);
            payHistAmt = itemView.findViewById(R.id.PayHisAmtTxt);
            payHistDue = itemView.findViewById(R.id.PayHistDueTxt);
        }
    }
}
