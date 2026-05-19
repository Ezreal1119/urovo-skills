# Urovo EMV Kernel SDK API Knowledge Base

Source document: `User guide of EMV kernel library API_V2.4.104.pdf`  
Demo project: `UrovoPosSdkDemo`  
Generated: 2026-05-13  
Chunking rule: split on `^### `; each `###` section is intended to describe one EMV API, callback, enum, or FAQ entry.

### EMV API / startKernel

- Source section: `1.01`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
EMV process.


Signature/prototype:
```java
void startKernel(Hashtable<String,Object>transParams)
```


Parameters:
See below Hashtablekeystable.


Return value:
None


Usage notes:
onRequestSetAmount
startEmvHashtableKeys1/2
TransactionSettings
• emvOption: Indicatewhether toforcetransactiongo online, seeEmvOption
enumeration.
• checkCardMode:Indicatewhich checkcardmodetouse, seeCheckCardMode
enumeration.
• transactionType: Indicatethetype of financialtransaction(eg:00-goods,
01-cash,09-cashback, 20--refund)
• amount:Thetransactionamount. (e.g. "0.01")
• cashbackAmount:Thecashbackamount.
• amountEx:Thetransactionamount. (e.g. "001")
• cashbackAmountEx:Thecashbackamount.
• currencyCode:The3-digits transactioncurrencycode (e.g. "840" for USD).
• checkCardTimeout: Indicatethecheckcardtimeout, insecond.
• isEnterAmtAfterReadRecord:thisflagistrue,youneedtoentertheamountinthe
onRequestSetAmount callback
• FallbackSwitch: enableor disable thefallbackfunction 0-disable 1-enable
• supportDRL: true -Terminal support DRL for Visa, false-Terminel not support
DRLforVisa.
•enableBeeper: true - enable beeper when card read successful , false - disable
beeper when cardreadsuccessful.
•enableTapSwipeCollision: true - endable Tap/Swipe card collision check, false -
disableTap/Swipe cardcollision check.
•DisableCheckMSRFormat: true - Disable check MSR format ,false - Check MSR
format
•NeedFallBackTryTimes: (eg:1,2,3…) How many times does it take to fallback after
thecardreading fails.
•DisableCheckMSRFormat: true - check the magnetic stripe Format, false - Don`t

check
Mag StripeFormat.
• enableEncMagStripe: true - Return Encrypt magnetic stripe, false - Return
Plaintext magnetic stripe
•MSRKeyIndex: (1-4) DUKPTkey indexfor encrypted magneticstripe
•forceInputPIN: true - Force input of online PIN, valid for contactless. Note: This
functioncannot beusedfor L2authentication


Simplified example:
```java
Hashtable<String, Object> data = new Hashtable<>();
data.put("checkCardMode", ContantPara.CheckCardMode.SWIPE_OR_INSERT_OR_TAP);
data.put("emvOption", ContantPara.EmvOption.START);
data.put("amount", "0");
data.put("cashbackAmount", "0");
data.put("checkCardTimeout", "30");
data.put("transactionType", "00");
data.put("currencyCode", "156");
emv.updateTerminalParamters(ContantPara.CardSlot.ICC, "9F3303E0F8C85F2A0201569F1A020156");
emv.updateTerminalParamters(ContantPara.CardSlot.PICC, "9F3303E0F8C85F2A0201569F1A020156");
emv.startKernel(data);
```


Source details:
```text
Declare: void startKernel(Hashtable<String,Object>transParams)
Start the transaction with specified CheckCardMode,go through all the
Description: 
EMV process.
Parameters: See below Hashtablekeystable.
Return: None
Remark: onRequestSetAmount
startEmvHashtableKeys1/2
TransactionSettings
• emvOption: Indicatewhether toforcetransactiongo online, seeEmvOption
enumeration.
• checkCardMode:Indicatewhich checkcardmodetouse, seeCheckCardMode
enumeration.
• transactionType: Indicatethetype of financialtransaction(eg:00-goods,
01-cash,09-cashback, 20--refund)
• amount:Thetransactionamount. (e.g. "0.01")
• cashbackAmount:Thecashbackamount.
• amountEx:Thetransactionamount. (e.g. "001")
• cashbackAmountEx:Thecashbackamount.
• currencyCode:The3-digits transactioncurrencycode (e.g. "840" for USD).
• checkCardTimeout: Indicatethecheckcardtimeout, insecond.
• isEnterAmtAfterReadRecord:thisflagistrue,youneedtoentertheamountinthe
onRequestSetAmount callback
• FallbackSwitch: enableor disable thefallbackfunction 0-disable 1-enable
• supportDRL: true -Terminal support DRL for Visa, false-Terminel not support
DRLforVisa.
•enableBeeper: true - enable beeper when card read successful , false - disable
beeper when cardreadsuccessful.
•enableTapSwipeCollision: true - endable Tap/Swipe card collision check, false -
disableTap/Swipe cardcollision check.
•DisableCheckMSRFormat: true - Disable check MSR format ,false - Check MSR
format
•NeedFallBackTryTimes: (eg:1,2,3…) How many times does it take to fallback after
thecardreading fails.
•DisableCheckMSRFormat: true - check the magnetic stripe Format, false - Don`t

check
Mag StripeFormat.
• enableEncMagStripe: true - Return Encrypt magnetic stripe, false - Return
Plaintext magnetic stripe
•MSRKeyIndex: (1-4) DUKPTkey indexfor encrypted magneticstripe
•forceInputPIN: true - Force input of online PIN, valid for contactless. Note: This
functioncannot beusedfor L2authentication
```

### EMV API / abortKernel

- Source section: `1.02`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Declare: public void  abortKernel ()


Signature/prototype:
```java
public void  abortKernel ()
```


Simplified example:
```java
if (emv != null) {
    emv.abortKernel();
}
```


Source details:
```text
Declare: public void  abortKernel ()
Description: abortKernel,stopcheckcard,stopseephonecheckcard
Parameters: none
Return: 
Remark:
```

### EMV API / setAmountEx

- Source section: `1.03`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
storethevaluesand
IfthefunctionreturnFalse, thetransactionshould beterminate
• amount :Thetransactionamount.


Signature/prototype:
```java
cashbackAmount)
```


Parameters:
• cashbackAmount :The cashbackamount.
True-Amountvalues areset.


Return value:
False- Failed toset values.


Usage notes:
OnlyuseinonRequestSetAmount


Simplified example:
```java
public void onRequestSetAmount() {
    emv.setAmountEx(100L, 0L);
}
```


Source details:
```text
public Boolean setAmountEx(String amount, String
Declare: 
cashbackAmount)
Set theamount,cashbackamountin responseto
onRequestSetAmount.
Thisfunction canbe calledbefore atransaction.TheAPI will
Description: 
storethevaluesand
IfthefunctionreturnFalse, thetransactionshould beterminate
• amount :Thetransactionamount.
Parameters: • cashbackAmount :The cashbackamount.
True-Amountvalues areset.
Return: 
False- Failed toset values.
Remark: OnlyuseinonRequestSetAmount
```

### EMV API / sendConfirmCardnoResult

- Source section: `1.04`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Confirmthecardnumberisright,callatonRequestConfirmCardno()


Signature/prototype:
```java
void sendConfirmCardnoResult(Booleanisconfirm)
```


Parameters:
True false


Return value:
None


Usage notes:
None


Source details:
```text
Declare: void sendConfirmCardnoResult(Booleanisconfirm)

Description: Confirmthecardnumberisright,callatonRequestConfirmCardno()
Parameters: True false
Return: None
Remark: None
```

### EMV API / selectApplication

- Source section: `1.05`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
onRequestSelectApplication.
TheAppshould prompt thecustomerfor anapplication to
continuethe
transaction.


Signature/prototype:
```java
voidselectApplication(int index)
```


Parameters:
Theindex oftheapplication selected.


Return value:
None


Usage notes:
onRequestSelectApplication


Source details:
```text
Declare: voidselectApplication(int index)
Anchip cardmaysupportmultiple payment applications.The list
ofApplications
IDssupportedbythecardanddevice isreturnedin
Description: onRequestSelectApplication.
TheAppshould prompt thecustomerfor anapplication to
continuethe
transaction.
Parameters: Theindex oftheapplication selected.
Return: None
Remark: onRequestSelectApplication
```

### EMV API / sendFinalConfirmResult

- Source section: `1.06`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
inresponsetoonRequestFinalConfirm.
• isConfirmed: Indicate theconfirmation result.True- Confirm


Signature/prototype:
```java
void sendFinalConfirmResult(boolean isConfirmed)
```


Parameters:
tocontinue thetransaction. False- Cancel thetransaction.


Return value:
None


Usage notes:
onRequestFinalConfirm


Source details:
```text
Declare: void sendFinalConfirmResult(boolean isConfirmed)
Sendthefinal confirmationtoproceedor cancelthetransaction
Description: 
inresponsetoonRequestFinalConfirm.
• isConfirmed: Indicate theconfirmation result.True- Confirm
Parameters: 
tocontinue thetransaction. False- Cancel thetransaction.
Return: None
Remark: onRequestFinalConfirm
```

### EMV API / sendOnlineProcessResult

- Source section: `1.07`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
bOnlineResult
• true: go online success
• false: goonline fail(unable togoonline)

Aset of transaction resultsinTag-Length-Value format.
Thefollowing tagsareusually returnedtothecard:
• Tag8A:AuthorisationResponseCode (Mandatory)
• Tag89:Authorisation Code


Signature/prototype:
```java
void sendOnlineProcessResult(BooleanbOnlineResult, String tlv)
```


Parameters:
• Tag91: IssuerAuthentication Data
• Tag71: Issuer ScriptTemplate1
• Tag72: Issuer ScriptTemplate2
Example:
String tlv= "8A02303091083132333435363738"


Return value:
None


Usage notes:
onRequestOnlineProcess


Simplified example:
```java
public void onRequestOnlineProcess(String cardTlvData, String dataKsn) {
    String issuerTlv = "8A023030"; // approval response code 00
    emv.sendOnlineProcessResult(issuerTlv);
}
```


Source details:
```text
Declare: void sendOnlineProcessResult(BooleanbOnlineResult, String tlv)
Sendbacktheonline processresulttothecardin responseto
onRequestOnlineProcess.Toterminatethetransaction, sendnull
oremptystring as result.
Description: 
bOnlineResult
• true: go online success
• false: goonline fail(unable togoonline)

Aset of transaction resultsinTag-Length-Value format.
Thefollowing tagsareusually returnedtothecard:
• Tag8A:AuthorisationResponseCode (Mandatory)
• Tag89:Authorisation Code
Parameters: • Tag91: IssuerAuthentication Data
• Tag71: Issuer ScriptTemplate1
• Tag72: Issuer ScriptTemplate2
Example:
String tlv= "8A02303091083132333435363738"
Return: None
Remark: onRequestOnlineProcess
```

