package com.urovo.sdk.model;

import java.io.Serializable;

public class Pinpad implements Serializable {
    public Key key_0;
    public Key key_1;
    public Key key_2;
    public Key key_3;
    public Key key_4;
    public Key key_5;
    public Key key_6;
    public Key key_7;
    public Key key_8;
    public Message title;
    public Message head;
    public Message money;
    public Message info;
    public Echo echo;
    public Echo view;
    private Echo imageview;
    private pinpadArea body_imageview;
    private pinpadArea key_imageview;
    public pinpadArea body_area;
    public pinpadArea key_area;
    public pinpadArea backspace;
    public echoImageviews echo_imageviews;

    //add by urovo liangjiancong on 20230828
    public Translations translations;

    public Translations getTranslations() {
        return translations;
    }

    public void setTranslations(Translations translations) {
        this.translations = translations;
    }

    public Echo getImageview() {
        return imageview;
    }

    public void setImageview(Echo imageview) {
        this.imageview = imageview;
    }

    public pinpadArea getBody_imageview() {
        return body_imageview;
    }

    public void setBody_imageview(pinpadArea body_imageview) {
        this.body_imageview = body_imageview;
    }

    public pinpadArea getKey_imageview() {
        return key_imageview;
    }

    public void setKey_imageview(pinpadArea key_imageview) {
        this.key_imageview = key_imageview;
    }

    public pinpadArea getBackspace() {
        return backspace;
    }

    public void setBackspace(pinpadArea backspace) {
        this.backspace = backspace;
    }

    public pinpadArea getBody_area() {
        return body_area;
    }

    public void setBody_area(pinpadArea body_area) {
        this.body_area = body_area;
    }

    public pinpadArea getKey_area() {
        return key_area;
    }

    public void setKey_area(pinpadArea key_area) {
        this.key_area = key_area;
    }

    public Echo getView() {
        return view;
    }

    public void setView(Echo view) {
        this.view = view;
    }

    public Echo getEcho() {
        return echo;
    }

    public void setEcho(Echo echo) {
        this.echo = echo;
    }

    public Message getTitle() {
        return title;
    }

    public void setTitle(Message title) {
        this.title = title;
    }

    public Message getHead() {
        return head;
    }

    public void setHead(Message head) {
        this.head = head;
    }

    public Message getMoney() {
        return money;
    }

    public void setMoney(Message money) {
        this.money = money;
    }

    public Message getInfo() {
        return info;
    }

    public void setInfo(Message info) {
        this.info = info;
    }

    public Key getKey_0() {
        return key_0;
    }

    public void setKey_0(Key key_0) {
        this.key_0 = key_0;
    }

    public Key getKey_1() {
        return key_1;
    }

    public void setKey_1(Key key_1) {
        this.key_1 = key_1;
    }

    public Key getKey_2() {
        return key_2;
    }

    public void setKey_2(Key key_2) {
        this.key_2 = key_2;
    }

    public Key getKey_3() {
        return key_3;
    }

    public void setKey_3(Key key_3) {
        this.key_3 = key_3;
    }

    public Key getKey_4() {
        return key_4;
    }

    public void setKey_4(Key key_4) {
        this.key_4 = key_4;
    }

    public Key getKey_5() {
        return key_5;
    }

    public void setKey_5(Key key_5) {
        this.key_5 = key_5;
    }

    public Key getKey_6() {
        return key_6;
    }

    public void setKey_6(Key key_6) {
        this.key_6 = key_6;
    }

    public Key getKey_7() {
        return key_7;
    }

    public void setKey_7(Key key_7) {
        this.key_7 = key_7;
    }

    public Key getKey_8() {
        return key_8;
    }

    public void setKey_8(Key key_8) {
        this.key_8 = key_8;
    }

    public Key getKey_9() {
        return key_9;
    }

    public void setKey_9(Key key_9) {
        this.key_9 = key_9;
    }

    public Key key_9;
    public Key key_cancel;
    public Key key_del;
    public Key key_ok;
    public Key key_continue;
    public Key key_blank1;
    public Key key_blank2;

    public Key getKey_blank1() {
        return key_blank1;
    }

    public void setKey_blank1(Key key_blank1) {
        this.key_blank1 = key_blank1;
    }

    public Key getKey_blank2() {
        return key_blank2;
    }

