package com.k14bktpm.daihieptn97.answerquestions;

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
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

/**
 * Created by Hiep Tran on 10/12/2017.
 */

public class adpterListView extends BaseAdapter {

    private ArrayList<Question> listQuestion;
    private Context context;
    private int layout;

    public adpterListView(ArrayList<Question> listQuestion, Context context, int layout) {
        this.listQuestion = listQuestion;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return listQuestion.size();
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
        convertView = inflater.inflate(R.layout.adapter_history, null);
        AnhXa(convertView);

        tvCauHoi.setText(listQuestion.get(position).getTheQuestion());
        tvTime.setText(listQuestion.get(position).getTime());

        TextDrawable textDrawable;

        if (listQuestion.get(position).getYesOrNo() == 0) {
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


    private TextView tvCauHoi, tvTime;
    private ImageView imgTitle;

    private void AnhXa(View itemView) {
        tvCauHoi = itemView.findViewById(R.id.tvadapterCauhoi);
        imgTitle = itemView.findViewById(R.id.img_adpater_Title);
        tvTime = itemView.findViewById(R.id.tvadapterTime);
    }

}