### EMV API / ProcOnlinePinAgain

- Source section: `1.08`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Ifyounotsupportbypass,youcancallthisapi,restartpopuppinpad


Signature/prototype:
```java
public void  ProcOnlinePinAgain()
```


Parameters:
None


Return value:
None


Usage notes:
Useinonlinepin


Source details:
```text
Declare: public void  ProcOnlinePinAgain()
Description: Ifyounotsupportbypass,youcancallthisapi,restartpopuppinpad
Parameters: None
Return: None
Remark: Useinonlinepin
```

### EMV API / sendPinEntry

- Source section: `1.09`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Tell Userhasenteredonline pin


Signature/prototype:
```java
void sendPinEntry()
```


Return value:
None


Usage notes:
Useinonlinepin


Source details:
```text
Declare: void sendPinEntry()
Description: Tell Userhasenteredonline pin
Parameters: 
Return: None
Remark: Useinonlinepin
```

### EMV API / bypassPinEntry

- Source section: `1.10`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
thecarddoes not acceptbypassing, thetransactionwill be
aborted.


Signature/prototype:
```java
void bypassPinEntry()
```


Parameters:
None


Return value:
None


Usage notes:
Useinonlinepin


Source details:
```text
Declare: void bypassPinEntry()
Bypass thePINentrystepinresponsetoonRequestPinEntry If
Description: thecarddoes not acceptbypassing, thetransactionwill be
aborted.
Parameters: None
Return: None
Remark: Useinonlinepin
```

### EMV API / cancelPinEntry

- Source section: `1.11`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
thecarddoes not acceptbypassing, thetransactionwill be
aborted.


Signature/prototype:
```java
void cancelPinEntry()
```


Parameters:
None


Return value:
None


Usage notes:
Useinonlinepin


Source details:
```text
Declare: void cancelPinEntry()
CancelthePINentry stepin responsetoonRequestPinEntry If
Description: thecarddoes not acceptbypassing, thetransactionwill be
aborted.
Parameters: None
Return: None
Remark: Useinonlinepin
```

### EMV API / updateTerminalParamters

- Source section: `1.12`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
issupportedand allow toupdate.
ContantPara.CardSlot.ICC: UpdateICCTerminalParamters
ContantPara.CardSlot.PICC:UpdatePICCTerminal Paramters
ContantPara.CardSlot.UNKNOW:UpdateICCandPICC


Signature/prototype:
```java
cardSlot,String tlv)
```


Parameters:
TerminalParamters.
Aterminal configurationparameterinTag-Length-Value format.
See below  tablefor thelist of tags.


Return value:
True-updatesuccess,False-updatefailed


Usage notes:
Tag Format Length describe
9F1A n3 2 bytes Terminal Country
Code.
9F01 n12 6 bytes Transaction
CurrencyCode.
5F36 b 1 byte Transaction
Currency
Exponent.
9F4E ans0-40 Variable 0-40 Merchant Name
andLocation.
9F16 Ans0-15 Variable0-15 bytes MerchantIdentifier.
9F1C An0-8 Variable 0-8 bytes Terminal
Identification.
9F33 b 3 bytes Terminal
Capabilities.

(default: E0F8C8)
9F35 n2 1 byte TerminalType.
(default: 22)
9F40 b 5 byte Additional Terminal
Capabilities
9F15 n4 2 byte Merchant Category
Code
DF02 b 1 byte Ramdom
TransactionSwitch
DF03 b 1 byte Exception File
CheckSwitch
DF04 b 1 byte SupportSM
DF05 b 1 byte Valocity Check
enable
DF7F b 5-17 byte Terminals prioritize
AID (PURE/VCCS
kernel)


Simplified example:
```java
String terminalTlv = "9F3303E0F8C85F2A0201569F1A020156";
emv.updateTerminalParamters(ContantPara.CardSlot.UNKNOW, terminalTlv);
```


Source details:
```text
Boolean updateTerminalParamters (ContantPara.CardSlot
Declare: 
cardSlot,String tlv)
Updateaterminal configurationparameter,provided thatthetag
Description: 
issupportedand allow toupdate.
ContantPara.CardSlot.ICC: UpdateICCTerminalParamters
ContantPara.CardSlot.PICC:UpdatePICCTerminal Paramters
ContantPara.CardSlot.UNKNOW:UpdateICCandPICC
Parameters: TerminalParamters.
Aterminal configurationparameterinTag-Length-Value format.
See below  tablefor thelist of tags.
Return: True-updatesuccess,False-updatefailed
Remark: 
Tag Format Length describe
9F1A n3 2 bytes Terminal Country
Code.
9F01 n12 6 bytes Transaction
CurrencyCode.
5F36 b 1 byte Transaction
Currency
Exponent.
9F4E ans0-40 Variable 0-40 Merchant Name
andLocation.
9F16 Ans0-15 Variable0-15 bytes MerchantIdentifier.
9F1C An0-8 Variable 0-8 bytes Terminal
Identification.
9F33 b 3 bytes Terminal
Capabilities.

(default: E0F8C8)
9F35 n2 1 byte TerminalType.
(default: 22)
9F40 b 5 byte Additional Terminal
Capabilities
9F15 n4 2 byte Merchant Category
Code
DF02 b 1 byte Ramdom
TransactionSwitch
DF03 b 1 byte Exception File
CheckSwitch
DF04 b 1 byte SupportSM
DF05 b 1 byte Valocity Check
enable
DF7F b 5-17 byte Terminals prioritize
AID (PURE/VCCS
kernel)
```

### EMV API / updateCAPK

- Source section: `1.13`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/TestEmv.java`



Purpose:
UpdateaCertificateAuthorityPublic Key objectin thedevice.
TheCAPKobjecttobeupdated.Theobjectcontainsthelocation
oftheCAPK in thedevice, theRID, index, modulus,exponent
andthechecksumof theCAPK, all
datainhex stringformat.
Rid:Identify public keyof certification center
Index:Identifypublic keyofcertificationcentertogetherwithRID
Exponent: Public keyexponent
Modulus:Module value of public key
Checksum:Toverifypublic keyof certificationcenter


Signature/prototype:
```java
Hashtable<String,String>capkParams)
```


Parameters:
Toremove aCAPK entry at thespecifiedlocation, set withthe
following values:
• RID (5bytes) = "A000000004"
• Index(1byte) = "01"
• Exponent (1byte or 3bytes) = "03"
• Modulus (Nbytes) ="00"
• Size (2bytes) (not use)
• Checksum (20bytes) = SHA-1[RID|| Index|| Modulus ||
Exponent]


Return value:
True-updatesuccess False-updatefailed


Usage notes:
Mandatory: RID&Index&Exponent&Modulus


Simplified example:
```java
Hashtable<String, String> capk = new Hashtable<>();
capk.put("RID", "A000000003");
capk.put("Index", "08");
capk.put("Exponent", "03");
capk.put("Modulus", "...");
capk.put("Checksum", "...");
boolean ok = emv.updateCAPK(ContantPara.Operation.ADD, capk);
```


Source details:
```text
Boolean updateCAPK(ContantPara.Operation operation,
Declare: 
Hashtable<String,String>capkParams)
Description: UpdateaCertificateAuthorityPublic Key objectin thedevice.
TheCAPKobjecttobeupdated.Theobjectcontainsthelocation
oftheCAPK in thedevice, theRID, index, modulus,exponent
andthechecksumof theCAPK, all
datainhex stringformat.
Rid:Identify public keyof certification center
Index:Identifypublic keyofcertificationcentertogetherwithRID
Exponent: Public keyexponent
Modulus:Module value of public key
Checksum:Toverifypublic keyof certificationcenter
Parameters: 
Toremove aCAPK entry at thespecifiedlocation, set withthe
following values:
• RID (5bytes) = "A000000004"
• Index(1byte) = "01"
• Exponent (1byte or 3bytes) = "03"
• Modulus (Nbytes) ="00"
• Size (2bytes) (not use)
• Checksum (20bytes) = SHA-1[RID|| Index|| Modulus ||
Exponent]
Return: True-updatesuccess False-updatefailed

Remark: Mandatory: RID&Index&Exponent&Modulus
```

### EMV API / updateAID

- Source section: `1.14`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/TestEmv.java`



Purpose:
modifyingtheAID infowithupdateAID command.

Hashtablekeys
• CardType: "IcCard"
• aid:Application Identifier (AID).
• appVersion:Application VersionNumber.
• contactTACDefault: IdentifytheAcquirer'sconditionsto
rejecttransactionswhen thetransactionis able tobecompleted
online but theterminal lackssuchcapability.
• contactTACDenial:IdentifytheAcquirer's conditionsfor not
tryinganyonline processingi.e. rejectionof transactions.
• contactTACOnline:IdentifytheAcquirer's conditionsfor
online transactions
• defaultTDOL:TransactionCertificateData ObjectList
(TDOL).
• defaultDDOL: DynamicDataAuthenticationDataObjectList
(DDOL). DDOLused forestablishmentof internalcertification
commandwhen thereis no DDOLin thecard.
• terminalFloorLimit: ContactTerminal Floor Limit. The
lowest offlinelimit allowed at theterminalin ICcardpurchase
•AppSelIndicator:application selectindicator 00-party
match,01-fullmatch,default 00
• AcquirerIdentifier:Acquirer Identifier.


Signature/prototype:
```java
Hashtable<String,String>aidParams)
```


Parameters:
• TerminalCapabilities: terminal Capabilities 9F33
• terminalCountryCode:terminal countrycode9F1A
• ThresholdValue:Value used in terminal risk management for
random transaction selection. Present if the Combination
supportsRandom TransactionSelection (EMV Mode only)
Eg:"000000002000"
• TargetPercentage: Present if the Combination supports Random
TransactionSelection(EMVModeonly)
Eg:"00"
•MaxTargetPercentage:Value used in terminal risk management for
random transaction selection - present if the Combination supports
RandomTransactionSelection(EMVModeonly)
Eg:"00"
HashtableValues
• Datain hex stringformat.
Example:
b8Statuscheckingsupported
b7RFU
b6transactionlimitcheckingsupported
b5contactlessfloorlimitcheckingsupported
b4contactlesscvmlimitcheckingsupported
b3zeroamountcheckingsupported
b2zeroamountcheckingoption1supported
b1RFU
b8Statuscheckingsupported
b7RFU
b6transactionlimitcheckingsupported
b5contactlessfloorlimitcheckingsupported
b4contactlesscvmlimitcheckingsupported
b3zeroamountcheckingsupported
b2zeroamountcheckingoption1supported
b1RFU
b8Statuscheckingsupported
b7RFU
b6transactionlimitcheckingsupported
b5contactlessfloorlimitcheckingsupported
b4contactlesscvmlimitcheckingsupported
b3zeroamountcheckingsupported
b2zeroamountcheckingoption1supported
b1RFU


