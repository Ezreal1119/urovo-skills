package com.urovo.sdk.emv.emvbean;

public class AmexAidBean extends EmvAidCapkBean {

    private String CardType = "AmexCard";
    private String TransactionType = "";
    private String ApplicationIdentifier = "";
    private String TerminalTransactionQualifiers = "";
    private String ContactlessReaderCapabilities = "";
    private String TransactionLimit = "999999999999";
    private String FloorLimit = "000000000000";
    private String CvmRequiredLimit = "999999999999";
    private String LimitSwitch = "";
    private String TerminalCountryCode = "";
    private String EmvTerminalFloorLimit = "";
    private String ApplicationVersion = "";
    private String TerminalActionCodesOnLine = "";
    private String TerminalActionCodesDenial = "";
    private String TerminalActionCodesDefault = "";
    private String AmexRandom = "";
    private String AdditionalTerminalCapabilities = "";
    private String TerminalCapabilities = "";
    private String AppSelIndicator = "";
    private String TerminalType = "";
    private String DefaultTDOL = "";
    private String DefaultDDOL = "";
    private String DefaultDRL = "";
    private String DRLSet = "";

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

    public String getContactlessReaderCapabilities() {
        return ContactlessReaderCapabilities;
    }

    public void setContactlessReaderCapabilities(String contactlessReaderCapabilities) {
        ContactlessReaderCapabilities = contactlessReaderCapabilities;
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

    public String getLimitSwitch() {
        return LimitSwitch;
    }

    public void setLimitSwitch(String limitSwitch) {
        LimitSwitch = limitSwitch;
    }

    public String getTerminalCountryCode() {
        return TerminalCountryCode;
    }

    public void setTerminalCountryCode(String terminalCountryCode) {
        TerminalCountryCode = terminalCountryCode;
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

    public String getAmexRandom() {
        return AmexRandom;
    }

    public void setAmexRandom(String amexRandom) {
        AmexRandom = amexRandom;
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

    public String getDefaultDRL() {
        return DefaultDRL;
    }

    public void setDefaultDRL(String defaultDRL) {
        DefaultDRL = defaultDRL;
    }

    public String getDRLSet() {
        return DRLSet;
    }

    public void setDRLSet(String DRLSet) {
        this.DRLSet = DRLSet;
    }

}
