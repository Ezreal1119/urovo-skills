# Urovo POS General SDK API Knowledge Base

Source document: `POS API specification 260305.doc`  
Demo project: `UrovoPosSdkDemo`  
Generated: 2026-05-13  
Chunking rule: split on `^### `; each `###` section is intended to describe one API or closely related SDK operation.

### Beeper / startBeep

- Source section: `1.1`

- Package/class path: `com.urovo.sdk.beeper.BeeperImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/BeeperActivity.java`



Purpose:
Start Beeping


Signature/prototype:
```java
void startBeep(int cnts, int msec)
```


Parameters:
Input
cnts - frequency.
cnts=1, msec=timeout.
cnts>1, mescTime = interval time of each beep.

msec- the duration of the beep, in milliseconds.
Output
None


Return value:
None


Usage notes:
The buzzer sounds in a non-blocking manner. After calling this function, the function returns immediately and does not block the call.


Simplified example:
```java
BeeperImpl.getInstance().startBeep(1, 100); // one short beep; returns immediately
```


Source details:
```text
Prototype
void startBeep(int cnts, int msec)
Description
Start Beeping
Parameters
Input
cnts - frequency.
cnts=1, msec=timeout.
cnts>1, mescTime = interval time of each beep.

msec- the duration of the beep, in milliseconds.
Output
None
Return
None
Remark
The buzzer sounds in a non-blocking manner. After calling this function, the function returns immediately and does not block the call.
```

### Beeper / stopBeep

- Source section: `1.2`

- Package/class path: `com.urovo.sdk.beeper.BeeperImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/BeeperActivity.java`



Purpose:
Stop Beeping


Signature/prototype:
```java
void stopBeep()
```


Parameters:
Input
None
Output
None


Return value:
None


Usage notes:
Stop the beep immediately after calling this method.


Simplified example:
```java
BeeperImpl.getInstance().stopBeep();
```


Source details:
```text
Prototype
void stopBeep()
Description
Stop Beeping
Parameters
Input
None
Output
None
Return
None
Remark
Stop the beep immediately after calling this method.
```

### LED Light / turnOn

- Source section: `2.1`

- Package/class path: `com.urovo.sdk.led.LEDDriverImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/LedActivity.java`



Purpose:
Turn on LED light


Signature/prototype:
```java
void turnOn(int led)
```


Parameters:
Input
led - 1: blue light, 2: yellow light, 3: green light, 4: red
Output
None


Usage notes:
Refer to Constant.Light.


Simplified example:
```java
LEDDriverImpl.getInstance().turnOn(Constant.Light.BLUE);
```


Source details:
```text
Prototype
void turnOn(int led)
Description
Turn on LED light
Parameters
Input
led - 1: blue light, 2: yellow light, 3: green light, 4: red
Output
None
Return

Remark
Refer to Constant.Light.
```

### LED Light / turnOff

- Source section: `2.2`

- Package/class path: `com.urovo.sdk.led.LEDDriverImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/LedActivity.java`



Purpose:
Turn off LED light


Signature/prototype:
```java
void turnOff(int led)
```


Parameters:
Input
led - 1: blue light, 2: yellow light, 3: green light, 4: red.
Output
None


Usage notes:
Refer to Constant.Light.


Simplified example:
```java
LEDDriverImpl.getInstance().turnOff(Constant.Light.BLUE);
```


Source details:
```text
Prototype
void turnOff(int led)
Description
Turn off LED light
Parameters
Input
led - 1: blue light, 2: yellow light, 3: green light, 4: red.
Output
None
Return

Remark
Refer to Constant.Light.
```

### Pinpad / isKeyExist

- Source section: `3.1`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Determine is key exist


Signature/prototype:
```java
boolean isKeyExist(int keyType, int keyId)
```


Parameters:
Input
keyType - key type
- 0-Main key
- 1-MAC key
- 2-PIN key
- 3-TD key

Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.

Load the index of the key store
Output
None


Return value:
Existence returns true, otherwise it returns false.


Simplified example:
```java
boolean exists = PinPadProviderImpl.getInstance().isKeyExist(Constant.KeyType.PIN_KEY, 1);
```


Source details:
```text
Prototype
boolean isKeyExist(int keyType, int keyId)
Description
Determine is key exist
Parameters
Input
keyType - key type
- 0-Main key
- 1-MAC key
- 2-PIN key
- 3-TD key

Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.

Load the index of the key store
Output
None
Return
Existence returns true, otherwise it returns false.
Remark
```

### Pinpad / loadTEK

- Source section: `3.2`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Load TEK, and TEK is the key for encrypting main key.


Signature/prototype:
```java
boolean loadTEK(int keyId, byte[] key, byte[] checkValue)
```


Parameters:
Input
keyId - TEKKey storage index

key - Load key

checkValue - Check value KCV is allowed to be null (3DES encryption 4 bytes all 00, take the first 4 bytes)
Output
None


Return value:
Load successfully returns true, otherwise returns false.


Source details:
```text
Prototype
boolean loadTEK(int keyId, byte[] key, byte[] checkValue)
Description
Load TEK, and TEK is the key for encrypting main key.
Parameters
Input
keyId - TEKKey storage index

key - Load key

checkValue - Check value KCV is allowed to be null (3DES encryption 4 bytes all 00, take the first 4 bytes)
Output
None
Return
Load successfully returns true, otherwise returns false.
Remark
```

### Pinpad / loadEncryptMainKey

- Source section: `3.3`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Load Encrypt main key, encrypted by TEK.

tekkId - the index of TEK.


Signature/prototype:
```java
boolean loadEncryptMainKey(int tekId, int keyId, byte[] key, byte[] check Value)
```


Parameters:
Input
keyId - load the index of the key store.

key - load key

checkValue - Check value KCV
Output
None


Return value:
Load successfully returns true, otherwise returns false.


Usage notes:
If the key Encrypt data is the IC key [KEY(16Byte)] structure, check Value is null.
If the Key data is an integer multiple of 4, check Value is 4 bytes KCV


Source details:
```text
Prototype
boolean loadEncryptMainKey(int tekId, int keyId, byte[] key, byte[] check Value)
Description
Load Encrypt main key, encrypted by TEK.

tekkId - the index of TEK.
Parameters
Input
keyId - load the index of the key store.

key - load key

checkValue - Check value KCV
Output
None
Return
Load successfully returns true, otherwise returns false.
Remark
If the key Encrypt data is the IC key [KEY(16Byte)] structure, check Value is null.
If the Key data is an integer multiple of 4, check Value is 4 bytes KCV
```

### Pinpad / loadMainKey

- Source section: `3.4`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Load main key


Signature/prototype:
```java
boolean loadMainKey(int keyId, byte[] key, byte[] checkValue)
```


Parameters:
Input
keyId - load the index of the key store
key - load key
checkValue - Check value KCV
Output
None


Return value:
Load successfully returns true, otherwise returns false.


Usage notes:
If the key Encrypt data is the IC key [KEY(16Byte)] structure, check Value is null.
If the Key data is an integer multiple of 4, check Value is 4 bytes KCV


Simplified example:
```java
byte[] key = BytesUtil.hexString2Bytes("11111111111111111111111111111111");
boolean ok = PinPadProviderImpl.getInstance().loadMainKey(0, key, null);
```


Source details:
```text
Prototype
boolean loadMainKey(int keyId, byte[] key, byte[] checkValue)
Description
Load main key
Parameters
Input
keyId - load the index of the key store
key - load key
checkValue - Check value KCV
Output
None
Return
Load successfully returns true, otherwise returns false.
Remark
If the key Encrypt data is the IC key [KEY(16Byte)] structure, check Value is null.
If the Key data is an integer multiple of 4, check Value is 4 bytes KCV
```

### Pinpad / loadWorkKey

- Source section: `3.5`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Load work key, including PIN key, MAC key, and track encryption key


Signature/prototype:
```java
boolean loadWorkKey(int keyType, int mkId, int wkId,
byte[] key, byte[] checkValue)
```


Parameters:
Input
keyType - key type
- 0-Main key
- 1-MAC key
- 2-PIN key
- 3-TD key

Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.
mkId - the index of main key
wkId - the index of load work key store
Key - key data
checkValue - Check value KCV
Output
None


Return value:
Load successfully returns true, otherwise returns false.


Usage notes:
If the key Encrypt data is the IC key [KEY(16Byte)] structure, check Value is null.
If the Key data is an integer multiple of 4, check Value is 4 bytes KCV.


Simplified example:
```java
boolean ok = pinpad.loadWorkKey(Constant.KeyType.PIN_KEY, 0, 1, encryptedPinKey, null);
```


Source details:
```text
Prototype
boolean loadWorkKey(int keyType, int mkId, int wkId,
byte[] key, byte[] checkValue)
Description
Load work key, including PIN key, MAC key, and track encryption key
Parameters
Input
keyType - key type
- 0-Main key
- 1-MAC key
- 2-PIN key
- 3-TD key

Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.
mkId - the index of main key
wkId - the index of load work key store
Key - key data
checkValue - Check value KCV
Output
None
Return
Load successfully returns true, otherwise returns false.
Remark
If the key Encrypt data is the IC key [KEY(16Byte)] structure, check Value is null.
If the Key data is an integer multiple of 4, check Value is 4 bytes KCV.
```

### Pinpad / calcMAC

- Source section: `3.6`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Calculating MAC


Signature/prototype:
```java
byte[] calcMAC(int keyId, byte[] data, int mode)
```


Parameters:
Input
keyId - MAC Key index
data - the data to be calculated
mode - MAC algorithm:
0x00: XOR.
0x01: ANSI 9.9.
0x02: ECB.
0x11: ANSI 9.19.
0x10: POS_ECB.
0x07: CMAC.
Output
None


Return value:
macResult:0x01&0x11:BCD format,0x10:ASCII format.


Simplified example:
```java
byte[] mac = PinPadProviderImpl.getInstance().calcMAC(1, data, 0x11);
```


Source details:
```text
Prototype
byte[] calcMAC(int keyId, byte[] data, int mode)
Description
Calculating MAC
Parameters
Input
keyId - MAC Key index
data - the data to be calculated
mode - MAC algorithm:
0x00: XOR.
0x01: ANSI 9.9.
0x02: ECB.
0x11: ANSI 9.19.
0x10: POS_ECB.
0x07: CMAC.
Output
None
Return
macResult:0x01&0x11:BCD format,0x10:ASCII format.
Remark
```

### Pinpad / calculateDes

- Source section: `3.7`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Encrypt Track Data


Signature/prototype:
```java
int calculateDes(int desMode, int algorithm, int keyType,int keyId, byte[] data, byte[] dataOut)
```


Parameters:
Input
desMode - encryption mode, 0: ENC , 1: DEC.
Refer to com.urovo.sdk.pinpad.utils.Constant.DesMode.
algorithm - 1:DES ECB; 2:DES CBC; 3:SM4; 7:AES ECB; 8:AES CBC.
Refer to com.urovo.sdk.pinpad.utils Constant.Algorithm.
keyType - the key type.
Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType
keyId - the key index
data - source data
Output
dataOut - des result out


Return value:
0:success, others:failed.


Source details:
```text
Prototype
int calculateDes(int desMode, int algorithm, int keyType,int keyId, byte[] data, byte[] dataOut)
Description
Encrypt Track Data
Parameters
Input
desMode - encryption mode, 0: ENC , 1: DEC.
Refer to com.urovo.sdk.pinpad.utils.Constant.DesMode.
algorithm - 1:DES ECB; 2:DES CBC; 3:SM4; 7:AES ECB; 8:AES CBC.
Refer to com.urovo.sdk.pinpad.utils Constant.Algorithm.
keyType - the key type.
Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType
keyId - the key index
data - source data
Output
dataOut - des result out
Return
0:success, others:failed.
Remark
```

### Pinpad / downloadKeyDukpt

- Source section: `3.8`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
downloadKeyDukpt


Signature/prototype:
```java
int downloadKeyDukpt(int keyIndex, in byte[] Bdk, int BdkLen, in byte[] Ksn, int KsnLen, in byte[] bsIpek, int bsIpeklength)
```


Parameters:
Input
keyIndex - index of which key sets(1-4).
Bdk - BDK data
BdkLen - The length of Bdk
ksn - KSN data
KsnLen - The length of KSN data
bsIpek - IPEK data
bsIpeklength - The length of IPEK data
Output
None


Return value:
ErrorCode


Source details:
```text
Prototype
int downloadKeyDukpt(int keyIndex, in byte[] Bdk, int BdkLen, in byte[] Ksn, int KsnLen, in byte[] bsIpek, int bsIpeklength)
Description
downloadKeyDukpt
Parameters
Input
keyIndex - index of which key sets(1-4).
Bdk - BDK data
BdkLen - The length of Bdk
ksn - KSN data
KsnLen - The length of KSN data
bsIpek - IPEK data
bsIpeklength - The length of IPEK data
Output
None
Return
ErrorCode
Remark
```

### Pinpad / calculateMACOfDUKPTExtend

- Source section: `3.9`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Calculate the MAC under DUKPT(X9.19 Retail MAC)


Signature/prototype:
```java
int calculateMACOfDUKPTExtend(int keySetNum, in byte[] rawData, in int rawDataLen, in byte[] outData, in int[] outDataLen, in byte[] outKsn, in int[] KsnLen)
```


Parameters:
Input
keySetNum - key index.
rawData - input data
rawDataLen - The length of rawData
outData - The MAC result of rawData
outDataLen - The length of outData
outKsn - OutKsn Data
KsnLen - The length of outKsn.
Output
None


Return value:
ErrorCode


Source details:
```text
Prototype
int calculateMACOfDUKPTExtend(int keySetNum, in byte[] rawData, in int rawDataLen, in byte[] outData, in int[] outDataLen, in byte[] outKsn, in int[] KsnLen)
Description
Calculate the MAC under DUKPT(X9.19 Retail MAC)
Parameters
Input
keySetNum - key index.
rawData - input data
rawDataLen - The length of rawData
outData - The MAC result of rawData
outDataLen - The length of outData
outKsn - OutKsn Data
KsnLen - The length of outKsn.
Output
None
Return
ErrorCode
Remark
```

### Pinpad / encryptWithPEK(Deprecated)

- Source section: `3.10`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Basic DUKPT Key encryption function


Signature/prototype:
```java
int encryptWithPEK(int keyUsage, int keySetNum, in byte[] rawData, in int rawDataLen, in byte[] outData, in int[] outDataLen, in byte[] outKsn, in int[] KsnLen)
```


Parameters:
Input

keyUsage:
* 0x01 - Pin
* 0x02 - Mac
* 0x03 - TrackData
keySetNum - index of which key sets.
rawData - input data.
rawDataLen - The length of rawData.
outData - The encrypt result of rawData
( the length of outdata must be (inputlen/8+ 1)*8)
outDataLen - The length of outData
outKsn - outKsn data
KsnLen - The length of outKsn.
Output
None


Return value:
result


Usage notes:
Suggest using method 3.19 DukptEncrytDataIV.


Source details:
```text
Prototype
int encryptWithPEK(int keyUsage, int keySetNum, in byte[] rawData, in int rawDataLen, in byte[] outData, in int[] outDataLen, in byte[] outKsn, in int[] KsnLen)
Description
Basic DUKPT Key encryption function
Parameters
Input

keyUsage:
* 0x01 - Pin
* 0x02 - Mac
* 0x03 - TrackData
keySetNum - index of which key sets.
rawData - input data.
rawDataLen - The length of rawData.
outData - The encrypt result of rawData
( the length of outdata must be (inputlen/8+ 1)*8)
outDataLen - The length of outData
outKsn - outKsn data
KsnLen - The length of outKsn.
Output
None
Return
result
Remark
Suggest using method 3.19 DukptEncrytDataIV.
```

### Pinpad / getDukptPinBlock

- Source section: `3.11`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Calculate PinBlock under DUKPT


Signature/prototype:
```java
void GetDukptPinBlock(Bundle bundle, PinInputListener listener)
```


Parameters:
Input

OnlinePin(boolean) - isOnline pin
PINKeyNo(int) - pin key index
cardNo(String) - card holder number.

title(String) - The Title to be displayed on the pinpad
message(String) - The messge to be displayed on the pinpad
supportPinLen(String) - default(0, 4,5,6,7,8,9,10,11,12)
bypass(boolean) - is support bypass
timeoutMs(long) - enter pin Timeout(ms).
sound(boolean) - whether play keyboard sound
FullScreen(boolean) - whether the pinpad is displayed in full screen
randomKeyboard(boolean) - whether display random number.
Shortarray meaning of each index(0-6):
* 0: SECURITY_KEYBOARD_TITLE.
* 1: SECURITY_KEYBOARD_INFO.
* 2: SECURITY_KEYBOARD_PASSWORD.
* 3: SECURITY_KEYBOARD_KEY_NUMBER.
* 4: SECURITY_KEYBOARD_KEY_CANCEL.
* 5: SECURITY_KEYBOARD_KEY_DELETE.
* 6: SECURITY_KEYBOARD_KEY_OK.
textSize(shortArray): {10, 10, 10, 10, 10, 10, 10}.
leftMargin: {10, 10, 10, 10, 10, 10, 10}.
topMargin: {10, 10, 10, 10, 10, 10, 10}.
rightMargin: {10, 10, 10, 10, 10, 10, 10}.
bottomMargin: {10, 10, 10, 10, 10, 10, 10}.

IntArray meaning of each index(0-6):
* 0: SECURITY_KEYBOARD_TITLE.
* 1: SECURITY_KEYBOARD_INFO.
* 2: SECURITY_KEYBOARD_PASSWORD.
* 3: SECURITY_KEYBOARD_KEY_NUMBER.
* 4: SECURITY_KEYBOARD_KEY_CANCEL.
* 5: SECURITY_KEYBOARD_KEY_DELETE.
* 6: SECURITY_KEYBOARD_KEY_OK.
backgroundColor(IntArray):{ xff0C9213, 0xff0C9213, 0, 0, 0xFFFF0000, 0xffFFFE00, 0xff0C9213}.

backgroundColor(IntArray):{ xff0C9213, 0xff0C9213, 0, 0, 0xFFFF0000, 0xffFFFE00, 0xff0C9213}.
* - numberText(StringArray): {0, 1,2,3,4,5,6,7,8,9}.
* - cancelText: "CANCEL".
* - deleteText:"DELETE".
* - okText:"OK".
listener : The PedInputListener that will be called when a input key event is fired.
listener : callback listener for pin enter
PIN input process listener
Interface PinInputListener {
/**
* Button press event
* @param len - password length entered
* @param key - the current Key value
*/
void onInput(int len, int key);

/**
* Called when the user confirms the PIN input
* @param data - pin code, null when input is empty
* @param isNonePin - true if the input is empty
*/
void onConfirm(in byte[] data, boolean isNonePin);

/**
* Called when canceling PIN input
*/
void onCancel();
/**
* Called when PIN input timeOut
*/
void onTimeOut();

/**
* Callback when wrong
* @param errorCode - error code
*/
void onError(int errorCode);

/**
* Called when the user confirms the PIN input
* @param PinBlock - pinBlock data, null when input is empty
* @param ksn - ksn data, null when input is empty
*/
void onConfim_dukpt(byte[] PinBlock, byte[] ksn);
}
Output
None


