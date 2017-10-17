package com.k14bktpm.daihieptn97.answerquestions;

/**
 * Created by Hiep Tran on 10/10/2017.
 */

public class Question {
    private String theQuestion;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTheQuestion() {
        return theQuestion;
    }

    public void setTheQuestion(String theQuestion) {
        this.theQuestion = theQuestion;
    }

    public int getYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(int yesOrNo) {
        this.yesOrNo = yesOrNo;
    }

    private int yesOrNo;

}
