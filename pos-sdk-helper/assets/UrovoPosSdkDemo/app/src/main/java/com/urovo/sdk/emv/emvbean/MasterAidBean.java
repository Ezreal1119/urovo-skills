package com.urovo.sdk.emv.emvbean;

/**
 * For Master contactless aid.
 */
public class MasterAidBean extends EmvAidCapkBean {

    private String CardType = "MasterCard";
    private String TransactionType = "";
    private String ApplicationIdentifier = "";
    private String AcquirerIdentifier = "";
    private String AdditionalTerminalCapabilities = "";
    private String ApplicationVersion = "";
    private String CardDataInputCapability = "";
    private String KernelConfiguration = "";
    private String CVMCapabilityPerCVMRequired = "";
    private String MagStripeCVMCapabilityCVMRequired = "";
    private String SecurityCapability = "";
    private String MagStripeCVMCapabilityPerNoCVMRequired = "";
    private String CVMCapabilityNoCVMRequired = "";
    private String IFDsn = "";
    private String MerchantCategoryCode = "";
    private String MerchantIdentifier = "";
    private String MerchantNameAndLocation = "";
    private String DefaultUDOL = "";
    private String FloorLimit = "000000000000";
    private String NoOnDeviceCVM = "999999999999";
    private String OnDeviceCVM = "999999999999";
    private String CvmRequiredLimit = "";
    private String TerminalActionCodesOnLine = "";
    private String TerminalActionCodesDenial = "";
    private String TerminalActionCodesDefault = "";
    private String TerminalRiskManagement = "";
    private String TerminalCountryCode = "";
    private String TerminalType = "";
    private String DSVNTerm = "";
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

    public String getAcquirerIdentifier() {
        return AcquirerIdentifier;
    }

    public void setAcquirerIdentifier(String acquirerIdentifier) {
        AcquirerIdentifier = acquirerIdentifier;
    }

    public String getAdditionalTerminalCapabilities() {
        return AdditionalTerminalCapabilities;
    }

    public void setAdditionalTerminalCapabilities(String additionalTerminalCapabilities) {
        AdditionalTerminalCapabilities = additionalTerminalCapabilities;
    }

    public String getApplicationVersion() {
        return ApplicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        ApplicationVersion = applicationVersion;
    }

    public String getCardDataInputCapability() {
        return CardDataInputCapability;
    }

    public void setCardDataInputCapability(String cardDataInputCapability) {
        CardDataInputCapability = cardDataInputCapability;
    }

    public String getKernelConfiguration() {
        return KernelConfiguration;
    }

    public void setKernelConfiguration(String kernelConfiguration) {
        KernelConfiguration = kernelConfiguration;
    }

    public String getCVMCapabilityPerCVMRequired() {
        return CVMCapabilityPerCVMRequired;
    }

    public void setCVMCapabilityPerCVMRequired(String CVMCapabilityPerCVMRequired) {
        this.CVMCapabilityPerCVMRequired = CVMCapabilityPerCVMRequired;
    }

    public String getMagStripeCVMCapabilityCVMRequired() {
        return MagStripeCVMCapabilityCVMRequired;
    }

    public void setMagStripeCVMCapabilityCVMRequired(String magStripeCVMCapabilityCVMRequired) {
        MagStripeCVMCapabilityCVMRequired = magStripeCVMCapabilityCVMRequired;
    }

    public String getSecurityCapability() {
        return SecurityCapability;
    }

    public void setSecurityCapability(String securityCapability) {
        SecurityCapability = securityCapability;
    }

    public String getMagStripeCVMCapabilityPerNoCVMRequired() {
        return MagStripeCVMCapabilityPerNoCVMRequired;
    }

    public void setMagStripeCVMCapabilityPerNoCVMRequired(String magStripeCVMCapabilityPerNoCVMRequired) {
        MagStripeCVMCapabilityPerNoCVMRequired = magStripeCVMCapabilityPerNoCVMRequired;
    }

    public String getCVMCapabilityNoCVMRequired() {
        return CVMCapabilityNoCVMRequired;
    }

    public void setCVMCapabilityNoCVMRequired(String CVMCapabilityNoCVMRequired) {
        this.CVMCapabilityNoCVMRequired = CVMCapabilityNoCVMRequired;
    }

    public String getIFDsn() {
        return IFDsn;
    }

    public void setIFDsn(String IFDsn) {
        this.IFDsn = IFDsn;
    }

    public String getMerchantCategoryCode() {
        return MerchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        MerchantCategoryCode = merchantCategoryCode;
    }

    public String getMerchantIdentifier() {
        return MerchantIdentifier;
    }

    public void setMerchantIdentifier(String merchantIdentifier) {
        MerchantIdentifier = merchantIdentifier;
    }

    public String getMerchantNameAndLocation() {
        return MerchantNameAndLocation;
    }

    public void setMerchantNameAndLocation(String merchantNameAndLocation) {
        MerchantNameAndLocation = merchantNameAndLocation;
    }

    public String getDefaultUDOL() {
        return DefaultUDOL;
    }

    public void setDefaultUDOL(String defaultUDOL) {
        DefaultUDOL = defaultUDOL;
    }

    public String getFloorLimit() {
        return FloorLimit;
    }

    public void setFloorLimit(String floorLimit) {
        FloorLimit = floorLimit;
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

    public String getTerminalRiskManagement() {
        return TerminalRiskManagement;
    }

    public void setTerminalRiskManagement(String terminalRiskManagement) {
        TerminalRiskManagement = terminalRiskManagement;
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

    public String getDSVNTerm() {
        return DSVNTerm;
    }

    public void setDSVNTerm(String DSVNTerm) {
        this.DSVNTerm = DSVNTerm;
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