Source details:
```text
Prototype
void GetDukptPinBlock(Bundle bundle, PinInputListener listener)
Description
Calculate PinBlock under DUKPT
Parameters
Input

OnlinePin(boolean) - isOnline pin
PINKeyNo(int) - pin key index
cardNo(String) - card holder number.

title(String) - The Title to be displayed on the pinpad
message(String) - The messge to be displayed on the pinpad
supportPinLen(String) - default(0, 4,5,6,7,8,9,10,11,12)
bypass(boolean) - is support bypass
timeoutMs(long) - enter pin Timeout(ms).
sound(boolean) - whether play keyboard sound
FullScreen(boolean) - whether the pinpad is displayed in full screen
randomKeyboard(boolean) - whether display random number.
Shortarray meaning of each index(0-6):
* 0: SECURITY_KEYBOARD_TITLE.
* 1: SECURITY_KEYBOARD_INFO.
* 2: SECURITY_KEYBOARD_PASSWORD.
* 3: SECURITY_KEYBOARD_KEY_NUMBER.
* 4: SECURITY_KEYBOARD_KEY_CANCEL.
* 5: SECURITY_KEYBOARD_KEY_DELETE.
* 6: SECURITY_KEYBOARD_KEY_OK.
textSize(shortArray): {10, 10, 10, 10, 10, 10, 10}.
leftMargin: {10, 10, 10, 10, 10, 10, 10}.
topMargin: {10, 10, 10, 10, 10, 10, 10}.
rightMargin: {10, 10, 10, 10, 10, 10, 10}.
bottomMargin: {10, 10, 10, 10, 10, 10, 10}.

IntArray meaning of each index(0-6):
* 0: SECURITY_KEYBOARD_TITLE.
* 1: SECURITY_KEYBOARD_INFO.
* 2: SECURITY_KEYBOARD_PASSWORD.
* 3: SECURITY_KEYBOARD_KEY_NUMBER.
* 4: SECURITY_KEYBOARD_KEY_CANCEL.
* 5: SECURITY_KEYBOARD_KEY_DELETE.
* 6: SECURITY_KEYBOARD_KEY_OK.
backgroundColor(IntArray):{ xff0C9213, 0xff0C9213, 0, 0, 0xFFFF0000, 0xffFFFE00, 0xff0C9213}.

backgroundColor(IntArray):{ xff0C9213, 0xff0C9213, 0, 0, 0xFFFF0000, 0xffFFFE00, 0xff0C9213}.
* - numberText(StringArray): {0, 1,2,3,4,5,6,7,8,9}.
* - cancelText: "CANCEL".
* - deleteText:"DELETE".
* - okText:"OK".
listener : The PedInputListener that will be called when a input key event is fired.
listener : callback listener for pin enter
PIN input process listener
Interface PinInputListener {
/**
* Button press event
* @param len - password length entered
* @param key - the current Key value
*/
void onInput(int len, int key);

/**
* Called when the user confirms the PIN input
* @param data - pin code, null when input is empty
* @param isNonePin - true if the input is empty
*/
void onConfirm(in byte[] data, boolean isNonePin);

/**
* Called when canceling PIN input
*/
void onCancel();
/**
* Called when PIN input timeOut
*/
void onTimeOut();

/**
* Callback when wrong
* @param errorCode - error code
*/
void onError(int errorCode);
Source note: section truncated for chunk size; consult the source document for remaining detailed tables (93 source lines total).
```

### Pinpad / getPinBlockEx

- Source section: `3.12`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Calculate PinBlock under MK/SK


Signature/prototype:
```java
void getPinBlockEx(Bundle bundle, PinInputListener listener)
```


Parameters:
Input

onlinePin(boolean) - true-online, false-offline.
PINKeyNo(int) - pin key index
pinAlgMode(int) - 0-format 0, 1-format 3. (Optional: default is format 0)
cardNo(String) - card holder number.

title(String) - The Title to be displayed on the pinpad.
message(String) - The messge to be displayed on the pinpad .
supportPinLen (String) - default(0, 4,5,6,7,8,9,10,11,12).
bypass(boolean) - is support bypass.
timeoutMs(long) - enter pin Timeout(ms).
sound(boolean) - whether play keyboard sound.
FullScreen(boolean) - whether the pinpad is displayed in full screen.
randomKeyboard(boolean) - whether display random number.
soundVolume(int) - the volumes of keyboard(1-15).
infoLocation(String) - The location of the message:LEFT,CENTER,RIGHT.
money(String) - money text
randomKeyBoradLocation(boolean) - If true,the vertical position of the key area will automatically change.
randomKeyboardStaticLocation(intArray) - The coordinates((X,Y)) of the keyboard area. If the Y=0, setting is invalid.
customization - If true, can customize pinpad UI.
Refer to the appendix A.
strJson(String) - If customization is true, customize the pinpad UI with this paramater.
cancelBitmap(Bitmap) - cancel button
delBitmap(Bitmap) - delete button
okBitemap(Bitmap) - ok button
backspaceBitmap(Bitmap) - back button
bodyBitmap(Bitmap) - Full screen background image
keyBitmap(Bitmap) - Keyboard background image
viewBitmap(Bitmap):
ShortArray meaning of each index(0-6):
* 0: SECURITY_KEYBOARD_TITLE.
* 1: SECURITY_KEYBOARD_INFO.
* 2: SECURITY_KEYBOARD_PASSWORD.
* 3: SECURITY_KEYBOARD_KEY_NUMBER.
* 4: SECURITY_KEYBOARD_KEY_CANCEL.
* 5: SECURITY_KEYBOARD_KEY_DELETE.
* 6: SECURITY_KEYBOARD_KEY_OK.

textSize(shortArray): {10, 10, 10, 10, 10, 10, 10}.
leftMargin: {10, 10, 10, 10, 10, 10, 10}.
topMargin: {10, 10, 10, 10, 10, 10, 10}.
rightMargin: {10, 10, 10, 10, 10, 10, 10}.
bottomMargin: {10, 10, 10, 10, 10, 10, 10}.

IntArray meaning of each index(0-6):
* 0: SECURITY_KEYBOARD_TITLE.
* 1: SECURITY_KEYBOARD_INFO.
* 2: SECURITY_KEYBOARD_PASSWORD.
* 3: SECURITY_KEYBOARD_KEY_NUMBER.
* 4: SECURITY_KEYBOARD_KEY_CANCEL.
* 5: SECURITY_KEYBOARD_KEY_DELETE.
* 6: SECURITY_KEYBOARD_KEY_OK.
backgroundColor(IntArray):{ xff0C9213, 0xff0C9213, 0, 0, 0xFFFF0000, 0xffFFFE00, 0xff0C9213}.

* - numberText(StringArray): {0, 1,2,3,4,5,6,7,8,9}.
* - cancelText: "CANCEL".
* - deleteText:"DELETE".
* - okText:"OK".

listener : The PedInputListener that will be called when a input key event is fired.
listener : callback listener for pin enter
PIN input process listener
Interface PinInputListener {
/**
* Button press event
* @param len - password length entered
* @param key - the current Key value
*/
void onInput(int len, int key);

/**
* Called when the user confirms the PIN input
* @param data - ciphertext pin block(ASCII)
* @param isNonePin - true if the input is empty
*/
void onConfirm(in byte[] data, boolean isNonePin);

/**
* Called when canceling PIN input
*/
void onCancel();
/**
* Called when PIN input timeOut
*/
void onTimeOut();

/**
* Callback when wrong
* @param errorCode - error code
*/
void onError(int errorCode);

/**
* Called when the user confirms the PIN input
* @param PinBlock - ciphertext pin block(ASCII)
* @param ksn - ksn data, null when input is empty
*/
void onConfim_dukpt(byte[] PinBlock, byte[] ksn);
}
Output
None


Simplified example:
```java
Bundle args = new Bundle();
args.putBoolean("onlinePin", true);
args.putInt("PINKeyNo", 1);
args.putString("cardNo", pan);
args.putString("supportPinLen", "0,4,5,6");
args.putLong("timeoutMs", 30000L);
PinPadProviderImpl.getInstance().getPinBlockEx(args, listener);
```


Source details:
```text
Prototype
void getPinBlockEx(Bundle bundle, PinInputListener listener)
Description
Calculate PinBlock under MK/SK
Parameters
Input

onlinePin(boolean) - true-online, false-offline.
PINKeyNo(int) - pin key index
pinAlgMode(int) - 0-format 0, 1-format 3. (Optional: default is format 0)
cardNo(String) - card holder number.

title(String) - The Title to be displayed on the pinpad.
message(String) - The messge to be displayed on the pinpad .
supportPinLen (String) - default(0, 4,5,6,7,8,9,10,11,12).
bypass(boolean) - is support bypass.
timeoutMs(long) - enter pin Timeout(ms).
sound(boolean) - whether play keyboard sound.
FullScreen(boolean) - whether the pinpad is displayed in full screen.
randomKeyboard(boolean) - whether display random number.
soundVolume(int) - the volumes of keyboard(1-15).
infoLocation(String) - The location of the message:LEFT,CENTER,RIGHT.
money(String) - money text
randomKeyBoradLocation(boolean) - If true,the vertical position of the key area will automatically change.
randomKeyboardStaticLocation(intArray) - The coordinates((X,Y)) of the keyboard area. If the Y=0, setting is invalid.
customization - If true, can customize pinpad UI.
Refer to the appendix A.
strJson(String) - If customization is true, customize the pinpad UI with this paramater.
cancelBitmap(Bitmap) - cancel button
delBitmap(Bitmap) - delete button
okBitemap(Bitmap) - ok button
backspaceBitmap(Bitmap) - back button
bodyBitmap(Bitmap) - Full screen background image
keyBitmap(Bitmap) - Keyboard background image
viewBitmap(Bitmap):
ShortArray meaning of each index(0-6):
* 0: SECURITY_KEYBOARD_TITLE.
* 1: SECURITY_KEYBOARD_INFO.
* 2: SECURITY_KEYBOARD_PASSWORD.
* 3: SECURITY_KEYBOARD_KEY_NUMBER.
* 4: SECURITY_KEYBOARD_KEY_CANCEL.
* 5: SECURITY_KEYBOARD_KEY_DELETE.
* 6: SECURITY_KEYBOARD_KEY_OK.

textSize(shortArray): {10, 10, 10, 10, 10, 10, 10}.
leftMargin: {10, 10, 10, 10, 10, 10, 10}.
topMargin: {10, 10, 10, 10, 10, 10, 10}.
rightMargin: {10, 10, 10, 10, 10, 10, 10}.
bottomMargin: {10, 10, 10, 10, 10, 10, 10}.

IntArray meaning of each index(0-6):
* 0: SECURITY_KEYBOARD_TITLE.
* 1: SECURITY_KEYBOARD_INFO.
* 2: SECURITY_KEYBOARD_PASSWORD.
* 3: SECURITY_KEYBOARD_KEY_NUMBER.
* 4: SECURITY_KEYBOARD_KEY_CANCEL.
* 5: SECURITY_KEYBOARD_KEY_DELETE.
* 6: SECURITY_KEYBOARD_KEY_OK.
backgroundColor(IntArray):{ xff0C9213, 0xff0C9213, 0, 0, 0xFFFF0000, 0xffFFFE00, 0xff0C9213}.

* - numberText(StringArray): {0, 1,2,3,4,5,6,7,8,9}.
* - cancelText: "CANCEL".
* - deleteText:"DELETE".
* - okText:"OK".

listener : The PedInputListener that will be called when a input key event is fired.
listener : callback listener for pin enter
PIN input process listener
Interface PinInputListener {
/**
* Button press event
* @param len - password length entered
* @param key - the current Key value
*/
void onInput(int len, int key);

/**
* Called when the user confirms the PIN input
* @param data - ciphertext pin block(ASCII)
* @param isNonePin - true if the input is empty
Source note: section truncated for chunk size; consult the source document for remaining detailed tables (110 source lines total).
```

### Pinpad / getRSAPublicKeyModel

- Source section: `3.13`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Obtain RSA Public key Exponent and Modulus


Signature/prototype:
```java
boolean getRSAPublicKeyModel(byte[] publickey, int[] publickeyLen,
int[] exponent)
```


Parameters:
Input

Publickey - Public Key Modulus Byte Array
publickeyLen - Length of Public Key Modulus Byte Array
Exponent - RSA Exponent
Output
None


Return value:
true:success, false:failed.


Source details:
```text
Prototype
boolean getRSAPublicKeyModel(byte[] publickey, int[] publickeyLen,
int[] exponent)
Description
Obtain RSA Public key Exponent and Modulus
Parameters
Input

Publickey - Public Key Modulus Byte Array
publickeyLen - Length of Public Key Modulus Byte Array
Exponent - RSA Exponent
Output
None
Return
true:success, false:failed.
Remark
```

### Pinpad / loadDukptBlob

- Source section: `3.14`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Decrypt Dukpt and Load The Key Into The Terminal.
Source key data format:
type(1 bytes)+keyId(3 bytes)+ipek(16 bytes)+ksn(10 bytes).


Signature/prototype:
```java
int loadDukptBlob(int keySlot, byte[] blob, int blobLen)
```


Parameters:
Input

keySlot - Dukpt keytype
Blob - The Encrypted Data
blobLen - Length of The Encrypted Data
Output
None


Return value:
0:success, others:failed


Source details:
```text
Prototype
int loadDukptBlob(int keySlot, byte[] blob, int blobLen)
Description
Decrypt Dukpt and Load The Key Into The Terminal.
Source key data format:
type(1 bytes)+keyId(3 bytes)+ipek(16 bytes)+ksn(10 bytes).
Parameters
Input

keySlot - Dukpt keytype
Blob - The Encrypted Data
blobLen - Length of The Encrypted Data
Output
None
Return
0:success, others:failed
Remark
```

### Pinpad / DukptGetKsn

- Source section: `3.15`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
getKsn


Signature/prototype:
```java
int DukptGetKsn(int keySetNum, byte[] outKsn);
```


Parameters:
Input
keySetNum - keyIndex, 1-4
Output
outKsn - the ksn value


Return value:
0:success , others:failed


Source details:
```text
Prototype
int DukptGetKsn(int keySetNum, byte[] outKsn);
Description
getKsn
Parameters
Input
keySetNum - keyIndex, 1-4
Output
outKsn - the ksn value
Return
0:success , others:failed
Remark
```

### Pinpad / DiversifiedKey

- Source section: `3.16`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Generate new pin key before starting pin input.(3DES ECB)


Signature/prototype:
```java
int diversifiedKey(int masterkeyIndex, int sourceKeyIndex, int destKeyIndex, String diversifyingData);
```


Parameters:
Input
masterkeyIndex - master key index
sourceKeyIndex - soruce pin key index
destKeyIndex - new pin key index
diversifyingData - the data used to generate the new key.(32 hex string)
Output
outKsn - the ksn value


Return value:
0:success, others:failed


Source details:
```text
Prototype
int diversifiedKey(int masterkeyIndex, int sourceKeyIndex, int destKeyIndex, String diversifyingData);
Description
Generate new pin key before starting pin input.(3DES ECB)
Parameters
Input
masterkeyIndex - master key index
sourceKeyIndex - soruce pin key index
destKeyIndex - new pin key index
diversifyingData - the data used to generate the new key.(32 hex string)
Output
outKsn - the ksn value
Return
0:success, others:failed
Remark
```

### Pinpad / genKeyHashValue

- Source section: `3.17`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Do SHA-256 hashing with key.


Signature/prototype:
```java
int genKeyHashValue(int KeyUsage, int KeyNo, byte[] DataIn, int DataInLen,
byte[] ResponseData, byte[] ResLen)
```


Parameters:
Input
keyUsage - keyType
keyType - key type
- 0-Main key
- 1-MAC key
- 2-PIN key
- 3-TD key

Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType
KeyNo - key index
DataIn - source data.
DataInLen - the length of source data
Output
ResponseData - response data.

ResLen: the length of response data. Length = ResLen[0].


Return value:
0:success, others:failed


Source details:
```text
Prototype
int genKeyHashValue(int KeyUsage, int KeyNo, byte[] DataIn, int DataInLen,
byte[] ResponseData, byte[] ResLen)
Description
Do SHA-256 hashing with key.
Parameters
Input
keyUsage - keyType
keyType - key type
- 0-Main key
- 1-MAC key
- 2-PIN key
- 3-TD key

Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType
KeyNo - key index
DataIn - source data.
DataInLen - the length of source data
Output
ResponseData - response data.

ResLen: the length of response data. Length = ResLen[0].
Return
0:success, others:failed
Remark
```

### Pinpad / EndPinInputEvent

- Source section: `3.18`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Close the PIN pad


Signature/prototype:
```java
void EndPinInputEvent(int Event)
```


Parameters:
Input
Event - the oparation code.(16 -cancel)
Output
None


Return value:
None


Source details:
```text
Prototype
void EndPinInputEvent(int Event)
Description
Close the PIN pad
Parameters
Input
Event - the oparation code.(16 -cancel)
Output
None
Return
None
Remark
```

### Pinpad / DukptEncrytDataIV

- Source section: `3.19`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
DUKPT encryption and decryption.


Signature/prototype:
```java
int DukptEncryptDataIV(int keyUsage, int keyIndex, int encMode, in byte[] iv,
int ivLen, byte[] dataIn, int inLen, byte[] dataOut, int[] outLen, byte[] outKsn,
int[] KsnLen)
```


Parameters:
Input

keyUsage:
* 0x01 - Pin
* 0x02 - Mac
* 0x03 - TrackData
* 0x04 - Mac
keySetNum : index of which key sets.
encMode:
When keyType = 1/2/3:
* 0x00: ecb encryption.
* 0x01: cbc encryption.
* 0x10: ecb decryption.
* 0x11: cbc decryption.

When keyType = 4:
bit 2-5: mac algorithm 0-5.
bit 6-7: encryption type, 0-ASE, 1-TDES.
iv - the initial vector,default:new byte[8].
ivLen - The length of initial vector.
dataIn - input data.
inLen - The length of rawData.
Output
outData - The encrypt result of rawData
(the length of outdata must be (inputlen/8+ 1)*8)

outDataLen - The length of outData.

outKsn - outKsn data(10-bytes).

KsnLen - The length of outKsn.


Return value:
0-success, others-failed.


Source details:
```text
Prototype
int DukptEncryptDataIV(int keyUsage, int keyIndex, int encMode, in byte[] iv,
int ivLen, byte[] dataIn, int inLen, byte[] dataOut, int[] outLen, byte[] outKsn,
int[] KsnLen)
Description
DUKPT encryption and decryption.
Parameters
Input

keyUsage:
* 0x01 - Pin
* 0x02 - Mac
* 0x03 - TrackData
* 0x04 - Mac
keySetNum : index of which key sets.
encMode:
When keyType = 1/2/3:
* 0x00: ecb encryption.
* 0x01: cbc encryption.
* 0x10: ecb decryption.
* 0x11: cbc decryption.

When keyType = 4:
bit 2-5: mac algorithm 0-5.
bit 6-7: encryption type, 0-ASE, 1-TDES.
iv - the initial vector,default:new byte[8].
ivLen - The length of initial vector.
dataIn - input data.
inLen - The length of rawData.
Output
outData - The encrypt result of rawData
(the length of outdata must be (inputlen/8+ 1)*8)

outDataLen - The length of outData.

outKsn - outKsn data(10-bytes).

KsnLen - The length of outKsn.
Return
0-success, others-failed.
Remark
```

### Pinpad / deleteKey(MK/SK)

- Source section: `3.20`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Delete the key (only for MK/SK).


Signature/prototype:
```java
Int deleteKey(int keyType, int keyId)
```


Parameters:
Input
keyType - key type
- 0-Main key
- 1-MAC key
- 2-PIN key
- 3-TD key

Refer to Constant.KeyType.

keyId - the index of the key.
Output
None


Return value:
0:success, 23:key is not exist, others:failed.


Source details:
```text
Prototype
Int deleteKey(int keyType, int keyId)
Description
Delete the key (only for MK/SK).
Parameters
Input
keyType - key type
- 0-Main key
- 1-MAC key
- 2-PIN key
- 3-TD key

Refer to Constant.KeyType.

keyId - the index of the key.
Output
None
Return
0:success, 23:key is not exist, others:failed.
Remark
```

