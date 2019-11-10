package dietrixapp.dietrix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IncomeHisRecAdapter extends RecyclerView.Adapter<IncomeHisRecAdapter.MyHolderIncome> {

    private Context ctx;
    private ArrayList<IncomeHisRecInfo> arrayList;

    IncomeHisRecAdapter(Context ctx, ArrayList<IncomeHisRecInfo> arrayList)
    {
        this.ctx = ctx;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public IncomeHisRecAdapter.MyHolderIncome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(ctx).inflate(R.layout.custom_row_income_his,parent,false);
        return new IncomeHisRecAdapter.MyHolderIncome(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeHisRecAdapter.MyHolderIncome holder, int position) {
        holder.Amt.setText(arrayList.get(position).getAmt());
        Date dt = new Date();
        try { dt = new SimpleDateFormat("yyyy-MM-dd").parse(arrayList.get(position).getMonth()); }
        catch (ParseException e) { e.printStackTrace(); }
        String str = new SimpleDateFormat("MMM yyyy").format(dt);
        holder.Month.setText(str);
        holder.Act.setText(arrayList.get(position).getAct());
        holder.Act.append(" Active clients.");
    }

    @Override
    public int getItemCount() {
        if(arrayList != null)
            return arrayList.size();

        return 0;
    }

    class MyHolderIncome extends RecyclerView.ViewHolder{
        TextView Amt, Month, Act;

        MyHolderIncome(View itemView) {
            super(itemView);
            Amt = itemView.findViewById(R.id.IncHisAmtTxt);
            Month = itemView.findViewById(R.id.IncHisMonthTxt);
            Act = itemView.findViewById(R.id.IncHisActCltTxt);
        }
    }
}
