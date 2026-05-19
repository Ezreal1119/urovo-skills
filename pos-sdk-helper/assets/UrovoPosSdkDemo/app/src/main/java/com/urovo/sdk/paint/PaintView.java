package com.urovo.sdk.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PaintView {

    public static final String TAG = "PaintView===>";

    private static Context mContext;
    private static Paint mPaint;
    private static Canvas cacheCanvas;
    private static Bitmap cachebBitmap;
    private static PaintView mPaintView;
    public static String fontName_default = "simsun";

    public static final int MAX_PAGEWIDTH = 380;
    public static final int DEF_FONT_SIZE_SMALL = 16;
    public static final int DEF_FONT_SIZE = 24;
    public static final int DEF_FONT_SIZE_BIG = 32;

    public static PaintView getInstance(Context context) {
        if (mPaintView == null) {
            mPaintView = new PaintView();
        }
        if (mContext == null) {
            mContext = context;
        }
        return mPaintView;
    }

    public void init(int width, int height) {
        cachebBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);//高清，650, 380
        cachebBitmap.eraseColor(0xffffffff);
        cacheCanvas = new Canvas(cachebBitmap);
        //cacheCanvas.drawColor(Color.parseColor("#ffffff"));
        cacheCanvas.save();
        cacheCanvas.restore();

        mPaint = new Paint();
        mPaint.reset();
        mPaint.setColor(Color.BLACK);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setUnderlineText(false);
        mPaint.setStrikeThruText(false);
    }

    public void close() {
        if (cachebBitmap != null) {
            cachebBitmap.recycle();
            cachebBitmap = null;
        }
        cacheCanvas = null;
        mPaint = null;
        mPaintView = null;
    }

    private Align getAlign(int align) {
        if (align == 1 || align == 4) {
            return Align.CENTER;
        } else if (align == 2) {
            return Align.RIGHT;
        } else {
            return Align.LEFT;
        }
    }

    private Bitmap getBitmap() {
        return cachebBitmap;
    }

    public static PrintContentBean addBlankLine(int height) {
        PrintContentBean contentBean = new PrintContentBean();
        contentBean.setPrintType(PrintContentBean.PrintType_BLANK);
        contentBean.setHeight(height);
        return contentBean;
    }

    public static PrintContentBean addBitmap(Bitmap bitmap, int width, int align) {
        PrintContentBean contentBean = new PrintContentBean();
        contentBean.setPrintType(PrintContentBean.PrintType_Bitmap);
        contentBean.setAlign(align);
        Bitmap scaleBitmap = scaleBitmap(bitmap, width);
        contentBean.setBitmap(scaleBitmap);
        contentBean.setHeight(scaleBitmap.getHeight());
        return contentBean;
    }

    public static PrintContentBean addBitmapWithText(Bitmap bitmap, int width, String text, boolean bold, String fontName, int align, int... font) {
        PrintContentBean contentBean = new PrintContentBean();
        contentBean.setPrintType(PrintContentBean.getPrintType_Text_Bitmap);
        contentBean.setAlign(align);
        //Add Text
        contentBean.setFontName(fontName);
        if (font != null && font.length >= 1) {
            contentBean.setFont(font[0]);
            if (font.length > 1) {
                contentBean.setFontSize(font[1]);
            }
        }
        contentBean.setBold(bold);
        contentBean.setContent(text);

        //Add bitmap
        contentBean.setAlign(align);
        Bitmap scaleBitmap = scaleBitmap(bitmap, width);
        contentBean.setBitmap(scaleBitmap);
        contentBean.setHeight(scaleBitmap.getHeight());

        return contentBean;
    }

    private static Bitmap scaleBitmap(Bitmap sourceBitmap, int scaleWidth) {
        // 获得图片的宽高
        int sourceWidth = sourceBitmap.getWidth();
        int sourceHeight = sourceBitmap.getHeight();
        // 计算缩放比
        if (sourceWidth > MAX_PAGEWIDTH) {
            sourceWidth = MAX_PAGEWIDTH;
        }
        if (scaleWidth > MAX_PAGEWIDTH) {
            scaleWidth = MAX_PAGEWIDTH;
        }
        float scale = ((float) scaleWidth) / sourceWidth;
        int newWidth = Math.round(sourceWidth * scale);
        int newHeight = Math.round(sourceHeight * scale);
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        // 关键：设置高质量的绘制参数
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true); // 重要：启用抖动

        canvas.drawColor(Color.TRANSPARENT); // 清除为透明背景
        canvas.drawBitmap(sourceBitmap, matrix, paint);

        return scaledBitmap;
    }

    public static PrintContentBean addTextContent(String text, boolean bold, String fontName, int align, int... font) {
        PrintContentBean contentBean = new PrintContentBean();
        contentBean.setPrintType(PrintContentBean.PrintType_Text);
        contentBean.setFontName(fontName);
        if (font != null && font.length >= 1) {
            contentBean.setFont(font[0]);
            if (font.length > 1) {
                contentBean.setFontSize(font[1]);
            }
        }
        contentBean.setAlign(align);
        contentBean.setBold(bold);
        contentBean.setContent(text);

        return contentBean;
    }

    public static PrintContentBean addQRCode(String qrCode, int align, int width, int height) {
        PrintContentBean contentBean = new PrintContentBean();
        contentBean.setContent(qrCode);
        contentBean.setPrintType(PrintContentBean.PrintType_QRCode);
        contentBean.setAlign(align);
        contentBean.setWidth(width);
        contentBean.setHeight(height);

        return contentBean;
    }

    public static PrintContentBean addBarcode(String qrCode, int align, int width, int height) {
        PrintContentBean contentBean = new PrintContentBean();
        contentBean.setContent(qrCode);
        contentBean.setPrintType(PrintContentBean.PrintType_BarCode);
        contentBean.setAlign(align);
        contentBean.setWidth(width);
        contentBean.setHeight(height);

        return contentBean;
    }

    public static List<PrintContentBean> addContentLeftRight(String leftText, String rightText, boolean bold, String fontName, int... font) {
        List<PrintContentBean> contentList = new ArrayList<PrintContentBean>();
        PrintContentBean contentBean = new PrintContentBean();
        contentBean.setPrintType(PrintContentBean.PrintType_Text);
        contentBean.setFontName(fontName);
        if (font != null && font.length >= 1) {
            contentBean.setFont(font[0]);
            if (font.length > 1) {
                contentBean.setFontSize(font[1]);
            }
        }
        contentBean.setAlign(PrintFormat.ALIGN_LEFT_RIGHT);
        contentBean.setBold(bold);
        contentBean.setContent(leftText);
        contentList.add(contentBean);

        contentBean = new PrintContentBean();
        contentBean.setPrintType(PrintContentBean.PrintType_Text);
        contentBean.setFontName(fontName);
        if (font != null && font.length >= 1) {
            contentBean.setFont(font[0]);
            if (font.length > 1) {
                contentBean.setFontSize(font[1]);
            }
        }
        contentBean.setAlign(PrintFormat.ALIGN_RIGHT);
        contentBean.setBold(bold);
        contentBean.setContent(rightText);
        contentList.add(contentBean);

        return contentList;
    }

    public static List<PrintContentBean> addContentLeftCenterRight(String leftText, String centerText, String rightText, boolean bold, String fontName, int... font) {
        List<PrintContentBean> contentList = new ArrayList<PrintContentBean>();
        PrintContentBean contentBean = new PrintContentBean();
        contentBean.setFontName(fontName);
        contentBean.setPrintType(PrintContentBean.PrintType_Text);
        if (font != null && font.length >= 1) {
            contentBean.setFont(font[0]);
            if (font.length > 1) {
                contentBean.setFontSize(font[1]);
            }
        }
        contentBean.setAlign(PrintFormat.ALIGN_LEFT_RIGHT_CENTER);
        contentBean.setBold(bold);
        contentBean.setContent(centerText);
        contentList.add(contentBean);

        contentBean = new PrintContentBean();
        contentBean.setFontName(fontName);
        contentBean.setPrintType(PrintContentBean.PrintType_Text);
        if (font != null && font.length >= 1) {
            contentBean.setFont(font[0]);
            if (font.length > 1) {
                contentBean.setFontSize(font[1]);
            }
        }
        contentBean.setAlign(PrintFormat.ALIGN_LEFT_RIGHT);
        contentBean.setBold(bold);
        contentBean.setContent(leftText);
        contentList.add(contentBean);

        contentBean = new PrintContentBean();
        contentBean.setFontName(fontName);
        contentBean.setPrintType(PrintContentBean.PrintType_Text);
        if (font != null && font.length >= 1) {
            contentBean.setFont(font[0]);
            if (font.length > 1) {
                contentBean.setFontSize(font[1]);
            }
        }
        contentBean.setAlign(PrintFormat.ALIGN_RIGHT);
        contentBean.setBold(bold);
        contentBean.setContent(rightText);
        contentList.add(contentBean);

        return contentList;
    }

    private static ArrayMap<String, Typeface> TYPEFACE_CACHE = new ArrayMap();

    private Typeface getTypeFaceCache(boolean bold, String fontName) {
        String name = bold + "_" + fontName;
        if (!TYPEFACE_CACHE.containsKey(name)) {
            try {
                File file = new File(fontName);
                Log.e(TAG, "TypefaceHelper===>TYPEFACE_CACHE not contains " + name);
                Typeface typeface;
                if (file != null && file.exists()) {
                    typeface = Typeface.create(Typeface.createFromFile(file), bold ? Typeface.BOLD : Typeface.NORMAL);
                } else {
                    typeface = TypefaceHelper.getDefault(bold, fontName);
                }
                TYPEFACE_CACHE.put(name, typeface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPEFACE_CACHE.get(name);
    }

    public int getBitmapHeight(List<PrintContentBean> contentBeanList) {
        if (contentBeanList == null || contentBeanList.size() == 0) {
            return 0;
        }
        int paintY = 0;
        PrintContentBean printContentBean = null;
        for (int i = 0; i < contentBeanList.size(); i++) {
            printContentBean = contentBeanList.get(i);
            if (printContentBean == null) {
                continue;
            }
            if (i == 0) {
                if (printContentBean.getPrintType() == PrintContentBean.PrintType_Text) {
                    paintY = (int) getTextHeight_single(printContentBean);
                } else if (printContentBean.getPrintType() == PrintContentBean.PrintType_BLANK) {
                    paintY = printContentBean.getHeight();
                }
            }
            int align = printContentBean.getAlign();
            switch (printContentBean.getPrintType()) {
                case PrintContentBean.PrintType_BarCode:
                case PrintContentBean.PrintType_QRCode:
                case PrintContentBean.PrintType_Bitmap:
                    paintY += printContentBean.getHeight() + 5;
                    break;
                case PrintContentBean.PrintType_BLANK:
                    paintY += printContentBean.getHeight();
                    break;
                case PrintContentBean.getPrintType_Text_Bitmap:
                    paintY += printContentBean.getHeight();
                    break;
                default:
                    if (align == PrintFormat.ALIGN_LEFT_RIGHT || align == PrintFormat.ALIGN_LEFT_RIGHT_CENTER) {
                        paintY += 0;
                    } else {
                        paintY += getTextHeight(printContentBean);
                    }
                    break;
            }
        }

        Log.e(TAG, "getBitmapHeight:paintY=" + paintY);
        return paintY;
    }

    /**
     * 格绘制打印图片
     *
     * @param contentBeanList
     * @param height
     * @return
     */
    public Bitmap drawPrintBitmap(List<PrintContentBean> contentBeanList, int width, int height) {
        if (contentBeanList == null || contentBeanList.size() == 0) {
            return null;
        }
        init(width, height);
        int paintY = 0;
        PrintContentBean printContentBean = null;
        for (int i = 0; i < contentBeanList.size(); i++) {
            printContentBean = contentBeanList.get(i);
            if (printContentBean == null) {
                continue;
            }
            //Canvans.drawText方法的坐标是以文字基准线为准，不是左上角，所以当你的坐标为0，是看不见的，起始坐标y要有一定的深度。
            if (i == 0) {
                if (printContentBean.getPrintType() == PrintContentBean.PrintType_Text) {
                    paintY = (int) getTextHeight_single(printContentBean);
                } else if (printContentBean.getPrintType() == PrintContentBean.PrintType_BLANK) {
                    paintY = printContentBean.getHeight();
                }
            }
            int align = printContentBean.getAlign();
            switch (printContentBean.getPrintType()) {
                case PrintContentBean.PrintType_BarCode:
                    paintY += drawBitmap(printContentBean, getBarCodeBitmap(printContentBean.getContent(), printContentBean.getWidth(), printContentBean.getHeight()),
                            printContentBean.getOffset(), paintY, align) + 5;
                    break;
                case PrintContentBean.PrintType_QRCode:
                    Bitmap qrBitmap = getQRCodeBitmap(printContentBean.getContent(), printContentBean.getWidth(), printContentBean.getHeight());
                    paintY += drawBitmap(printContentBean, qrBitmap, printContentBean.getOffset(), paintY, align) + 5;
                    break;
                case PrintContentBean.PrintType_Bitmap:
                    paintY += drawBitmap(printContentBean, printContentBean.getBitmap(), printContentBean.getOffset(), paintY, align) + 5;
                    break;
                case PrintContentBean.PrintType_BLANK:
                    paintY += printContentBean.getHeight();
                    break;
                case PrintContentBean.getPrintType_Text_Bitmap:
                    paintY += drawBitmapWithText(printContentBean, paintY);
                    break;
                default:
                    if (align == PrintFormat.ALIGN_LEFT_RIGHT || align == PrintFormat.ALIGN_LEFT_RIGHT_CENTER) {
                        drawText(printContentBean, paintY);
                    } else {
                        paintY += drawText(printContentBean, paintY);
                    }
                    break;
            }
        }
        Log.e(TAG, "drawPrintBitmap:paintY=" + paintY);
        Bitmap mBitmap = getBitmap();
        return mBitmap;
    }

    private float drawText(PrintContentBean bean, int y) {
        String text = bean.getContent();
        int font = bean.getFont();
        int align = bean.getAlign();
        boolean isBold = bean.isBold();
        String fontName = bean.getFontName();
        float lineHeight = 0;
        int x = 0;
        if (align == PrintFormat.ALIGN_CENTER || align == PrintFormat.ALIGN_LEFT_RIGHT_CENTER) {
            x = MAX_PAGEWIDTH / 2;
        } else if (align == PrintFormat.ALIGN_RIGHT) {
            x = MAX_PAGEWIDTH;
        }
        int fontSize = DEF_FONT_SIZE;
        if (font == PrintFormat.FONT_SMALL) {
            fontSize = DEF_FONT_SIZE_SMALL;
        } else if (font == PrintFormat.FONT_LARGE) {
            fontSize = DEF_FONT_SIZE_BIG;
        }
        if (bean.getFontSize() > 0) {
            fontSize = bean.getFontSize();
        }
        Typeface typeface = getTypeFaceCache(isBold, fontName);
        mPaint.setTypeface(typeface);
        mPaint.setFakeBoldText(isBold);
        mPaint.setTextSize(fontSize);
        mPaint.setTextAlign(getAlign(align));
        //通过设置Flag来应用抗锯齿效果
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        cacheCanvas.drawText(text, x, y, mPaint);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float fonthight = fm.bottom - fm.top;
        lineHeight = fonthight;
        if (fontSize < DEF_FONT_SIZE) {
            lineHeight += 5;
        } else if (fontSize >= DEF_FONT_SIZE_BIG) {
            lineHeight -= 5;
        }
        return lineHeight;
    }

    /**
     * 获取每一行的高度
     *
     * @param bean
     * @return
     */
    private float getTextHeight(PrintContentBean bean) {
        String text = bean.getContent();
        int font = bean.getFont();
        int align = bean.getAlign();
        boolean isBold = bean.isBold();
        String fontName = bean.getFontName();
        float lineHeight = 0;
        int fontSize = DEF_FONT_SIZE;
        if (font == PrintFormat.FONT_SMALL) {
            fontSize = DEF_FONT_SIZE_SMALL;
        } else if (font == PrintFormat.FONT_LARGE) {
            fontSize = DEF_FONT_SIZE_BIG;
        }
        if (bean.getFontSize() > 0) {
            fontSize = bean.getFontSize();
        }
        Typeface typeface = getTypeFaceCache(isBold, fontName);
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setFakeBoldText(isBold);
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        lineHeight = fm.bottom - fm.top;
        if (font == PrintFormat.FONT_LARGE) {
            //    lineHeight+=5;
        }
        if (align == PrintFormat.ALIGN_LEFT_RIGHT || align == PrintFormat.ALIGN_LEFT_RIGHT_CENTER) {
            return 0;
        }
        if (fontSize < DEF_FONT_SIZE) {
            lineHeight += 5;
        } else if (fontSize >= DEF_FONT_SIZE_BIG) {
            lineHeight -= 5;
        }
        return lineHeight;
    }

    /**
     * 获取每一行的高度:只用于drawPrintBitmap
     *
     * @param bean
     * @return
     */
    private float getTextHeight_single(PrintContentBean bean) {
        int font = bean.getFont();
        int align = bean.getAlign();
        boolean isBold = bean.isBold();
        String fontName = bean.getFontName();
        float lineHeight = 0;
        int fontSize = DEF_FONT_SIZE;
        if (font == PrintFormat.FONT_SMALL) {
            fontSize = DEF_FONT_SIZE_SMALL;
        } else if (font == PrintFormat.FONT_LARGE) {
            fontSize = DEF_FONT_SIZE_BIG;
        }
        if (bean.getFontSize() > 0) {
            fontSize = bean.getFontSize();
        }
        Typeface typeface = getTypeFaceCache(isBold, fontName);

        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setFakeBoldText(isBold);
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        lineHeight = fm.bottom - fm.top;
        return lineHeight;
    }

    private float drawBitmap(PrintContentBean bean, Bitmap bitmap, int offset, int y, int align) {
        Log.e(TAG, "drawBitmap:" + bitmap);
        if (bitmap == null) {
            Log.e(TAG, "drawBitmap bitmap is NULL");
            return 0;
        }
        if (offset <= 0) {
            offset = 0;
            if (align == PrintFormat.ALIGN_LEFT) {
                offset = 0;
            } else if (align == PrintFormat.ALIGN_CENTER) {
                offset = (MAX_PAGEWIDTH - bitmap.getWidth()) / 2;
            } else if (align == PrintFormat.ALIGN_RIGHT) {
                offset = MAX_PAGEWIDTH - bitmap.getWidth();
            }
        }
        cacheCanvas.drawBitmap(bitmap, offset, y, mPaint);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bean.getHeight();
    }

    private float drawBitmapWithText(PrintContentBean bean, int y) {
        String text = bean.getContent();
        Bitmap bitmap = bean.getBitmap();
        int font = bean.getFont();
        int align = bean.getAlign();
        boolean isBold = bean.isBold();
        String fontName = bean.getFontName();
        float lineHeight = 0;
        int x = 0;
        if (align == PrintFormat.ALIGN_CENTER || align == PrintFormat.ALIGN_LEFT_RIGHT_CENTER) {
            x = MAX_PAGEWIDTH / 2;
        } else if (align == PrintFormat.ALIGN_RIGHT) {
            x = MAX_PAGEWIDTH;
        }
        int fontSize = DEF_FONT_SIZE;
        if (font == PrintFormat.FONT_SMALL) {
            fontSize = DEF_FONT_SIZE_SMALL;
        } else if (font == PrintFormat.FONT_LARGE) {
            fontSize = DEF_FONT_SIZE_BIG;
        }
        if (bean.getFontSize() > 0) {
            fontSize = bean.getFontSize();
        }

        //add text
        Typeface typeface = getTypeFaceCache(isBold, fontName);
        mPaint.setTypeface(typeface);
        mPaint.setTextAlign(getAlign(align));
        //通过设置Flag来应用抗锯齿效果
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFakeBoldText(isBold);
        mPaint.setTextSize(fontSize);
        cacheCanvas.drawText(text, x, y, mPaint);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float fonthight = fm.bottom - fm.top;
        lineHeight = fonthight;
        if (fontSize < DEF_FONT_SIZE) {
            lineHeight += 5;
        } else if (fontSize >= DEF_FONT_SIZE_BIG) {
            lineHeight -= 5;
        }

        //add bitmap
        float textWidth = mPaint.measureText(text);
        cacheCanvas.drawBitmap(bitmap, MAX_PAGEWIDTH - textWidth - bitmap.getWidth(), y - fonthight + (bitmap.getHeight() - fonthight) / 2, mPaint);

        return lineHeight;
    }

    public Bitmap getQRCodeBitmap(String qrCode, int width, int height) {
        if (width > MAX_PAGEWIDTH) {
            width = MAX_PAGEWIDTH;
        }
        try {
            if (qrCode != null && qrCode.length() != 0) {
                Bitmap qrCodeBmp = EncodingHandler.createQRImage(qrCode, width, height);
                return qrCodeBmp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getBarCodeBitmap(String barCode, int width, int height) {
        if (height > MAX_PAGEWIDTH) {
            height = MAX_PAGEWIDTH;
        }
        try {
            if (barCode != null && barCode.length() != 0) {
                Bitmap bitmap = EncodingHandler.creatBarcode(barCode, 1, height,
                        false, 1, BarcodeFormat.CODE_128);
                // 获得图片的宽高
                int sourceWidth = bitmap.getWidth();
                int sourceHeight = bitmap.getHeight();
                if (height > 0 && width > 0) {
                    // 计算缩放比
                    float scaleWidth = ((float) width) / sourceWidth;
                    float scaleHeight = ((float) height) / sourceHeight;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
