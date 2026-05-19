package com.urovo.sdk.emv.emvbean;

public class JcbAidBean extends EmvAidCapkBean {

    private String CardType = "JcbCard";
    private String TransactionType = "";
    private String ApplicationIdentifier = "";
    private String ConfigurationCombinationOptions = "";
    private String StaticTerminalInterchangeProfile = "";
    private String TerminalCountryCode = "";
    private String TerminalCapabilities = "";
    private String TransactionLimit = "999999999999";
    private String FloorLimit = "000000000000";
    private String CvmRequiredLimit = "999999999999";
    private String EmvTerminalFloorLimit = "";
    private String ApplicationVersion = "";
    private String TerminalActionCodesOnLine = "";
    private String TerminalActionCodesDenial = "";
    private String TerminalActionCodesDefault = "";
    private String ThresholdValue = "";
    private String TargetPercentage = "";
    private String MaxTargetPercentage = "";
    private String AcquirerIdentifier = "";
    private String MerchantCategoryCode = "";
    private String MerchantNameAndLocation = "";
    private String RemovalTimeout = "";
    private String AppSelIndicator = "";
    private String TerminalType = "";
    private String DefaultTDOL = "";
    private String DefaultDDOL = "";
    private String OnDeviceCVM = "";

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

    public String getConfigurationCombinationOptions() {
        return ConfigurationCombinationOptions;
    }

    public void setConfigurationCombinationOptions(String configurationCombinationOptions) {
        ConfigurationCombinationOptions = configurationCombinationOptions;
    }

    public String getStaticTerminalInterchangeProfile() {
        return StaticTerminalInterchangeProfile;
    }

    public void setStaticTerminalInterchangeProfile(String staticTerminalInterchangeProfile) {
        StaticTerminalInterchangeProfile = staticTerminalInterchangeProfile;
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

    public String getAcquirerIdentifier() {
        return AcquirerIdentifier;
    }

    public void setAcquirerIdentifier(String acquirerIdentifier) {
        AcquirerIdentifier = acquirerIdentifier;
    }

    public String getMerchantCategoryCode() {
        return MerchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        MerchantCategoryCode = merchantCategoryCode;
    }

    public String getMerchantNameAndLocation() {
        return MerchantNameAndLocation;
    }

    public void setMerchantNameAndLocation(String merchantNameAndLocation) {
        MerchantNameAndLocation = merchantNameAndLocation;
    }

    public String getRemovalTimeout() {
        return RemovalTimeout;
    }

    public void setRemovalTimeout(String removalTimeout) {
        RemovalTimeout = removalTimeout;
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

    public String getOnDeviceCVM() {
        return OnDeviceCVM;
    }

    public void setOnDeviceCVM(String onDeviceCVM) {
        OnDeviceCVM = onDeviceCVM;
    }

}