Return value:
True-updatesuccess False-updatefailed


Simplified example:
```java
Hashtable<String, String> aid = new Hashtable<>();
aid.put("CardType", "IcCard");
aid.put("aid", "A0000000031010");
aid.put("appVersion", "008C");
aid.put("terminalCountryCode", "0156");
boolean ok = emv.updateAID(ContantPara.Operation.ADD, aid);
```


Source details:
```text
boolean updateAID(ContantPara.Operationoperation,
Declare: 
Hashtable<String,String>aidParams)
UpdatetheAIDinfo.
Description: 
modifyingtheAID infowithupdateAID command.

Hashtablekeys
• CardType: "IcCard"
• aid:Application Identifier (AID).
• appVersion:Application VersionNumber.
• contactTACDefault: IdentifytheAcquirer'sconditionsto
rejecttransactionswhen thetransactionis able tobecompleted
online but theterminal lackssuchcapability.
• contactTACDenial:IdentifytheAcquirer's conditionsfor not
tryinganyonline processingi.e. rejectionof transactions.
• contactTACOnline:IdentifytheAcquirer's conditionsfor
online transactions
• defaultTDOL:TransactionCertificateData ObjectList
(TDOL).
• defaultDDOL: DynamicDataAuthenticationDataObjectList
(DDOL). DDOLused forestablishmentof internalcertification
commandwhen thereis no DDOLin thecard.
• terminalFloorLimit: ContactTerminal Floor Limit. The
lowest offlinelimit allowed at theterminalin ICcardpurchase
•AppSelIndicator:application selectindicator 00-party
match,01-fullmatch,default 00
• AcquirerIdentifier:Acquirer Identifier.
Parameters: • TerminalCapabilities: terminal Capabilities 9F33
• terminalCountryCode:terminal countrycode9F1A
• ThresholdValue:Value used in terminal risk management for
random transaction selection. Present if the Combination
supportsRandom TransactionSelection (EMV Mode only)
Eg:"000000002000"
• TargetPercentage: Present if the Combination supports Random
TransactionSelection(EMVModeonly)
Eg:"00"
•MaxTargetPercentage:Value used in terminal risk management for
random transaction selection - present if the Combination supports
RandomTransactionSelection(EMVModeonly)
Eg:"00"
HashtableValues
• Datain hex stringformat.
Example:
b8Statuscheckingsupported
b7RFU
b6transactionlimitcheckingsupported
b5contactlessfloorlimitcheckingsupported
b4contactlesscvmlimitcheckingsupported
b3zeroamountcheckingsupported
b2zeroamountcheckingoption1supported
b1RFU
b8Statuscheckingsupported
b7RFU
b6transactionlimitcheckingsupported
b5contactlessfloorlimitcheckingsupported
b4contactlesscvmlimitcheckingsupported
b3zeroamountcheckingsupported
b2zeroamountcheckingoption1supported
b1RFU
b8Statuscheckingsupported
b7RFU
b6transactionlimitcheckingsupported
b5contactlessfloorlimitcheckingsupported
b4contactlesscvmlimitcheckingsupported
b3zeroamountcheckingsupported
b2zeroamountcheckingoption1supported
b1RFU

Return: True-updatesuccess False-updatefailed
Remark:
```

### EMV API / updateExceptFile

- Source section: `1.15`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Updateblacklist tokernel
Mode:0-Add
1-Delete
2-Modify


Signature/prototype:
```java
boolean updateExceptFile(int mode,String ExceptTLV)
```


Parameters:
3-Clear
ExceptTLV:
tag5A+tag5F34
eg:5A0852364979362037965F340101


Return value:
None


Usage notes:
IfyouwantnotsupportExceptfilecheck,don`tupdateanyblacklist


Source details:
```text
Declare: boolean updateExceptFile(int mode,String ExceptTLV)
Description: Updateblacklist tokernel
Mode:0-Add
1-Delete
2-Modify
Parameters: 
3-Clear
ExceptTLV:
tag5A+tag5F34
eg:5A0852364979362037965F340101
Return: None
Remark: IfyouwantnotsupportExceptfilecheck,don`tupdateanyblacklist
```

### EMV API / LogOutEnable

- Source section: `1.16`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Enableor disablecontact/contactlesstransactionlog
0-disable


Signature/prototype:
```java
voidLogOutEnable(int enable)
```


Parameters:
1-enable


Return value:
None
adbpull/sdcard/UROPE/Trace.txt….


Usage notes:
adbpull/sdcard/UROPE/TraceCL.txt….
adbpull/sdcard/UROPE/ICCMD.log…


Simplified example:
```java
emv.LogOutEnable(BuildConfig.DEBUG ? 1 : 0); // disable in production
```


Source details:
```text
Declare: voidLogOutEnable(int enable)
Description: Enableor disablecontact/contactlesstransactionlog
0-disable
Parameters: 
1-enable
Return: None
adbpull/sdcard/UROPE/Trace.txt….
Remark: adbpull/sdcard/UROPE/TraceCL.txt….
adbpull/sdcard/UROPE/ICCMD.log…
```

### EMV API / sendOfflinePINVerifyResult

- Source section: `1.17`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Androdos 8.0,other versionnot use this,offlinepinverify
bykernel)
(-198) //Returncodeerror
(-202) //ICcommand failed
(-192) //PINBLOCKED


Signature/prototype:
```java
boolean sendOfflinePINVerifyResult(int iResult)
```


Parameters:
(-199) //user cancel or Pinpadtimeout
(1) //bypass
(0) //success


Return value:
0-success


Usage notes:
CallthisincallbackonRequestOfflinePINVerify


Source details:
```text
Declare: boolean sendOfflinePINVerifyResult(int iResult)
Sendoffline pinverifyresult tokernel(thisapi use in
Description: Androdos 8.0,other versionnot use this,offlinepinverify
bykernel)
(-198) //Returncodeerror
(-202) //ICcommand failed
(-192) //PINBLOCKED
Parameters: 
(-199) //user cancel or Pinpadtimeout
(1) //bypass
(0) //success
Return: 0-success

Remark: CallthisincallbackonRequestOfflinePINVerify
```

### EMV API / getIssuerScriptResult

- Source section: `1.18`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
GetScriptResult


Signature/prototype:
```java
booleangetIssuerScriptResult()
```


Parameters:
none


Return value:
0-getsuccessifsuccesswillcallbackonReturnIssuerScriptResult


Usage notes:
CallthisincallbackonReturnIssuerScriptResult


Source details:
```text
Declare: booleangetIssuerScriptResult()
Description: GetScriptResult
Parameters: none
Return: 0-getsuccessifsuccesswillcallbackonReturnIssuerScriptResult
Remark: CallthisincallbackonReturnIssuerScriptResult
```

### EMV API / getEmvAIDDetail

- Source section: `1.19`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Declare: public List<EmvAidData> getEmvAIDDetail()


Signature/prototype:
```java
public List<EmvAidData> getEmvAIDDetail()
```


Parameters:
none


Usage notes:
Donotcallduringthetransaction


Source details:
```text
Declare: public List<EmvAidData> getEmvAIDDetail()
ReturnallAidparamters,seetheEmvAidData.class
Description: 
Parameters: none
Return: 
Remark: Donotcallduringthetransaction
```

### EMV API / getNfcAIDDetail

- Source section: `1.20`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Declare: public List<ContactlessAidData> getNfcAIDDetail()


Signature/prototype:
```java
public List<ContactlessAidData> getNfcAIDDetail()
```


Parameters:
none


Usage notes:
Donotcallduringthetransaction


Source details:
```text
Declare: public List<ContactlessAidData> getNfcAIDDetail()
ReturnallAidparamters,seetheContactlessAidData.class
Description: 
Parameters: none
Return: 
Remark: Donotcallduringthetransaction
```

### EMV API / getCAPKDetail

