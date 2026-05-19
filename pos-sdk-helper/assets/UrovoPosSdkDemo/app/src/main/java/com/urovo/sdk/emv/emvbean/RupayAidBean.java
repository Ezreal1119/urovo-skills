package com.urovo.sdk.emv.emvbean;

public class RupayAidBean extends EmvAidCapkBean {

    private String CardType = "RupayCard";
    private String TransactionType = "";
    private String TerminalTransactionQualifiers = "";
    private String ApplicationIdentifier = "";
    private String CvmRequiredLimit = "";
    private String FloorLimit = "000000000000";
    private String TransactionLimit = "999999999999";
    private String ApplicationVersion = "";
    private String TerminalActionCodesOnLine = "";
    private String TerminalActionCodesDenial = "";
    private String TerminalActionCodesDefault = "";
    private String ThresholdValue = "";
    private String TargetPercentage = "";
    private String MaxTargetPercentage = "";
    private String TerminalCountryCode = "";
    private String AdditionalTerminalCapabilities = "";
    private String TerminalCapabilities = "";
    private String AdditionalTerminalCapabilitiesExtension = "";
    private String ServiceFormatData = "";
    private String DefaultTDOL = "";

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public String getTerminalTransactionQualifiers() {
        return TerminalTransactionQualifiers;
    }

    public void setTerminalTransactionQualifiers(String terminalTransactionQualifiers) {
        TerminalTransactionQualifiers = terminalTransactionQualifiers;
    }

    public String getApplicationIdentifier() {
        return ApplicationIdentifier;
    }

    public void setApplicationIdentifier(String applicationIdentifier) {
        ApplicationIdentifier = applicationIdentifier;
    }

    public String getCvmRequiredLimit() {
        return CvmRequiredLimit;
    }

    public void setCvmRequiredLimit(String cvmRequiredLimit) {
        CvmRequiredLimit = cvmRequiredLimit;
    }

    public String getFloorLimit() {
        return FloorLimit;
    }

    public void setFloorLimit(String floorLimit) {
        FloorLimit = floorLimit;
    }

    public String getTransactionLimit() {
        return TransactionLimit;
    }

    public void setTransactionLimit(String transactionLimit) {
        TransactionLimit = transactionLimit;
    }

    public String getApplicationVersion() {
        return ApplicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        ApplicationVersion = applicationVersion;
    }

    public String getTerminalActionCodesOnLine() {
        return TerminalActionCodesOnLine;
    }

    public void setTerminalActionCodesOnLine(String terminalActionCodesOnLine) {
        TerminalActionCodesOnLine = terminalActionCodesOnLine;
    }

    public String getTerminalActionCodesDenial() {
        return TerminalActionCodesDenial;
    }

    public void setTerminalActionCodesDenial(String terminalActionCodesDenial) {
        TerminalActionCodesDenial = terminalActionCodesDenial;
    }

    public String getTerminalActionCodesDefault() {
        return TerminalActionCodesDefault;
    }

    public void setTerminalActionCodesDefault(String terminalActionCodesDefault) {
        TerminalActionCodesDefault = terminalActionCodesDefault;
    }

    public String getThresholdValue() {
        return ThresholdValue;
    }

    public void setThresholdValue(String thresholdValue) {
        ThresholdValue = thresholdValue;
    }

    public String getTargetPercentage() {
        return TargetPercentage;
    }

    public void setTargetPercentage(String targetPercentage) {
        TargetPercentage = targetPercentage;
    }

    public String getMaxTargetPercentage() {
        return MaxTargetPercentage;
    }

    public void setMaxTargetPercentage(String maxTargetPercentage) {
        MaxTargetPercentage = maxTargetPercentage;
    }

    public String getTerminalCountryCode() {
        return TerminalCountryCode;
    }

    public void setTerminalCountryCode(String terminalCountryCode) {
        TerminalCountryCode = terminalCountryCode;
    }

    public String getAdditionalTerminalCapabilities() {
        return AdditionalTerminalCapabilities;
    }

    public void setAdditionalTerminalCapabilities(String additionalTerminalCapabilities) {
        AdditionalTerminalCapabilities = additionalTerminalCapabilities;
    }

    public String getTerminalCapabilities() {
        return TerminalCapabilities;
    }

    public void setTerminalCapabilities(String terminalCapabilities) {
        TerminalCapabilities = terminalCapabilities;
    }

    public String getAdditionalTerminalCapabilitiesExtension() {
        return AdditionalTerminalCapabilitiesExtension;
    }

    public void setAdditionalTerminalCapabilitiesExtension(String additionalTerminalCapabilitiesExtension) {
        AdditionalTerminalCapabilitiesExtension = additionalTerminalCapabilitiesExtension;
    }

    public String getServiceFormatData() {
        return ServiceFormatData;
    }

    public void setServiceFormatData(String serviceFormatData) {
        ServiceFormatData = serviceFormatData;
    }

    public String getDefaultTDOL() {
        return DefaultTDOL;
    }

    public void setDefaultTDOL(String defaultTDOL) {
        DefaultTDOL = defaultTDOL;
    }

}