### Pinpad / setKeyAlgorithm

- Source section: `3.21`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Set Key Algorithm.


Signature/prototype:
```java
void setKeyAlgorithm(int algorithm)
```


Parameters:
Input
algorithm- key algorithm
- 0-Des
- 1-SM4
- 2-AES

Refer to com.urovo.sdk.pinpad.utils.Constant.KeyAlgorithm.
Output
None


Return value:
None


Source details:
```text
Prototype
void setKeyAlgorithm(int algorithm)
Description
Set Key Algorithm.
Parameters
Input
algorithm- key algorithm
- 0-Des
- 1-SM4
- 2-AES

Refer to com.urovo.sdk.pinpad.utils.Constant.KeyAlgorithm.
Output
None
Return
None
Remark
```

### Pinpad / DukptAesInitial

- Source section: `3.22`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Inject the AES BDK or Initial KEY (AES-128,AES-192,AES-256)


Signature/prototype:
```java
int DukptAesInitial(int keyIndex, byte[] Bdk, int BdkLen, byte[] Ipek, int IpekLen,
int DeriveKeyType, byte[] Ksn, int ksnLen)
```


Parameters:
Input
keyIndex - key index(1-4)
DeriveKeyType -
Refer to com.urovo.sdk.pinpad.utils.Constant.DukptKeyType.
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
int DukptAesInitial(int keyIndex, byte[] Bdk, int BdkLen, byte[] Ipek, int IpekLen,
int DeriveKeyType, byte[] Ksn, int ksnLen)
Description
Inject the AES BDK or Initial KEY (AES-128,AES-192,AES-256)
Parameters
Input
keyIndex - key index(1-4)
DeriveKeyType -
Refer to com.urovo.sdk.pinpad.utils.Constant.DukptKeyType.
Output
None
Return
0-success, others-failed.
Remark
```

### Pinpad / DukptAesGetKsn

- Source section: `3.23`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Get the current KSN for AES DUKPT


Signature/prototype:
```java
int DukptAesGetKsn(int keyIndex, byte[] outKsn)
```


Parameters:
Input
keyIndex - key index(1-4)
Output
outKsn - 12 bytes.


Return value:
0-success


Source details:
```text
Prototype
int DukptAesGetKsn(int keyIndex, byte[] outKsn)
Description
Get the current KSN for AES DUKPT
Parameters
Input
keyIndex - key index(1-4)
Output
outKsn - 12 bytes.
Return
0-success
Remark
```

### Pinpad / DukptAesUpdateKsn

- Source section: `3.24`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Generate the New KSN for AES DUKPT (KSN counter will add 1)


Signature/prototype:
```java
int DukptAesUpdateKsn(int keyIndex, byte[] outKsn)
```


Parameters:
Input
keyIndex - key index(1-4)
Output
outKsn - 12 bytes


Return value:
0-success


Source details:
```text
Prototype
int DukptAesUpdateKsn(int keyIndex, byte[] outKsn)
Description
Generate the New KSN for AES DUKPT (KSN counter will add 1)
Parameters
Input
keyIndex - key index(1-4)
Output
outKsn - 12 bytes
Return
0-success
Remark
```

### Pinpad / DukptAesEncryptDataIV

- Source section: `3.25`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Use Dukpt-Aes to encrypt data or calc MAC.


Signature/prototype:
```java
int DukptAesEncryptDataIV(int keyType, int keyIndex, int encMode,
int workkeytype, byte[] iv, int ivLen, byte[] dataIn, int inLen, byte[] dataOut,
int[] outLen, byte[] outKsn, int[] KsnLen)
```


Parameters:
Input
keyType - Key type for Alg
bit0~7:
0x01 - Pin;
0x02 - Mac;
0x03 - TrackData.
bit8~15:
when bit0~7=1 Pin
0x00 // Encrypt a PIN message
when bit0~7=2 Mac
0x00, // MAC code verification
0x01, // MAC code generation
0x02, // Two-way message authentication
when bit0~7=3 TrackData
0x00, // Data encryption
0x01, // Data decryption
0x02, // Two-way data encryption
keyIndex - key index(1-4).
encMode - 1bytes
when Keytype=1，encmode：
0x00: format0
0x01: format1
0x02: format2
0x03: format3
0x04: format4
when Keytype=3，encmode：
0x00: ecb encode.
0x01: cbc encode.
0x10: ecb decode.
0x11: cbc decode.
when Keytype=2, encmode:
mac alg
0x01: MAC_ALG_X9_19.
0x02: MAC_ALG_ISO_9797_1_MAC_ALG5.
0x03: CMAC.
workkeytype- com.urovo.sdk.pinpad.utils.Constant.DukptKeyType.
_2TDEA = 0
_3TDEA = 1
_AES128 = 2
_AES192 = 3
_AES256 = 4
iv - the initial vector,default:new byte[16].
ivLen - The length of initial vector.
dataIn - input data.
inLen - The length of rawData.
Output
dataOut - The encrypt result of rawData.
outLen - The length of outData.
outKsn - outKsn data(12-bytes).
outLen - The length of outData.


Return value:
0-success


Source details:
```text
Prototype
int DukptAesEncryptDataIV(int keyType, int keyIndex, int encMode,
int workkeytype, byte[] iv, int ivLen, byte[] dataIn, int inLen, byte[] dataOut,
int[] outLen, byte[] outKsn, int[] KsnLen)
Description
Use Dukpt-Aes to encrypt data or calc MAC.
Parameters
Input
keyType - Key type for Alg
bit0~7:
0x01 - Pin;
0x02 - Mac;
0x03 - TrackData.
bit8~15:
when bit0~7=1 Pin
0x00 // Encrypt a PIN message
when bit0~7=2 Mac
0x00, // MAC code verification
0x01, // MAC code generation
0x02, // Two-way message authentication
when bit0~7=3 TrackData
0x00, // Data encryption
0x01, // Data decryption
0x02, // Two-way data encryption
keyIndex - key index(1-4).
encMode - 1bytes
when Keytype=1，encmode：
0x00: format0
0x01: format1
0x02: format2
0x03: format3
0x04: format4
when Keytype=3，encmode：
0x00: ecb encode.
0x01: cbc encode.
0x10: ecb decode.
0x11: cbc decode.
when Keytype=2, encmode:
mac alg
0x01: MAC_ALG_X9_19.
0x02: MAC_ALG_ISO_9797_1_MAC_ALG5.
0x03: CMAC.
workkeytype- com.urovo.sdk.pinpad.utils.Constant.DukptKeyType.
_2TDEA = 0
_3TDEA = 1
_AES128 = 2
_AES192 = 3
_AES256 = 4
iv - the initial vector,default:new byte[16].
ivLen - The length of initial vector.
dataIn - input data.
inLen - The length of rawData.
Output
dataOut - The encrypt result of rawData.
outLen - The length of outData.
outKsn - outKsn data(12-bytes).
outLen - The length of outData.
Return
0-success
Remark
```

### Pinpad / GetDukptAesPinBlock

- Source section: `3.26`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Calculate PinBlock under DUKPT AES , like as GetDukptPinBlock.


Signature/prototype:
```java
void GetDukptAesPinBlock(Bundle bundle, final PinInputListener pinInputListener)
```


Parameters:
Input
Same as GetDukptPinBlock parameters
Add:
WorkKeyType(int) - com.urovo.sdk.pinpad.utils.Constant.DukptKeyType
Output


Source details:
```text
Prototype
void GetDukptAesPinBlock(Bundle bundle, final PinInputListener pinInputListener)
Description
Calculate PinBlock under DUKPT AES , like as GetDukptPinBlock.
Parameters
Input
Same as GetDukptPinBlock parameters
Add:
WorkKeyType(int) - com.urovo.sdk.pinpad.utils.Constant.DukptKeyType
Output

Return

Remark
```

### Pinpad / getOfflinePinBlcok

- Source section: `3.27`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Offline pin verification in emv process.


Signature/prototype:
```java
void getOfflinePinBlock(Bundle bundle, final OfflinePinInputListener listener)
```


Parameters:
Input
title(String): The Title to be displayed on the pinpad.

message (String): The messge to be displayed on the pinpad.

supportPinLen(String) : default(0, 4,5,6,7,8,9,10,11,12).

bypass(boolean): is support bypass.

timeoutMs(long) : enter pin Timeout(ms).

sound(boolean)：whether play keyboard sound.

FullScreen(boolean):whether the pinpad is displayed in full screen.

randomKeyboard(boolean):whether display random number.

onlinePin(boolean): fixed with false.

CardSlot(int): fixed with 0.

inputType(int):3-plaintext pin, 4-ciphertext pin.

ModuleLen(int): used for ciphertext pin.

Module(String): used for ciphertext pin.

ExponentLen(int): used for ciphertext pin.

Exponent(String): used for ciphertext offline pin.

listener : callback listener for pin enter
PIN input process listener:
need to call "EmvNfcKernelApi.sendOfflinePINVerifyResult(int result)"
after onCofirm/onCancel/onTimeOut/onError callback.
Interface OfflinePinInputListener{
/**
* Button press event
* @param len - password length entered
* @param key - the current Key value
*/
void onInput(int len, int key);

/**
* Called when the user confirms the PIN input.
* @param resultCode: offline verification result.
*/
void onConfirm(int resultCode);

/**
* Called when canceling PIN input.
* @param pinEntryType: 0-plaintext pin, 1-ciphertext pin.
* @param retryTimes: remaining retry attempts.
*/
void onRetry(int pinEntryType, int retryTimes);;

/**
* Called when canceling PIN input.
* @param resultCode: offline verification result.
*/
void onCancel(int errorCode);

/**
* Called when PIN input timeOut.
* @param resultCode: offline verification result.
*/
void onTimeOut(int errorCode);

/**
* Callback when wrong
* @param errorCode - error code.
* @param resultCode: offline verification result.
*/
void onError(int errorCode);
}
Output
None


Return value:
None


Source details:
```text
Prototype
void getOfflinePinBlock(Bundle bundle, final OfflinePinInputListener listener)
Description
Offline pin verification in emv process.
Parameters
Input
title(String): The Title to be displayed on the pinpad.

message (String): The messge to be displayed on the pinpad.

supportPinLen(String) : default(0, 4,5,6,7,8,9,10,11,12).

bypass(boolean): is support bypass.

timeoutMs(long) : enter pin Timeout(ms).

sound(boolean)：whether play keyboard sound.

FullScreen(boolean):whether the pinpad is displayed in full screen.

randomKeyboard(boolean):whether display random number.

onlinePin(boolean): fixed with false.

CardSlot(int): fixed with 0.

inputType(int):3-plaintext pin, 4-ciphertext pin.

ModuleLen(int): used for ciphertext pin.

Module(String): used for ciphertext pin.

ExponentLen(int): used for ciphertext pin.

Exponent(String): used for ciphertext offline pin.

listener : callback listener for pin enter
PIN input process listener:
need to call "EmvNfcKernelApi.sendOfflinePINVerifyResult(int result)"
after onCofirm/onCancel/onTimeOut/onError callback.
Interface OfflinePinInputListener{
/**
* Button press event
* @param len - password length entered
* @param key - the current Key value
*/
void onInput(int len, int key);

/**
* Called when the user confirms the PIN input.
* @param resultCode: offline verification result.
*/
void onConfirm(int resultCode);

/**
* Called when canceling PIN input.
* @param pinEntryType: 0-plaintext pin, 1-ciphertext pin.
* @param retryTimes: remaining retry attempts.
*/
void onRetry(int pinEntryType, int retryTimes);;

/**
* Called when canceling PIN input.
* @param resultCode: offline verification result.
*/
void onCancel(int errorCode);

/**
* Called when PIN input timeOut.
* @param resultCode: offline verification result.
*/
void onTimeOut(int errorCode);

/**
* Callback when wrong
* @param errorCode - error code.
* @param resultCode: offline verification result.
*/
void onError(int errorCode);
}
Source note: section truncated for chunk size; consult the source document for remaining detailed tables (85 source lines total).
```

### Pinpad / downloadKeyTR31

- Source section: `3.28`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Load mksk key in tr31 format


Signature/prototype:
```java
boolean downloadKeyTR31(int mKeyType, int wKeyType, Bundle bundle)
```


Parameters:
Input
mKeyType - kbpk type.
Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.
wKeyType - work key type.
Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.
bundle:
/**
keyNo: kbpk index.
sKeyNo: work key index.
content-tr31: tr31 keyblock.
content_size: length of tr31 keyblock.
**/
Output
None


Return value:
true-success, false: failed.


Source details:
```text
Prototype
boolean downloadKeyTR31(int mKeyType, int wKeyType, Bundle bundle)
Description
Load mksk key in tr31 format
Parameters
Input
mKeyType - kbpk type.
Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.
wKeyType - work key type.
Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.
bundle:
/**
keyNo: kbpk index.
sKeyNo: work key index.
content-tr31: tr31 keyblock.
content_size: length of tr31 keyblock.
**/
Output
None
Return
true-success, false: failed.
Remark
```

### Pinpad / DukptInitialTr31

- Source section: `3.29`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Load tdes dukpt key in tr31 format


Signature/prototype:
```java
int DukptInitialTr31(int KbpkUsage, int KbpkKeyNo, int keySetNum, int isBdk, byte[] KeyInfo, int KeyLen, byte[] Ksn, int KsnLen, int Kcv_Alg, byte[] Kcv_IV, int Kcv_IVLen, byte[] out_kcv, int[] outLen)
```


Parameters:
Input
KbpkUsage - kbpk type.
Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.
KbpkKeyNo - kbpk index.
keySetNum - dukpt key index.
isBdk - bdk or ipek.(0-ipek, others-bdk).
KeyInfo - tr31 key block.
KeyLen - length of tr31 key block.
Ksn - ksn.
KsnLen - length of ksn.
Kcv_Alg - kcv algorithm.(0-ecb, 1-cmac).
Kcv_IV - kcv iv data.
Kcv_IVLen - length of kcv iv data.
Output
out_kcv - response kcv.

outLen - length of reponse kcv.


Return value:
0-success, else: failed.


Source details:
```text
Prototype
int DukptInitialTr31(int KbpkUsage, int KbpkKeyNo, int keySetNum, int isBdk, byte[] KeyInfo, int KeyLen, byte[] Ksn, int KsnLen, int Kcv_Alg, byte[] Kcv_IV, int Kcv_IVLen, byte[] out_kcv, int[] outLen)
Description
Load tdes dukpt key in tr31 format
Parameters
Input
KbpkUsage - kbpk type.
Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.
KbpkKeyNo - kbpk index.
keySetNum - dukpt key index.
isBdk - bdk or ipek.(0-ipek, others-bdk).
KeyInfo - tr31 key block.
KeyLen - length of tr31 key block.
Ksn - ksn.
KsnLen - length of ksn.
Kcv_Alg - kcv algorithm.(0-ecb, 1-cmac).
Kcv_IV - kcv iv data.
Kcv_IVLen - length of kcv iv data.
Output
out_kcv - response kcv.

outLen - length of reponse kcv.
Return
0-success, else: failed.
Remark
```

### Pinpad / DukptAesInitialTr31

- Source section: `3.30`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Load tdes dukpt key in tr31 format


Signature/prototype:
```java
int DukptAesInitialTr31(int KbpkUsage, int KbpkKeyNo, int keySetNum, int isBdk, int driveKeyType, byte[] KeyInfo, int KeyLen, byte[] Ksn, int KsnLen, int Kcv_Alg, byte[] Kcv_IV, int Kcv_IVLen, byte[] out_kcv, int[] outLen)
```


Parameters:
Input
KbpkUsage - kbpk type.
Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.
KbpkKeyNo - kbpk index.
keySetNum - dukpt key index.
isBdk - bdk or ipek.(0-ipek, others-bdk).
driveKeyType:
Refer to com.urovo.sdk.pinpad.utils.Constant.DukptKeyType.
KeyInfo - tr31 key block.
KeyLen - length of tr31 key block.
Ksn - ksn.
KsnLen - length of ksn.
Kcv_Alg - kcv algorithm.(0-ecb, 1-cmac).
Kcv_IV - kcv iv data.
Kcv_IVLen - length of kcv iv data.
Output
out_kcv: response kcv.

outLen: length of reponse kcv.


Return value:
0-success, else: failed.


Source details:
```text
Prototype
int DukptAesInitialTr31(int KbpkUsage, int KbpkKeyNo, int keySetNum, int isBdk, int driveKeyType, byte[] KeyInfo, int KeyLen, byte[] Ksn, int KsnLen, int Kcv_Alg, byte[] Kcv_IV, int Kcv_IVLen, byte[] out_kcv, int[] outLen)
Description
Load tdes dukpt key in tr31 format
Parameters
Input
KbpkUsage - kbpk type.
Refer to com.urovo.sdk.pinpad.utils.Constant.KeyType.
KbpkKeyNo - kbpk index.
keySetNum - dukpt key index.
isBdk - bdk or ipek.(0-ipek, others-bdk).
driveKeyType:
Refer to com.urovo.sdk.pinpad.utils.Constant.DukptKeyType.
KeyInfo - tr31 key block.
KeyLen - length of tr31 key block.
Ksn - ksn.
KsnLen - length of ksn.
Kcv_Alg - kcv algorithm.(0-ecb, 1-cmac).
Kcv_IV - kcv iv data.
Kcv_IVLen - length of kcv iv data.
Output
out_kcv: response kcv.

outLen: length of reponse kcv.
Return
0-success, else: failed.
Remark
```

### Pinpad / generateRSAKey

- Source section: `3.31`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Generates RSA key pair.


Signature/prototype:
```java
int generateRSAKey(int rsaIndex, int keySize, String exponent)
```


Parameters:
Input
rsaIndex - 0-9.
keySize - 512, 1024, 2048, 3072, 4096.
exponent: - default value is 010001.
Output
None


Return value:
0:success, others:failed


Source details:
```text
Prototype
int generateRSAKey(int rsaIndex, int keySize, String exponent)
Description
Generates RSA key pair.
Parameters
Input
rsaIndex - 0-9.
keySize - 512, 1024, 2048, 3072, 4096.
exponent: - default value is 010001.
Output
None
Return
0:success, others:failed
Remark
```

### Pinpad / readRSAPublicKey

- Source section: `3.32`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Read RSA public key from device.


Signature/prototype:
```java
RSAPublicKey readRSAPublicKey(int rsaIndex)
```


Parameters:
Input
rsaIndex - 0-9.
Output
None


Return value:
The RSA public key.


Source details:
```text
Prototype
RSAPublicKey readRSAPublicKey(int rsaIndex)
Description
Read RSA public key from device.
Parameters
Input
rsaIndex - 0-9.
Output
None
Return
The RSA public key.
Remark
```

### Pinpad / calculateWithRSAPrivateKey

- Source section: `3.33`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Calculation with RSA private key.


Signature/prototype:
```java
byte[] calculateWithRSAPrivateKey(int rsaIndex, byte[] data)
```


Parameters:
Input
rsaIndex - 0-9.

data -the data used for calculation.(Data with padding).
Output
None


Return value:
The calculation result.


Source details:
```text
Prototype
byte[] calculateWithRSAPrivateKey(int rsaIndex, byte[] data)
Description
Calculation with RSA private key.
Parameters
Input
rsaIndex - 0-9.

data -the data used for calculation.(Data with padding).
Output
None
Return
The calculation result.
Remark
```

### Pinpad / calculateWithRSAPublicKey

- Source section: `3.34`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Calculation with RSA public key.


Signature/prototype:
```java
byte[] calculateWithRSAPublicKey(int rsaIndex, byte[] data)
```


Parameters:
Input
rsaIndex - 0-9.

data -the data used for calculation.(Data with padding).
Output
None


Return value:
The calculation result.


Source details:
```text
Prototype
byte[] calculateWithRSAPublicKey(int rsaIndex, byte[] data)
Description
Calculation with RSA public key.
Parameters
Input
rsaIndex - 0-9.