- Source section: `1.21`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
public List<CAPK> getCAPKDetail(ContantPara.CardSlot


Signature/prototype:
```java
cardSlot)
```


Parameters:
none


Usage notes:
Donotcallduringthetransaction


Source details:
```text
public List<CAPK> getCAPKDetail(ContantPara.CardSlot
Declare: 
cardSlot)
ReturnallCAPKparamters,seetheCAPK.class
Description: 
Parameters: none
Return: 
Remark: Donotcallduringthetransaction
```

### EMV API / getAIDList

- Source section: `1.22`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Declare: public List<String> getAIDList(ContantPara.CardSlot cardSlot)


Signature/prototype:
```java
public List<String> getAIDList(ContantPara.CardSlot cardSlot)
```


Parameters:
none


Usage notes:
Donotcallduringthetransaction


Source details:
```text
Declare: public List<String> getAIDList(ContantPara.CardSlot cardSlot)
ReturnallAID
Description: 
Parameters: none
Return: 
Remark: Donotcallduringthetransaction
```

### EMV API / getCAPKList

- Source section: `1.23`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Key: RID


Signature/prototype:
```java
getCAPKList(ContantPara.CardSlot cardSlot)
```


Parameters:
Index


Return value:
List


Usage notes:
Donotcallduringthetransaction


Source details:
```text
public List<Hashtable<String,String>>
Declare: 
getCAPKList(ContantPara.CardSlot cardSlot)
ReturnallCAPK
Description: 
Key: RID
Parameters: 
Index
Return: List
Remark: Donotcallduringthetransaction
```

### EMV API / getTlvByTagLists

- Source section: `1.24`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
GettheTLVstring basedontag


Signature/prototype:
```java
public String getTlvByTagLists(List<String>TagList)
```


Parameters:
TagList:All tags


Return value:
TLVdatastring


Usage notes:
none


Source details:
```text
Declare: public String getTlvByTagLists(List<String>TagList)
Description: GettheTLVstring basedontag
Parameters: TagList:All tags
Return: TLVdatastring
Remark: none
```

### EMV API / getEMVLibVers

- Source section: `1.25`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Getemvso version


Signature/prototype:
```java
public String getEMVLibVers(ContantPara.CardSlot cardSlot)
```


Parameters:
none


Return value:
Versionstring


Usage notes:
none


Source details:
```text
Declare: public String getEMVLibVers(ContantPara.CardSlot cardSlot)
Description: Getemvso version
Parameters: none
Return: Versionstring
Remark: none
```

### EMV API / getEMVjarVers

- Source section: `1.26`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Getemvjar version


Signature/prototype:
```java
public String getEMVjarVers()
```


Parameters:
none


Return value:
Versionstring


Usage notes:
none


Source details:
```text
Declare: public String getEMVjarVers()
Description: Getemvjar version
Parameters: none
Return: Versionstring
Remark: none
```

### EMV API / getValByTag

- Source section: `1.27`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Get onecontactor contactlesstagvalue


Signature/prototype:
```java
public String getValByTag(int tag)
```


Parameters:
tag(e.g. 0x57)


Return value:
Returntagvaluewithstring


Usage notes:
none


Simplified example:
```java
String aid = emv.getValByTag(0x84);
```


Source details:
```text
Declare: public String getValByTag(int tag)
Description: Get onecontactor contactlesstagvalue
Parameters: tag(e.g. 0x57)
Return: Returntagvaluewithstring
Remark: none
```

### EMV API / setContext

- Source section: `1.28`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Setcontext,soemvcancallUIandpinpadhandleoffliepin


Signature/prototype:
```java
public void  setContext(Context context)
```


Usage notes:
Useinandroid5.1or6.0


Simplified example:
```java
EmvNfcKernelApi emv = EmvNfcKernelApi.getInstance(context);
emv.setContext(context);
emv.setListener(listener);
```


Source details:
```text
Declare: public void  setContext(Context context)
Description: Setcontext,soemvcancallUIandpinpadhandleoffliepin
Parameters: 
Return: 
Remark: Useinandroid5.1or6.0
```

### EMV API / SetTLV

- Source section: `1.29`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Set onecontacttagdata
tag(e.g. 0x95)


Signature/prototype:
```java
public int  SetTLV(int tag,byte[]value, int valueLen)
```


Parameters:
value: tagvalue hex
valueLen:lengthofvalue
0:success


Return value:
Other:failed


Usage notes:
Onlyusefor contact


Source details:
```text
Declare: public int  SetTLV(int tag,byte[]value, int valueLen)
Description: Set onecontacttagdata
tag(e.g. 0x95)
Parameters: value: tagvalue hex
valueLen:lengthofvalue
0:success
Return: 
Other:failed
Remark: Onlyusefor contact
```

### EMV API / getMstripFlag

- Source section: `1.30`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Note:VISAnotsupportMstripmode


Signature/prototype:
```java
public int  getMstripFlag ()
```


Parameters:
0 Mchip(EMVmode)


Return value:
1 Mstrip(Master/Amex)
4 JCBlegacymode


Usage notes:
8 DiscoverZIPmode


Source details:
```text
Declare: public int  getMstripFlag ()
CheckthecontactlesscardisEMVmodeorMagstripmode
Description: 
Note:VISAnotsupportMstripmode
Parameters: 
0 Mchip(EMVmode)
Return: 
1 Mstrip(Master/Amex)
4 JCBlegacymode
Remark: 
8 DiscoverZIPmode
```

### EMV API / CheckCardIsOut

- Source section: `1.31`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
transaction ，Otherwise,itwillcauseakernelcardreadingconflict,


Signature/prototype:
```java
public boolean  CheckCardIsOut(long ms)
```


Parameters:
milliseconds


Return value:
True:ifcardisremovedduringtimeout False:cardnotremoved
call it at ContantPara.CheckCardResult.NEED_FALLBACK or


Usage notes:
ContantPara.CheckCardResult.NOT_ICC or
ContantPara.TransactionResult.ICC_CARD_REMOVED


Source details:
```text
Declare: public boolean  CheckCardIsOut(long ms)
Check IC card is removed from device , but this api can`t call during
Description: 
transaction ，Otherwise,itwillcauseakernelcardreadingconflict,
Parameters: milliseconds
Return: True:ifcardisremovedduringtimeout False:cardnotremoved
call it at ContantPara.CheckCardResult.NEED_FALLBACK or
Remark: ContantPara.CheckCardResult.NOT_ICC or
ContantPara.TransactionResult.ICC_CARD_REMOVED
```

### EMV API / CheckCardIsOutDuringTrans

- Source section: `1.32`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
thePINpad forcontacttransaction ，Otherwise,itwill causeakernel
cardreadingconflict


Signature/prototype:
```java
public boolean  CheckCardIsOutDuringTrans(long ms)
```


Parameters:
milliseconds


Return value:
True:ifcardisremovedduringtimeout False:cardnotremoved


Usage notes:
UseininputPINflow,whenthecardremoved


Source details:
```text
Declare: public boolean  CheckCardIsOutDuringTrans(long ms)
Check IC card is removed from device , this api can only call during Pop
Description: thePINpad forcontacttransaction ，Otherwise,itwill causeakernel
cardreadingconflict
Parameters: milliseconds
Return: True:ifcardisremovedduringtimeout False:cardnotremoved
Remark: UseininputPINflow,whenthecardremoved
```

### EMV API / GetField55ForSAMA

- Source section: `1.33`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Getfiled55forSAMA


Signature/prototype:
```java
public String GetField55ForSAMA()
```


Parameters:
null


Return value:
TLV


Usage notes:
Callitatgoonlinecallback


Source details:
```text
Declare: public String GetField55ForSAMA()
Description: Getfiled55forSAMA
Parameters: null
Return: TLV
Remark: Callitatgoonlinecallback
```

### EMV API / getDeviceType()

- Source section: `1.34`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
GetDeviceTypeforContactless


Signature/prototype:
```java
public int  getDeviceType()
```


Parameters:
null


Return value:
0-Realcard, other-MobilePhone
Call it at go online callback, If the kernel cannot determine, the default


Usage notes:
realcard


Source details:
```text
Declare: public int  getDeviceType()
Description: GetDeviceTypeforContactless
Parameters: null
Return: 0-Realcard, other-MobilePhone
Call it at go online callback, If the kernel cannot determine, the default
Remark: 
realcard
```

### EMV API / getField55ForJIO()

- Source section: `1.34`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Getfiled55forRelience


Signature/prototype:
```java
public Hashtable<String,String> getField55ForJIO(int KeySetNum)
```


Parameters:
KeySetNum--DUKPTKeySetNum


Return value:
Hashtable: KEY/VALUE:EMVDATA KSN TRACKDATA


Usage notes:
Callitatgoonlinecallback


Source details:
```text
Declare: public Hashtable<String,String> getField55ForJIO(int KeySetNum)
Description: Getfiled55forRelience
Parameters: KeySetNum--DUKPTKeySetNum
Return: Hashtable: KEY/VALUE:EMVDATA KSN TRACKDATA
Remark: Callitatgoonlinecallback
```

### EMV API / GetL1Version

- Source section: `1.35`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
GetL1Versionofdevice


Signature/prototype:
```java
cardSlot)
```


Parameters:
cardSlot-ICCorPICC


Return value:
Hashtable: KEY/VALUE:IFMHardwareSoftwarePCD


Usage notes:
SubjecttotheL1certificate


Source details:
```text
public Hashtable<String,String> GetL1Version(ContantPara.CardSlot
Declare: 
cardSlot)
Description: GetL1Versionofdevice
Parameters: cardSlot-ICCorPICC
Return: Hashtable: KEY/VALUE:IFMHardwareSoftwarePCD
Remark: SubjecttotheL1certificate
```

### EMV API / getNFCLibVers

- Source section: `1.36`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
GetKernelnameandVersion
KernelID - 02-Master 03-Visa 04-Amex 05-JCB 06-Discover 0D-Rupay


Signature/prototype:
```java
public String getNFCLibVers(byteKernelID)
```


Parameters:
2D-PURE


Return value:
NameandVersion


Usage notes:
SubjecttotheL2certificate


Source details:
```text
Declare: public String getNFCLibVers(byteKernelID)
Description: GetKernelnameandVersion
KernelID - 02-Master 03-Visa 04-Amex 05-JCB 06-Discover 0D-Rupay
Parameters: 
2D-PURE
Return: NameandVersion
Remark: SubjecttotheL2certificate
```

### EMV API / updateCAPK

- Source section: `1.37`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/TestEmv.java`



Purpose:
UpdateaCertificateAuthorityPublic Key objectin thedevice.
TheCAPKobjecttobeupdated.Theobjectcontainsthelocation
oftheCAPK in thedevice, theRID, index, modulus,exponent
andthechecksumof theCAPK, all
datainhex stringformat.
cardSlot:
ContantPara.CardSlot.ICC,ContantPara.CardSlot.PICCor
ContantPara.CardSlot.UNKNOWN
Rid:Identify public keyof certification center
Index:Identifypublic keyofcertificationcentertogetherwithRID
Exponent: Public keyexponent


Signature/prototype:
```java
ContantPara.Operationoperation, Hashtable<String,String>
capkParams)
```


Parameters:
Modulus:Module value of public key
Checksum:Toverifypublic keyof certificationcenter
Toremove aCAPK entry at thespecifiedlocation, set withthe
following values:
• RID (5bytes) = "A000000004"
• Index(1byte) = "01"
• Exponent (1byte or 3bytes) = "03"
• Modulus (Nbytes) ="00"
• Size (2bytes) (not use)
• Checksum (20bytes) = SHA-1[RID|| Index|| Modulus ||
Exponent]


Return value:
True-updatesuccess False-updatefailed


Usage notes:
Mandatory: RID&Index&Exponent&Modulus


Simplified example:
```java
Hashtable<String, String> capk = new Hashtable<>();
capk.put("RID", "A000000003");
capk.put("Index", "08");
capk.put("Exponent", "03");
capk.put("Modulus", "...");
capk.put("Checksum", "...");
boolean ok = emv.updateCAPK(ContantPara.Operation.ADD, capk);
```


Source details:
```text
Boolean updateCAPK(ContantPara.CardSlot cardSlot,
Declare: 
ContantPara.Operationoperation, Hashtable<String,String>

capkParams)
Description: UpdateaCertificateAuthorityPublic Key objectin thedevice.
TheCAPKobjecttobeupdated.Theobjectcontainsthelocation
oftheCAPK in thedevice, theRID, index, modulus,exponent
andthechecksumof theCAPK, all
datainhex stringformat.
cardSlot:
ContantPara.CardSlot.ICC,ContantPara.CardSlot.PICCor
ContantPara.CardSlot.UNKNOWN
Rid:Identify public keyof certification center
Index:Identifypublic keyofcertificationcentertogetherwithRID
Exponent: Public keyexponent
Parameters: 
Modulus:Module value of public key
Checksum:Toverifypublic keyof certificationcenter
Toremove aCAPK entry at thespecifiedlocation, set withthe
following values:
• RID (5bytes) = "A000000004"
• Index(1byte) = "01"
• Exponent (1byte or 3bytes) = "03"
• Modulus (Nbytes) ="00"
• Size (2bytes) (not use)
• Checksum (20bytes) = SHA-1[RID|| Index|| Modulus ||
Exponent]
Return: True-updatesuccess False-updatefailed
Remark: Mandatory: RID&Index&Exponent&Modulus
```

