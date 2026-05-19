package com.urovo.sdk.emv.emvbean;

public class MirAidBean extends EmvAidCapkBean {

    private String CardType = "MirCard";
    private String TransactionType = "";
    private String ApplicationIdentifier = "";
    private String TerminalTransactionQualifiers = "";
    private String FloorLimit = "000000000000";
    private String ApplicationVersion = "";
    private String TerminalActionCodesOnLine = "";
    private String TerminalActionCodesDenial = "";
    private String TerminalActionCodesDefault = "";
    private String ThresholdValue = "";
    private String TargetPercentage = "";
    private String MaxTargetPercentage = "";
    private String EmvTerminalFloorLimit = "";
    private String NoOnDeviceCVM = "999999999999";
    private String OnDeviceCVM = "999999999999";
    private String CvmRequiredLimit = "";
    private String TerminalCountryCode = "";
    private String TerminalType = "";
    private String DefaultTDOL = "";
    private String DefaultDDOL = "";
    private String TPMCapabilities = "";
    private String TransactionRecoveryLimit = "";
    private String DataExchangeTagList = "";
    private String AcquirerIdentifier = "";
    private String TerminalCapabilities = "";
    private String ApplicationVersionNumber = "";

    private String NfcAEC = "";

    public String getNfcAEC() {
		return NfcAEC;
	}

    public void setNfcAEC(String nfcAEC) {
		NfcAEC = nfcAEC;
	}

    public  String getApplicationVersionNumber() {
		return ApplicationVersionNumber;
	}

    public void setApplicationVersionNumber(String applicationVersionNumber) {
		ApplicationVersionNumber = applicationVersionNumber;
	}

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

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

    public String getFloorLimit() {
        return FloorLimit;
    }

    public void setFloorLimit(String floorLimit) {
        FloorLimit = floorLimit;
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

    public String getEmvTerminalFloorLimit() {
        return EmvTerminalFloorLimit;
    }

    public void setEmvTerminalFloorLimit(String emvTerminalFloorLimit) {
        EmvTerminalFloorLimit = emvTerminalFloorLimit;
    }

    public String getNoOnDeviceCVM() {
        return NoOnDeviceCVM;
    }

    public void setNoOnDeviceCVM(String noOnDeviceCVM) {
        NoOnDeviceCVM = noOnDeviceCVM;
    }

    public String getOnDeviceCVM() {
        return OnDeviceCVM;
    }

    public void setOnDeviceCVM(String onDeviceCVM) {
        OnDeviceCVM = onDeviceCVM;
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

    public String getTPMCapabilities() {
        return TPMCapabilities;
    }

    public void setTPMCapabilities(String TPMCapabilities) {
        this.TPMCapabilities = TPMCapabilities;
    }

    public String getTransactionRecoveryLimit() {
        return TransactionRecoveryLimit;
    }

    public void setTransactionRecoveryLimit(String transactionRecoveryLimit) {
        TransactionRecoveryLimit = transactionRecoveryLimit;
    }

    public String getDataExchangeTagList() {
        return DataExchangeTagList;
    }

    public void setDataExchangeTagList(String dataExchangeTagList) {
        DataExchangeTagList = dataExchangeTagList;
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

}