data -the data used for calculation.(Data with padding).
Output
None
Return
The calculation result.
Remark
```

### Pinpad / isDukptKeyExist

- Source section: `3.35`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
Determine is the dukpt key exist.


Signature/prototype:
```java
boolean isDukptKeyExist(boolean aesAlg, int keyIndex, byte[] outKsn)
```


Parameters:
Input
aesAlg - is aes algorithm.

keyIndex - key index.

outKsn - the ksn.
Output
None


Return value:
The calculation result.


Source details:
```text
Prototype
boolean isDukptKeyExist(boolean aesAlg, int keyIndex, byte[] outKsn)
Description
Determine is the dukpt key exist.
Parameters
Input
aesAlg - is aes algorithm.

keyIndex - key index.

outKsn - the ksn.
Output
None
Return
The calculation result.
Remark
```

### Pinpad / getLastErrorCode

- Source section: `3.36`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
The last error code


Signature/prototype:
```java
int getLastErrorCode()
```


Parameters:
Input
None
Output
None


Return value:
The error code.


Usage notes:
Refer to the Pinpad Error Code Definition.


Source details:
```text
Prototype
int getLastErrorCode()
Description
The last error code
Parameters
Input
None
Output
None
Return
The error code.
Remark
Refer to the Pinpad Error Code Definition.
```

### Pinpad / getLastErrorMsg

- Source section: `3.35`

- Package/class path: `com.urovo.sdk.pinpad.PinPadProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PinpadActivity.java and UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/utils/PinpadUtil.java`



Purpose:
The last error message.


Signature/prototype:
```java
String getLastErrorMsg()
```


Parameters:
Input
None
Output
None


Return value:
The error message.


Usage notes:
Pinpad Error Code Definition
Error Code
Error Description
0x00
Success
0x01
Unsupported command
0x02
Command length error
0x03
Command separator error
0x04
Command separator length error
0x05
command head (CB) error
0x06
Data crc16 error
0x07
Message mac error
0x09
Message format error
0x0C
Unsupported algorithm
0x0D
Unsupported format
0x0E
Unsupported mode
0x12
Keys not ready or init
0x14
Keys number not found
0x15
Keys number out of range
0x16
Keys download
0x17
Keys not download
0x18
Keys out of space
0x19
Keys exists or can not be overwrite
0x1A
Keys encrypt keys not download
0x1B
Keys unsupported specified use
0x1C
Unsupported keys length
0x1D
Reserved
0x28
Unsupported pinblock format
0x29
Username length error
0x2A
Username value error
0x2B
User pinblock length error
0x2C
User pinblock value error
0x2D
Pinblock char error
100
Parameter is null or empty
101
Mismatch data length
102
Invalid data length
103
Invalid key index
104
Invalid parameter
105
InvalidRSA public key
106
Runtime exption


Source details:
```text
Prototype
String getLastErrorMsg()
Description
The last error message.
Parameters
Input
None
Output
None
Return
The error message.
Remark

Pinpad Error Code Definition
Error Code
Error Description
0x00
Success
0x01
Unsupported command
0x02
Command length error
0x03
Command separator error
0x04
Command separator length error
0x05
command head (CB) error
0x06
Data crc16 error
0x07
Message mac error
0x09
Message format error
0x0C
Unsupported algorithm
0x0D
Unsupported format
0x0E
Unsupported mode
0x12
Keys not ready or init
0x14
Keys number not found
0x15
Keys number out of range
0x16
Keys download
0x17
Keys not download
0x18
Keys out of space
0x19
Keys exists or can not be overwrite
0x1A
Keys encrypt keys not download
0x1B
Keys unsupported specified use
0x1C
Unsupported keys length
0x1D
Reserved
0x28
Unsupported pinblock format
0x29
Username length error
0x2A
Username value error
0x2B
User pinblock length error
0x2C
User pinblock value error
0x2D
Pinblock char error
Parameter is null or empty
Mismatch data length
Invalid data length
Invalid key index
Invalid parameter
InvalidRSA public key
Source note: section truncated for chunk size; consult the source document for remaining detailed tables (81 source lines total).
```

### Printer / getStatus

- Source section: `4.1`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Get printer status


Signature/prototype:
```java
int getStatus()
```


Parameters:
input
None
output
None


Return value:
Printer status.
/**
0x00-success;
0xF0-no paper;
0xF3-over heart;
0xE1-low val;
0xF7-printer busy;
0xFB-printer error;
0xF2-hardware error;
others:print failed;
**/


Usage notes:
Refer to com.urovo.sdk.print.PrintStatus.


Source details:
```text
Prototype
int getStatus()
Description
Get printer status
Parameters
input
None
output
None
Return
Printer status.
/**
0x00-success;
0xF0-no paper;
0xF3-over heart;
0xE1-low val;
0xF7-printer busy;
0xFB-printer error;
0xF2-hardware error;
others:print failed;
**/
Remark
Refer to com.urovo.sdk.print.PrintStatus.
```

### Printer / setGrayLevel

- Source section: `4.2`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Set printer grayscale


Signature/prototype:
```java
void setGray(int gray)
```


Parameters:
input
Gray-print grayscale,-6~6 level
output
None


Source details:
```text
Prototype
void setGray(int gray)
Description
Set printer grayscale
Parameters
input
Gray-print grayscale,-6~6 level
output
None
Return

Remark
```

### Printer / addText

- Source section: `4.3`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add a line of printed text in the specified format


Signature/prototype:
```java
void addText(Bundle format, String text)
```


Parameters:
input
format - Specify print font format
*font(int): 0:small,1:normal,2:large,
default: 1:normal font.
*fontBold(boolean): default: false.
*align(int): alignment, default left, 0: left, 1: center, 2: right
*fontName(string):the path of customization ttf file. eg: /sdcard/xxx.ttf.
*fontSize(int):if this parameter exist, will ignore the font.
*lineHeight(int): the distance of each line.
text - print text
output
None


Usage notes:
Refer to com.urovo.sdk.print.PrintFormat.


Source details:
```text
Prototype
void addText(Bundle format, String text)
Description
Add a line of printed text in the specified format
Parameters
input
format - Specify print font format
*font(int): 0:small,1:normal,2:large,
default: 1:normal font.
*fontBold(boolean): default: false.
*align(int): alignment, default left, 0: left, 1: center, 2: right
*fontName(string):the path of customization ttf file. eg: /sdcard/xxx.ttf.
*fontSize(int):if this parameter exist, will ignore the font.
*lineHeight(int): the distance of each line.
text - print text
output
None
Return

Remark
Refer to com.urovo.sdk.print.PrintFormat.
```

### Printer / addText_Left_Right(Deprecated)

- Source section: `4.4`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add a line of printed text in the specified format


Signature/prototype:
```java
void addTextLeft_Right(String textLeft, String textRight,
int font, boolean fontBold)
```


Parameters:
input
textLeft - print left content.
textRight - print right content.
font(int) - font,0:small,1:normal,2:large,
default :normal font.
fontBold(boolean) - default: false.
output
None


Usage notes:
Refer to com.urovo.sdk.print.PrintFormat.
Suggest using method 4.5.


Source details:
```text
Prototype
void addTextLeft_Right(String textLeft, String textRight,
int font, boolean fontBold)
Description
Add a line of printed text in the specified format
Parameters
input
textLeft - print left content.
textRight - print right content.
font(int) - font,0:small,1:normal,2:large,
default :normal font.
fontBold(boolean) - default: false.
output
None
Return

Remark
Refer to com.urovo.sdk.print.PrintFormat.
Suggest using method 4.5.
```

### Printer / addText_Left_Right

- Source section: `4.5`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add a line of printed text in the specified format


Signature/prototype:
```java
void addTextLeft_Right(Bundle format, String textLeft,
String textRigh)
```


Parameters:
input
format - Specify print font format
/**
*font(int):font,0:small,1:normal,2:large,
default: 1:normal font.
*fontBold(boolean):,default: false.
*align(int): alignment, default left, 0: left, 1: center, 2: right
*fontName(string) : font to be used, otherwise, default system font is used. Or custom fonts i.e. /mnt/sdcard/xxx.ttf the path.
*fontSize(int):if this parameter exist, will ignore the font.
*lineHeight(int): the distance of each line.
**/
textLeft - print left contet.
textRight - print right content.
output
None


Usage notes:
Refer to com.urovo.sdk.print.PrintFormat.


Source details:
```text
Prototype
void addTextLeft_Right(Bundle format, String textLeft,
String textRigh)
Description
Add a line of printed text in the specified format
Parameters
input
format - Specify print font format
/**
*font(int):font,0:small,1:normal,2:large,
default: 1:normal font.
*fontBold(boolean):,default: false.
*align(int): alignment, default left, 0: left, 1: center, 2: right
*fontName(string) : font to be used, otherwise, default system font is used. Or custom fonts i.e. /mnt/sdcard/xxx.ttf the path.
*fontSize(int):if this parameter exist, will ignore the font.
*lineHeight(int): the distance of each line.
**/
textLeft - print left contet.
textRight - print right content.
output
None
Return

Remark
Refer to com.urovo.sdk.print.PrintFormat.
```

### Printer / addText_Left_Right_Center(Deprecated)

- Source section: `4.6`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add a line of printed text in the specified format


Signature/prototype:
```java
void addTextLeft_Right_Center(String textLeft,
String textCenter, String textRight, int font,
boolean fontBold)
```


Parameters:
input
textLeft - print left content.
textCenter - print center contet.
textRigh - print right content.
font(int) - font,0:small,1:normal,2:large.
default: normal font.
fontBold(boolean) - default: false.
output
None


Usage notes:
Refer to com.urovo.sdk.print.PrintFormat.
Suggest using method 4.7.


Source details:
```text
Prototype
void addTextLeft_Right_Center(String textLeft,
String textCenter, String textRight, int font,
boolean fontBold)
Description
Add a line of printed text in the specified format
Parameters
input
textLeft - print left content.
textCenter - print center contet.
textRigh - print right content.
font(int) - font,0:small,1:normal,2:large.
default: normal font.
fontBold(boolean) - default: false.
output
None
Return

Remark
Refer to com.urovo.sdk.print.PrintFormat.
Suggest using method 4.7.
```

### Printer / addText_Left_Right_Center

- Source section: `4.7`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add a line of printed text in the specified format


Signature/prototype:
```java
void addTextLeft_Right_Center(Bundle format,
String textLeft, String textCenter, String textRight)
```


Parameters:
input
format - Specify print font format
/**
*font(int):font,0:small,1:normal,2:large,
default: 1:normal font.
*fontBold(boolean):,default: false.
*align(int): alignment, default left, 0: left, 1: center, 2: right
*fontName(string) : font to be used, otherwise, default system font is used. Or custom fonts i.e. /mnt/sdcard/xxx.ttf the path.
*fontSize(int):if this parameter exist, will ignore the font.
*lineHeight(int): the distance of each line.
**/
textLeft - print left content.
textCenter - print center content.
textRigh - print right content.
output
None


Usage notes:
Refer to com.urovo.sdk.print.PrintFormat.


Source details:
```text
Prototype
void addTextLeft_Right_Center(Bundle format,
String textLeft, String textCenter, String textRight)
Description
Add a line of printed text in the specified format
Parameters
input
format - Specify print font format
/**
*font(int):font,0:small,1:normal,2:large,
default: 1:normal font.
*fontBold(boolean):,default: false.
*align(int): alignment, default left, 0: left, 1: center, 2: right
*fontName(string) : font to be used, otherwise, default system font is used. Or custom fonts i.e. /mnt/sdcard/xxx.ttf the path.
*fontSize(int):if this parameter exist, will ignore the font.
*lineHeight(int): the distance of each line.
**/
textLeft - print left content.
textCenter - print center content.
textRigh - print right content.
output
None
Return

Remark
Refer to com.urovo.sdk.print.PrintFormat.
```

### Printer / addBarCode

- Source section: `4.8`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add barcode print.


Signature/prototype:
```java
void addBarCode(Bundle format, String barcode)
```


Parameters:
input
format - print format.
align(int): 0: left,1: center,2: right
width(int): width
height(int): height
barcode_type: the barcode type.
barcode - barcode content.
output
None


Usage notes:
Refer to com.urovo.sdk.print.PrintFormat.


Source details:
```text
Prototype
void addBarCode(Bundle format, String barcode)
Description
Add barcode print.
Parameters
input
format - print format.
align(int): 0: left,1: center,2: right
width(int): width
height(int): height
barcode_type: the barcode type.
barcode - barcode content.
output
None
Return

Remark
Refer to com.urovo.sdk.print.PrintFormat.
```

### Printer / addQrCode

- Source section: `4.9`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add QR code print


Signature/prototype:
```java
void addQrCode(Bundle format, String qrCode)
```


Parameters:
input
format - print format
align(int): 0: left,1: center,2: right.
(Takes effect when offset=-1).
offset(int): print start position.
expectedHeight(int): expected height
qrCode - QR code content.
output
None


Usage notes:
Refer to com.urovo.sdk.print.PrintFormat.


Source details:
```text
Prototype
void addQrCode(Bundle format, String qrCode)
Description
Add QR code print
Parameters
input
format - print format
align(int): 0: left,1: center,2: right.
(Takes effect when offset=-1).
offset(int): print start position.
expectedHeight(int): expected height
qrCode - QR code content.
output
None
Return

Remark
Refer to com.urovo.sdk.print.PrintFormat.
```

### Printer / addImage

- Source section: `4.10`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add bitmap image print.


Signature/prototype:
```java
void addImage(Bundle format, byte[] imageData)
```


Parameters:
input
format - print format, set print position, height,width.
offset(int): print start position, if offset<=0, aligh, align takes effect.
width(int): width.(max is 380)
height(int): height
imageData - image content.
output
None


Source details:
```text
Prototype
void addImage(Bundle format, byte[] imageData)
Description
Add bitmap image print.
Parameters
input
format - print format, set print position, height,width.
offset(int): print start position, if offset<=0, aligh, align takes effect.
width(int): width.(max is 380)
height(int): height
imageData - image content.
output
None
Return

Remark
```

### Printer / feedLine

- Source section: `4.11`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add feedline.


Signature/prototype:
```java
void feedLine(int lines)
```


Parameters:
input
lines - number of lines.
//If lines=0, add blank space at the bottom.
//If lines=-1, no blank space at the bottom.
output
None


Usage notes:
Refer to com.urovo.sdk.print.PrintFormat.


Source details:
```text
Prototype
void feedLine(int lines)
Description
Add feedline.
Parameters
input
lines - number of lines.
//If lines=0, add blank space at the bottom.
//If lines=-1, no blank space at the bottom.
output
None
Return

Remark
Refer to com.urovo.sdk.print.PrintFormat.
```

### Printer / startPrint

- Source section: `4.12`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Start the printing process.


Signature/prototype:
```java
int startPrint()
```


Return value:
printStatus -Refer to com.urovo.sdk.print.PrintStatus.
/**
0x00-success;
0xF0-no paper;
0xF3-over heart;
0xE1-low val;
0xF7-printer busy;
0xF2-hardware error;
others:print failed;
**/


Simplified example:
```java
PrinterProviderImpl printer = PrinterProviderImpl.getInstance(context);
printer.addText("APPROVED", 24, Align.CENTER, true);
printer.feedLine(3);
printer.startPrint();
```


Source details:
```text
Prototype
int startPrint()
Description
Start the printing process.
Return
printStatus -Refer to com.urovo.sdk.print.PrintStatus.
/**
0x00-success;
0xF0-no paper;
0xF3-over heart;
0xE1-low val;
0xF7-printer busy;
0xF2-hardware error;
others:print failed;
**/
```

### Printer / addImageWithText

- Source section: `4.13`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add bitmap image print


Signature/prototype:
```java
void addImageWithText(Bundle format, byte[] imageData)
```


Parameters:
input
format - print format, set print position, height,width.
align: for image, 0-left, 1-center, 2-right.
offset(int): print start position, if offset<=0, aligh, align takes effect.
width(int): width.
height(int): height.
YAlign(int): for text, 0-top, 1-center, 2-bottom.
text(String): text to content.
font(int): font size, 0-samll, 1-normal, 2-large.
fontBold(boolean): default: false.
align(int): alignment, default left, 0: left, 1: center, 2: right
fontName(string) : font to be used, otherwise, default system font is used. Or custom fonts i.e. /mnt/sdcard/xxx.ttf the path.
imageData - image content.
output
None


Source details:
```text
Prototype
void addImageWithText(Bundle format, byte[] imageData)
Description
Add bitmap image print
Parameters
input
format - print format, set print position, height,width.
align: for image, 0-left, 1-center, 2-right.
offset(int): print start position, if offset<=0, aligh, align takes effect.
width(int): width.
height(int): height.
YAlign(int): for text, 0-top, 1-center, 2-bottom.
text(String): text to content.
font(int): font size, 0-samll, 1-normal, 2-large.
fontBold(boolean): default: false.
align(int): alignment, default left, 0: left, 1: center, 2: right
fontName(string) : font to be used, otherwise, default system font is used. Or custom fonts i.e. /mnt/sdcard/xxx.ttf the path.
imageData - image content.
output
None
Return

Remark
```

### Printer / addHtml

- Source section: `4.14`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add html content.


Signature/prototype:
```java
void addHtml(Bundle format, String content)
```


Parameters:
input
format - print format, set print position, height,width
offset(int): print start position, if offset<=0, aligh, align takes effect.
width(int): width.(max is 380)
height(int): height
content: html content.
output
None


Source details:
```text
Prototype
void addHtml(Bundle format, String content)
Description
Add html content.
Parameters
input
format - print format, set print position, height,width
offset(int): print start position, if offset<=0, aligh, align takes effect.
width(int): width.(max is 380)
height(int): height
content: html content.
output
None
Return

Remark
```

### Printer / addBlankLine

- Source section: `4.15`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add blank line.


Signature/prototype:
```java
void addBlankLine(int height)
```


Parameters:
input
height - the blank height.
output
None


Source details:
```text
Prototype
void addBlankLine(int height)
Description
Add blank line.
Parameters
input
height - the blank height.
output
None
Return

Remark
```

### Printer / setSpeed

- Source section: `4.16`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Set printer speed.


Signature/prototype:
```java
int setSpeed(int speedLevel)
```


Parameters:
input
speedLevel - 10-20.
output
None


Return value:
0-success, others:failed.


Source details:
```text
Prototype
int setSpeed(int speedLevel)
Description
Set printer speed.
Parameters
input
speedLevel - 10-20.
output
None
Return
0-success, others:failed.
Remark
```

### Printer / addBlackLine

- Source section: `4.17`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Add blank line.


Signature/prototype:
```java
void addBlackLine(int... lineHeight)
```


Parameters:
input
lineHeight - the distance of each line.
//Opitional, variable length.
output
None


Source details:
```text
Prototype
void addBlackLine(int... lineHeight)
Description
Add blank line.
Parameters
input
lineHeight - the distance of each line.
//Opitional, variable length.
output
None
Return