### EMV API / updateContactAID_TLV

- Source section: `1.38`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/TestEmv.java`



Purpose:
UpdateContactAIDParameters,UseTLVString

AIDTLV
Tag:
M:
9F06:AID
9F09:TerminalApplicationVersion(eg:0001)
DF11:TAC-Default(eg:0000000000)
DF12:TAC-Online(eg:DC4004F800)
DF13:TAG-Denial(eg:0000000000)
DF14:DefaultDDOL(eg:9F3704)
9F1B:TerminalFloorLimit(eg:00000000)
O:
DF01: Application Select Indicator (eg:00-Partial Match 01-Full
Match)
DF02:TerminalApplicationPriority(eg:00-0F)
DF22:DefaultTDOL(eg:9F0206)
DF15:ThresholdValue(eg:000000002000)
DF16:MaxTargetPercentage(eg:00)
DF17:TargetPercentage(eg:00)
9F01:AcquirerID(eg:303030313131)
9F33:TerminalCapabilities(eg:E0F8C8)
9F1A:TerminalCountryCode(eg:0356)


Signature/prototype:
```java
public boolean updateContactAID_TLV(String TLV)
```


Parameters:
DF23:TerminalFloorLimitCheck(eg:01or00)
5F2A:TransactionCurrencyCode(eg:0156)
5F36:TransactionCurrencyCodeExponent(eg:02)
DF24:ApplicationDefaultLabel(eg:4D617374657243617264)
9F16:MerchantID(eg:123456789012345123456789012345)
9F1C:TerminalID(eg:3132333435363738)
9F15:MerchantCategoryCode(eg:1122)
9F40:AdditionalTerminalCapabilities(eg:F000F0A001)
9F35:TerminalType(eg:22)
Note:M---Mandatory O--Optional
Eg:
9F0607A00000000410109F09020002DF010100DF020100DF1105000
0000000DF13050010000000DF1205DC4004F800DF22039F0206DF1
4039F3704DF1506000000002000DF170100DF1601009F1B0400000
0009F01063030303131319F3303E0F8C89F1A020682DF230101
Funs.Find_TLV
Funs.AddChild_TLV
Funs.New_TLV
Funs.Remove_TLV
YoucanusetheseApistoconstructTLV


Return value:
truefalse


Usage notes:
Payattentiontothedatalengthandformat


Source details:
```text
Declare: public boolean updateContactAID_TLV(String TLV)
Description: UpdateContactAIDParameters,UseTLVString

AIDTLV
Tag:
M:
9F06:AID
9F09:TerminalApplicationVersion(eg:0001)
DF11:TAC-Default(eg:0000000000)
DF12:TAC-Online(eg:DC4004F800)
DF13:TAG-Denial(eg:0000000000)
DF14:DefaultDDOL(eg:9F3704)
9F1B:TerminalFloorLimit(eg:00000000)
O:
DF01: Application Select Indicator (eg:00-Partial Match 01-Full
Match)
DF02:TerminalApplicationPriority(eg:00-0F)
DF22:DefaultTDOL(eg:9F0206)
DF15:ThresholdValue(eg:000000002000)
DF16:MaxTargetPercentage(eg:00)
DF17:TargetPercentage(eg:00)
9F01:AcquirerID(eg:303030313131)
9F33:TerminalCapabilities(eg:E0F8C8)
9F1A:TerminalCountryCode(eg:0356)
Parameters: 
DF23:TerminalFloorLimitCheck(eg:01or00)
5F2A:TransactionCurrencyCode(eg:0156)
5F36:TransactionCurrencyCodeExponent(eg:02)
DF24:ApplicationDefaultLabel(eg:4D617374657243617264)
9F16:MerchantID(eg:123456789012345123456789012345)
9F1C:TerminalID(eg:3132333435363738)
9F15:MerchantCategoryCode(eg:1122)
9F40:AdditionalTerminalCapabilities(eg:F000F0A001)
9F35:TerminalType(eg:22)
Note:M---Mandatory O--Optional
Eg:
9F0607A00000000410109F09020002DF010100DF020100DF1105000
0000000DF13050010000000DF1205DC4004F800DF22039F0206DF1
4039F3704DF1506000000002000DF170100DF1601009F1B0400000
0009F01063030303131319F3303E0F8C89F1A020682DF230101
Funs.Find_TLV
Funs.AddChild_TLV
Funs.New_TLV
Funs.Remove_TLV
YoucanusetheseApistoconstructTLV
Return: truefalse
Remark: Payattentiontothedatalengthandformat
```

### EMV API / updateContactlessAID_TLV

- Source section: `1.39`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/TestEmv.java`



Purpose:
UpdateContactlessAIDParameters,UseTLVString

AIDTLV
Note:M---Mandatory O--Optional
Tag:
M:
9F06: ApplicationIdentifier
ContantPara.NfcCardType.MasterCard:
DF8120:TerminalActionCodesDefault
DF8121:TerminalActionCodesDenial
DF8122:TerminalActionCodesOnLine
DF8123：FloorLimit
DF8124:NoOnDeviceCVM
DF8125:OnDeviceCVM
DF8126:CVMRequiredLimit
DF811A:DefaultUDOL
9F1D:TerminalRiskManagement
DF811B:KernelConfiguration
DF8117:CardDataInputCapability
DF8118:CVMCapabilityPerCVMRequired
DF8119: CVMCapabilityNoCVMRequired
DF811E:MagStripeCVMCapabilityCVMRequired


Signature/prototype:
```java
nfcCardType,String TLV)
```


Parameters:
DF811F:SecurityCapability
DF812C:MagStripeCVMCapabilityPerNoCVMRequired
Eg:
9F0607A00000000410109F09020002DF81170160DF81180160DF811
90108DF811A039F6A04DF811B0130DF810C01029F6D020001DF81
1E0110DF812C0100DF812306000000000000DF8124060000500000
00DF812506000050000000DF812606000000030000DF811F0108DF
812205FC50BCF800DF8121050000000000DF812005FC50BC80009
F1D086C7A000000000000
ContantPara.NfcCardType.VisaCard:
9F66:TerminalTransactionQualifiers
9F92810D:TransactionLimit
9F92810E:CvmRequiredLimit
9F92810F:FloorLimit
9F92810A:LimitSwitch
9F928102:ProRestrictionDisable
Eg:
9F0607A00000000310109F6604360040009F92810D0699999999999
99F92810F060000000000009F92810E060000005000009F92810A02
FE009F1B0400000000


Return value:
truefalse


Usage notes:
Payattentiontothedatalengthandformat


Source details:
```text
public boolean updateContactlessAID_TLV(ContantPara.NfcCardType
Declare: 
nfcCardType,String TLV)
Description: UpdateContactlessAIDParameters,UseTLVString

AIDTLV
Note:M---Mandatory O--Optional
Tag:
M:
9F06: ApplicationIdentifier
ContantPara.NfcCardType.MasterCard:
DF8120:TerminalActionCodesDefault
DF8121:TerminalActionCodesDenial
DF8122:TerminalActionCodesOnLine
DF8123：FloorLimit
DF8124:NoOnDeviceCVM
DF8125:OnDeviceCVM
DF8126:CVMRequiredLimit
DF811A:DefaultUDOL
9F1D:TerminalRiskManagement
DF811B:KernelConfiguration
DF8117:CardDataInputCapability
DF8118:CVMCapabilityPerCVMRequired
DF8119: CVMCapabilityNoCVMRequired
DF811E:MagStripeCVMCapabilityCVMRequired
Parameters: 
DF811F:SecurityCapability
DF812C:MagStripeCVMCapabilityPerNoCVMRequired
Eg:
9F0607A00000000410109F09020002DF81170160DF81180160DF811
90108DF811A039F6A04DF811B0130DF810C01029F6D020001DF81
1E0110DF812C0100DF812306000000000000DF8124060000500000
00DF812506000050000000DF812606000000030000DF811F0108DF
812205FC50BCF800DF8121050000000000DF812005FC50BC80009
F1D086C7A000000000000
ContantPara.NfcCardType.VisaCard:
9F66:TerminalTransactionQualifiers
9F92810D:TransactionLimit
9F92810E:CvmRequiredLimit
9F92810F:FloorLimit
9F92810A:LimitSwitch
9F928102:ProRestrictionDisable
Eg:
9F0607A00000000310109F6604360040009F92810D0699999999999
99F92810F060000000000009F92810E060000005000009F92810A02
FE009F1B0400000000

Return: truefalse
Remark: Payattentiontothedatalengthandformat
```

### EMV API / updateCAPK_TLV

- Source section: `1.40`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
UpdateCAPKParameters,UseTLVString
cardSlot:ICC,PICC,UNKNOWN
ICC-contact
PICC-contactless
UNKNOWN-contactandcontactless
Tag:
M:
9F06:RID
9F22:Index
DF02:Modulus
DF04:Exponent
O:
DF03:CheckSum
DF05:ExpiredDate
DF07:AlgorithmIndicator
Note:M---Mandatory O--Optional


Signature/prototype:
```java
cardSlot,String TLV)
```


Parameters:
Eg:
9F0605A0000000039F220108DF040103DF050420401231DF0281B0
D9FD6ED75D51D0E30664BD157023EAA1FFA871E4DA65672B863
D255E81E137A51DE4F72BCC9E44ACE12127F87E263D3AF9DD9C
F35CA4A7B01E907000BA85D24954C2FCA3074825DDD4C0C8F186
CB020F683E02F2DEAD3969133F06F7845166ACEB57CA0FC26034
45469811D293BFEFBAFAB57631B3DD91E796BF850A25012F1AE3
8F05AA5C4D6D03B1DC2E568612785938BBC9B3CD3A910C1DA55
A5A9218ACE0F7A21287752682F15832A678D6E1ED0BDF03130000
0000000000000000000000000000000000
Funs.Find_TLV
Funs.AddChild_TLV
Funs.New_TLV
Funs.Remove_TLV
YoucanusetheseApistoconstructTLV


Return value:
truefalse


Usage notes:
Payattentiontothedatalengthandformat


