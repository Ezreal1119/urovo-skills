package com.urovo.sdk.emv.emvbean;

/**
 * For EMV contact aid
 */
public class ICAidBean extends EmvAidCapkBean {

    private String CardType = "IcCard";
    private String aid = "";
    private String ApplicationIdentifier = "";
    private String appVersion = "";
    private String AppSelIndicator = "00";
    private String TerminalAppPriority = "00";
    private String contactTACDefault = "";
    private String contactTACDenial = "";
    private String contactTACOnline = "";
    private String defaultTDOL = "";
    private String defaultDDOL = "";
    private String ThresholdValue = "";
    private String TargetPercentage = "";
    private String MaxTargetPercentage = "";
    private String contactlessCVMRequiredLimit = "";
    private String contactlessFloorLimit = "";
    private String contactlessTransactionLimit = "";
    private String terminalFloorLimit = "00000000";
    private String AcquirerIdentifier = "";
    private String TerminalCapabilities = "";
    private String terminalCountryCode = "";
    private String terminalFloorLimitCheck = "01";
    private String TransactionCurrencyCode = "";
    private String TransactionCurrencyCodeExponent = "";
    private String ApplicationDefaultLabel = "";
    private String MerchantID = "";
    private String MerchantName = "";
    private String TerminalID = "";
    private String MerchantCategoryCode = "";
    private String AdditionalTerminalCapabilities = "";
    private String TerminalType = "";

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getApplicationIdentifier() {
        return ApplicationIdentifier;
    }

    public void setApplicationIdentifier(String applicationIdentifier) {
        ApplicationIdentifier = applicationIdentifier;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppSelIndicator() {
        return AppSelIndicator;
    }

    public void setAppSelIndicator(String appSelIndicator) {
        AppSelIndicator = appSelIndicator;
    }

    public String getTerminalAppPriority() {
        return TerminalAppPriority;
    }

    public void setTerminalAppPriority(String terminalAppPriority) {
        TerminalAppPriority = terminalAppPriority;
    }

    public String getContactTACDefault() {
        return contactTACDefault;
    }

    public void setContactTACDefault(String contactTACDefault) {
        this.contactTACDefault = contactTACDefault;
    }

    public String getContactTACDenial() {
        return contactTACDenial;
    }

    public void setContactTACDenial(String contactTACDenial) {
        this.contactTACDenial = contactTACDenial;
    }

    public String getContactTACOnline() {
        return contactTACOnline;
    }

    public void setContactTACOnline(String contactTACOnline) {
        this.contactTACOnline = contactTACOnline;
    }

    public String getDefaultTDOL() {
        return defaultTDOL;
    }

    public void setDefaultTDOL(String defaultTDOL) {
        this.defaultTDOL = defaultTDOL;
    }

    public String getDefaultDDOL() {
        return defaultDDOL;
    }

    public void setDefaultDDOL(String defaultDDOL) {
        this.defaultDDOL = defaultDDOL;
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

    public String getContactlessCVMRequiredLimit() {
        return contactlessCVMRequiredLimit;
    }

    public void setContactlessCVMRequiredLimit(String contactlessCVMRequiredLimit) {
        this.contactlessCVMRequiredLimit = contactlessCVMRequiredLimit;
    }

    public String getContactlessFloorLimit() {
        return contactlessFloorLimit;
    }

    public void setContactlessFloorLimit(String contactlessFloorLimit) {
        this.contactlessFloorLimit = contactlessFloorLimit;
    }

    public String getContactlessTransactionLimit() {
        return contactlessTransactionLimit;
    }

    public void setContactlessTransactionLimit(String contactlessTransactionLimit) {
        this.contactlessTransactionLimit = contactlessTransactionLimit;
    }

    public String getTerminalFloorLimit() {
        return terminalFloorLimit;
    }

    public void setTerminalFloorLimit(String terminalFloorLimit) {
        this.terminalFloorLimit = terminalFloorLimit;
    }

    public String getAcquirerIdentifier() {
        return AcquirerIdentifier;
    }

    public void setAcquirerIdentifier(String acquirerIdentifier) {
        AcquirerIdentifier = acquirerIdentifier;
    }

    public String getTerminalCapabilities() {
        return TerminalCapabilities;
    }

    public void setTerminalCapabilities(String terminalCapabilities) {
        TerminalCapabilities = terminalCapabilities;
    }

    public String getTerminalCountryCode() {
        return terminalCountryCode;
    }

    public void setTerminalCountryCode(String terminalCountryCode) {
        this.terminalCountryCode = terminalCountryCode;
    }

    public String getTerminalFloorLimitCheck() {
        return terminalFloorLimitCheck;
    }

    public void setTerminalFloorLimitCheck(String terminalFloorLimitCheck) {
        this.terminalFloorLimitCheck = terminalFloorLimitCheck;
    }

    public String getTransactionCurrencyCode() {
        return TransactionCurrencyCode;
    }

    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        TransactionCurrencyCode = transactionCurrencyCode;
    }

    public String getTransactionCurrencyCodeExponent() {
        return TransactionCurrencyCodeExponent;
    }

    public void setTransactionCurrencyCodeExponent(String transactionCurrencyCodeExponent) {
        TransactionCurrencyCodeExponent = transactionCurrencyCodeExponent;
    }

    public String getApplicationDefaultLabel() {
        return ApplicationDefaultLabel;
    }

    public void setApplicationDefaultLabel(String applicationDefaultLabel) {
        ApplicationDefaultLabel = applicationDefaultLabel;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public String getTerminalID() {
        return TerminalID;
    }

    public void setTerminalID(String terminalID) {
        TerminalID = terminalID;
    }

    public String getMerchantCategoryCode() {
        return MerchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        MerchantCategoryCode = merchantCategoryCode;
    }

    public String getAdditionalTerminalCapabilities() {
        return AdditionalTerminalCapabilities;
    }

    public void setAdditionalTerminalCapabilities(String additionalTerminalCapabilities) {
        AdditionalTerminalCapabilities = additionalTerminalCapabilities;
    }

    public String getTerminalType() {
        return TerminalType;
    }

    public void setTerminalType(String terminalType) {
        TerminalType = terminalType;
    }

}
