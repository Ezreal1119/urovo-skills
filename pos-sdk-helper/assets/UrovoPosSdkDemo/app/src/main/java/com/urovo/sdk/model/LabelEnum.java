package com.urovo.sdk.model;

public enum LabelEnum {
    LABEL_30x30("30x30", new LabelConfig(180, 170, 8, 150)),
    LABEL_40x30("40x30", new LabelConfig(260, 200, 8, 110)),
    LABEL_50x30("50x30", new LabelConfig(350, 210, 8, 30)),
    LABEL_50x40("50x40", new LabelConfig(350, 290, 8, 30));

    private final String label;
    private final LabelConfig labelConfig;

    LabelEnum(String label, LabelConfig labelConfig) {
        this.label = label;
        this.labelConfig = labelConfig;
    }

    public static LabelEnum getConfigByLabel(String label) {
        for (LabelEnum item : values()) {
            if (item.label.equals(label)) {
                return item;
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }

    public LabelConfig getLabelConfig() {
        return labelConfig;
    }
}