Source details:
```text
Public boolean updateCAPK_TLV(ContantPara.CardSlot
Declare: cardSlot,String TLV)
Description: UpdateCAPKParameters,UseTLVString
cardSlot:ICC,PICC,UNKNOWN
ICC-contact
PICC-contactless
UNKNOWN-contactandcontactless
Tag:
M:
9F06:RID
9F22:Index
DF02:Modulus
DF04:Exponent
O:
DF03:CheckSum
DF05:ExpiredDate
DF07:AlgorithmIndicator
Note:M---Mandatory O--Optional
Parameters: 
Eg:
9F0605A0000000039F220108DF040103DF050420401231DF0281B0
D9FD6ED75D51D0E30664BD157023EAA1FFA871E4DA65672B863
D255E81E137A51DE4F72BCC9E44ACE12127F87E263D3AF9DD9C
F35CA4A7B01E907000BA85D24954C2FCA3074825DDD4C0C8F186
CB020F683E02F2DEAD3969133F06F7845166ACEB57CA0FC26034
45469811D293BFEFBAFAB57631B3DD91E796BF850A25012F1AE3
8F05AA5C4D6D03B1DC2E568612785938BBC9B3CD3A910C1DA55
A5A9218ACE0F7A21287752682F15832A678D6E1ED0BDF03130000
Funs.Find_TLV
Funs.AddChild_TLV
Funs.New_TLV
Funs.Remove_TLV
YoucanusetheseApistoconstructTLV

Return: truefalse
Remark: Payattentiontothedatalengthandformat
```

### EMV API / exportLogFilesToExternalStorage

- Source section: `1.41`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Exportlogfilesfrominternalstoragetoexternalstorage


Signature/prototype:
```java
context)
```


Parameters:
context:usedtogetinternalstoragepath


Return value:
truefalse


Usage notes:
usedtogetinternalstoragepath


Source details:
```text
public static boolean exportLogFilesToExternalStorage(Context
Declare: 
context)
Description: Exportlogfilesfrominternalstoragetoexternalstorage
Parameters: context:usedtogetinternalstoragepath
Return: truefalse
Remark: usedtogetinternalstoragepath
```

### EMV API / getNfcCvmCode

- Source section: `1.42`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
GetstheCVMcodefromthecurrentNFCtransaction


Signature/prototype:
```java
public int  getNfcCvmCode()
```


Parameters:
null
Anintegercodeindicatingtherequiredverificationmethod.
 0x00-Noverificationrequired(NOCVM)
 0x10-Signatureverificationrequired(SIGNATURE)


Return value:
 0x20-OnlinePINverificationrequired(ONLINEPIN)
 0x30-Confirmationcodehasbeenverified(CONFIRMATION
CODEVERIFIED)
 0xF0-NotApplicable(N/A)


Usage notes:
None


Source details:
```text
Declare: public int  getNfcCvmCode()
Description: GetstheCVMcodefromthecurrentNFCtransaction
Parameters: null
Anintegercodeindicatingtherequiredverificationmethod.
 0x00-Noverificationrequired(NOCVM)
 0x10-Signatureverificationrequired(SIGNATURE)
Return:  0x20-OnlinePINverificationrequired(ONLINEPIN)
 0x30-Confirmationcodehasbeenverified(CONFIRMATION
CODEVERIFIED)
 0xF0-NotApplicable(N/A)
Remark: None
```

### EMV API / getODAstatus

- Source section: `1.43`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
GetstheODAresultofthetransaction


Signature/prototype:
```java
publicbyte getODAstatus()
```


Parameters:
null
 0x01 SDAFail
 0x02 DDAFail
 0x03 CDAFail
 0x04 FDDAFail


Return value:
 0x81 SDASUCESS
 0x82 DDASUCESS
 0x83 CDASUCESS
 0x84 FDDASUCESS


Usage notes:
onlyusedformastercardandvisacard


Source details:
```text
Declare: publicbyte getODAstatus()
Description: GetstheODAresultofthetransaction
Parameters: null
 0x01 SDAFail
 0x02 DDAFail
 0x03 CDAFail
 0x04 FDDAFail
Return: 
 0x81 SDASUCESS
 0x82 DDASUCESS
 0x83 CDASUCESS
 0x84 FDDASUCESS

Remark: onlyusedformastercardandvisacard
```

### EMV API / setApproveRespcodeList

- Source section: `1.44`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
transactionfromthehost.
respcodeList：AuthorisationResponseCodeList,Separatewith'|'.such


Signature/prototype:
```java
public boolean setApproveRespcodeList(StringrespcodeList)
```


Parameters:
as"00|10|11"


Return value:
trueorfalse


Usage notes:
onlyusedforcontacttrade.
2.Callback function


Source details:
```text
Declare: public boolean setApproveRespcodeList(StringrespcodeList)
SettheAuthorizationResponseCodelistthatindicatesanapproved
Description: 
transactionfromthehost.
respcodeList：AuthorisationResponseCodeList,Separatewith'|'.such
Parameters: 
as"00|10|11"
Return: trueorfalse
Remark: onlyusedforcontacttrade.
2.Callback function
```

### EMV Callback / onRequestSetAmount

- Source section: `2.01`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
atstartEmv,thiscallback will not betriggered.Tocancelthe
process,call cancelSetAmount.


Signature/prototype:
```java
voidonRequestSetAmount()
```


Return value:
None


Simplified example:
```java
public void onRequestSetAmount() {
    emv.setAmountEx(100L, 0L);
}
```


Source details:
```text
Declare: voidonRequestSetAmount()
Requestinput of amountin theEMV process.If amountis input
Description: atstartEmv,thiscallback will not betriggered.Tocancelthe
process,call cancelSetAmount.
Return: None
```

### EMV Callback / onRequestConfirmCardno

- Source section: `2.02`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Confirm theCardnumber,shouldcallsendConfirmCardnoResult(true)


Signature/prototype:
```java
voidonRequestConfirmCardno()
```


Simplified example:
```java
public void onRequestConfirmCardno() {
    emv.sendConfirmCardnoResult(true);
}
```


Source details:
```text
Declare: voidonRequestConfirmCardno()
Description: 
Confirm theCardnumber,shouldcallsendConfirmCardnoResult(true)
Return:
```

### EMV Callback / onReturnCheckCardResult

- Source section: `2.03`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
card.

•Seeenum CheckCardResult
Key: StripInfo


Signature/prototype:
```java
checkCardResult, Hashtable<String,String>
decodeData)
Returntheresultsinresponsetoaswiped, insertedor tapped
```


Return value:
CardNo


Source details:
```text
voidonReturnCheckCardResult(CheckCardResult
Declare: checkCardResult, Hashtable<String,String>
decodeData)
Returntheresultsinresponsetoaswiped, insertedor tapped
Description: 
card.

•Seeenum CheckCardResult
Key: StripInfo
Return: CardNo
```

### EMV Callback / onRequestSelectApplication

- Source section: `2.04`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
canceltheprocess, call cancelSelectApplication.
Returnalist of applications supportedbythemPOSdevice and


Signature/prototype:
```java
voidonRequestSelectApplication(ArrayList<String> appList)
```


Return value:
theEMV chipcard.Theapplications aresortedbypriority,where
index0 isthehighestpriority.


Simplified example:
```java
public void onRequestSelectApplication(ArrayList<String> apps) {
    emv.selectApplication(0);
}
```


Source details:
```text
Declare: voidonRequestSelectApplication(ArrayList<String> appList)
Requestselection of anapplication from thereturnedlist.To
Description: 
canceltheprocess, call cancelSelectApplication.
Returnalist of applications supportedbythemPOSdevice and
Return: theEMV chipcard.Theapplications aresortedbypriority,where
index0 isthehighestpriority.
```

### EMV Callback / onRequestPinEntry

- Source section: `2.05`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
theprocess, call cancelPinEntry.This callbackcan betriggered
bystartKernel.
IndicatethePINentrysource, seePinEntrySource


Signature/prototype:
```java
voidonRequestPinEntry(PinEntrySource pinEntrySource)
```


Return value:
enumeration.


Simplified example:
```java
public void onRequestPinEntry(ContantPara.PinEntrySource source) {
    emv.sendPinEntry();
}
```


Source details:
```text
Declare: voidonRequestPinEntry(PinEntrySource pinEntrySource)
RequestPIN entryfrom thespecifiedPinEntrySource.Tocancel
Description: theprocess, call cancelPinEntry.This callbackcan betriggered
bystartKernel.
IndicatethePINentrysource, seePinEntrySource
Return: 
enumeration.
```

### EMV Callback / onRequestFinalConfirm

- Source section: `2.06`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
thefirst generateAC command.Toproceedor cancelthe
transaction, call sendFinalConfirmResult.


Signature/prototype:
```java
voidonRequestFinalConfirm()
```


Return value:
None


Source details:
```text
Declare: voidonRequestFinalConfirm()
TheEMV processrequest fora finalconfirmation beforecalling
Description: thefirst generateAC command.Toproceedor cancelthe
transaction, call sendFinalConfirmResult.
Return: None
```

### EMV Callback / onRequestOnlineProcess

- Source section: `2.07`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
transaction, call sendOnlineProcessResult.

Aset of transaction datainTag-Length-Valueformatthat should
besent backtoonline server.


Signature/prototype:
```java
voidonRequestOnlineProcess(String tlv,StringdataKsn)
```


Return value:
Tlv:TLVdata
dataKsn:KSN fordataencryption


Simplified example:
```java
public void onRequestOnlineProcess(String cardTlvData, String dataKsn) {
    // Send cardTlvData as field 55, then return issuer TLV.
    emv.sendOnlineProcessResult("8A023030");
}
```


Source details:
```text
Declare: voidonRequestOnlineProcess(String tlv,StringdataKsn)
Requestthetransactiongo online.Toproceedor terminatethe
Description: 
transaction, call sendOnlineProcessResult.

Aset of transaction datainTag-Length-Valueformatthat should
besent backtoonline server.
Return: 
Tlv:TLVdata
dataKsn:KSN fordataencryption
```

### EMV Callback / onReturnBatchData

- Source section: `2.08`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Returnthebatchdataafter completionof an EMV transaction.
Aset of transaction datainTag-Length-Valueformatthat should


Signature/prototype:
```java
voidonReturnBatchData(String tlv)
```


Return value:
besent backtoserver forprocessing.(Usein offlineapprove)


Source details:
```text
Declare: voidonReturnBatchData(String tlv)
Description: Returnthebatchdataafter completionof an EMV transaction.
Aset of transaction datainTag-Length-Valueformatthat should
Return: 
besent backtoserver forprocessing.(Usein offlineapprove)
```

### EMV Callback / onReturnTransactionResult

- Source section: `2.09`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Returnthetransactionresult.
Indicatethetransactionresult, seeTransactionResult


Signature/prototype:
```java
voidonReturnTransactionResult(TransactionResult transResult)
```


Return value:
enumeration.


Simplified example:
```java
public void onReturnTransactionResult(ContantPara.TransactionResult result) {
    if (result == ContantPara.TransactionResult.ONLINE_APPROVAL ||
        result == ContantPara.TransactionResult.OFFLINE_APPROVAL) {
        // complete approved transaction
    } else {
        // decline, reverse, or show failure according to business rules
    }
}
```


