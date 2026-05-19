# TAPSYS Firmware Handling

## Key Insights from Session

- TAPSYS firmware is located in the `i9100` product folder, not in a separate TAPSYS root folder
- The specific variation folder is `SQ29MB(A13 go)` for 29MB GO devices
- The TAPSYS-specific firmware folder is named `海外标准化Tapsys UFS定制包`
- The firmware file follows the standard naming pattern ending with `uFWsign_VendorEN.zip`

## Search Pattern

When user requests TAPSYS firmware:
1. First check `i9100/SQ29MB(A13 go)` product/variation path
2. Look for folders containing "Tapsys" or "TAPSYS" in the name
3. Verify the firmware file ends with `uFWsign_VendorEN.zip`

## Example Path

`os/i9100/SQ29MB(A13 go)/海外标准化Tapsys UFS定制包/SQ29MB_UFS_TAPSYS_SIGNCHECK_V13.25.1202.01-251202-S_uFWsign_VendorEN.zip`