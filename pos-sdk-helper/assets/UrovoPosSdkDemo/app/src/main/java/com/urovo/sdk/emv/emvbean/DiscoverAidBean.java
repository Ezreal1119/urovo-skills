package com.urovo.sdk.emv.emvbean;

public class DiscoverAidBean extends EmvAidCapkBean {

    private String CardType = "DiscoverCard";
    private String TransactionType = "";
    private String ApplicationIdentifier = "";
    private String TerminalTransactionQualifiers = "";
    private String TerminalCountryCode = "";
    private String TransactionLimit = "999999999999";
    private String FloorLimit = "000000000000";
    private String CvmRequiredLimit = "999999999999";
    private String EmvTerminalFloorLimit = "";
    private String ApplicationVersion = "";
    private String TerminalActionCodesOnLine = "";
    private String TerminalActionCodesDenial = "";
    private String TerminalActionCodesDefault = "";
    private String ZeroAmountCheckFlag = "";
    private String ZeroAmountAllowedOfflineFlag = "";
    private String StatusCheckFlag = "";
    private String MerchantCategoryCode = "";
    private String MerchantNameAndLocation = "";
    private String TerminalCapabilities = "";
    private String AppSelIndicator = "";
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

    public String getTerminalCountryCode() {
        return TerminalCountryCode;
    }

    public void setTerminalCountryCode(String terminalCountryCode) {
        TerminalCountryCode = terminalCountryCode;
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

    public String getZeroAmountCheckFlag() {
        return ZeroAmountCheckFlag;
    }

    public void setZeroAmountCheckFlag(String zeroAmountCheckFlag) {
        ZeroAmountCheckFlag = zeroAmountCheckFlag;
    }

    public String getZeroAmountAllowedOfflineFlag() {
        return ZeroAmountAllowedOfflineFlag;
    }

    public void setZeroAmountAllowedOfflineFlag(String zeroAmountAllowedOfflineFlag) {
        ZeroAmountAllowedOfflineFlag = zeroAmountAllowedOfflineFlag;
    }

    public String getStatusCheckFlag() {
        return StatusCheckFlag;
    }

    public void setStatusCheckFlag(String statusCheckFlag) {
        StatusCheckFlag = statusCheckFlag;
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
