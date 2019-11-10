package dietrixapp.dietrix;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static dietrixapp.dietrix.MyDb.TblBoot.USRWT;

class WeightRecyclerAdapter extends RecyclerView.Adapter<WeightRecyclerAdapter.MyHolderWeight> {

    private Context ctx;
    private ArrayList<WeightRecInfo> weightRecInfo;

    WeightRecyclerAdapter(Context ctx, ArrayList<WeightRecInfo> weightRecInfo){
        this.ctx = ctx;
        this.weightRecInfo = weightRecInfo;
    }

    @NonNull
    @Override
    public WeightRecyclerAdapter.MyHolderWeight onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(ctx).inflate(R.layout.custom_row_weight,parent,false);
        return new MyHolderWeight(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WeightRecyclerAdapter.MyHolderWeight holder, int position) {
        holder.WeightID.setText(weightRecInfo.get(position).getIdW());
        holder.WeightName.setText(weightRecInfo.get(position).getNameW());
        holder.WeightPhone.setText(weightRecInfo.get(position).getPhoneW());
        holder.WeightHt.setText(weightRecInfo.get(position).getHtW());
        holder.WeightCWt.setText(weightRecInfo.get(position).getCWeightW());
        holder.WeightJnBMI.setText(weightRecInfo.get(position).getJnBmiW());
        holder.WeightCrBMI.setText(weightRecInfo.get(position).getCrBmiW());
        holder.WeightCWt.setCursorVisible(false);
        holder.WeightDate.setText(weightRecInfo.get(position).getCWtDtW());
        holder.WeightEdit.setVisibility(View.INVISIBLE);

        holder.WeightCWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.WeightEdit.setVisibility(View.VISIBLE);
                holder.WeightCWt.setCursorVisible(true);
                holder.WeightEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.WeightEdit.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
                        String date = df.format(c.getTime());
                        holder.WeightDate.setText(date);

                        String height1 = holder.WeightHt.getText().toString().substring(0,1);
                        String height2 = holder.WeightHt.getText().toString().substring(2);
                        double Height1 = Double.parseDouble(height1);
                        double Height2 = Double.parseDouble(height2) * 0.083;
                        double ht = Height1 + Height2;

                        double wt = Double.parseDouble(holder.WeightCWt.getText().toString());
                        DecimalFormat decf = new DecimalFormat(".##");
                        double NewBmi = (wt/((ht*0.305)*(ht*0.305)));
                        String BMI = decf.format(NewBmi);
                        holder.WeightCrBMI.setText(BMI);

                        boolean WeightUpdated = MyDb.TblClients.updateWeight(holder.WeightID.getText().toString(),
                                holder.WeightCWt.getText().toString(),holder.WeightDate.getText().toString(),
                                holder.WeightCrBMI.getText().toString());

                        long inserted = MyDb.TblWeights.insertWeightData(holder.WeightName.getText().toString(),
                                holder.WeightPhone.getText().toString(),holder.WeightCWt.getText().toString(),
                                holder.WeightDate.getText().toString(), holder.WeightJnBMI.getText().toString(),
                                holder.WeightCrBMI.getText().toString());

                        if(WeightUpdated && inserted > 0){
                            Toast.makeText(ctx,holder.WeightName.getText().toString()+"'s Current Weight Updated Successfully.",
                                    Toast.LENGTH_SHORT).show();
                        holder.WeightEdit.setVisibility(View.INVISIBLE);
                        holder.WeightCWt.setCursorVisible(false);
                        }
                        else {Toast.makeText(ctx,"Unable to Update the Weight!",Toast.LENGTH_SHORT).show();}
                    }
                });
            }
        });

        holder.WeightCWt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                holder.WeightEdit.setVisibility(View.INVISIBLE);
            }
        });

        holder.WeightRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.WeightRemind.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                String Reminder = "REMINDER: Please measure your Current Weight and send it to me by today.";
                Cursor cursor = MyDb.TblBoot.getBootWtRmd();
                if(cursor != null){
                    cursor.moveToPosition(-1);
                    while (cursor.moveToNext()){
                       Reminder = cursor.getString(cursor.getColumnIndex(USRWT));
                    }
                }
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_TEXT, Reminder);
                intent.putExtra("jid",holder.WeightPhone.getText().toString().replace("+","")
                        +"@s.whatsapp.net");
                intent.setPackage("com.whatsapp");
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(weightRecInfo != null)
        return weightRecInfo.size();
        else return 0;
    }

    class MyHolderWeight extends RecyclerView.ViewHolder {
        TextView WeightID, WeightName, WeightPhone, WeightDate, WeightHt, WeightJnBMI, WeightCrBMI;
        EditText WeightCWt;
        Button WeightRemind;
        ImageButton WeightEdit;
        MyHolderWeight(View itemView) {
            super(itemView);
            WeightID = itemView.findViewById(R.id.WeightID);
            WeightName = itemView.findViewById(R.id.WeightNameTxt);
            WeightPhone = itemView.findViewById(R.id.WeightPhone);
            WeightCWt = itemView.findViewById(R.id.WeightCrntWt);
            WeightDate = itemView.findViewById(R.id.WeightDate);
            WeightHt = itemView.findViewById(R.id.Weightheight);
            WeightJnBMI = itemView.findViewById(R.id.WeightJnBMITxt);
            WeightCrBMI = itemView.findViewById(R.id.WeightCrBMITxt);
            WeightRemind = itemView.findViewById(R.id.WeightRemind);
            WeightEdit = itemView.findViewById(R.id.WeightEdit);
        }
    }

    void setFilter(ArrayList<WeightRecInfo> FilterClList)
    {
        weightRecInfo = new ArrayList<>();
        weightRecInfo.addAll(FilterClList);
        notifyDataSetChanged();
    }
}
