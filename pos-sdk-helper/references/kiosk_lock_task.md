# Kiosk / Lock Task Mode

**DISCOVERED**: These APIs exist in `DeviceManager` and are used in the demo (`SystemActivity.java`), but are NOT documented in the official SDK reference docs. Found by searching demo source code.

## DeviceManager / setLockTaskMode

- **Package/class**: `com.urovo.device.DeviceManager`
- **Demo reference**: `assets/UrovoPosSdkDemo/.../view/SystemActivity.java` (lines 142-148)

### Purpose
Enable or disable Android Lock Task Mode (kiosk mode) for a specific app package. When enabled, the user is locked to the specified app — cannot press Home, Recent Apps, or swipe away. Navigation bar is hidden/disabled. Power button short-press is suppressed.

### Signature
```java
void setLockTaskMode(String packageName, boolean lock)
```

### Parameters
| Name | Type | Description |
|---|---|---|
| packageName | String | The package name of the app to lock/unlock (use `getPackageName()`) |
| lock | boolean | `true` to enable lock task mode, `false` to disable |

### Example
```java
// Enable kiosk mode before transaction
new DeviceManager().setLockTaskMode(getPackageName(), true);

// Disable kiosk mode after transaction
new DeviceManager().setLockTaskMode(getPackageName(), false);
```

---

## DeviceManager / setLockTaskModePassword

- **Package/class**: `com.urovo.device.DeviceManager`
- **Demo reference**: `assets/UrovoPosSdkDemo/.../view/SystemActivity.java` (line 144)

### Purpose
Set the password required to exit Lock Task Mode. Should be called together with `setLockTaskMode(packageName, true)`.

### Signature
```java
void setLockTaskModePassword(String password)
```

### Parameters
| Name | Type | Description |
|---|---|---|
| password | String | Password to unlock/exit kiosk mode (e.g., "123456") |

### Example
```java
new DeviceManager().setLockTaskMode(getPackageName(), true);
new DeviceManager().setLockTaskModePassword("123456");
```

---

## Recommended Pattern for Payment Apps

```java
// Transaction starts — lock user to payment app
new DeviceManager().setLockTaskMode(getPackageName(), true);
new DeviceManager().setLockTaskModePassword("123456");

// Optional: also hide status bar for cleaner UI
View decorView = getWindow().getDecorView();
decorView.setSystemUiVisibility(
    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    | View.SYSTEM_UI_FLAG_FULLSCREEN
    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
);

// Transaction ends (success, cancel, or error) — unlock
new DeviceManager().setLockTaskMode(getPackageName(), false);
```

## Related APIs (documented elsewhere)

- `SystemProviderImpl.setLockScreenNon()` — disable screen lock entirely (documented in general_sdk.md section 12.3)
- `BaseActivity.fullScreen()` — standard Android fullscreen helper (demo code)
