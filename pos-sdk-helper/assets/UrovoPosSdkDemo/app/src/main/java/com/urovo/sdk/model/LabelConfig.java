package com.urovo.sdk.model;

public class LabelConfig {

    private int width;
    private int height;
    private int topMargin;
    private int leftOffset;

    // 构造方法
    public LabelConfig(int width, int height, int topMargin, int leftOffset) {
        this.width = width;
        this.height = height;
        this.topMargin = topMargin;
        this.leftOffset = leftOffset;
    }

    // Getter 方法
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public int getLeftOffset() {
        return leftOffset;
    }

    @Override
    public String toString() {
        return "Width: " + width + ", Height: " + height + ", Top Margin: " + topMargin + ", Left Offset: " + leftOffset;
    }
}
