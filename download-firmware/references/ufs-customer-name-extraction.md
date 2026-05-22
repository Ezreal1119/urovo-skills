# UFS Customer Name Extraction Edge Cases

## "A的B" Chinese Pattern (2026-05-21)

User prompt: "固件下载：SQ29MB GO的BOG的UFS包上传一下链接"

- Interpretation: "GO的BOG" means "BOG of GO" or "BOG under GO project"
- Tried `custom_name="BOG GO"` → Failed: `No UFS folder found for customer 'BOG GO' under /os/i9100/SQ29MB(A13 go)`
- Tried `custom_name="GO BOG"` → Failed: `No UFS folder found for customer 'GO BOG' under /os/i9100/SQ29MB(A13 go)`
- Tried `custom_name="BOG"` → **Success**: Found folder `/os/i9100/SQ29MB(A13 go)/UFS/BOG/`
- Result: `SQ29MB_UFS_BOG_SIGNCHECK_V13.26.0129.01-260129-S_uFWsign_VendorEN.zip`

**Strategy**: When combined names fail, try the SECOND part alone first (the part after "的"). In "GO的BOG", "BOG" is the actual customer name and "GO" is a project identifier.

## Lessons
- When "的" appears, both parts of the phrase are customer/project identifiers
- The combined name may not directly match server folder names
- Server path structure: `/os/i9100/<model>/UFS/<customer_folder>/`
- Try order: second-part-alone → first-part-alone → combined forms
- If script reports "No UFS folder found", ask user to confirm the exact customer/folder name
