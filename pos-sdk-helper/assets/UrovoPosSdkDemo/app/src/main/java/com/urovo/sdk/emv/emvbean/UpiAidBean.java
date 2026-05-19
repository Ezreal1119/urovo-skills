package com.urovo.sdk.emv.emvbean;

public class UpiAidBean extends EmvAidCapkBean {

    private String CardType = "UpiCard";
    private String TransactionType = "";
    private String ApplicationIdentifier = "";
    private String TerminalTransactionQualifiers = "";
    private String TransactionLimit = "999999999999";
    private String FloorLimit = "000000000000";
    private String CvmRequiredLimit = "999999999999";
    private String TerminalCountryCode = "";
    private String LimitSwitch = "";
    private String EmvTerminalFloorLimit = "";
    private String AppSelIndicator = "";
    private String TerminalType = "";
    private String TerminalCapabilities = "";
    private String ECTransactionLimit = "";
    private String DefaultTDOL = "";
    private String DefaultDDOL = "";

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

    public String getTerminalTransactionQualifiers() {
        return TerminalTransactionQualifiers;
    }

    public void setTerminalTransactionQualifiers(String terminalTransactionQualifiers) {
        TerminalTransactionQualifiers = terminalTransactionQualifiers;
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

    public String getTerminalCountryCode() {
        return TerminalCountryCode;
    }

    public void setTerminalCountryCode(String terminalCountryCode) {
        TerminalCountryCode = terminalCountryCode;
    }

    public String getLimitSwitch() {
        return LimitSwitch;
    }

    public void setLimitSwitch(String limitSwitch) {
        LimitSwitch = limitSwitch;
    }

    public String getEmvTerminalFloorLimit() {
        return EmvTerminalFloorLimit;
    }

    public void setEmvTerminalFloorLimit(String emvTerminalFloorLimit) {
        EmvTerminalFloorLimit = emvTerminalFloorLimit;
    }

    public String getAppSelIndicator() {
        return AppSelIndicator;
    }

    public void setAppSelIndicator(String appSelIndicator) {
        AppSelIndicator = appSelIndicator;
    }

    public String getTerminalType() {
        return TerminalType;
    }

    public void setTerminalType(String terminalType) {
        TerminalType = terminalType;
    }

    public String getTerminalCapabilities() {
        return TerminalCapabilities;
    }

    public void setTerminalCapabilities(String terminalCapabilities) {
        TerminalCapabilities = terminalCapabilities;
    }

    public String getECTransactionLimit() {
        return ECTransactionLimit;
    }

    public void setECTransactionLimit(String ECTransactionLimit) {
        this.ECTransactionLimit = ECTransactionLimit;
    }

    public String getDefaultTDOL() {
        return DefaultTDOL;
    }

    public void setDefaultTDOL(String defaultTDOL) {
        DefaultTDOL = defaultTDOL;
    }

    public String getDefaultDDOL() {
        return DefaultDDOL;
    }

    public void setDefaultDDOL(String defaultDDOL) {
        DefaultDDOL = defaultDDOL;
    }

}
