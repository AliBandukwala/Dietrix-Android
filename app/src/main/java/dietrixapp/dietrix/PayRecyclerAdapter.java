package dietrixapp.dietrix;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import static dietrixapp.dietrix.MyDb.TblBoot.USRPAY;

class PayRecyclerAdapter extends RecyclerView.Adapter<PayRecyclerAdapter.MyHolder> {

    private Context ctx;
    private ArrayList<PayRecInfo> payRecInfo;

    PayRecyclerAdapter(Context ctx, ArrayList<PayRecInfo> payRecInfo){
        this.ctx = ctx;
        this.payRecInfo = payRecInfo;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(ctx).inflate(R.layout.custom_row_payments,parent,false);
        return new MyHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        try
        {
          if(payRecInfo != null) {
              holder.PayId.setText(payRecInfo.get(position).getId2());
              holder.PayName.setText(payRecInfo.get(position).getName2());
              String due = payRecInfo.get(position).getDue2();
              SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
              Date date = df.parse(due);
              df = new SimpleDateFormat("dd MMM, yyyy");
              String newDue = df.format(date);
              holder.PayDue.setText(newDue);
              holder.PayFee.setText(payRecInfo.get(position).getFee2());
              holder.PayPhone.setText(payRecInfo.get(position).getPhone2());
              holder.PayDuration.setText(payRecInfo.get(position).getDuration2());
          }

          holder.PayRemindBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Cursor cursor = MyDb.TblBoot.getBootPayRmd();
                  String Reminder = "REMINDER: Your payment is pending. So please complete it by tomorrow to continue your services";
                  if(cursor != null){
                      cursor.moveToPosition(-1);
                      while (cursor.moveToNext()){
                          Reminder = cursor.getString(cursor.getColumnIndex(USRPAY));
                      }
                  }
                  holder.PayRemindBtn.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                  Intent intent = new Intent("android.intent.action.MAIN");
                  intent.setAction(Intent.ACTION_SEND);
                  intent.setType("text/*");
                  intent.putExtra(Intent.EXTRA_TEXT, Reminder);
                  intent.putExtra("jid",holder.PayPhone.getText().toString().replace("+","")
                          +"@s.whatsapp.net");
                  intent.setPackage("com.whatsapp");
                  ctx.startActivity(intent);

              }
          });
        } catch(Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        if(payRecInfo != null)
        return payRecInfo.size();
        else return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView PayId, PayName, PayDue, PayFee, PayPhone,PayDuration;
        Button PayRemindBtn;
        MyHolder(View itemView) {
            super(itemView);
            PayId = itemView.findViewById(R.id.PayId);
            PayName = itemView.findViewById(R.id.PayNameTxt);
            PayDue = itemView.findViewById(R.id.PayDueDateTxt);
            PayFee = itemView.findViewById(R.id.PayFeeTxt);
            PayPhone = itemView.findViewById(R.id.PayPhoneNo);
            PayDuration = itemView.findViewById(R.id.PayDuration);
            PayRemindBtn = itemView.findViewById(R.id.RemindBtn);
        }
    }
}
