package com.urovo.sdk.emv.emvbean;

public class PureAidBean extends EmvAidCapkBean {

    private String CardType = "PureCard";
    private String TransactionType = "";
    private String ApplicationIdentifier = "";
    private String TransactionLimit = "999999999999";
    private String FloorLimit = "000000000000";
    private String CvmRequiredLimit = "999999999999";
    private String EmvTerminalFloorLimit = "";
    private String ApplicationVersion = "";
    private String TerminalCountryCode = "";
    private String TerminalCapabilities = "";
    private String TerminalActionCodesOnLine = "";
    private String TerminalActionCodesDenial = "";
    private String TerminalActionCodesDefault = "";
    private String ThresholdValue = "";
    private String TargetPercentage = "";
    private String MaxTargetPercentage = "";
    private String ContactlessApplicationCapabilities = "";
    private String DefaultDDOL = "";
    private String ContactlessImplementationOptions = "";
    private String LimitSwitch = "";
    private String ATOL = "";
    private String ATDTOL = "";
    private String MTOL = "";
    private String TransactionTypeValueforAAT = "";

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public String getApplicationIdentifier() {
        return ApplicationIdentifier;
    }

    public void setApplicationIdentifier(String applicationIdentifier) {
        ApplicationIdentifier = applicationIdentifier;
    }

    public String getTransactionLimit() {
        return TransactionLimit;
    }

    public void setTransactionLimit(String transactionLimit) {
        TransactionLimit = transactionLimit;
    }

    public String getFloorLimit() {
        return FloorLimit;
    }

    public void setFloorLimit(String floorLimit) {
        FloorLimit = floorLimit;
    }

    public String getCvmRequiredLimit() {
        return CvmRequiredLimit;
    }

    public void setCvmRequiredLimit(String cvmRequiredLimit) {
        CvmRequiredLimit = cvmRequiredLimit;
    }

    public String getEmvTerminalFloorLimit() {
        return EmvTerminalFloorLimit;
    }

    public void setEmvTerminalFloorLimit(String emvTerminalFloorLimit) {
        EmvTerminalFloorLimit = emvTerminalFloorLimit;
    }

    public String getApplicationVersion() {
        return ApplicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        ApplicationVersion = applicationVersion;
    }

    public String getTerminalCountryCode() {
        return TerminalCountryCode;
    }

    public void setTerminalCountryCode(String terminalCountryCode) {
        TerminalCountryCode = terminalCountryCode;
    }

    public String getTerminalCapabilities() {
        return TerminalCapabilities;
    }

    public void setTerminalCapabilities(String terminalCapabilities) {
        TerminalCapabilities = terminalCapabilities;
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

    public String getContactlessApplicationCapabilities() {
        return ContactlessApplicationCapabilities;
    }

    public void setContactlessApplicationCapabilities(String contactlessApplicationCapabilities) {
        ContactlessApplicationCapabilities = contactlessApplicationCapabilities;
    }

    public String getDefaultDDOL() {
        return DefaultDDOL;
    }

    public void setDefaultDDOL(String defaultDDOL) {
        DefaultDDOL = defaultDDOL;
    }

    public String getContactlessImplementationOptions() {
        return ContactlessImplementationOptions;
    }

    public void setContactlessImplementationOptions(String contactlessImplementationOptions) {
        ContactlessImplementationOptions = contactlessImplementationOptions;
    }

    public String getLimitSwitch() {
        return LimitSwitch;
    }

    public void setLimitSwitch(String limitSwitch) {
        LimitSwitch = limitSwitch;
    }

    public String getATOL() {
        return ATOL;
    }

    public void setATOL(String ATOL) {
        this.ATOL = ATOL;
    }

    public String getATDTOL() {
        return ATDTOL;
    }

    public void setATDTOL(String ATDTOL) {
        this.ATDTOL = ATDTOL;
    }

    public String getMTOL() {
        return MTOL;
    }

    public void setMTOL(String MTOL) {
        this.MTOL = MTOL;
    }

    public String getTransactionTypeValueforAAT() {
        return TransactionTypeValueforAAT;
    }

    public void setTransactionTypeValueforAAT(String transactionTypeValueforAAT) {
        TransactionTypeValueforAAT = transactionTypeValueforAAT;
    }

}
