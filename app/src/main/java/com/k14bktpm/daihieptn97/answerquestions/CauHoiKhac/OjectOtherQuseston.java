package com.k14bktpm.daihieptn97.answerquestions.CauHoiKhac;

import com.k14bktpm.daihieptn97.answerquestions.Question;

import java.util.Date;


public class OjectOtherQuseston extends Question {
    private String mUsename;
    private Date thoiGianDang;

    public Date getThoiGianDang() {
        return thoiGianDang;
    }

    public void setThoiGianDang(Date thoiGianDang) {
        this.thoiGianDang = thoiGianDang;
    }

    public String getmUsename() {
        return mUsename;
    }
    public void setmUsename(String mUsename) {
        this.mUsename = mUsename;
    }
}
