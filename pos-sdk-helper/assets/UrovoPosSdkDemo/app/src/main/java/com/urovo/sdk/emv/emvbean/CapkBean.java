package com.urovo.sdk.emv.emvbean;

/**
 * For EMV CAPK.
 */
public class CapkBean extends EmvAidCapkBean {

    private String Rid = "";
    private String Index = "";
    private String ArithInd = "";
    private String Exponent = "";
    private String Modulus = "";
    private String Checksum = "";

    public String getRid() {
        return Rid;
    }

    public void setRid(String rid) {
        Rid = rid;
    }

    public String getIndex() {
        return Index;
    }

    public void setIndex(String index) {
        Index = index;
    }

    public String getArithInd() {
        return ArithInd;
    }

    public void setArithInd(String arithInd) {
        ArithInd = arithInd;
    }

    public String getExponent() {
        return Exponent;
    }

    public void setExponent(String exponent) {
        Exponent = exponent;
    }

    public String getModulus() {
        return Modulus;
    }

    public void setModulus(String modulus) {
        Modulus = modulus;
    }

    public String getChecksum() {
        return Checksum;
    }

    public void setChecksum(String checksum) {
        Checksum = checksum;
    }

}
