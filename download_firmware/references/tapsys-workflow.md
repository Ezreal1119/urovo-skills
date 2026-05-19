# TAPSYS Firmware Workflow

## Discovery Summary

During firmware download sessions for TAPSYS devices, the following pattern was observed:

1. **Product Identification**: TAPSYS 29MB GO devices use the i9100 platform
2. **Variation Path**: `i9100/SQ29MB(A13 go)`
3. **TAPSYS Folder**: `海外标准化Tapsys UFS定制包`
4. **Firmware Pattern**: Files ending with `uFWsign_VendorEN.zip`

## Verification Steps

- Always verify the firmware file size matches expectations (e.g., ~46MB for TAPSYS 29MB GO)
- Check that the filename contains both "TAPSYS" and "uFWsign_VendorEN.zip"
- Confirm the path follows the standard structure: `os/<product>/<variation>/<tapsys-folder>/<firmware-file>`

## Common Issues

- TAPSYS folders may be named with variations like "Tapsys", "TAPSYS", or "TAP SYS"
- The i9100 product folder contains multiple 29MB variants - ensure selecting the correct (A13 go) version
- Always double-check that the firmware is for the correct region (海外 = overseas)