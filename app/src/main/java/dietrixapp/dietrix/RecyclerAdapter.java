package dietrixapp.dietrix;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;


class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyOwnHolder>{

    private Context ctx;
    private ArrayList<RecClNames> recClNames;

    RecyclerAdapter(Context ctx, ArrayList<RecClNames> recClNames) {
        this.ctx = ctx;
        this.recClNames = recClNames;
    }

    @Override
    public RecyclerAdapter.MyOwnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.custom_row_clients,parent,false);
        return new MyOwnHolder(view,ctx,recClNames);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.MyOwnHolder holder, int position) {
        holder.CltNames.setText(recClNames.get(position).getName());
        holder.CltId.setText(recClNames.get(position).getID());
        holder.CltPh.setText(recClNames.get(position).getPhone());
        holder.CltDue.setText(recClNames.get(position).getDue());
        holder.CltAct.setText(recClNames.get(position).getActive());
        holder.CltSpCase.setText(recClNames.get(position).getSpCase());
    }

    @Override
    public int getItemCount() {
       if(recClNames!=null)
        return recClNames.size();
        else
            return 0;
    }

   class MyOwnHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
   {
       TextView CltId,CltNames, CltPh, CltAct, CltSpCase, CltDue;
       Context ctx;
       ArrayList<RecClNames> recClNames = new ArrayList<>();
       MyOwnHolder(View itemView, Context ctx, ArrayList<RecClNames> recClNamesArrayList)
       {
           super(itemView);
           this.ctx=ctx;
           this.recClNames= recClNamesArrayList;

           CltId = itemView.findViewById(R.id.ClIdTxt);
           CltNames = itemView.findViewById(R.id.ClientNameText);
           CltPh = itemView.findViewById(R.id.ClPhTxt);
           CltAct = itemView.findViewById(R.id.ClActTxt);
           CltSpCase = itemView.findViewById(R.id.ClSpCsTxt);
           CltDue = itemView.findViewById(R.id.ClDueTxt);

           itemView.setOnClickListener(this);
           itemView.setOnLongClickListener(this);
       }

       @Override
       public void onClick(View v) {
          int position = getAdapterPosition();
           if(recClNames != null)
           {
               RecClNames recClNames1 = this.recClNames.get(position);
               Intent intent = new Intent(ctx,ClientDetails.class);
               intent.putExtra("ID",recClNames1.getID());
               this.ctx.startActivity(intent);
           }
       }

       @Override
       public boolean onLongClick(View v) {
           v.setFocusableInTouchMode(true);
           v.requestFocus();
           ClipData data = ClipData.newPlainText("","");
           View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
           v.startDragAndDrop(data, shadowBuilder,v,0);
           return true;
       }
   }

   void setFilter(ArrayList<RecClNames> FilterClList)
   {
       recClNames = new ArrayList<>();
       recClNames.addAll(FilterClList);
       notifyDataSetChanged();
   }
}