Remark
```

### Printer / supportLabelPrint

- Source section: `4.18`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Return whether label printing function is supported.


Signature/prototype:
```java
boolean supportLabelPrint()
```


Return value:
True：Supported
False：Not supported


Source details:
```text
Prototype
boolean supportLabelPrint()
Description
Return whether label printing function is supported.
Return
True：Supported
False：Not supported
```

### Printer / setPrinterMode

- Source section: `4.19`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Set printer mode.


Signature/prototype:
```java
int setPrinterMode(boolean data)
```


Parameters:
input
true - Label mode
false - Normal mode.
output
None


Return value:
True：Setting successful
False：Setting failed.


Source details:
```text
Prototype
int setPrinterMode(boolean data)
Description
Set printer mode.
Parameters
input
true - Label mode
false - Normal mode.
output
None
Return
True：Setting successful
False：Setting failed.
```

### Printer / setLabelFeed

- Source section: `4.20`

- Package/class path: `com.urovo.sdk.print.PrinterProviderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/PrintActivity.java`



Purpose:
Set label printing mode.
Before executing label printing each time, you need to set PRN_LABEL_LOCATION or PRN_LABEL_CONTINUE. After printing is completed, you need to set PRN_LABEL_END.


Signature/prototype:
```java
int setLabelFeed(byte state)
```


Parameters:
input
com.urovo.sdk.print.PrinterLabelState
PRN_LABEL_STUDY = 0x00;
(Learning label height, this feature is not available.)

PRN_LABEL_LOCATION = 0x01;
(Label positioning, can be set for both single and multiple prints.)

PRN_LABEL_CONTINUE = 0x02;
(Label continuous printing settings.)

PRN_LABEL_END = 0x03;
(Settings after label printing is completed.)
output
None


Return value:
0：Setting successful
Other：Setting failed.

Printer Error Code Definition
Error Code
Error Description
0x00
Success
0xF0
No paper
0xF3
Over heart
0xE1
Low val
0xF2
Hardware error
0xF7
Printer busy


Source details:
```text
Prototype
int setLabelFeed(byte state)
Description
Set label printing mode.
Before executing label printing each time, you need to set PRN_LABEL_LOCATION or PRN_LABEL_CONTINUE. After printing is completed, you need to set PRN_LABEL_END.
Parameters
input
com.urovo.sdk.print.PrinterLabelState
PRN_LABEL_STUDY = 0x00;
(Learning label height, this feature is not available.)

PRN_LABEL_LOCATION = 0x01;
(Label positioning, can be set for both single and multiple prints.)

PRN_LABEL_CONTINUE = 0x02;
(Label continuous printing settings.)

PRN_LABEL_END = 0x03;
(Settings after label printing is completed.)
output
None
Return
0：Setting successful
Other：Setting failed.

Printer Error Code Definition
Error Code
Error Description
0x00
Success
0xF0
No paper
0xF3
Over heart
0xE1
Low val
0xF2
Hardware error
0xF7
Printer busy
```

### Serial Port(Deprecated, suggest using module 14.SerialTool) / Open

- Source section: `5.1`

- Package/class path: `com.urovo.smartpos.device.core.SerialPortDriverImpl`



Purpose:
Open the serial port.


Signature/prototype:
```java
boolean open(String portPath, int bps, int par, int dbs)
```


Parameters:
Input
portPath - The serial port path.

bps - Baud rate
*
* - BPS_1200(0x01) - 1200
* - BPS_2400(0x02) - 2400
* - BPS_4800(0x03) - 4800
* - BPS_9600(0x04) - 9600
* - BPS_14400(0x05) - 14400
* - BPS_28800(0x06) - 28800
* - BPS_19200(0x07) - 19200
* - BPS_57600(0x08) - 57600
* - BPS_115200(0x09) - 115200
* - BPS_38400(0x0A) - 38400
*
par - check
*
* - PAR_NOPAR('N')
* - PAR_EVEN('E')
* - PAR_ODD('O')
*
dbs - data bits
*
* - DBS_7(0x07) - 7
* - DBS_8(0x08) - 8
*
Output
None


Return value:
Return true when update successful, return false when update failed.


Source details:
```text
Prototype
boolean open(String portPath, int bps, int par, int dbs)
Description
Open the serial port.
Parameters
Input
portPath - The serial port path.

bps - Baud rate
*
* - BPS_1200(0x01) - 1200
* - BPS_2400(0x02) - 2400
* - BPS_4800(0x03) - 4800
* - BPS_9600(0x04) - 9600
* - BPS_14400(0x05) - 14400
* - BPS_28800(0x06) - 28800
* - BPS_19200(0x07) - 19200
* - BPS_57600(0x08) - 57600
* - BPS_115200(0x09) - 115200
* - BPS_38400(0x0A) - 38400
*
par - check
*
* - PAR_NOPAR('N')
* - PAR_EVEN('E')
* - PAR_ODD('O')
*
dbs - data bits
*
* - DBS_7(0x07) - 7
* - DBS_8(0x08) - 8
*
Output
None
Return
Return true when update successful, return false when update failed.
Remark
```

### Serial Port(Deprecated, suggest using module 14.SerialTool) / close

- Source section: `5.2`

- Package/class path: `com.urovo.smartpos.device.core.SerialPortDriverImpl`



Purpose:
Stop the serial port.


Signature/prototype:
```java
boolean close()
```


Parameters:
Input
None
Output
None


Return value:
None


Usage notes:
Return true when update successful, return false when update failed.


Source details:
```text
Prototype
boolean close()
Description
Stop the serial port.
Parameters
Input
None
Output
None
Return
None
Remark
Return true when update successful, return false when update failed.
```

### Serial Port(Deprecated, suggest using module 14.SerialTool) / read

- Source section: `5.3`

- Package/class path: `com.urovo.smartpos.device.core.SerialPortDriverImpl`



Purpose:
Read data from serial port.


Signature/prototype:
```java
int read(out byte[] buffer, int timeout)
```


Parameters:
Input
timeout - tiem out(millisecond)
Output
buffer - read data buffer


Return value:
Return true when update successful, return false when update failed.


Source details:
```text
Prototype
int read(out byte[] buffer, int timeout)
Description
Read data from serial port.
Parameters
Input
timeout - tiem out(millisecond)
Output
buffer - read data buffer
Return
Return true when update successful, return false when update failed.
Remark
```

### Serial Port(Deprecated, suggest using module 14.SerialTool) / send

- Source section: `5.4`

- Package/class path: `com.urovo.smartpos.device.core.SerialPortDriverImpl`



Purpose:
Send data to serial port.


Signature/prototype:
```java
int write(byte[] data, int timeout)
```


Parameters:
Input
data - data sent.
timeout - tiem out(millisecond)
Output
None


Return value:
None


Usage notes:
Return the number of bytes actually sent when sent successful, return -1 when sent failed.


Source details:
```text
Prototype
int write(byte[] data, int timeout)
Description
Send data to serial port.
Parameters
Input
data - data sent.
timeout - tiem out(millisecond)
Output
None
Return
None
Remark
Return the number of bytes actually sent when sent successful, return -1 when sent failed.
```

### Serial Port(Deprecated, suggest using module 14.SerialTool) / clearInputBuffer

- Source section: `5.5`

- Package/class path: `com.urovo.smartpos.device.core.SerialPortDriverImpl`



Purpose:
Empty reveive buffer.


Signature/prototype:
```java
boolean clearInputBuffer()
```


Parameters:
Input
None
Output
None


Return value:
Return true when update successful, return false when update failed.


Source details:
```text
Prototype
boolean clearInputBuffer()
Description
Empty reveive buffer.
Parameters
Input
None
Output
None
Return
Return true when update successful, return false when update failed.
Remark
```

### Serial Port(Deprecated, suggest using module 14.SerialTool) / isbufferEmpty

- Source section: `5.6`

- Package/class path: `com.urovo.smartpos.device.core.SerialPortDriverImpl`



Purpose:
Check that the buffer is empty.


Signature/prototype:
```java
boolean isBufferEmpty(boolean input)
```


Parameters:
Input
input - true(input buffer), false(output buff).
Output
None


Return value:
Return true when update successful, return false when update failed.


Source details:
```text
Prototype
boolean isBufferEmpty(boolean input)
Description
Check that the buffer is empty.
Parameters
Input
input - true(input buffer), false(output buff).
Output
None
Return
Return true when update successful, return false when update failed.
Remark
```

### Scanner / startScan

- Source section: `6.1`

- Package/class path: `com.urovo.sdk.scanner.InnerScannerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/ScanActivity.java`



Purpose:
Start scanning code.


Signature/prototype:
```java
void startScan(Context context, Bundle bundle, int cameraId, long timeout, ScannerListener listener)
```


Parameters:
Input
context - context.
bundle: refer to com.urovo.sdk.scanner.utils.Constant.Scankey.
*title(String).
*upPromptString(String).
*downPromptString(String).
*flash_enable(boolean).
*codeType_disable(StringArray):
(refer to com.urovo.sdk.scanner.utils.Constant.CodeType).
cameraId(int) - 0:front, 1:back, 2:top.
(Refer to com.urovo.sdk.scanner.utils.Constant.CameraID)
timeout(int) - time out(seconds).

listener - scanning result listener.
Output
None


Usage notes:
/**
* Scanning process listener interface definition
*/
interface ScannerListener {
/**
* Code scanning successful
* @param data
* @param byData
*/
void onSuccess(String data, byte[] byData);
/**
* Code scanning error
* @param error - error code
* @param message - error description
*/
void onError(int error, String message);
/**
* Code scanning timeout*/
void onTimeout();
/**
* Code scanning cancel*/
void onCancel();

}


Simplified example:
```java
Bundle options = new Bundle();
InnerScannerImpl.getInstance(context).startScan(context, options, 0, 30, listener);
```


Source details:
```text
Prototype
void startScan(Context context, Bundle bundle, int cameraId, long timeout, ScannerListener listener)
Description
Start scanning code.
Parameters
Input
context - context.
bundle: refer to com.urovo.sdk.scanner.utils.Constant.Scankey.
*title(String).
*upPromptString(String).
*downPromptString(String).
*flash_enable(boolean).
*codeType_disable(StringArray):
(refer to com.urovo.sdk.scanner.utils.Constant.CodeType).
cameraId(int) - 0:front, 1:back, 2:top.
(Refer to com.urovo.sdk.scanner.utils.Constant.CameraID)
timeout(int) - time out(seconds).

listener - scanning result listener.
Output
None
Return

Remark
/**
* Scanning process listener interface definition
*/
interface ScannerListener {
/**
* Code scanning successful
* @param data
* @param byData
*/
void onSuccess(String data, byte[] byData);
/**
* Code scanning error
* @param error - error code
* @param message - error description
*/
void onError(int error, String message);
/**
* Code scanning timeout*/
void onTimeout();
/**
* Code scanning cancel*/
void onCancel();

}
```

### Scanner / stopScan

- Source section: `6.2`

- Package/class path: `com.urovo.sdk.scanner.InnerScannerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/ScanActivity.java`



Purpose:
Stop scanning.


Signature/prototype:
```java
void stopScan()
```


Parameters:
Input
None
Output
None


Return value:
None


Simplified example:
```java
InnerScannerImpl.getInstance(context).stopScan();
```


Source details:
```text
Prototype
void stopScan()
Description
Stop scanning.
Parameters
Input
None
Output
None
Return
None
Remark
```

### Scanner Custom / startScan

- Source section: `7.1`

- Package/class path: `com.urovo.sdk.scanner.InnerScannerCustomImpl`



Purpose:
Start scanning code.


Signature/prototype:
```java
void startScan(Context context, View view, Bundle bundle,
ScannerListener listener)
```


Parameters:
Input
context: context.
view: layout view.
bundle : refer to com.urovo.sdk.scanner.utils.Constant.Scankey.
*cameraId(int) - 0:front, 1:back, 2:top.
(Refer to com.urovo.sdk.scanner.utils.Constant.CameraID)
*timeout(int) - time out(seconds).
codeType_disable(String[]): The code types that need to be disabled.
(Refer to com.urovo.sdk.scanner.utils.Constant.CodeType)
*codeType_disable(StringArray):
(refer to com.ubx.scanner.utils.Constant.CodeType).

listener - scanning result listener.
Output
None


Usage notes:
/**
* Scanning process listener interface definition
*/
interface ScannerListener {
/**
* Code scanning successful
* @param data
* @param byData
*/
void onSuccess(String data, byte[] byData);
/**
* Code scanning error
* @param error - error code
* @param message - error description
*/
void onError(int error, String message);
/**
* Code scanning timeout*/
void onTimeout();
/**
* Code scanning cancel*/
void onCancel();

}


Source details:
```text
Prototype
void startScan(Context context, View view, Bundle bundle,
ScannerListener listener)
Description
Start scanning code.
Parameters
Input
context: context.
view: layout view.
bundle : refer to com.urovo.sdk.scanner.utils.Constant.Scankey.
*cameraId(int) - 0:front, 1:back, 2:top.
(Refer to com.urovo.sdk.scanner.utils.Constant.CameraID)
*timeout(int) - time out(seconds).
codeType_disable(String[]): The code types that need to be disabled.
(Refer to com.urovo.sdk.scanner.utils.Constant.CodeType)
*codeType_disable(StringArray):
(refer to com.ubx.scanner.utils.Constant.CodeType).

listener - scanning result listener.
Output
None
Return

Remark
/**
* Scanning process listener interface definition
*/
interface ScannerListener {
/**
* Code scanning successful
* @param data
* @param byData
*/
void onSuccess(String data, byte[] byData);
/**
* Code scanning error
* @param error - error code
* @param message - error description
*/
void onError(int error, String message);
/**
* Code scanning timeout*/
void onTimeout();
/**
* Code scanning cancel*/
void onCancel();

}
```

### Scanner Custom / stopScan

- Source section: `7.2`

- Package/class path: `com.urovo.sdk.scanner.InnerScannerCustomImpl`



Purpose:
Stop scanning.


Signature/prototype:
```java
void stopScan()
```


Parameters:
Input
None
Output
None


Return value:
None


Source details:
```text
Prototype
void stopScan()
Description
Stop scanning.
Parameters
Input
None
Output
None
Return
None
Remark
```

### MagCardReader / SearchCard

- Source section: `8.1`

- Package/class path: `com.urovo.sdk.magcard.MagCardReaderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/MagCardReaderActivity.java`



Purpose:
Wait for swiping card to get card magnetic stripe data.


Signature/prototype:
```java
void searchCard(int timeout, MagCardListener listener)
```


Parameters:
Input
timeout - time out(second).
listener - swipe card result listener.
Output
None


Usage notes:
Swip card process listener interface definition：
interface MagCardListener {
/**
* Swip card successful
* @param track - Magnetic card data
*
* - PAN(String) - card number
* - TRACK1(String) - Track 1 data
* - TRACK2(String) - Track 2 data
* - TRACK3(String) - Track 3 data
* - SERVICE_CODE(String) - Service card
* - EXPIRED_DATE(String) - card expired date
* */
void onSuccess(in Bundle track);
/**
* Swipe card error
* @param error - error code
* @param message - error description
*/
void onError(int error, String message);
/**
* Swip card timeout */
void onTimeout();
}


Simplified example:
```java
MagCardReaderImpl.getInstance().searchCard(30, listener);
```


Source details:
```text
Prototype
void searchCard(int timeout, MagCardListener listener)
Description
Wait for swiping card to get card magnetic stripe data.
Parameters
Input
timeout - time out(second).
listener - swipe card result listener.
Output
None
Return