Source details:
```text
Declare: voidonReturnTransactionResult(TransactionResult transResult)
Description: Returnthetransactionresult.
Indicatethetransactionresult, seeTransactionResult
Return: 
enumeration.
```

### EMV Callback / onRequestDisplayText

- Source section: `2.10`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Returnthemessage toprompt customer.
Indicatethemessageto bedisplayed, seeDisplayText


Signature/prototype:
```java
voidonRequestDisplayText(DisplayText displayText)
```


Return value:
enumeration.


Source details:
```text
Declare: voidonRequestDisplayText(DisplayText displayText)
Description: Returnthemessage toprompt customer.
Indicatethemessageto bedisplayed, seeDisplayText
Return: 
enumeration.
```

### EMV Callback / onRequestOfflinePinEntry

- Source section: `2.11`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Usuallynotuse


Signature/prototype:
```java
pinEntrySource,int PinTryCount);
```


Source details:
```text
voidonRequestOfflinePinEntry(ContantPara.PinEntrySource
Declare: 
pinEntrySource,int PinTryCount);
Description: Usuallynotuse
Return:
```

### EMV Callback / onRequestOfflinePINVerify

- Source section: `2.12`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Offlinepinverify(use inAndroidos8.0)


Signature/prototype:
```java
pinEntrySource,int pinEntryType, Bundlebundle);
```


Parameters:
pinEntrySource-seeeuam ContantPara.PinEntrySource

pinEntryType:0-----Offline PlainPin1--- OfflineencryptionPin
bundle:key: ModuleLen


Return value:
Module
ExponentLen
Exponent


Usage notes:
Pleasecheckthesampledemoapp


Simplified example:
```java
public void onRequestOfflinePINVerify(ContantPara.PinEntrySource source, int type, Bundle data) {
    int retries = emv.getOfflinePinTryTimes();
    // collect offline PIN, then call sendOfflinePINVerifyResult(result) when required
}
```


Source details:
```text
voidonRequestOfflinePINVerify(ContantPara.PinEntrySource
Declare: 
pinEntrySource,int pinEntryType, Bundlebundle);
Description: Offlinepinverify(use inAndroidos8.0)
Parameters: pinEntrySource-seeeuam ContantPara.PinEntrySource

pinEntryType:0-----Offline PlainPin1--- OfflineencryptionPin
bundle:key: ModuleLen
Return: Module
ExponentLen
Exponent
Remark: Pleasecheckthesampledemoapp
```

### EMV Callback / onReturnIssuerScriptResult

- Source section: `2.13`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Tlv:-field55data


Signature/prototype:
```java
onReturnIssuerScriptResult(ContantPara.IssuerScriptResult
scriptResult,String tlv)
ReturnScriptResult,tagDF31isscarptresult
```


Return value:
none


Source details:
```text
void
Declare: onReturnIssuerScriptResult(ContantPara.IssuerScriptResult
scriptResult,String tlv)
ReturnScriptResult,tagDF31isscarptresult
Description: 
Tlv:-field55data
Return: none
```

### EMV Callback / onNFCrequestTipsConfirm

- Source section: `2.14`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
messageID:checkenumNfcTipMessageID
msg:
PLS_REMOVE_CARD,
PLS_USE_CONTACT_IC_CARD,//1
NEED_SIGNATURE,//2
END_APPLICATION,//3
SEE_PHONE_REMOVE_AND_PRESENT_CARD,//4
DISPLAY_BALANCE,//5


Signature/prototype:
```java
messageID,Stringmsg);
```


Parameters:
CARD_READ_OK,//6
PLS_SECOND_TAP_CARD,//7
APPLICATION_BLOCKED,//8
TRY_AGAIN_RESENT_CARD,//9
USE_MAG_STRIPE,//10
INSERT_SWIPE_OR_TRY_ANOTHER_CARD,//11
TERMINATE,
CARD_ERROR,
PROCESSING_ERROR,
UNKNOW


Usage notes:
CalledatprocessPayPassKernel


Source details:
```text
void onNFCrequestTipsConfirm(ContantPara.NfcTipMessageID
Declare: 
messageID,Stringmsg);
Requestapplicationdisplaymassage.
Description: 
messageID:checkenumNfcTipMessageID
msg:
PLS_REMOVE_CARD,
PLS_USE_CONTACT_IC_CARD,//1
NEED_SIGNATURE,//2
END_APPLICATION,//3
SEE_PHONE_REMOVE_AND_PRESENT_CARD,//4
DISPLAY_BALANCE,//5
Parameters: CARD_READ_OK,//6
PLS_SECOND_TAP_CARD,//7
APPLICATION_BLOCKED,//8
TRY_AGAIN_RESENT_CARD,//9
USE_MAG_STRIPE,//10
INSERT_SWIPE_OR_TRY_ANOTHER_CARD,//11
TERMINATE,
CARD_ERROR,
PROCESSING_ERROR,
UNKNOW

Remark: CalledatprocessPayPassKernel
```

### EMV Callback / onNFCrequestOnline

- Source section: `2.15`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Whentransactionneedgoonline
Hashtable:
"KSN" :customformatksn,usuallynotuse


Signature/prototype:
```java
void onNFCrequestOnline (Hashtable<String,String>hashtable);
```


Parameters:
"TRACKDATA" :customformatdata,usuallynotuse
"EMVDATA":contactlesstransactionTLVdata


Return value:
If0x5A(PAN)isnotreturned,pleaseParsingtag0x57togetPAN,if0x57


Usage notes:
is not returned, please check whether it's a mstrip card. For mastercard
mstriptheTrack2EquivalentDatawillbesaveattag0x9f6b


Source details:
```text
Declare: void onNFCrequestOnline (Hashtable<String,String>hashtable);
Description: Whentransactionneedgoonline
Hashtable:
"KSN" :customformatksn,usuallynotuse
Parameters: "TRACKDATA" :customformatdata,usuallynotuse
"EMVDATA":contactlesstransactionTLVdata
Return: 
If0x5A(PAN)isnotreturned,pleaseParsingtag0x57togetPAN,if0x57
Remark: is not returned, please check whether it's a mstrip card. For mastercard
mstriptheTrack2EquivalentDatawillbesaveattag0x9f6b
```

### EMV Callback / onNFCrequestImportPin

- Source section: `2.16`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
ParametersNotusednow
Callthepasswordkeyboardinthiscallbackfunction,youmayneedtoget
thecardnumber


Signature/prototype:
```java
voidonNFCrequestImportPin (int type, int lasttimeFlag, Stringamt);
```


Parameters:
You can get the card number through tag5A, if tag5A is empty, you can
getthecardnumberthroughtag57


Source details:
```text
Declare: voidonNFCrequestImportPin (int type, int lasttimeFlag, Stringamt);
ContactlessrequestimportPIN
Description: 
ParametersNotusednow
Callthepasswordkeyboardinthiscallbackfunction,youmayneedtoget
thecardnumber
Parameters: 
You can get the card number through tag5A, if tag5A is empty, you can
getthecardnumberthroughtag57
Return: 
Remark:
```

### EMV Callback / onNFCTransResult

- Source section: `2.17`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Returnthetransactionresulttoapplication

Result:
CheckenumNfcTransResult
ONLINE_APPROVAL,
DECLINE_ONLINE,
TERMINATE,
OFFLINE_APPROVAL,


Signature/prototype:
```java
voidonNFCTransResult(ContantPara.NfcTransResultresult);
```


Parameters:
OTHER_INTERFACES,
RETRY,
CARD_REMOVED,
ISSUER_SCRIPT_UPDATE_SUCCESSFUL,//8
ISSUER_SCRIPT_UPDATE_FAILED,//9
DECLINE_OFFLINE
None


Return value:
If the result is OFFLINE_APPROVAL, please call getNfcCvmCode() to
get the CVM (Cardholder Verification Method) code. Then, If the return


Usage notes:
value of the CVM code is 0x10 (indicating signature required), then
requireasignaturewhenprintingtheslip.


Source details:
```text
Declare: voidonNFCTransResult(ContantPara.NfcTransResultresult);
Description: Returnthetransactionresulttoapplication

Result:
CheckenumNfcTransResult
ONLINE_APPROVAL,
DECLINE_ONLINE,
TERMINATE,
OFFLINE_APPROVAL,
Parameters: 
OTHER_INTERFACES,
RETRY,
CARD_REMOVED,
ISSUER_SCRIPT_UPDATE_SUCCESSFUL,//8
ISSUER_SCRIPT_UPDATE_FAILED,//9
DECLINE_OFFLINE
None
Return: 
If the result is OFFLINE_APPROVAL, please call getNfcCvmCode() to
get the CVM (Cardholder Verification Method) code. Then, If the return
Remark: 
value of the CVM code is 0x10 (indicating signature required), then
requireasignaturewhenprintingtheslip.
```

### EMV Callback / onNFCErrorInfor

- Source section: `2.18`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
Contactlesstransactionerrorcallbackfunction
errID:checkenum NfcErrMessageID
strErrInfo:
ERR_LOAD_CALLBACK,
ICS_PARAM_NOT_FIND,
KERNEL_ERR,


Signature/prototype:
```java
strErrInfo);
```


Parameters:
ERR_PIN_LENTH,
ERR_MULT_CARD,
ERR_CHECK_CARD,
AID_PARAM_NOT_FIND,
CAPK_PARAM_NOT_FIND,
GET_KERNEL_DATA_FAILED,
QPBOC_FDDA_FAILED,
PURE_ELE_CASH_CARD_NOT_ALLOW_ONLINE_TRANS,


Source details:
```text
void onNFCErrorInfor (ContantPara.NfcErrMessageID errID, String
Declare: 
strErrInfo);
Description: Contactlesstransactionerrorcallbackfunction
errID:checkenum NfcErrMessageID
strErrInfo:
ERR_LOAD_CALLBACK,
ICS_PARAM_NOT_FIND,
KERNEL_ERR,
Parameters: ERR_PIN_LENTH,
ERR_MULT_CARD,
ERR_CHECK_CARD,
AID_PARAM_NOT_FIND,
CAPK_PARAM_NOT_FIND,
GET_KERNEL_DATA_FAILED,
QPBOC_FDDA_FAILED,
PURE_ELE_CASH_CARD_NOT_ALLOW_ONLINE_TRANS,
Return: 

Remark:
```

### EMV Callback / onReturnNfcCardData

- Source section: `2.19`

