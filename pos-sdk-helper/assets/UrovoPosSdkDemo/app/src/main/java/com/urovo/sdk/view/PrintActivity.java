package com.urovo.sdk.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.urovo.sdk.R;
import com.urovo.sdk.model.LabelConfig;
import com.urovo.sdk.model.LabelEnum;
import com.urovo.sdk.paint.PaintView;
import com.urovo.sdk.paint.PrintContentBean;
import com.urovo.sdk.paint.SignFiles;
import com.urovo.sdk.print.PrintFormat;
import com.urovo.sdk.print.PrinterLabelState;
import com.urovo.sdk.print.PrinterProviderImpl;
import com.urovo.sdk.utils.FileUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PrintActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private PrinterProviderImpl mPrintManager = null;
    private EditText editText_ttf;
    private EditText editText_gray;
    private EditText editText_speed;
    private EditText editText_count;
    private Spinner spinner_label;
    private String labelSelected = "30x30";
    boolean isPrinting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        initView();
        editText_ttf = (EditText) findViewById(R.id.editText_ttf);
        editText_gray = (EditText) findViewById(R.id.editText_gray);
        editText_speed = (EditText) findViewById(R.id.editText_speed);
        editText_count = (EditText) findViewById(R.id.editText_count);
        spinner_label = findViewById(R.id.spinner_label);
        setSpinner();

        mPrintManager = PrinterProviderImpl.getInstance(PrintActivity.this);
    }

    private void setSpinner() {
        spinner_label.setOnItemSelectedListener(this);
        // 选项列表
        String[] options = {"30x30", "40x30", "50x30", "50x40"};
        // 适配器并绑定数据
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_label.setAdapter(adapter);
        spinner_label.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // 获取选中的项
        labelSelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {//判断是否选择和Code判断
            try {
                Uri uri = data.getData();//拿到路径
                String filePath = FileUtil.getPath(PrintActivity.this, uri);
                Log.e(TAG, "file Path:" + filePath);
                editText_ttf.setText("" + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Exception:" + e.getMessage());
                outputColorText(TextColor.RED, "Failed to get file:" + e.getMessage());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_path:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_getStatus:
                try {
                    outputColorText(TextColor.BLUE, "getStatus");
                    mPrintManager.initPrint();
                    int status = mPrintManager.getStatus();
                    outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "print status:" + status);
                    mPrintManager.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_feedLine:
                new Thread() {

                    @Override
                    public void run() {
                        super.run();
                        try {
                            outputColorText(TextColor.BLUE, "feed line");
                            String countStr = editText_count.getText().toString().trim();
                            if (TextUtils.isEmpty(countStr) || TextUtils.equals(countStr, "0")) {
                                countStr = "1";
                            }
                            int count = Integer.parseInt(countStr);

                            mPrintManager.initPrint();
                            mPrintManager.feedLine(count);

                            int status = mPrintManager.startPrint();
                            outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "print result:" + status);
                            mPrintManager.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case R.id.btn_paperFeed:
                new Thread() {

                    @Override
                    public void run() {
                        super.run();
                        try {
                            outputColorText(TextColor.BLUE, "paper feed");
                            String countStr = editText_count.getText().toString().trim();
                            if (TextUtils.isEmpty(countStr) || TextUtils.equals(countStr, "0")) {
                                countStr = "100";
                            }
                            int count = Integer.parseInt(countStr);
                            mPrintManager.initPrint();
                            mPrintManager.paperFeed(count);
                            int status = mPrintManager.startPrint();
                            outputColorText(TextColor.BLUE, "print result:" + status);
                            mPrintManager.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case R.id.btn_startPrint:
                startPrint();
                break;
            case R.id.btn_startPrint_Bitmap:
                startPrint_Bitmap(false);
                break;
            case R.id.btn_startPrint_Html:
                startPrint_Html();
                break;
            case R.id.btn_startPrint_Label:
                startPrint_Label();
                break;
        }
    }

    public void startPrint() {
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    outputText("isPrinting:" + isPrinting);
                    if (isPrinting) {
                        return;
                    }
                    isPrinting = true;
                    mPrintManager.initPrint();
                    int status = mPrintManager.getStatus();
                    outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "getCurrentStatus before print:" + status);
                    if (status != 0) {
                        isPrinting = false;
                        return;
                    }

                    String grayStr = editText_gray.getText().toString().trim();
                    if (TextUtils.isEmpty(grayStr)) {
                        grayStr = "0";
                    }
                    int gray = Integer.parseInt(grayStr);
                    mPrintManager.setGray(gray);

                    String speedStr = editText_speed.getText().toString().trim();
                    if (!TextUtils.isEmpty(speedStr)) {
                        int speed = Integer.parseInt(speedStr);
                        mPrintManager.setSpeed(speed);
                    }

                    String fontPath = "";
                    String fontPath1 = "";
                    if (!TextUtils.isEmpty(editText_ttf.getText().toString().trim())) {
                        fontPath = editText_ttf.getText().toString().trim();
                        fontPath1 = editText_ttf.getText().toString().trim();
                    }

                    Bundle format = new Bundle();

                    //===========================
                    String countStr = editText_count.getText().toString().trim();
                    if (TextUtils.isEmpty(countStr) || TextUtils.equals(countStr, "0")) {
                        countStr = "1";
                    }
                    int count = Integer.parseInt(countStr);
                    for (int i = 0; i < count; i++) {
//                                Bitmap bitmap = getLogoBitmap(PrintActivity.this, R.drawable.icon_rf);
//                                byte[] imageData = getBitmapBytes(bitmap);
//                                format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_CENTER);
//                                format.putInt(PrintFormat.OFFSET, 190);
//                                format.putInt(PrintFormat.WIDTH, 100);
//                                format.putInt(PrintFormat.HEIGHT, 100);
//                                format.putString("text", "ACHAT");
//                                format.putInt(PrintFormat.Y_ALIGN, PrintFormat.Y_ALIGN_BOTTOM);
//                                format.putInt(PrintFormat.FONT, PrintFormat.FONT_LARGE);
//                                format.putBoolean(PrintFormat.FONTBOLD, true);
//                                format.putString(PrintFormat.FONTNAME, fontPath);
//                                mPrintManager.addImageWithText(format, imageData);

                        Bitmap bitmap = getLogoBitmap(PrintActivity.this, R.drawable.visa);
                        byte[] imageData = getBitmapBytes(bitmap);
                        format = new Bundle();
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_CENTER);
                        format.putInt(PrintFormat.OFFSET, 0);
                        format.putInt(PrintFormat.WIDTH, 196);
                        format.putInt(PrintFormat.HEIGHT, 58);
                        mPrintManager.addImage(format, imageData);

                        mPrintManager.addBlankLine(10);
                        mPrintManager.addBlackLine(10);

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_CENTER);
                        format.putBoolean(PrintFormat.FONTBOLD, true);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        format.putInt(PrintFormat.LINEHEIGHT, 10);
                        mPrintManager.addText(format, "CENTER");
                        mPrintManager.addText(format, "CENTERCENTERCENTERCENTERCENTERCENTERCENTERCENTER");

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_CENTER);
                        format.putBoolean(PrintFormat.FONTBOLD, true);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        format.putInt(PrintFormat.LINEHEIGHT, 10);
                        mPrintManager.addText(format, "居中");
                        mPrintManager.addText(format, "居中居中居中居中居中居中居中居中居中居中");

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_LEFT);
                        format.putString(PrintFormat.FONTNAME, fontPath1);
                        format.putInt(PrintFormat.LINEHEIGHT, 10);
                        mPrintManager.addText(format, "LEFT");
                        mPrintManager.addText(format, "LEFTLEFTLEFTLEFTLEFTLEFTLEFTLEFTLEFTLEFTLEFTLEFT");
                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_LEFT);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        format.putInt(PrintFormat.LINEHEIGHT, 10);
                        mPrintManager.addText(format, "左对齐左对齐");
                        mPrintManager.addText(format, "左对齐左对齐左对齐左对齐左对齐左对齐左对齐左对齐");

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_RIGHT);
                        format.putString(PrintFormat.FONTNAME, fontPath1);
                        format.putInt(PrintFormat.LINEHEIGHT, 10);
                        mPrintManager.addText(format, "RIGHT");
                        mPrintManager.addText(format, "RIGHTRIGHTRIGHTRIGHTRIGHTRIGHTRIGHTRIGHTRIGHTRIGHT");

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_RIGHT);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        format.putInt(PrintFormat.LINEHEIGHT, 10);
                        mPrintManager.addText(format, "右对齐右对齐");
                        mPrintManager.addText(format, "右对齐右对齐右对齐右对齐右对齐右对齐右对齐右对齐");

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putBoolean(PrintFormat.FONTBOLD, false);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        mPrintManager.addTextLeft_Right(format, "LEFTLEFT", "RIGHT");
                        mPrintManager.addTextLeft_Right(format, "LEFTLEFTLEFTLEFTLEFTLEFTLEFTLEFT", "RIGHTRIGHTRIGHTRIGHTRIGHT");

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_RIGHT);
                        format.putInt(PrintFormat.LINEHEIGHT, 20);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        mPrintManager.addTextLeft_Right(format, "健力宝", "15元");

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_RIGHT);
                        format.putInt(PrintFormat.LINEHEIGHT, 10);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        mPrintManager.addTextLeft_Right(format, "健力宝", "15元");

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        mPrintManager.addTextLeft_Center_Right(format, "LEFT", "CENTER", "RIGHT");
                        mPrintManager.addTextLeft_Center_Right(format, "LEFTLEFTLEFT", "CENTERCENTERCENTER", "RIGHTRIGHTRIGHT");

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_RIGHT);
                        format.putInt(PrintFormat.LINEHEIGHT, 20);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        mPrintManager.addTextLeft_Center_Right(format, "健力宝", "16", "15元");

                        format = new Bundle();
                        format.putInt(PrintFormat.FONT, PrintFormat.FONT_NORMAL);
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_RIGHT);
                        format.putInt(PrintFormat.LINEHEIGHT, 0);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        mPrintManager.addTextLeft_Center_Right(format, "健力宝", "16", "15元");
                        mPrintManager.feedLine(1);

                        format = new Bundle();
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_LEFT);
                        format.putInt(PrintFormat.WIDTH, 300);
                        format.putInt(PrintFormat.HEIGHT, 100);
                        format.putString(PrintFormat.FONTNAME, fontPath);
                        mPrintManager.addBarCode(format, "1111111111111111");
                        mPrintManager.feedLine(3);

                        format = new Bundle();
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_CENTER);
                        format.putInt(PrintFormat.WIDTH, 300);
                        format.putInt(PrintFormat.HEIGHT, 100);
                        format.putSerializable(PrintFormat.BARCODE_TYPE, BarcodeFormat.CODE_39);
                        mPrintManager.addBarCode(format, "33333333333333");
                        mPrintManager.feedLine(3);

                        format = new Bundle();
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_CENTER);
                        format.putInt(PrintFormat.OFFSET, 20);
                        format.putInt(PrintFormat.EXHEIGHT, 50);
                        mPrintManager.addQrCode(format, "222222222222222222222");
                        mPrintManager.feedLine(3);

                        format = new Bundle();
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_RIGHT);
                        format.putInt(PrintFormat.OFFSET, 20);
                        format.putInt(PrintFormat.EXHEIGHT, 100);
                        mPrintManager.addQrCode(format, "222222222222222222222");
                        mPrintManager.feedLine(3);

                        format = new Bundle();
                        format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_LEFT);
                        format.putInt(PrintFormat.OFFSET, -1);
                        format.putInt(PrintFormat.EXHEIGHT, 250);
                        mPrintManager.addQrCode(format, "222222222222222222222");
                        mPrintManager.feedLine(3);

                        mPrintManager.feedLine(-1);

                        int iRet = mPrintManager.startPrint();
                        outputColorText(iRet == 0 ? TextColor.BLUE : TextColor.RED, "print result:" + iRet);
                        if (iRet != 0) {
                            break;
                        }
                    }
                    //===========================

                    mPrintManager.close();
                    isPrinting = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    isPrinting = false;
                }
            }
        }.start();
    }

    public void startPrint_Bitmap(final boolean longImage) {
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    isPrinting = true;
                    mPrintManager.initPrint();
                    int status = mPrintManager.getStatus();
                    outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "getCurrentStatus before print:" + status);
                    if (status != 0) {
                        isPrinting = false;
                        return;
                    }

                    String grayStr = editText_gray.getText().toString().trim();
                    if (TextUtils.isEmpty(grayStr)) {
                        grayStr = "6";
                    }
                    int gray = Integer.parseInt(grayStr);
                    mPrintManager.setGray(gray);

                    List<PrintContentBean> list = new ArrayList<>();
                    String fontPath = Environment.getExternalStorageDirectory() + "/Cousine-Bold.ttf";
                    String fontPath1 = Environment.getExternalStorageDirectory() + "/Cousine-Bold.ttf";

                    //====================================
                    String countStr = editText_count.getText().toString().trim();
                    if (TextUtils.isEmpty(countStr) || TextUtils.equals(countStr, "0")) {
                        countStr = "1";
                    }
                    int count = Integer.parseInt(countStr);
                    for (int i = 0; i < count; i++) {
                        Log.e(TAG, "PaintView start:" + i);
                        Bitmap bitmap = getLogoBitmap(PrintActivity.this, R.drawable.visa);
                        list.add(PaintView.addBitmap(bitmap, 300, PrintFormat.ALIGN_CENTER));
                        list.add(PaintView.addBlankLine(20));

                        list.add(PaintView.addTextContent("CENTERCENTERCENTER", true, fontPath, PrintFormat.ALIGN_CENTER, PrintFormat.FONT_NORMAL));
                        list.add(PaintView.addTextContent("LEFTLEFTLEFTLEFT", true, fontPath, PrintFormat.ALIGN_LEFT, PrintFormat.FONT_NORMAL));
                        list.add(PaintView.addTextContent("RIGHTRIGHTRIGHT", true, fontPath, PrintFormat.ALIGN_RIGHT, PrintFormat.FONT_NORMAL));
                        list.addAll(PaintView.addContentLeftRight("LEFTLEFTLEFT", "RIGHTRIGHT", false, fontPath, PrintFormat.FONT_NORMAL));
                        list.addAll(PaintView.addContentLeftCenterRight("LEFT", "CENTER", "RIGHT", false, fontPath, PrintFormat.FONT_NORMAL));
                        list.add(PaintView.addBarcode("111111111111111111", PrintFormat.ALIGN_CENTER, 300, 100));
                        list.add(PaintView.addBlankLine(10));
                        list.add(PaintView.addQRCode("111111111111111111", PrintFormat.ALIGN_CENTER, 250, 250));

                        if (longImage) {
                            PaintView paintView = PaintView.getInstance(PrintActivity.this);
                            int heightTotal = paintView.getBitmapHeight(list);
                            Bitmap paintViewBitmap = paintView.drawPrintBitmap(list, PaintView.MAX_PAGEWIDTH, heightTotal);
                            byte[] imageData = getBitmapBytes(paintViewBitmap);
                            Bundle format = new Bundle();
                            format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_CENTER);
                            format.putInt(PrintFormat.OFFSET, 0);
                            format.putInt(PrintFormat.WIDTH, paintViewBitmap.getWidth());
                            format.putInt(PrintFormat.HEIGHT, paintViewBitmap.getHeight());
                            mPrintManager.addImage(format, imageData);

                            list.clear();
                            paintView.close();
                        } else {
                            PaintView paintView = PaintView.getInstance(PrintActivity.this);
                            int heightTotal = paintView.getBitmapHeight(list);
                            Bitmap paintViewBitmap = paintView.drawPrintBitmap(list, PaintView.MAX_PAGEWIDTH, heightTotal);
                            Log.e(TAG, "PaintView end:" + i);
                            byte[] imageData = getBitmapBytes(paintViewBitmap);
                            Bundle format = new Bundle();
                            format.putInt(PrintFormat.WIDTH, paintViewBitmap.getWidth());
                            format.putInt(PrintFormat.HEIGHT, paintViewBitmap.getHeight());
                            mPrintManager.addImage(format, imageData);

                            status = mPrintManager.startPrint();
                            outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "print result:" + status);
                            list.clear();
                            paintView.close();
                        }
                    }
                    if (longImage) {
                        mPrintManager.feedLine(-1);
                        status = mPrintManager.startPrint();
                        outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "print result:" + status);
                        mPrintManager.close();
                    }
                    isPrinting = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void startPrint_Html() {
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    outputColorText(TextColor.BLUE, "feed line");

                    isPrinting = true;
                    mPrintManager.initPrint();
                    int status = mPrintManager.getStatus();
                    outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "getCurrentStatus before print:" + status);
                    if (status != 0) {
                        isPrinting = false;
                        return;
                    }

                    String grayStr = editText_gray.getText().toString().trim();
                    if (TextUtils.isEmpty(grayStr)) {
                        grayStr = "6";
                    }
                    int gray = Integer.parseInt(grayStr);
                    mPrintManager.setGray(gray);

                    String content = htmlConvertToString(PrintActivity.this, "html_text_document.txt");
                    if (TextUtils.isEmpty(content)) {
                        outputColorText(TextColor.RED, "HTML content is empty");
                        return;
                    }

                    Bundle format = new Bundle();
                    format.putInt(PrintFormat.ALIGN, PrintFormat.ALIGN_CENTER);
                    format.putInt(PrintFormat.OFFSET, 0);
                    format.putInt(PrintFormat.WIDTH, 380);
                    mPrintManager.addHtml(format, content);

                    status = mPrintManager.startPrint();
                    outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "print result:" + status);
                    mPrintManager.close();
                    isPrinting = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void startPrint_Label() {
        if (TextUtils.isEmpty(labelSelected) || LabelEnum.getConfigByLabel(labelSelected) == null) {
            showMessage("标签规格未选中或不存在");
            return;
        }
        final LabelEnum enumData = LabelEnum.getConfigByLabel(labelSelected);
        final LabelConfig config = enumData.getLabelConfig();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    outputColorText(TextColor.BLUE, "startPrint_Label");

                    isPrinting = true;
                    mPrintManager.initPrint();
                    int status = mPrintManager.getStatus();
                    outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "getCurrentStatus before print:" + status);
                    if (status != 0) {
                        isPrinting = false;
                        return;
                    }

                    String grayStr = editText_gray.getText().toString().trim();
                    if (TextUtils.isEmpty(grayStr)) {
                        grayStr = "6";
                    }
                    int gray = Integer.parseInt(grayStr);
                    mPrintManager.setGray(gray);

                    boolean support = mPrintManager.supportLabelPrint();
                    if (!support) {
                        outputColorText(TextColor.RED, "supportLabelPrint:" + support);
                        return;
                    }

                    support = mPrintManager.setPrinterMode(true);
                    if (!support) {
                        outputColorText(TextColor.RED, "setPrinterMode:" + support);
                        return;
                    }

                    String countStr = editText_count.getText().toString().trim();
                    if (TextUtils.isEmpty(countStr) || TextUtils.equals(countStr, "0")) {
                        countStr = "1";
                    }

                    List<PrintContentBean> list = new ArrayList<>();
                    int count = Integer.parseInt(countStr);
                    for (int i = 0; i < count; i++) {
                        list.add(PaintView.addBlankLine(20));
                        if (enumData == LabelEnum.LABEL_40x30) {
                            list.add(PaintView.addBarcode("11111111111111111111", PrintFormat.ALIGN_CENTER, 180, 120));
                        }
                        if (enumData == LabelEnum.LABEL_50x40
                                || enumData == LabelEnum.LABEL_50x30) {
                            list.add(PaintView.addTextContent("11111111111111111111", true, "", PrintFormat.ALIGN_CENTER, PrintFormat.FONT_NORMAL));
                            list.add(PaintView.addTextContent("22222222222222222222", true, "", PrintFormat.ALIGN_CENTER, PrintFormat.FONT_NORMAL));
                        }
                        if (enumData == LabelEnum.LABEL_50x40
                                || enumData == LabelEnum.LABEL_50x30
                                || enumData == LabelEnum.LABEL_30x30) {
                            list.add(PaintView.addQRCode("11111111111111111111", PrintFormat.ALIGN_CENTER, 80, 80));
                        }
                        if (enumData == LabelEnum.LABEL_50x40) {
                            list.add(PaintView.addTextContent("           ", true, "", PrintFormat.ALIGN_CENTER, PrintFormat.FONT_NORMAL));
                            list.add(PaintView.addTextContent("△△△△△△△△△△", true, "", PrintFormat.ALIGN_CENTER, PrintFormat.FONT_NORMAL));
                            list.add(PaintView.addTextContent("□□□□□□□□□□", true, "", PrintFormat.ALIGN_CENTER, PrintFormat.FONT_NORMAL));
                        }

                        // 需要在添加打印内容之前先设置
                        status = mPrintManager.setLabelFeed(PrinterLabelState.PRN_LABEL_LOCATION);

                        mPrintManager.addBlankLine(config.getTopMargin());
                        PaintView paintView = PaintView.getInstance(PrintActivity.this);
                        //paintView.drawRect(0, 0, config.getWidth(), config.getHeight());
                        Bitmap paintViewBitmap = paintView.drawPrintBitmap(list, config.getWidth(), config.getHeight());
                        mPrintManager.addBitmap(paintViewBitmap, config.getLeftOffset());
                        list.clear();
                        paintView.close();

                        mPrintManager.feedLine(-1);
                        outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "setLabelFeed: " + status);
                        status = mPrintManager.startPrint();
                        outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "print result: " + status);
                        status = mPrintManager.setLabelFeed(PrinterLabelState.PRN_LABEL_END);
                        outputColorText(status == 0 ? TextColor.BLUE : TextColor.RED, "PRN_LABEL_END: " + status);
                    }

                    mPrintManager.close();
                    isPrinting = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Bitmap getLogoBitmap(Context context, int id) {
        Drawable drawable = context.getResources().getDrawable(id);
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }

        // 转换 VectorDrawable 或其他类型
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public byte[] getBitmapBytes(Bitmap bitmap) {
        byte[] imageData = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageData = baos.toByteArray();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
        return imageData;
    }

    String base64Image = "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";

    public Bitmap getBitmapFromBase64String(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap originalBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        Bitmap newBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), originalBitmap.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        originalBitmap.recycle();
        String path = SignFiles.createSignFile(newBitmap);
        return newBitmap;
    }

    public Bitmap convertTransparentToWhite(Bitmap originalBitmap) {
        Bitmap newBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), originalBitmap.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        originalBitmap.recycle();
        String path = SignFiles.createSignFile(newBitmap);
        return newBitmap;
    }

    public static String htmlConvertToString(Context context, String fileName) {
        InputStream open = null;
        try {
            open = context.getResources().getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(open));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            bufferedReader.close();
            open.close();
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