Remark
Swip card process listener interface definition：
interface MagCardListener {
/**
* Swip card successful
* @param track - Magnetic card data
*
* - PAN(String) - card number
* - TRACK1(String) - Track 1 data
* - TRACK2(String) - Track 2 data
* - TRACK3(String) - Track 3 data
* - SERVICE_CODE(String) - Service card
* - EXPIRED_DATE(String) - card expired date
* */
void onSuccess(in Bundle track);
/**
* Swipe card error
* @param error - error code
* @param message - error description
*/
void onError(int error, String message);
/**
* Swip card timeout */
void onTimeout();
}
```

### MagCardReader / stopSearch

- Source section: `8.2`

- Package/class path: `com.urovo.sdk.magcard.MagCardReaderImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/MagCardReaderActivity.java`



Purpose:
Remove the waiting card.


Signature/prototype:
```java
void stopSearch()
```


Parameters:
Input
None
Output
None


Return value:
None


Simplified example:
```java
MagCardReaderImpl.getInstance().stopSearch();
```


Source details:
```text
Prototype
void stopSearch()
Description
Remove the waiting card.
Parameters
Input
None
Output
None
Return
None
Remark
```

### InsertCardReader / powerUp

- Source section: `9.1`

- Package/class path: `com.urovo.sdk.insertcard.InsertCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/ICReaderActivity.java`



Purpose:
Card power up.


Signature/prototype:
```java
int powerUp(byte cardSlot,byte[] atrData)
```


Parameters:
Input
cardSlot - 0:user card ,1:psam1,2:psam2.
Output
None


Return value:
0:success, others:failed.


Simplified example:
```java
byte[] atr = InsertCardHandlerImpl.getInstance().powerUp(Constant.SlotType.USER_CARD);
```


Source details:
```text
Prototype
int powerUp(byte cardSlot,byte[] atrData)
Description
Card power up.
Parameters
Input
cardSlot - 0:user card ,1:psam1,2:psam2.
Output
None
Return
0:success, others:failed.
Remark
```

### InsertCardReader / powerDown

- Source section: `9.2`

- Package/class path: `com.urovo.sdk.insertcard.InsertCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/ICReaderActivity.java`



Purpose:
Card power down.


Signature/prototype:
```java
boolean powerDown(byte cardSlot)
```


Parameters:
Input
cardSlot -0:user card ,1:psam1,2:psam2.
Output
None


Return value:
Return true when power down successful, return false when power down failed.


Source details:
```text
Prototype
boolean powerDown(byte cardSlot)
Description
Card power down.
Parameters
Input
cardSlot -0:user card ,1:psam1,2:psam2.
Output
None
Return
Return true when power down successful, return false when power down failed.
Remark
```

### InsertCardReader / IisCardIn

- Source section: `9.3`

- Package/class path: `com.urovo.sdk.insertcard.InsertCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/ICReaderActivity.java`



Purpose:
Is the card in place.


Signature/prototype:
```java
boolean isCardIn()
```


Parameters:
Input
None
Output
None


Return value:
Return true when card exist, return false when card does not exist.


Source details:
```text
Prototype
boolean isCardIn()
Description
Is the card in place.
Parameters
Input
None
Output
None
Return
Return true when card exist, return false when card does not exist.
Remark
```

### InsertCardReader / exchangeApdu

- Source section: `9.4`

- Package/class path: `com.urovo.sdk.insertcard.InsertCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/ICReaderActivity.java`



Purpose:
APDU data communication.


Signature/prototype:
```java
byte[] exchangeApdu(byte cardSlot, byte[] apdu)
```


Parameters:
Input
cardSlot - 0:user card ,1:psam1,2:psam2.
apdu - apdu data.
Output
None


Return value:
Return card response data when communication successful, return null when communication failed.


Simplified example:
```java
byte[] response = InsertCardHandlerImpl.getInstance().exchangeApdu(Constant.SlotType.USER_CARD, commandApdu);
```


Source details:
```text
Prototype
byte[] exchangeApdu(byte cardSlot, byte[] apdu)
Description
APDU data communication.
Parameters
Input
cardSlot - 0:user card ,1:psam1,2:psam2.
apdu - apdu data.
Output
None
Return
Return card response data when communication successful, return null when communication failed.
Remark
```

### InsertCardReader / isPSAMCardExist

- Source section: `9.5`

- Package/class path: `com.urovo.sdk.insertcard.InsertCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/ICReaderActivity.java`



Purpose:
Is the psam card in place.


Signature/prototype:
```java
boolean isPSAMCardExist(byte cardSlot)
```


Parameters:
Input
cardSlot: psam card slot.


Source details:
```text
Prototype
boolean isPSAMCardExist(byte cardSlot)
Description
Is the psam card in place.
Parameters
Input
cardSlot: psam card slot.
```

### - psam2. / setPsamParameter

- Source section: `9.5`



Purpose:
Set the parameters of the PSAM card.


Signature/prototype:
```java
int setPsamParameter(int slot, int vol, int dataRate, int autoResp, int pps)
```


Parameters:
Input
slot: psam card slot.


Source details:
```text
Prototype
int setPsamParameter(int slot, int vol, int dataRate, int autoResp, int pps)
Description
Set the parameters of the PSAM card.
Parameters
Input
slot: psam card slot.
```

### RFCardReader / searchCard

- Source section: `10.1`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Card search.


Signature/prototype:
```java
void searchCard(RFSearchListener listener)
```


Parameters:
Input
listener - searching card listener.
Output
None


Return value:
None.


Usage notes:
/**
* Searching card process listener interface definition
*/
interface RFSearchListener {
/**
* card search successful
* @param cardType - card type.
Refer to com.urovo.sdk.rfcard.utils.Constant.CardType.
*
* - S50_CARD(0x00) - S50
* - S70_CARD(0x01) - S70
* - PRO_CARD(0x02) - PRO
* - S50_PRO_CARD(0x03) -Pro card supporting S50 driver and pro driver
* - S70_PRO_CARD(0x04) -Pro card supporting S70 driver and pro driver
* - CPU_CARD(0x05) - CPU card
*
* @param uid- card uid
* @param uid- CardInfo: MODE, SAK, ATQA, UID.

*/
void onCardPass(int cardType, byte[] uid, CardInfo cardInfo);
/**
* card searcher failed
* @param error - error code
*
* - ERROR_TRANSERR(0xA2) -Communication failure
* - ERROR_PROTERR(0xA3) - The data returned from the card does not meet the specification requirements
* - ERROR_MULTIERR(0xA4) - There are many cards in the sensing area
* - ERROR_CARDTIMEOUT(0xA7) - No response after timeout
* - ERROR_CARDNOACT(0xB3) - Pro Card or TypeB card not activated
* - ERROR_MCSERVICE_CRASH(0xff01) - Master service exception
* - ERROR_REQUEST_EXCEPTION(0xff02) - Request exception
*
* @param message - error description */
void onFail(int error, String message);
}


Simplified example:
```java
RFCardReaderImpl.getInstance().searchCard(30, listener);
```


Source details:
```text
Prototype
void searchCard(RFSearchListener listener)
Description
Card search.
Parameters
Input
listener - searching card listener.
Output
None
Return
None.
Remark
/**
* Searching card process listener interface definition
*/
interface RFSearchListener {
/**
* card search successful
* @param cardType - card type.
Refer to com.urovo.sdk.rfcard.utils.Constant.CardType.
*
* - S50_CARD(0x00) - S50
* - S70_CARD(0x01) - S70
* - PRO_CARD(0x02) - PRO
* - S50_PRO_CARD(0x03) -Pro card supporting S50 driver and pro driver
* - S70_PRO_CARD(0x04) -Pro card supporting S70 driver and pro driver
* - CPU_CARD(0x05) - CPU card
*
* @param uid- card uid
* @param uid- CardInfo: MODE, SAK, ATQA, UID.

*/
void onCardPass(int cardType, byte[] uid, CardInfo cardInfo);
/**
* card searcher failed
* @param error - error code
*
* - ERROR_TRANSERR(0xA2) -Communication failure
* - ERROR_PROTERR(0xA3) - The data returned from the card does not meet the specification requirements
* - ERROR_MULTIERR(0xA4) - There are many cards in the sensing area
* - ERROR_CARDTIMEOUT(0xA7) - No response after timeout
* - ERROR_CARDNOACT(0xB3) - Pro Card or TypeB card not activated
* - ERROR_MCSERVICE_CRASH(0xff01) - Master service exception
* - ERROR_REQUEST_EXCEPTION(0xff02) - Request exception
*
* @param message - error description */
void onFail(int error, String message);
}
```

### RFCardReader / stopSearch

- Source section: `10.2`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
stop searching card.


Signature/prototype:
```java
void stopSearch()
```


Parameters:
Input
None
Output
None


Return value:
None


Source details:
```text
Prototype
void stopSearch()
Description
stop searching card.
Parameters
Input
None
Output
None
Return
None
Remark
```

### RFCardReader / activate

- Source section: `10.3`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
activate card.


Signature/prototype:
```java
int activate(String driver, byte[] responseData)
```


Parameters:
Input
driver:
*
* - "S50" - S50 card
* - "S70" - S70 card
* - "CPU" - CPU card
* - "PRO" - PRO、S5O_PRO、S70_PRO card
*
Output
responseData - activate card response data


Return value:
The length of activate response, negative number if failed.


Source details:
```text
Prototype
int activate(String driver, byte[] responseData)
Description
activate card.
Parameters
Input
driver:
*
* - "S50" - S50 card
* - "S70" - S70 card
* - "CPU" - CPU card
* - "PRO" - PRO、S5O_PRO、S70_PRO card
*
Output
responseData - activate card response data
Return
The length of activate response, negative number if failed.
Remark
```

### RFCardReader / halt

- Source section: `10.4`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Close the card reader.


Signature/prototype:
```java
void halt()
```


Parameters:
Input
None
Output
None


Return value:
None


Source details:
```text
Prototype
void halt()
Description
Close the card reader.
Parameters
Input
None
Output
None
Return
None
Remark
```

### RFCardReader / isExist

- Source section: `10.5`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Check if the card exist.


Signature/prototype:
```java
boolean isCardIn()
```


Parameters:
Input
None
Output
None


Return value:
Return true when card exist, return false when card does not exist.


Source details:
```text
Prototype
boolean isCardIn()
Description
Check if the card exist.
Parameters
Input
None
Output
None
Return
Return true when card exist, return false when card does not exist.
Remark
```

### RFCardReader / exchangeApdu

- Source section: `10.6`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
APDU data communication.


Signature/prototype:
```java
byte[] exchangeApdu(byte[] apdu)
```


Parameters:
Input
apdu - apdu data.
Output
None


Return value:
Return card response data when communication successful, return null when communication failed.


Source details:
```text
Prototype
byte[] exchangeApdu(byte[] apdu)
Description
APDU data communication.
Parameters
Input
apdu - apdu data.
Output
None
Return
Return card response data when communication successful, return null when communication failed.
Remark
```

### RFCardReader / cardReset

- Source section: `10.7`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Card reset.


Signature/prototype:
```java
byte[] cardReset()
```


Parameters:
Input
cardSlot -0:user card ,1:psam1,2:psam2.
resetType -reset type
Output
None


Return value:
Return card reset data when reset successful, return null when reset failed.


Source details:
```text
Prototype
byte[] cardReset()
Description
Card reset.
Parameters
Input
cardSlot -0:user card ,1:psam1,2:psam2.
resetType -reset type
Output
None
Return
Return card reset data when reset successful, return null when reset failed.
Remark
```

### RFCardReader / authBlock

- Source section: `10.8`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Auth block.


Signature/prototype:
```java
int authBlock(int blockNo, int keyType, byte[] key)
```


Parameters:
Input
blockNo - the block number,0 =<blockNo<=255
Refer to com.urovo.sdk.rfcard.utils.Constant.KeyType.
keyType - KEY_A:0, KEY_B:1.
key: the password.
Output
None


Return value:
0:success, others:failed.


Source details:
```text
Prototype
int authBlock(int blockNo, int keyType, byte[] key)
Description
Auth block.
Parameters
Input
blockNo - the block number,0 =<blockNo<=255
Refer to com.urovo.sdk.rfcard.utils.Constant.KeyType.
keyType - KEY_A:0, KEY_B:1.
key: the password.
Output
None
Return
0:success, others:failed.
Remark
```

### RFCardReader / readBlock

- Source section: `10.9`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Read data from the block.


Signature/prototype:
```java
int readBlock(int blockNo, byte[] blockValue)
```


Parameters:
Input
blockNo - the sector number,0= <blockNo<=255
Output
blockValue - the block value


Return value:
The length of block value, negative number if read failed.


Source details:
```text
Prototype
int readBlock(int blockNo, byte[] blockValue)
Description
Read data from the block.
Parameters
Input
blockNo - the sector number,0= <blockNo<=255
Output
blockValue - the block value
Return
The length of block value, negative number if read failed.
Remark
```

### RFCardReader / writeBlock

- Source section: `10.10`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Write data to the block.


Signature/prototype:
```java
int writeBlock(int blockNo, byte[] blockValue)
```


Parameters:
Input
blockNo - the block number,0= <blockNo<=255
blockValue -the block value
Output


Return value:
0:success, others:failed.


Source details:
```text
Prototype
int writeBlock(int blockNo, byte[] blockValue)
Description
Write data to the block.
Parameters
Input
blockNo - the block number,0= <blockNo<=255
blockValue -the block value
Output

Return
0:success, others:failed.
Remark
```

### RFCardReader / increaseValue

- Source section: `10.11`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Increment the value block by iMoney amount.


Signature/prototype:
```java
int increaseValue(int blockNo, byte[] value)
```


Parameters:
Input
blockNo - the block number,0= <blockNo<=255
value - the value
Output


Return value:
0:success, others:failed.


Source details:
```text
Prototype
int increaseValue(int blockNo, byte[] value)
Description
Increment the value block by iMoney amount.
Parameters
Input
blockNo - the block number,0= <blockNo<=255
value - the value
Output

Return
0:success, others:failed.
Remark
```

### RFCardReader / decreaseValue

- Source section: `10.12`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Decrement the value block by iMoney amount.


Signature/prototype:
```java
int decreaseValue(int blockNo, byte[] value)
```


Parameters:
Input
blockNo - the block number,0= <blockNo<=255
value -the value.
Output


Return value:
0:success, others:failed.


Source details:
```text
Prototype
int decreaseValue(int blockNo, byte[] value)
Description
Decrement the value block by iMoney amount.
Parameters
Input
blockNo - the block number,0= <blockNo<=255
value -the value.
Output

Return
0:success, others:failed.
Remark
```

### RFCardReader / m1_amount _init

- Source section: `10.13`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Init the value block by value.


Signature/prototype:
```java
int m1_amount_init(int blockNo, int value)
```


Parameters:
Input
blockNo: the block number,0= <blockNo<=255


Return value:
0:success, others:failed.


Source details:
```text
Prototype
int m1_amount_init(int blockNo, int value)
Description
Init the value block by value.
Parameters
Input
blockNo: the block number,0= <blockNo<=255
Return
0:success, others:failed.
Remark
```

### RFCardReader / m1_amount_read

- Source section: `10.14`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Read the amount for the block number.


Signature/prototype:
```java
int m1_amount_read(int blockNo)
```


Parameters:
Input
blockNo - the block number,0= <blockNo<=255


Return value:
The amount, negative number if failed.


Source details:
```text
Prototype
int m1_amount_read(int blockNo)
Description
Read the amount for the block number.
Parameters
Input
blockNo - the block number,0= <blockNo<=255
Return
The amount, negative number if failed.
Remark
```

### RFCardReader / m1_amount_restore

- Source section: `10.15`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Move the content of a block into temporary data register.


Signature/prototype:
```java
int m1_amount_restore(int blockNo)
```


Parameters:
Input
blockNo - the block number,0= <blockNo<=255


Return value:
0:success, others:failed.


Source details:
```text
Prototype
int m1_amount_restore(int blockNo)
Description
Move the content of a block into temporary data register.
Parameters
Input
blockNo - the block number,0= <blockNo<=255
Return
0:success, others:failed.
Remark
```

### RFCardReader / m1_amount_transfer

- Source section: `10.16`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
Transfer from temporary internal data register to value block.


Signature/prototype:
```java
int m1_amount_transfer(int blockNo)
```


Parameters:
Input
blockNo - the block number,0= <blockNo<=255


Return value:
0:success, others:failed.


Source details:
```text
Prototype
int m1_amount_transfer(int blockNo)
Description
Transfer from temporary internal data register to value block.
Parameters
Input
blockNo - the block number,0= <blockNo<=255
Return
0:success, others:failed.
Remark
```

### RFCardReader / exchangeApdu(Mifare)

- Source section: `10.17`

- Package/class path: `com.urovo.sdk.rfcard.RFCardHandlerImpl`

- Demo reference: `UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/view/RFReaderActivity.java`



Purpose:
APDU data communication for Mifare.


Signature/prototype:
```java
byte[] exchangeApdu_M1(byte[] apdu, int... crcSpeed);
```


Parameters:
Input
apdu - apdu data.
crcSpeed(int...) - int[0]=crc, int[1]=speed. //Opitional, variable length.
Output
None


Return value:
Return card response data when communication successful, return null when communication failed.


Source details:
```text
Prototype
byte[] exchangeApdu_M1(byte[] apdu, int... crcSpeed);
Description
APDU data communication for Mifare.
Parameters
Input
apdu - apdu data.
crcSpeed(int...) - int[0]=crc, int[1]=speed. //Opitional, variable length.
Output
None
Return
Return card response data when communication successful, return null when communication failed.
Remark
```

### Install Manager / install

- Source section: `11.1`

- Package/class path: `com.urovo.sdk.install.InstallManagerImpl`



Purpose:
Override installation, silence.


Signature/prototype:
```java
void install(String apkPath, final InstallApkListener listener)
```


Parameters:
Input
apkPath - the apk file path in the device.
listener - installation and uninstallation result callback.
Output
None


Usage notes:
/**
* installation result callback listener interface definition
*/
interface InstallApkListener {
/**
* installation result
* @param packageName- app package name
* @param returnCode- result code.(Refer to the InstallAppCodeConstant).
* @param returnMsg- result description message
*/
void onInstallFinished(String packageName, int returnCode, String returnMsg);
/**
* uninstallation result
* @param packageName- app package name
* @param returnCode- result code.(Refer to InstallAppCodeConstant).
* @param returnMsg- result description message
*/
void onUnInstallFinished(String packageName, int returnCode, String returnMsg);
}


Source details:
```text
Prototype
void install(String apkPath, final InstallApkListener listener)
Description
Override installation, silence.
Parameters
Input
apkPath - the apk file path in the device.
listener - installation and uninstallation result callback.
Output
None
Return

Remark
/**
* installation result callback listener interface definition
*/
interface InstallApkListener {
/**
* installation result
* @param packageName- app package name
* @param returnCode- result code.(Refer to the InstallAppCodeConstant).
* @param returnMsg- result description message
*/
void onInstallFinished(String packageName, int returnCode, String returnMsg);
/**
* uninstallation result
* @param packageName- app package name
* @param returnCode- result code.(Refer to InstallAppCodeConstant).
* @param returnMsg- result description message
*/
void onUnInstallFinished(String packageName, int returnCode, String returnMsg);
}
```

### Install Manager / uninstall

- Source section: `11.2`

- Package/class path: `com.urovo.sdk.install.InstallManagerImpl`



Purpose:
Uninstallation, silence.


Signature/prototype:
```java
void uninstall(String packageName, final InstallApkListener listaner)
```


Parameters:
Input
packageName - the app package name.
listener - installation and uninstallation result callback.
Output
None


Usage notes:
/**
* installation result callback listener interface definition
*/
interface InstallApkListener {
/**
* installation result
* @param packageName- app package name
* @param returnCode- result code
* @param returnMsg- result description message
*/
void onInstallFinished(String packageName, int returnCode, String returnMsg);
/**
* uninstallation result
* @param packageName- app package name
* @param returnCode- result code
* @param returnMsg- result description message
*/
void onUnInstallFinished(String packageName, int returnCode, String returnMsg);
}


Source details:
```text
Prototype
void uninstall(String packageName, final InstallApkListener listaner)
Description
Uninstallation, silence.
Parameters
Input
packageName - the app package name.
listener - installation and uninstallation result callback.
Output
None
Return