- Package/class path: `com.urovo.i9000s.api.emv.EmvNfcKernelApi` / `com.urovo.i9000s.api.emv.EmvListener`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/emv/EmvActivityNew.java`



Purpose:
ReturncontactlesstransactionTLVdata
Hashtable:
"KSN" :customformatksn,usuallynotuse


Signature/prototype:
```java
public void onReturnNfcCardData(Hashtable<String,String>hashtable)
```


Parameters:
"TRACKDATA" :customformatdata,usuallynotuse
"EMVDATA":contactlesstransactionTLVdata
"QPBOCTYPE":ifnotsupportQPBOC,itisNULL
If0x5A(PAN)isnotreturned,pleaseParsingtag0x57togetPAN,if0x57


Usage notes:
is not returned, please check whether it's a mstrip card. For mastercard
mstriptheTrack2EquivalentDatawillbesaveattag0x9F6B
3.EMV Enum


Source details:
```text
Declare: public void onReturnNfcCardData(Hashtable<String,String>hashtable)
Description: ReturncontactlesstransactionTLVdata
Hashtable:
"KSN" :customformatksn,usuallynotuse
Parameters: 
"TRACKDATA" :customformatdata,usuallynotuse
"EMVDATA":contactlesstransactionTLVdata
"QPBOCTYPE":ifnotsupportQPBOC,itisNULL
If0x5A(PAN)isnotreturned,pleaseParsingtag0x57togetPAN,if0x57
Remark: is not returned, please check whether it's a mstrip card. For mastercard
mstriptheTrack2EquivalentDatawillbesaveattag0x9F6B
3.EMV Enum
```

### EMV Enum / EmvOption

- Source section: `3.01`



Purpose:
public static enum EmvOption


Source details:
```text
public static enum EmvOption
{
START, START_WITH_FORCE_ONLINE
}
```

### EMV Enum / CheckCardMode

- Source section: `3.02`



Purpose:
public static enum CheckCardMode{


Source details:
```text
public static enum CheckCardMode{
SWIPE, INSERT, TAP, SWIPE_OR_INSERT, SWIPE_OR_TAP,
INSERT_OR_TAP,
SWIPE_OR_INSERT_OR_TAP,
}
```

### EMV Enum / CheckCardResult

- Source section: `3.03`



Purpose:
public static enum CheckCardResult{


Source details:
```text
public static enum CheckCardResult{
NO_CARD INSERTED_CARD NOT_ICC BAD_SWIPE MSR USE_ICC_CARD
, , , , , ,
TAP_CARD_DETECTED NEED_FALLBACK TIMEOUT CANCEL DEVICE_BUSY
, , , ,
}
Checkcard result Result description
Nocardhasbeendetected,pleasecheck
NO_CARD
CheckCardMode
INSERTED_CARD ICCcardhasbeeninserted
NOT_ICC ICCcardreading failed,pleasetryto reinsertcard
Magneticstripecardreading failed,pleasetryto
BAD_SWIPE
re-swipethecard
MSR Magneticstripecardreading success
USE_ICC_CARD Pleaseinsert ICCcard
TAP_CARD_DETECTED Tapcardhasbeendetected
NEED_FALLBACK PleaseTaporSwipeCard
TIMEOUT Timeout,pleasetryto readcard again
Cardreadinghasbeencanceled,pleasetrytoreadcard
CANCEL
again
DEVICE_BUSY Cardreaderisbusy, pleasetryto read cardagain later
MULT_CARD Multiplecardsdetected ,Please taponecard
```

### EMV Enum / TransactionResult

- Source section: `3.04`



Purpose:
public static enum TransactionResult


Source details:
```text
public static enum TransactionResult
{
OFFLINE_APPROVAL,ONLINE_APPROVAL, TERMINATED,
OFFLINE_DECLINED,ONLINE_DECLINED, CANCELED, CANCELED_OR_TIMEOUT,
CARD_BLOCKED_APP_FAIL, NO_EMV_APPS, ICC_CARD_REMOVED,
SELECT_APP_FAIL, INVALID_ICC_DATA,
APPLICATION_BLOCKED_APP_FAIL
}
Transaction result Result description
OFFLINE_APPROVAL Offlinerequest hasbeen approved

ONLINE_APPROVAL Onlinerequesthasbeenapproved
TERMINATED Transactionhasbeenterminated
OFFLINE_DECLINED Offlinerequest hasbeen rejected
ONLINE_DECLINED Onlinerequesthasbeenrejected
CANCELED Transactionhasbeencanceled
CANCELED_OR_TIMEOUT Transactionhasbeencanceled ortimeout
CARD_BLOCKED_APP_FAIL Cardhasbeenblocked
NO_EMV_APPS Terminaldoesnot supportapplicationsofcard
ICC_CARD_REMOVED ICCcardhasbeenremoved
SELECT_APP_FAIL Terminalhasbeenfailto selectapplication ofcard
INVALID_ICC_DATA ICCcarddataisinvalid
APPLICATION_BLOCKED_APP_FAIL Applicationhasbeenblocked
```

### EMV Enum / DisplayText

- Source section: `3.05`



Purpose:
public static enum DisplayText


Source details:
```text
public static enum DisplayText
{
USE_MAG_STRIPE,
APPROVED_PLEASE_SIGN,//if CVM is Signature
}
```

### EMV Enum / PinEntryResult

- Source section: `3.06`



Purpose:
public static enum PinEntryResult{


Source details:
```text
public static enum PinEntryResult{
ENTERED, CANCEL, TIMEOUT, BYPASS, WRONG_PIN_LENGTH,
INCORRECT_PIN
}
```

### EMV Enum / PinEntrySource

- Source section: `3.07`



Purpose:
public static enum PinEntrySource{


Source details:
```text
public static enum PinEntrySource{
PHONE, KEYPAD
}
```

### EMV Enum / NfcTransResult

- Source section: `3.08`



Purpose:
public static enum NfcTransResult{


Source details:
```text
public static enum NfcTransResult{
ONLINE_APPROVAL,
DECLINE_ONLINE,
TERMINATE,
OFFLINE_APPROVAL,
OTHER_INTERFACES,
RETRY,
CARD_REMOVED,
ISSUER_SCRIPT_UPDATE_SUCCESSFUL, //8
ISSUER_SCRIPT_UPDATE_FAILED, //9
DECLINE_OFFLINE}
```

### EMV Enum / NfcTipMessageID

- Source section: `3.09`



Purpose:
publicstaticenumNfcTipMessageID


Source details:
```text
publicstaticenumNfcTipMessageID
{
PLS_REMOVE_CARD,
PLS_USE_CONTACT_IC_CARD,//1
NEED_SIGNATURE,//2
END_APPLICATION,//3
SEE_PHONE_REMOVE_AND_PRESENT_CARD,//4
DISPLAY_BALANCE,//5
CARD_READ_OK,//6
PLS_SECOND_TAP_CARD,//7
APPLICATION_BLOCKED,//8
TRY_AGAIN_RESENT_CARD,//9
USE_MAG_STRIPE,//10
INSERT_SWIPE_OR_TRY_ANOTHER_CARD,//11
TERMINATE,
CARD_ERROR,
PROCESSING_ERROR,
UNKNOW
}
```

### EMV Enum / NfcErrMessageID

- Source section: `3.10`



Purpose:
publicstaticenumNfcErrMessageID


Source details:
```text
publicstaticenumNfcErrMessageID
{
ERR_LOAD_CALLBACK,//0
ICS_PARAM_NOT_FIND,//needupdateterminalparamters
KERNEL_ERR,
ERR_PIN_LENTH,
ERR_MULT_CARD,
ERR_CHECK_CARD,
AID_PARAM_NOT_FIND,
CAPK_PARAM_NOT_FIND,
GET_KERNEL_DATA_FAILED,
QPBOC_APPLICATION,//9
QPBOC_FDDA_FAILED,
PURE_ELE_CASH_CARD_NOT_ALLOW_ONLINE_TRANS,
UNKNOW
}
```

### EMV Enum / Operation

- Source section: `3.11`



Purpose:
publicstaticenumOperation


Source details:
```text
publicstaticenumOperation
{
CLEAR, //clearcontactandcontactlessAID
ADD,
CLEAR_EMV_AID,//onlyclearcontactAID
CLEAR_NFC_AID//onlyclearcontactlessAID
}
4.Appendix
```

### EMV Appendix / CVM processing FAQ

- Source section: `4.1`



Purpose:
Q1:Howto handleOnlinePINverification?


Source details:
```text
Q1:Howto handleOnlinePINverification?
Trigger: The EMV kernel invokes the onRequestPinEntry() callback when cardholder PIN
verificationisrequired.
ApplicationResponse:CallgetPinBlock()orgetPinBlockEx()(refertodeviceSDK)tolaunchthe
PINentryinterface.
UserActionHandling:

 Success:CallmEmvApi.sendPinEntry()toconfirmPINsubmission.
 Cancellation:CallmEmvApi.cancelPinEntry()iftheuserabortsinput.
 Skip(ifallowed):CallmEmvApi.bypassPinEntry()forscenariosbypassingPINvalidation.
Q2:Howdoes EMVthe kernel handleofflineplainPin andofflineencrypt pin?
 TheEMVkernelhandlesPINentryandverificationautomaticallyviathePINpad.
 Noapplicationinterventionrequired(Trytestwithsampleapplication).
Q2:Howiscardholdersignaturetriggered inEMVtransactions?
The EMV kernel requests signature verification by invoking
onRequestDisplayText(ContantPara.DisplayText.APPROVED_PLEASE_SIGN) with parameter
ContantPara.DisplayText.APPROVED_PLEASE_SIGN
```

### EMV Appendix / EMV kernel using FAQ

- Source section: `4.2`



Purpose:
Q1:MissingPAN(Tag 0x5A) -Howto Fallback?​


Source details:
```text
Q1:MissingPAN(Tag 0x5A) -Howto Fallback?​
if0x5Anotreturn,needuse0x57datatogetPAN.
Q2:Howto Debug EMV(Contact/UPI) andNFC Issues?​
1. EnableLogging:CallLogOutEnable()API(activatesbothEMV&NFClogs).
2. PullLogsSeparately:PulllogsviaADB.
1. # For EMV (Contact/UPI) issues
2. adb pull /data/data/com.urovo.demos/files/UROPE/Trace.txt
3.
```

### EMV Appendix / # For NFC (Contactless) issues

- Source section: `4.3`



Purpose:
5. adb pull /data/data/com.urovo.demos/files/UROPE/TraceCL.txt


Source details:
```text
5. adb pull /data/data/com.urovo.demos/files/UROPE/TraceCL.txt
Q3: OfflinePIN fails onAndroid5.1/6.0?
Fix:CallsetContext(MainActivity.this)beforetransaction.
```

### EMV Appendix / Contact

- Source section: `4.2`



Purpose:
if you have any issue , please contact Xiray or Ron, xiaofeng.xia@urovo.com,


Source details:
```text
if you have any issue , please contact Xiray or Ron, xiaofeng.xia@urovo.com,
an.luo@urovo.com ,wewill help you.
```
