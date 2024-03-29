package jp.kenschool.tango1;

import java.io.Serializable;

public class Word implements Serializable {

    /*――――――――――――――――――――――――――――――――――――――――――――――
       フィールド（DBとやり取りするデータ）
     ―――――――――――――――――――――――――――――――――――――――――――――――*/
    private int wordID = 0;
    private String jpn = "";
    private String eng = "";
    private int cnt = 0;
    private int correct = 0;
    private int previous = 0;
    private int rate = 0;
    private String createdBy = "1900-01-01";
    private int cateID = 0;
    private int userID = 0;

     /*―――――――――――――――――――――――――――――――――――――――――――――
        表示用
     ―――――――――――――――――――――――――――――――――――――――――――――――*/
     @Override
     public String toString(){ //一覧を文字列にして返す
         //【1】りんご → apple [0,0,0,0,0]
         String str = "【"+wordID+"】 "+jpn+" → "+eng+"\n         ["
                 +cnt+", "+correct+", "+previous+", "+rate+", "
                 +createdBy+", "+cateID+", "+userID+" ]";
         return str;
     }

    /*―――――――――――――――――――――――――――――――――――――――――――――
        引数で指定した言語を返す
    ―――――――――――――――――――――――――――――――――――――――――――――――*/
    public String getLang(int lang) {
        if(lang % 2 == 1) return jpn;
        else return eng;
    }

    /*―――――――――――――――――――――――――――――――――――――――――――――
        getter, setter
     ――――――――――――――――――――――――――――――――――――――――――――――*/
    public int getWordID() {
        return wordID;
    }
    public void setWordID(int wordID) {
        this.wordID = wordID;
    }
    public String getJpn() {
        return jpn;
    }
    public void setJpn(String jpn) {
        this.jpn = jpn;
    }
    public String getEng() {
        return eng;
    }
    public void setEng(String eng) {
        this.eng = eng;
    }
    public int getCnt() {
        return cnt;
    }
    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
    public int getCorrect() {
        return correct;
    }
    public void setCorrect(int correct) {
        this.correct = correct;
    }
    public int getPrevious() {
        return previous;
    }
    public void setPrevious(int previous) {
        this.previous = previous;
    }
    public int getRate() {
        return rate;
    }
    public void setRate(int rate) {
        this.rate = rate;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public int getCateID() {
        return cateID;
    }
    public void setCateID(int cateID) {
        this.cateID = cateID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

}