Remark
/**
* installation result callback listener interface definition
*/
interface InstallApkListener {
/**
* installation result
* @param packageName- app package name
* @param returnCode- result code
* @param returnMsg- result description message
*/
void onInstallFinished(String packageName, int returnCode, String returnMsg);
/**
* uninstallation result
* @param packageName- app package name
* @param returnCode- result code
* @param returnMsg- result description message
*/
void onUnInstallFinished(String packageName, int returnCode, String returnMsg);
}
```

### System Manager / setDefaultDataSubId

- Source section: `12.1`

- Package/class path: `com.urovo.sdk.system.SystemProviderImpl`



Purpose:
switch the data sim card.


Signature/prototype:
```java
void setDefaultDataSubId(int subId)
```


Parameters:
Input
subId - sim id(0,1).
Output
None


Source details:
```text
Prototype
void setDefaultDataSubId(int subId)
Description
switch the data sim card.
Parameters
Input
subId - sim id(0,1).
Output
None
Return
```

### System Manager / setForceLockScreen

- Source section: `12.2`

- Package/class path: `com.urovo.sdk.system.SystemProviderImpl`



Purpose:
lock the screen.


Signature/prototype:
```java
void setForceLockScreen(boolean lock)
```


Parameters:
Input
lock - true.
Output
None


Source details:
```text
Prototype
void setForceLockScreen(boolean lock)
Description
lock the screen.
Parameters
Input
lock - true.
Output
None
Return
```

### System Manager / lockScreenNon

- Source section: `12.3`

- Package/class path: `com.urovo.sdk.system.SystemProviderImpl`



Purpose:
disable the screen lock.


Signature/prototype:
```java
void setLockScreenNon()
```


Parameters:
Input
None
Output
None


Source details:
```text
Prototype
void setLockScreenNon()
Description
disable the screen lock.
Parameters
Input
None
Output
None
Return
```

### System Manager / setLanguage

- Source section: `12.4`

- Package/class path: `com.urovo.sdk.system.SystemProviderImpl`



Purpose:
set system language.


Signature/prototype:
```java
void setLanguage(Local local)
```


Parameters:
Input
local:
eg: new Local("zh", "CN")
Output
None


Source details:
```text
Prototype
void setLanguage(Local local)
Description
set system language.
Parameters
Input
local:
eg: new Local("zh", "CN")
Output
None
Return
```

### DeviceManager / NTP Setting

- Source section: `13.1`



Purpose:
new DeviceManager().setSettingProperty("Global-ntp_server", "time.android.com");


Source details:
```text
new DeviceManager().setSettingProperty("Global-ntp_server", "time.android.com");
new DeviceManager().getSettingProperty("Global-ntp_server");
```

### DeviceManager / Time Zone Setting1

- Source section: `13.2`



Purpose:
1. new DeviceManager().setSettingProperty("persist-persist.sys.timezone","GMT+02:00");


Source details:
```text
1. new DeviceManager().setSettingProperty("persist-persist.sys.timezone","GMT+02:00");
2. new DeviceManager().setSettingProperty("persist-persist.sys.settimezone","America/Los_Angeles");
```

### DeviceManager / System Time Setting

- Source section: `13.3`



Purpose:
long time =


Source details:
```text
long time =
new SimpleDateFormat("yyyyMMddHHmmss").parse("20220101000000").getTime();
new DeviceManager().setCurrentTime(time);
```

### DeviceManager / Set APN

- Source section: `13.4`



Purpose:
boolean ret = new DeviceManager().setAPN(name, APN, "", 0, "", "", "", "", mcc, mnc, "", 0, 0, "", "", 0, "", true);


Source details:
```text
boolean ret = new DeviceManager().setAPN(name, APN, "", 0, "", "", "", "", mcc, mnc, "", 0, 0, "", "", 0, "", true);
```

### DeviceManager / Query APN Setting

- Source section: `13.5`



Purpose:
new DeviceManager().queryAPN(String selection, String[] selectionArgs).


Source details:
```text
new DeviceManager().queryAPN(String selection, String[] selectionArgs).
selection: this parameter is a key that you want to query,
the format seems like a sql command. eg:Telephony.Carriers.APN+" = ?".
Refer to the keys below:
Telephony.Carriers._ID,
Telephony.Carriers.NAME,
Telephony.Carriers.APN,
Telephony.Carriers.MCC,
Telephony.Carriers.MNC,
Telephony.Carriers.NUMERIC,
Telephony.Carriers.PROXY,
Telephony.Carriers.PORT,
Telephony.Carriers.MMSPROXY,
Telephony.Carriers.MMSPORT,
Telephony.Carriers.MMSC,
Telephony.Carriers.SERVER,
Telephony.Carriers.PASSWORD,
Telephony.Carriers.AUTH_TYPE,
Telephony.Carriers.TYPE,
Telephony.Carriers.USER,
Telephony.Carriers.CURRENT.
selectionArgs: this parameter is the value of the key what you want to query. eg:new String[]{"APN Test"}.
For example:queryAPN(Telephony.Carriers.NAME+" = ?", new String[]{"APN Test"});
```

### DeviceManager / Device Shutdown/Reboot

- Source section: `13.4`



Purpose:
new DeviceManager().shutdown(boolean reboot); //true: reboot, false:shutdown.


Source details:
```text
new DeviceManager().shutdown(boolean reboot); //true: reboot, false:shutdown.
```

### DeviceManager / Device Battery Info

- Source section: `13.5`



Purpose:
Bundle bundle = new DeviceManager().getBatteryInfo();


Source details:
```text
Bundle bundle = new DeviceManager().getBatteryInfo();
int level = bundle.getInt("level"); //battery percentage
int plugged = bundle.getInt("plugged"); //Is it charging, 0:no, 1:yes.
```

### DeviceManager / Launcher Setting

- Source section: `13.6`



Purpose:
new DeviceManager().setDefaultLauncher(ComponentName.unflattenFromString(componetStr);


Source details:
```text
new DeviceManager().setDefaultLauncher(ComponentName.unflattenFromString(componetStr);
//componentStr = "packageName/ActivityPath".
// eg: "com.urovo.demo/com.urovo.demo.MainActivity".
//<intent-filter>
<action android:name="android.intent.action.MAIN" />

<category android:name="android.intent.category.LAUNCHER" />

<category android:name="android.intent.category.HOME" />

<category android:name="android.intent.category.DEFAULT" />
</intent-filter>
```

### DeviceManager / Auto Running APP Setting

- Source section: `13.7`



Purpose:
new DeviceManager().setAutoRunningApp(ComponentName.unflattenFromString(componetStr, action); //action:0:disable, 1:enable


Source details:
```text
new DeviceManager().setAutoRunningApp(ComponentName.unflattenFromString(componetStr, action); //action:0:disable, 1:enable
//componentStr = "packageName/ActivityPath".
// eg: "com.urovo.demo/com.urovo.demo.MainActivity".
```

### DeviceManager / Double Click To Wake Up Screen

- Source section: `13.8`



Purpose:
DeviceManager deviceManager = new DeviceManager();


Source details:
```text
DeviceManager deviceManager = new DeviceManager();
enable:
deviceManager.setSettingProperty("persist-persist.sys.urv.tp.wakeup.gesture", "doubleclick");
disable:
deviceManager.setSettingProperty("persist-persist.sys.urv.tp.wakeup.gesture", "");
```

### DeviceManager / Get Keyboard State

- Source section: `13.9`



Purpose:
//keyboard state: 0-NUM, 1-LOWER, 2-UPPER.


Source details:
```text
//keyboard state: 0-NUM, 1-LOWER, 2-UPPER.
String state = new DeviceManager().getSettingProperty("Global-ufans.keyboard.state");
```

### DeviceManager / Disable Keyboard Switching

- Source section: `13.10`



Purpose:
DeviceManager deviceManager = new DeviceManager();


Source details:
```text
DeviceManager deviceManager = new DeviceManager();
//disable
deviceManager.setSettingProperty("persist-persist.sys.urv.disable.change_language", "true");
//enable
deviceManager.setSettingProperty("persist-persist.sys.urv.disable.change_language", "false");
```

### DeviceManager / Switch Keyboard State

- Source section: `13.11`



Purpose:
//After keyboard switching, need to update the keyboard state in the status bar.


Source details:
```text
//After keyboard switching, need to update the keyboard state in the status bar.
//keyboard mode: 0-NUM, 1-LOWER, 2-UPPER.
String MODE_KB = "0";
new DeviceManager.setSettingProperty("Global-ufans.keyboard.state", MODE_KB);
```

### DeviceManager / Update the Keyboard State in Status bar

- Source section: `13.12`



Purpose:
sendBroadcast(new Intent("android.intent.action.ACTION_SWITCH_KEY_STATE"));


Source details:
```text
sendBroadcast(new Intent("android.intent.action.ACTION_SWITCH_KEY_STATE"));
```

### DeviceManager / Switch Wi-Fi

- Source section: `13.13`



Purpose:
new DeviceManager().switchWifi(boolean enable);


Source details:
```text
new DeviceManager().switchWifi(boolean enable);
```

### DeviceManager / Switch Bluetooth

- Source section: `13.14`



Purpose:
new DeviceManager().switchBT(boolean enable);


Source details:
```text
new DeviceManager().switchBT(boolean enable);
```

### DeviceManager / Switch Airplane mode

- Source section: `13.15`



Purpose:
new DeviceManager().setAirplaneMode(boolean enable);


Source details:
```text
new DeviceManager().setAirplaneMode(boolean enable);
```

### DeviceManager / Forget All Wi-Fi

- Source section: `13.16`



Purpose:
new DeviceManager().forgetAllWifi();


Source details:
```text
new DeviceManager().forgetAllWifi();
```

### DeviceManager / Check Network Status

- Source section: `13.17`



Purpose:
int status = new DeviceManager().checkNetworkStatus();


Source details:
```text
int status = new DeviceManager().checkNetworkStatus();
//0:Normal network connection.
//1:SIM card not ready.
//2:Mobile network shutdown.
//3:Not registered on the network.
//4:APN configuration failed.
//5:No data connection.
//6:No Internet connection.
```

### DeviceManager / Set/Get GPS Accuracy

- Source section: `13.18`



Purpose:
DeviceManager deviceManager = new DeviceManager();


Source details:
```text
DeviceManager deviceManager = new DeviceManager();
boolean ret = deviceManager.setSettingProperty("Secure-location_mode", "3");
String value = deviceManager.getSettingProperty("Secure-location_mode");

boolean ret = deviceManager.setSettingProperty("ro.ufs.gps.high_accuracy", "true");
String value = deviceManager.getSettingProperty("ro.ufs.gps.high_accuracy");
```

### DeviceManager / Set physical button volume

- Source section: `13.19`



Purpose:
//volume: 0-15


Source details:
```text
//volume: 0-15
int volume = 0;
new DeviceManager().setSettingProperty("persist-persist.sys.urv.notification.volume",
"" + volume);
```

### Serial Port / setOnListener

- Source section: `14.1`

- Package/class path: `com.urovo.serial.utils.SerialPortTool`



Purpose:
set up data receiving listener.


Signature/prototype:
```java
void setOnListener(SerialPortListener listener)
```


Parameters:
Input
listener - tiem out(millisecond)
Output
None


Usage notes:
Data receiving listener interface definition：
interface SerialPortListener {
/**
* Received data successfully.
* @param data- response data.
*/
void onReceive(byte[] data);

* Received data failed.
* @param code- error code.
* @param msg- error message.
*/
void onFail(String code, String msg);
}


Source details:
```text
Prototype
void setOnListener(SerialPortListener listener)
Description
set up data receiving listener.
Parameters
Input
listener - tiem out(millisecond)
Output
None
Return

Remark
Data receiving listener interface definition：
interface SerialPortListener {
/**
* Received data successfully.
* @param data- response data.
*/
void onReceive(byte[] data);

* Received data failed.
* @param code- error code.
* @param msg- error message.
*/
void onFail(String code, String msg);
}
```

### Serial Port / openSerialPort

- Source section: `14.2`

- Package/class path: `com.urovo.serial.utils.SerialPortTool`



Purpose:
Open the serial port.


Signature/prototype:
```java
int openSerialPort(List<String> pathNameList, int bps)
```


Parameters:
Input
portPath - the serial port path.
bps - Baud rate
*
* - BPS_1200(0x01) - 1200
* - BPS_2400(0x02) - 2400
* - BPS_4800(0x03) - 4800
* - BPS_9600(0x04) - 9600
* - BPS_14400(0x05) - 14400
* - BPS_28800(0x06) - 28800
* - BPS_19200(0x07) - 19200
* - BPS_57600(0x08) - 57600
* - BPS_115200(0x09) - 115200
* - BPS_38400(0x0A) - 38400
*

bps - Baud rate
*
* - BPS_1200(0x01) - 1200
* - BPS_2400(0x02) - 2400
* - BPS_4800(0x03) - 4800
* - BPS_9600(0x04) - 9600
* - BPS_14400(0x05) - 14400
* - BPS_28800(0x06) - 28800
* - BPS_19200(0x07) - 19200
* - BPS_57600(0x08) - 57600
* - BPS_115200(0x09) - 115200
* - BPS_38400(0x0A) - 38400
*
Output
None


Return value:
0:success, others:failed.


Usage notes:
Refer to the
com.urovo.serial.common.GlobalConstant..getErrorMessage(int errorCode).


Source details:
```text
Prototype
int openSerialPort(List<String> pathNameList, int bps)
Description
Open the serial port.
Parameters
Input
portPath - the serial port path.
bps - Baud rate
*
* - BPS_1200(0x01) - 1200
* - BPS_2400(0x02) - 2400
* - BPS_4800(0x03) - 4800
* - BPS_9600(0x04) - 9600
* - BPS_14400(0x05) - 14400
* - BPS_28800(0x06) - 28800
* - BPS_19200(0x07) - 19200
* - BPS_57600(0x08) - 57600
* - BPS_115200(0x09) - 115200
* - BPS_38400(0x0A) - 38400
*

bps - Baud rate
*
* - BPS_1200(0x01) - 1200
* - BPS_2400(0x02) - 2400
* - BPS_4800(0x03) - 4800
* - BPS_9600(0x04) - 9600
* - BPS_14400(0x05) - 14400
* - BPS_28800(0x06) - 28800
* - BPS_19200(0x07) - 19200
* - BPS_57600(0x08) - 57600
* - BPS_115200(0x09) - 115200
* - BPS_38400(0x0A) - 38400
*
Output
None
Return
0:success, others:failed.
Remark
Refer to the
com.urovo.serial.common.GlobalConstant..getErrorMessage(int errorCode).
```

### Serial Port / close

- Source section: `14.3`

- Package/class path: `com.urovo.serial.utils.SerialPortTool`



Purpose:
Stop the serila port.


Signature/prototype:
```java
boolean close()
```


Parameters:
Input
None
Output
None


Return value:
None


Source details:
```text
Prototype
boolean close()
Description
Stop the serila port.
Parameters
Input
None
Output
None
Return
None
Remark
```

### Serial Port / sendData

- Source section: `14.4`

- Package/class path: `com.urovo.serial.utils.SerialPortTool`



Purpose:
Send data to serial port.


Signature/prototype:
```java
String sendData(byte[] SendBuff, int len)
```


Parameters:
Input
data - data.

len- data length.
Output
None


Return value:
null - success, others - failed.


Source details:
```text
Prototype
String sendData(byte[] SendBuff, int len)
Description
Send data to serial port.
Parameters
Input
data - data.

len- data length.
Output
None
Return
null - success, others - failed.
Remark
```

### SDK log output management. / Enable/Disable logcat.

- Source section: `15.1`



Purpose:
logfile.setLogcatOut(boolean logcatOut);


Source details:
```text
logfile.setLogcatOut(boolean logcatOut);
```

### SDK log output management. / Enable/Disable log file storage.

- Source section: `15.2`



Purpose:
//path:sdcard/urovosdkLibs/Log/


Source details:
```text
//path:sdcard/urovosdkLibs/Log/
logfile.setLogFileOut(boolean logFileOut);
```

### SDK log output management. / Enable/Disable Pinpad module log.

- Source section: `15.3`



Purpose:
PinPadProviderImpl.getInstance().LogOutEnable(boolean LogOutEnable);


Source details:
```text
PinPadProviderImpl.getInstance().LogOutEnable(boolean LogOutEnable);
```

### SDK log output management. / Enable/Disable EMV module log.

- Source section: `15.4`



Purpose:
//if sdk version >= V1.0.22, the log path is : data/data/packageName/files/UROPE, else the log path is: sdcard/UROPE/.


Source details:
```text
//if sdk version >= V1.0.22, the log path is : data/data/packageName/files/UROPE, else the log path is: sdcard/UROPE/.
```

### SDK log output management. / logoutEnable.

- Source section: `15.4.1`



Purpose:
EmvNfcKernelApi.getInstance(this).LogOutEnable(int enable): //1-enable, 0-disable.


Source details:
```text
EmvNfcKernelApi.getInstance(this).LogOutEnable(int enable): //1-enable, 0-disable.
```

### SDK log output management. / exportLogFilesToExternalStorage.

- Source section: `15.4.2`



Purpose:
//if sdk version >= V1.0.22, need to call the below function before pulling log file:


Source details:
```text
//if sdk version >= V1.0.22, need to call the below function before pulling log file:
EmvNfcKernelApi.exportLogFilesToExternalStorage(Context context).
```

### SLE4428/4436/4442 / open

- Source section: `16.1`

- Package/class path: `andoird.device.IccManager`



Purpose:
Open the IC card for operation.


Signature/prototype:
```java
int open(byte slot, byte CardType, byte Volt)
```


Parameters:
Input
slot - card slot type. (0-IC slot.)

CardType - card type. (0x02-SLE4428/4436/4442)

Volt - valtage to supply.
0x01 : 3V
0x02 : 5V
0x03 : 1.8V
Output
None


Return value:
0-success, others-failed.


Usage notes:
The card reader will automatically cycle through three voltage values, so it is only necessary to set the voltage value to 0x01.


Source details:
```text
Prototype
int open(byte slot, byte CardType, byte Volt)
Description
Open the IC card for operation.
Parameters
Input
slot - card slot type. (0-IC slot.)

CardType - card type. (0x02-SLE4428/4436/4442)

Volt - valtage to supply.
0x01 : 3V
0x02 : 5V
0x03 : 1.8V
Output
None
Return
0-success, others-failed.
Remark
The card reader will automatically cycle through three voltage values, so it is only necessary to set the voltage value to 0x01.
```

### SLE4428/4436/4442 / close

- Source section: `16.2`

- Package/class path: `andoird.device.IccManager`



Purpose:
Close the slot.


Signature/prototype:
```java
int close()
```


Parameters:
Input
None
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
int close()
Description
Close the slot.
Parameters
Input
None
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / detect

- Source section: `16.3`

- Package/class path: `andoird.device.IccManager`



Purpose:
Check if IC card is inserted.


Signature/prototype:
```java
int detect()
```


Parameters:
Input
None
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
int detect()
Description
Check if IC card is inserted.
Parameters
Input
None
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4428_reset

- Source section: `16.4`

- Package/class path: `andoird.device.IccManager`



Purpose:
reset the SLE4428.


Signature/prototype:
```java
public int sle4428_reset(byte[] pAtr)
```


Parameters:
Input
None
Output
pAtr - To store the return ATR.


Return value:
Length of ATR, negative number if failed.


Source details:
```text
Prototype
public int sle4428_reset(byte[] pAtr)
Description
reset the SLE4428.
Parameters
Input
None
Output
pAtr - To store the return ATR.
Return
Length of ATR, negative number if failed.
Remark
```

### SLE4428/4436/4442 / sle4428_readMemory

- Source section: `16.5`

- Package/class path: `andoird.device.IccManager`



Purpose:
Read the data stored in main memory for SLE4428.


Signature/prototype:
```java
public byte[] sle4428_readMemory(int addr, int length)
```


Parameters:
Input
addr - The starting address of operation data, the range of the parameters of the SLE4428 card is 0 to 0x3FF.
length - To read the data length, the range of the parameters of the SLE4428 card is 1 to 0x400. ByteAddr and Length cannot be greater than the actual capacity of the card, otherwise the reader will refuse to execute the command and returns an error.
Output
None


Return value:
Byte array indicating the store data if successful, null if failed.


Source details:
```text
Prototype
public byte[] sle4428_readMemory(int addr, int length)
Description
Read the data stored in main memory for SLE4428.
Parameters
Input
addr - The starting address of operation data, the range of the parameters of the SLE4428 card is 0 to 0x3FF.
length - To read the data length, the range of the parameters of the SLE4428 card is 1 to 0x400. ByteAddr and Length cannot be greater than the actual capacity of the card, otherwise the reader will refuse to execute the command and returns an error.
Output
None
Return
Byte array indicating the store data if successful, null if failed.
Remark
```

### SLE4428/4436/4442 / sle4428_writeMemory

- Source section: `16.6`

- Package/class path: `andoird.device.IccManager`



Purpose:
Write data to the main storage area SLE4428.


Signature/prototype:
```java
public int sle4428_writeMemory(int addr, byte[] data, int dataLen)
```


Parameters:
Input
addr - The starting address of operation data, the range of the parameters of the SLE4428 card is 0 to 0x3FF.

data - The data to be written.

dataLen - The data length, max 0x400.
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
public int sle4428_writeMemory(int addr, byte[] data, int dataLen)
Description
Write data to the main storage area SLE4428.
Parameters
Input
addr - The starting address of operation data, the range of the parameters of the SLE4428 card is 0 to 0x3FF.

data - The data to be written.

dataLen - The data length, max 0x400.
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4428_password

- Source section: `16.7`

- Package/class path: `andoird.device.IccManager`



Purpose:
Submit the transfer data, enter the personalization mode.


Signature/prototype:
```java
public int sle4428_password(int mode, byte[] data)
```


Parameters:
Input
mode - 0 verify password or 1 changed password.

data - The data buffer pointer password, password here to store data migration and card in the password.
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
public int sle4428_password(int mode, byte[] data)
Description
Submit the transfer data, enter the personalization mode.
Parameters
Input
mode - 0 verify password or 1 changed password.

data - The data buffer pointer password, password here to store data migration and card in the password.
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4436_reset

- Source section: `16.8`

- Package/class path: `andoird.device.IccManager`



Purpose:
Reset the SLE4436.


Signature/prototype:
```java
public int sle4436_reset(byte[] pAtr)
```


Parameters:
Input
pAtr - To store the return ATR.
Output
None


Return value:
Length of ATR, negative number if failed.


Source details:
```text
Prototype
public int sle4436_reset(byte[] pAtr)
Description
Reset the SLE4436.
Parameters
Input
pAtr - To store the return ATR.
Output
None
Return
Length of ATR, negative number if failed.
Remark
```

### SLE4428/4436/4442 / sle4436_readMemory

- Source section: `16.9`

- Package/class path: `andoird.device.IccManager`



Purpose:
Read the data stored in main memory for SLE4436.


Signature/prototype:
```java
public byte[] sle4436_readMemory(int addr, int length)
```


Parameters:
Input
addr - The starting address of operation data, the range of the parameters of the SLE4436 card is 0 to 112.

length - To read the data length, the range of the parameters of the SLE4436 card is 1 to 112. ByteAddr and Length cannot be greater than the actual capacity of the card, otherwise the reader will refuse to execute the command and returns an error.

Output
None


Return value:
Byte array indicating the store data if successful, null if failed.


Source details:
```text
Prototype
public byte[] sle4436_readMemory(int addr, int length)
Description
Read the data stored in main memory for SLE4436.
Parameters
Input
addr - The starting address of operation data, the range of the parameters of the SLE4436 card is 0 to 112.

length - To read the data length, the range of the parameters of the SLE4436 card is 1 to 112. ByteAddr and Length cannot be greater than the actual capacity of the card, otherwise the reader will refuse to execute the command and returns an error.

Output
None
Return
Byte array indicating the store data if successful, null if failed.
Remark
```

### SLE4428/4436/4442 / sle4436_writeMemory

- Source section: `16.10`

- Package/class path: `andoird.device.IccManager`



Purpose:
Write data to the main storage area SLE4436.


Signature/prototype:
```java
public int sle4436_writeMemory(int addr, byte[] data, int dataLen)
```


Parameters:
Input
addr - The starting address of operation data, the range of the parameters of the SLE4436 card is 0 to 112.

data - The data to be written.

dataLen - The data length.
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
public int sle4436_writeMemory(int addr, byte[] data, int dataLen)
Description
Write data to the main storage area SLE4436.
Parameters
Input
addr - The starting address of operation data, the range of the parameters of the SLE4436 card is 0 to 112.

data - The data to be written.

dataLen - The data length.
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4436_verifyPassword

- Source section: `16.11`

- Package/class path: `andoird.device.IccManager`



Purpose:
Submit the transfer data, enter the personalization mode.


Signature/prototype:
```java
public int sle4436_verifyPassword(byte[] passwd)
```


Parameters:
Input
passwd - The data buffer pointer password, password here to store data migration and card in the password.
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
public int sle4436_verifyPassword(byte[] passwd)
Description
Submit the transfer data, enter the personalization mode.
Parameters
Input
passwd - The data buffer pointer password, password here to store data migration and card in the password.
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4436_regIncrease

- Source section: `16.12`

- Package/class path: `andoird.device.IccManager`



Purpose:
Move the register.


Signature/prototype:
```java
public int sle4436_regIncrease(int shiftBits)
```


Parameters:
Input
shiftBits - Move Bits.
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
public int sle4436_regIncrease(int shiftBits)
Description
Move the register.
Parameters
Input
shiftBits - Move Bits.
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4436_readBit

- Source section: `16.13`

- Package/class path: `andoird.device.IccManager`



Purpose:
Read values from the Card bit.


Signature/prototype:
```java
public int sle4436_readBit(byte[] pData)
```


Parameters:
Input
pData - Store the value read from the card.
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
public int sle4436_readBit(byte[] pData)
Description
Read values from the Card bit.
Parameters
Input
pData - Store the value read from the card.
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4436_writeBit

- Source section: `16.14`

- Package/class path: `andoird.device.IccManager`



Purpose:
Write a bit for Card.


Signature/prototype:
```java
public int sle4436_writeBit()
```


Parameters:
Input
None
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
public int sle4436_writeBit()
Description
Write a bit for Card.
Parameters
Input
None
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4436_reloadByte

- Source section: `16.15`

- Package/class path: `andoird.device.IccManager`



Purpose:
Reload Byte.


Signature/prototype:
```java
public int sle4436_reloadByte()
```


Parameters:
Input
None
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
public int sle4436_reloadByte()
Description
Reload Byte.
Parameters
Input
None
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4436_decValue

- Source section: `16.16`

- Package/class path: `andoird.device.IccManager`



Purpose:
Reduction balance.


Signature/prototype:
```java
public int sle4436_decValue(int pValue)
```


Parameters:
Input
pValue - The amount of money to be eduction.
Output
None


Return value:
The card balance if successful, -1 if failed.


Source details:
```text
Prototype
public int sle4436_decValue(int pValue)
Description
Reduction balance.
Parameters
Input
pValue - The amount of money to be eduction.
Output
None
Return
The card balance if successful, -1 if failed.
Remark
```

### SLE4428/4436/4442 / sle4442_reset

- Source section: `16.17`

- Package/class path: `andoird.device.IccManager`



Purpose:
reset the SLE4442..


Signature/prototype:
```java
int sle4442_reset(byte[] pAtr)
```


Parameters:
Input
None
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
int sle4442_reset(byte[] pAtr)
Description
reset the SLE4442..
Parameters
Input
None
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4442_readMainMemory

- Source section: `16.18`

- Package/class path: `andoird.device.IccManager`



Purpose:
Read the data stored in main memory for SLE4442.


Signature/prototype:
```java
byte[] sle4442_readMainMemory(int addr, int length)
```


Parameters:
Input
addr - The starting address of operation data, the range of the parameters of the SLE4442 card is 0 to 255.
 length- To read the data length, the range of the parameters of the SLE4442 card is 1 to 256. ByteAddr and Length cannot be greater than the actual capacity of the card, otherwise the
reader will refuse to execute the command and returns an error.
Output
None


Return value:
Byte array indicating the store data if successful, null if failed.


Source details:
```text
Prototype
byte[] sle4442_readMainMemory(int addr, int length)
Description
Read the data stored in main memory for SLE4442.
Parameters
Input
addr - The starting address of operation data, the range of the parameters of the SLE4442 card is 0 to 255.
 length- To read the data length, the range of the parameters of the SLE4442 card is 1 to 256. ByteAddr and Length cannot be greater than the actual capacity of the card, otherwise the
reader will refuse to execute the command and returns an error.
Output
None
Return
Byte array indicating the store data if successful, null if failed.
Remark
```

### SLE4428/4436/4442 / sle4442_writeMainMemory

- Source section: `16.19`

- Package/class path: `andoird.device.IccManager`



Purpose:
Write data to the main storage area SLE4442.


Signature/prototype:
```java
int sle4442_writeMainMemory(int addr, byte[] data, int dataLen)
```


Parameters:
Input
addr - The starting address of operation data, the range of the parameters of the SLE4442 card is 0 to 255.

data - The data to be written.

dataLen - Length of data.
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
int sle4442_writeMainMemory(int addr, byte[] data, int dataLen)
Description
Write data to the main storage area SLE4442.
Parameters
Input
addr - The starting address of operation data, the range of the parameters of the SLE4442 card is 0 to 255.

data - The data to be written.

dataLen - Length of data.
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4442_readProtectionMemory

- Source section: `16.20`

- Package/class path: `andoird.device.IccManager`



Purpose:
Read SLE4442 card protection bit storage data (4 BYTE).


Signature/prototype:
```java
public byte[] sle4442_readProtectionMemory(int address, int len)
```


Parameters:
Input
address - The parameter range is 0x00 to 0xff.

Len - Length to read, 4 Bytes: bit31 - read out 32Bytes; bit0 - read out 1Byte; bit* - etc.
Output
None


Return value:
Byte array indicating protected storage data if successful, null if failed.


Usage notes:
4 bytes of data read is to save the SLE4442 bits data in storage area. Byte sequence bit: low in front, high in the post. Such as: read data: 30 FF 1F F8 from the first byte of 30H can be analyzed as follows: the binary 30H corresponding to the lower four bits: 00110000 to 0 represent the four byte SLE4442 of the card (answer to reset) cannot be changed.


Source details:
```text
Prototype
public byte[] sle4442_readProtectionMemory(int address, int len)
Description
Read SLE4442 card protection bit storage data (4 BYTE).
Parameters
Input
address - The parameter range is 0x00 to 0xff.

Len - Length to read, 4 Bytes: bit31 - read out 32Bytes; bit0 - read out 1Byte; bit* - etc.
Output
None
Return
Byte array indicating protected storage data if successful, null if failed.
Remark
4 bytes of data read is to save the SLE4442 bits data in storage area. Byte sequence bit: low in front, high in the post. Such as: read data: 30 FF 1F F8 from the first byte of 30H can be analyzed as follows: the binary 30H corresponding to the lower four bits: 00110000 to 0 represent the four byte SLE4442 of the card (answer to reset) cannot be changed.
```

### SLE4428/4436/4442 / sle4442_writeProtectionMemory

- Source section: `16.21`

- Package/class path: `andoird.device.IccManager`



Purpose:
The write protection bit storage area.


Signature/prototype:
```java
public int sle4442_writeProtectionMemory(int addr, byte[] data, int dataLen)
```


Parameters:
Input
addr - The starting address of operation data, the range of the parameters of the SLE4442 card is 0 to 31.

data - Write protect bit data.

dataLen - Length of data.
Output
None


Return value:
0-success, others-failed.


Usage notes:
Write protection on the 4442 card data (disposable, write protection is not able to restore). Behind the 32BYTE data only for SLE4442 write protect (corresponding to protect a storage area of 32 BIT). This function can be one of a plurality of consecutive bytes protection (up to 32 bytes). According to the write protection properties of SLE4442 card, a byte write protection must provide the bytes of data and sends the write protection command, at the same time, consistent data provides the data and the actual store only when the specified storage area is write protected.


Source details:
```text
Prototype
public int sle4442_writeProtectionMemory(int addr, byte[] data, int dataLen)
Description
The write protection bit storage area.
Parameters
Input
addr - The starting address of operation data, the range of the parameters of the SLE4442 card is 0 to 31.

data - Write protect bit data.

dataLen - Length of data.
Output
None
Return
0-success, others-failed.
Remark
Write protection on the 4442 card data (disposable, write protection is not able to restore). Behind the 32BYTE data only for SLE4442 write protect (corresponding to protect a storage area of 32 BIT). This function can be one of a plurality of consecutive bytes protection (up to 32 bytes). According to the write protection properties of SLE4442 card, a byte write protection must provide the bytes of data and sends the write protection command, at the same time, consistent data provides the data and the actual store only when the specified storage area is write protected.
```

### SLE4428/4436/4442 / sle4442_verifyPassword

- Source section: `16.22`

- Package/class path: `andoird.device.IccManager`



Purpose:
Comparison SLE4442 cards, each card compared to the card password.


Signature/prototype:
```java
public int sle4442_verifyPassword(byte[] passwd)
```


Parameters:
Input
passwd - The data buffer pointer password, password here to store data migration and card in the password.
Output
None


Return value:
0-success, others-failed.


Usage notes:
If all the card data become to read-only, you will not be able to do any write operation. Secure storage area (except the error counter outside) will not be able to read and write.


Source details:
```text
Prototype
public int sle4442_verifyPassword(byte[] passwd)
Description
Comparison SLE4442 cards, each card compared to the card password.
Parameters
Input
passwd - The data buffer pointer password, password here to store data migration and card in the password.
Output
None
Return
0-success, others-failed.
Remark
If all the card data become to read-only, you will not be able to do any write operation. Secure storage area (except the error counter outside) will not be able to read and write.
```

### SLE4428/4436/4442 / sle4442_changePassword

- Source section: `16.23`

- Package/class path: `andoird.device.IccManager`



Purpose:
Change password.


Signature/prototype:
```java
public int sle4442_changePassword(byte[] passwd)
```


Parameters:
Input
passwd - The data buffer point to the new password.
Output
None


Return value:
0-success, others-failed.


Source details:
```text
Prototype
public int sle4442_changePassword(byte[] passwd)
Description
Change password.
Parameters
Input
passwd - The data buffer point to the new password.
Output
None
Return
0-success, others-failed.
Remark
```

### SLE4428/4436/4442 / sle4442_readErrorCounter

- Source section: `16.24`

- Package/class path: `andoird.device.IccManager`



Purpose:
Read error code value.


Signature/prototype:
```java
public int sle4442_readErrorCounter(byte[] errorCount)
```


Parameters:
Input
errorCount - The data buffer point to error count, 1Byte.
Output
None


Return value:
0-success, others-failed.


Usage notes:
APPENDIX A
Custom pinpad UI
The parameters are mainly passed down through the json file, the application parses the json file into a string, and passes it to the pinpad through the bundle
strJson = getJson("json.json", mContext);
paramVar.putString("strJson", strJson);
paramVar.putString("bodyBitmap", bodyBitmap);// body_imageview
paramVar.putString("keyBitmap", keyBitmap); // key_imageview
paramVar.putString("viewBitmap", viewBitmap); // imageView
paramVar.putString("backsapceBitmap", backspaceBitmap); // body_imageview

Json description:
{
"body_area": {－―――――――――――――――――――――whole screen
"left": 0,―――――――――――――――――――――――－X-axis coordinate
"top": 0, ―――――――――――――――――――――――－Y-axis coordinate
"height": 1280,―――――――――――――――――――――Height
"width": 720,――――――――――――――――――――――Width
"backgroundImage": "",－――――――――――――――――Image path
"backgroundColor": "#ff1234"－――――――――――――background color
},
"body_imageview": {
"left": 0,―――――――――――――――――――――――－X-axis coordinate
"top": 0, ―――――――――――――――――――――――－Y-axis coordinate
"height": 1280,―――――――――――――――――――――Height
"width": 720,――――――――――――――――――――――Width
"backgroundImage": "",－――――――――――――――――Image path
"backgroundColor": "#ff1234"－――――――――――――background color
},
"backspace": {―――――――――――――――――――――――back button
"left": 640,
"top": 0,
"height": 70,
"width": 70,
"backgroundImage": "",
"backgroundColor": "#ff1234"
},
"title": {
"left": 0,
"top": 0,
"height": 70,
"width": 720,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "Security Keyboard",―――――――――――――displayed text
"fontSize": 18,―――――――――――――――――――――font size
"color": "#ff1234",―――――――――――――――――――font color
"display": "",―――――――――――――――――――――whether to display
"borderStyle": "solid",―――――――――――――whether to show border
"borderWidth": 0,―――――――――――――――――――――border width
"borderRadius": 0,―――――――――――――――――――――border radius
"borderColor": ""―――――――――――――――――――――border color
},
"head": {
"left": 0,
"top": 70,
"height": 70,
"width": 480,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "Head",
"fontSize": 28,
"color": "#ff1234",
"display": "none",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 0,
"borderColor": ""
},
"money": {
"left": 0,
"top": 90,
"height": 70,
"width": 720,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "222000",
"fontSize": 28,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 0,
"borderColor": ""
},
"info": {
"left": 0,
"top": 180,
"height": 60,
"width": 720,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "info",
"fontSize": 28,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 0,
"borderColor": ""
},
"imageview": {
"left": 0,
"top": 180,
"height": 200,
"width": 720,
"backgroundImage": "imageview",
"backgroundColor": "#ff1234",
"display": "",
"borderStyle": "top",
"borderWidth": 0,
"borderRadius": 0,
"borderColor": ""
},
"echo": {
"left": 50,
"top": 260,
"height": 80,
"width": 620,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"display": "",
"borderStyle": "none",
"borderWidth": 0,
"borderRadius": 0,
"borderColor": ""
},
"view": {
"left": 50,
"top": 340,
"height": 60,
"width": 620,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"display": "",
"borderStyle": "top",
"borderWidth": 0,
"borderRadius": 0,
"borderColor": ""
},
"key_area": {―――――――――――the key area
"left": 0,
"top": 640,
"height": 640,
"width": 720,
"backgroundImage": "",
"backgroundColor": "#ff1234"
},
"key_imageview": {
"left": 0,―――――――――――――――――――――――－X-axis coordinate
"top": 0, ―――――――――――――――――――――――－Y-axis coordinate
"height": 1280,―――――――――――――――――――――Height
"width": 720,――――――――――――――――――――――Width
"backgroundImage": "",－――――――――――――――――Image path
"backgroundColor": "#ff1234"－――――――――――――background color
},
"key_1": {
"left": 10,
"top": 0,
"height": 110,
"width": 220,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "1",
"fontSize": 30,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_2": {
"left": 240,
"top": 0,
"height": 110,
"width": 220,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "2",
"fontSize": 30,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_3": {
"left": 480,
"top": 0,
"height": 110,
"width": 220,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "3",
"fontSize": 30,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_4": {
"left": 10,
"top": 120,
"height": 110,
"width": 220,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "4",
"fontSize": 30,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_5": {
"left": 240,
"top": 120,
"height": 110,
"width": 220,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "5",
"fontSize": 30,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_6": {
"left": 480,
"top": 120,
"height": 110,
"width": 220,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "6",
"fontSize": 30,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_7": {
"left": 10,
"top": 240,
"height": 110,
"width": 220,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "7",
"fontSize": 30,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_8": {
"left": 240,
"top": 240,
"height": 110,
"width": 220,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "8",
"fontSize": 30,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_9": {
"left": 480,
"top": 240,
"height": 110,
"width": 220,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "9",
"fontSize": 30,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_0": {
"left": 240,
"top": 360,
"height": 110,
"width": 220,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "0",
"fontSize": 30,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},

For the Cance&del&ok buttons, if backgroundImage is not empty，backgroundImage is imageButton.
"key_cancel": {
"left": 10,
"top": 360,
"height": 220,
"width": 220,
"backgroundImage": "cancelBitmap",
"backgroundColor": "#ff1234",
"text": "cancel",
"fontSize": 20,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_del": {
"left": 240,
"top": 480,
"height": 110,
"width": 220,
"backgroundImage": "delBitmap",
"backgroundColor": "#ff1234",
"text": "del",
"fontSize": 20,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_ok": {
"left": 480,
"top": 360,
"height": 220,
"width": 220,
"backgroundImage": "okBitmap",
"backgroundColor": "#ff1234",
"text": "ok",
"fontSize": 20,
"color": "#ff1234",
"display": "",
"borderStyle": "",
"borderWidth": 0,
"borderRadius": 7,
"borderColor": ""
},
"key_blank1": {
"left": 3,
"top": 700,
"height": 97,
"width": 116,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "",
"fontSize": 20,
"color": "#ff1234",
"display": "none",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 0,
"borderColor": ""
},
"key_blank2": {
"left": 242,
"top": 700,
"height": 97,
"width": 116,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "",
"fontSize": 20,
"color": "#ff1234",
"display": "",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 0,
"borderColor": ""
}
}


Source details:
```text
Prototype
public int sle4442_readErrorCounter(byte[] errorCount)
Description
Read error code value.
Parameters
Input
errorCount - The data buffer point to error count, 1Byte.
Output
None
Return
0-success, others-failed.
Remark

APPENDIX A
Custom pinpad UI
The parameters are mainly passed down through the json file, the application parses the json file into a string, and passes it to the pinpad through the bundle
strJson = getJson("json.json", mContext);
paramVar.putString("strJson", strJson);
paramVar.putString("bodyBitmap", bodyBitmap);// body_imageview
paramVar.putString("keyBitmap", keyBitmap); // key_imageview
paramVar.putString("viewBitmap", viewBitmap); // imageView
paramVar.putString("backsapceBitmap", backspaceBitmap); // body_imageview

Json description:
{
"body_area": {－―――――――――――――――――――――whole screen
"left": 0, - －X-axis coordinate
"top": 0,  - －Y-axis coordinate
"height": 1280,―――――――――――――――――――――Height
"width": 720,――――――――――――――――――――――Width
"backgroundImage": "",－――――――――――――――――Image path
"backgroundColor": "#ff1234"－――――――――――――background color
},
"body_imageview": {
"left": 0, - －X-axis coordinate
"top": 0,  - －Y-axis coordinate
"height": 1280,―――――――――――――――――――――Height
"width": 720,――――――――――――――――――――――Width
"backgroundImage": "",－――――――――――――――――Image path
"backgroundColor": "#ff1234"－――――――――――――background color
},
"backspace": { - back button
"left": 640,
"top": 0,
"height": 70,
"width": 70,
"backgroundImage": "",
"backgroundColor": "#ff1234"
},
"title": {
"left": 0,
"top": 0,
"height": 70,
"width": 720,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "Security Keyboard",―――――――――――――displayed text
"fontSize": 18,―――――――――――――――――――――font size
"color": "#ff1234",―――――――――――――――――――font color
"display": "",―――――――――――――――――――――whether to display
"borderStyle": "solid",―――――――――――――whether to show border
"borderWidth": 0,―――――――――――――――――――――border width
"borderRadius": 0,―――――――――――――――――――――border radius
"borderColor": ""―――――――――――――――――――――border color
},
"head": {
"left": 0,
"top": 70,
"height": 70,
"width": 480,
"backgroundImage": "",
"backgroundColor": "#ff1234",
"text": "Head",
"fontSize": 28,
"color": "#ff1234",
"display": "none",
"borderStyle": "solid",
"borderWidth": 0,
"borderRadius": 0,
"borderColor": ""
Source note: section truncated for chunk size; consult the source document for remaining detailed tables (439 source lines total).
```