    public void setKey_blank2(Key key_blank2) {
        this.key_blank2 = key_blank2;
    }

    public Key getKey_cancel() {
        return key_cancel;
    }

    public void setKey_cancel(Key key_cancel) {
        this.key_cancel = key_cancel;
    }

    public Key getKey_del() {
        return key_del;
    }

    public void setKey_del(Key key_del) {
        this.key_del = key_del;
    }

    public Key getKey_ok() {
        return key_ok;
    }

    public void setKey_ok(Key key_ok) {
        this.key_ok = key_ok;
    }

    public Key getKey_continue() {
        return key_continue;
    }

    public void setKey_continue(Key key_continue) {
        this.key_continue = key_continue;
    }

    public echoImageviews getEcho_imageviews() {
        return echo_imageviews;
    }

    public void setEcho_imageviews(echoImageviews echo_imageviews) {
        this.echo_imageviews = echo_imageviews;
    }

    public class Key {

        public int left;
        public int top;
        public int height;
        public int width;
        public String backgroundImage;
        public String backgroundColor;
        public String text;
        public String color;
        public int fontSize;
        public String display;
        public String borderStyle;
        public int borderWidth;
        public int borderRadius;
        public String borderColor;

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getBackgroundImage() {
            return backgroundImage;
        }

        public void setBackgroundImage(String backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }


        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getBorderStyle() {
            return borderStyle;
        }

        public void setBorderStyle(String borderStyle) {
            this.borderStyle = borderStyle;
        }

        public int getBorderWidth() {
            return borderWidth;
        }

        public void setBorderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
        }

        public int getBorderRadius() {
            return borderRadius;
        }

        public void setBorderRadius(int borderRadius) {
            this.borderRadius = borderRadius;
        }

        public String getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(String borderColor) {
            this.borderColor = borderColor;
        }
    }

    public class Message {
        private int left;
        private int top;
        private int height;
        private int width;
        private String backgroundImage;
        private String backgroundColor;
        private String text;
        private String color;
        private int fontSize;
        private String display;
        private String borderStyle;
        private int borderWidth;
        private int borderRadius;
        private String borderColor;

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getBackgroundImage() {
            return backgroundImage;
        }

        public void setBackgroundImage(String backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getBorderStyle() {
            return borderStyle;
        }

        public void setBorderStyle(String borderStyle) {
            this.borderStyle = borderStyle;
        }

        public int getBorderWidth() {
            return borderWidth;
        }

        public void setBorderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
        }

        public int getBorderRadius() {
            return borderRadius;
        }

        public void setBorderRadius(int borderRadius) {
            this.borderRadius = borderRadius;
        }

        public String getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(String borderColor) {
            this.borderColor = borderColor;
        }
    }

    public class Echo {
        public int left;
        public int top;
        public int height;
        public int width;
        public String backgroundImage;
        public String backgroundColor;
        public String display;
        public String borderStyle;
        public int borderWidth;
        public int borderRadius;
        public String borderColor;

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getBackgroundImage() {
            return backgroundImage;
        }

        public void setBackgroundImage(String backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getBorderStyle() {
            return borderStyle;
        }

        public void setBorderStyle(String borderStyle) {
            this.borderStyle = borderStyle;
        }

        public int getBorderWidth() {
            return borderWidth;
        }

        public void setBorderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
        }

        public int getBorderRadius() {
            return borderRadius;
        }

        public void setBorderRadius(int borderRadius) {
            this.borderRadius = borderRadius;
        }

        public String getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(String borderColor) {
            this.borderColor = borderColor;
        }
    }

    public class pinpadArea {
        public int left;
        public int top;
        public int height;
        public int width;
        public String backgroundImage;
        public String backgroundColor;

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getBackgroundImage() {
            return backgroundImage;
        }

        public void setBackgroundImage(String backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }
    }

    public class echoImageviews {
        public int left;
        public int marginLeft;
        public int top;
        public int height;
        public int width;
        public String backgroundImage;
        public String backgroundColor;

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getMarginLeft() {
            return marginLeft;
        }

        public void setMarginLeft(int marginLeft) {
            this.marginLeft = marginLeft;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getBackgroundImage() {
            return backgroundImage;
        }

        public void setBackgroundImage(String backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }
    }
}



