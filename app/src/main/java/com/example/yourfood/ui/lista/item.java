package com.example.yourfood.ui.lista;

public class item {
    private int mImageConsumato;
    private String mText1;
    private String mText2;
    private String mText3;

    public item(String text1, String text2, String text3, int ImageConsumato){

   // mImageResource=imageResource;
    mText1=text1;
    mText2=text2;
    mImageConsumato=ImageConsumato;
    mText3=text3;
    }

   public int mImageConsumato() {
        return mImageConsumato;
    }

    public int getmImageConsumato(){

        return mImageConsumato;

    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }
    public String getText3() {
        return mText3;
    }
    public void changeText1(String text){

        mText1=text;
    }

    public void disableCheck( int image){

        mImageConsumato = image;
    }



}
