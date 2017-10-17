package com.k14bktpm.daihieptn97.answerquestions.CauHoiKhac;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.k14bktpm.daihieptn97.answerquestions.R;
import java.util.ArrayList;



public class adpter_otherQuestion extends BaseAdapter {
    private ArrayList<OjectOtherQuseston> listOtherQuestion;
    private Context context;
    int layout;

    public adpter_otherQuestion(ArrayList<OjectOtherQuseston> listOtherQuestion, Context context, int layout) {
        this.listOtherQuestion = listOtherQuestion;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return listOtherQuestion.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_other_queston, null);
        AnhXa(convertView);

        tvCauHoi.setText(listOtherQuestion.get(position).getTheQuestion());
        tvTime.setText(listOtherQuestion.get(position).getTime());
        tvUsername.setText(listOtherQuestion.get(position).getmUsename());
        TextDrawable textDrawable;

        if (listOtherQuestion.get(position).getYesOrNo() == 0) {
            String a = "Y";
            textDrawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(10)
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT_BOLD)
                    .fontSize(30) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRect(a, convertView.getResources().getColor(R.color.y_answer));

            //textDrawable = TextDrawable.builder().buildRound(a, convertView.getResources().getColor(R.color.y_answer));
        } else {
            String a = "N";
            // textDrawable = TextDrawable.builder().buildRound(a, Color.RED);
            textDrawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(10)
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT_BOLD)
                    .fontSize(30) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRect(a, convertView.getResources().getColor(R.color.N_answer));
        }

        imgTitle.setImageDrawable(textDrawable);


        return convertView;
    }


    private TextView tvCauHoi, tvTime, tvUsername;
    private ImageView imgTitle;

    private void AnhXa(View itemView) {
        tvUsername = itemView.findViewById(R.id.tvAdapterUseName);
        tvCauHoi = itemView.findViewById(R.id.tvadapterCauhoi);
        imgTitle = itemView.findViewById(R.id.img_adpater_Title);
        tvTime = itemView.findViewById(R.id.tvadapterTime);
    }
}
