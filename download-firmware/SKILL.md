---
name: download-firmware
description: Download firmware | Upload firmware | get a firmware download link | 获取固件 | 下载固件 | 上传固件 | 获取固件链接 | 固件链接 | OS/UFS上传链接 | OS/UFS下载 | firmware link, firmware upload, firmware download.
---

# Download Firmware Skill

## Purpose

This skill is only used for firmware download/upload/link tasks.

The Python script handles all firmware resolving, downloading, verification, uploading, retry, and final output.

Hermes only classifies the request and executes exactly one allowed Python command.

---

## Hard Rules

The ONLY terminal command allowed by this skill is:

```bash
python3 /Volumes/SSD1T/SharedFiles/hermes_workspace/scripts/get_firmware_link.py ...
```

Only use this script:

```text
/Volumes/SSD1T/SharedFiles/hermes_workspace/scripts/get_firmware_link.py
```

Only use this workspace:

```text
/Volumes/SSD1T/SharedFiles/hermes_workspace
```

Using terminal commands other than this one is strictly forbidden.

---

## Step 1: Validate Model / Internal Name

The prompt must contain either:

- an internal name starting with `SQ`, such as `SQ29M`, `SQ81A`, `SQ53ST`
- or `K388Pro`

Matching is case-insensitive. The script's regex is `sq[a-zA-Z0-9]*` — the `SQ` prefix is mandatory.

**Common shorthand**: Users often omit the `SQ` prefix (e.g. "65B" instead of "SQ65B", "65F" instead of "SQ65F"). If the prompt contains a number+letter pattern that looks like a model shorthand but lacks the `SQ` prefix, prepend `SQ` to the prompt before passing it to the script.

If the prompt does not contain `SQ` or `K388Pro` (after any shorthand resolution), return only:

```text
Please provide an internal model name, such as SQ29M, SQ81A, SQ53ST, or K388Pro.
```

Then end the task.

If the prompt contains more than one internal model name, return only:

```text
Multi-task detected. Please request only one firmware at a time.
```

Then end the task.

---

## Step 2: Classify Firmware Mode

Classify the request into exactly one of:

```text
os
ufs
se
```

### SE

If the prompt includes `SE` as a firmware type, return only:

```text
SE firmware is not supported now.
```

Then end the task.

### UFS

If the prompt includes any of the following, classify it as `ufs`:

```text
UFS
Custom
定制
```

Matching is case-insensitive.

For UFS, also extract `custom_name`.

The `custom_name` is usually an independent customer/project keyword, for example:

```text
WUZI
GETPAYED
OP
TAPSYS
PSG
GERTEC
```

Do not use these words as `custom_name`:

```text
SQ model names
UFS
Custom
定制
firmware
固件
download
下载
upload
上传
link
链接
OS
SE
```

**Chinese "的" separator**: When the prompt contains a "A的B" pattern (e.g. "GO的BOG"), both A and B may be relevant customer/project identifiers. Try the RIGHT side first (B) as a single `custom_name`. If that fails, try combined forms like "B A" or "A B". The script handles folder matching internally.

**Multiple candidate names**: If the prompt contains multiple potential customer names and it's unclear which combination is correct, follow this order:
1. If the prompt uses "的" (e.g. "GO的BOG"), try the **second part alone** first (e.g. "BOG"). This is typically the actual customer name.
2. If that fails, try the first part alone.
3. Then try combined forms (e.g. "BOG GO", "GO BOG").

The script will report `No UFS folder found for customer '<name>'` if the name doesn't match.

If the prompt is UFS but no obvious customer/project name is provided, return only:

```text
Please provide the UFS customer name as well.
```

Then end the task.

### OS

If the prompt is not SE and not UFS, classify it as `os`.

For OS, determine `firmware_type` as one of:

```text
fi
in
overseas
```

Use semantic judgment, not only exact keyword matching.

Classify as `fi` if the prompt means financial / standard / PCI firmware, for example:

```text
标准
金融
PCI
FI
Financial
financial version
standard financial
```

Classify as `in` if the prompt means non-financial / industrial firmware, for example:

```text
非金
非金融
行业
行业版
Non-PCI
IN
Industrial
Non-Financial
non financial
```

Classify as `overseas` if the prompt does not clearly indicate `fi` or `in`.

---

## Step 4: Execute Exactly One Command

Use `timeout: 3600` for the terminal command.
Do not use the default 300-second timeout because firmware download/upload may take longer.

### For OS

Execute exactly one terminal command:

```bash
python3 /Volumes/SSD1T/SharedFiles/hermes_workspace/scripts/get_firmware_link.py os "<original_prompt>" "<firmware_type>"
```

Examples:

```bash
python3 /Volumes/SSD1T/SharedFiles/hermes_workspace/scripts/get_firmware_link.py os "download SQ81A OS firmware" "overseas"
```

```bash
python3 /Volumes/SSD1T/SharedFiles/hermes_workspace/scripts/get_firmware_link.py os "download SQ65B financial firmware" "fi"
```

```bash
python3 /Volumes/SSD1T/SharedFiles/hermes_workspace/scripts/get_firmware_link.py os "download SQ65B non-financial firmware" "in"
```

### For UFS

Execute exactly one terminal command:

```bash
python3 /Volumes/SSD1T/SharedFiles/hermes_workspace/scripts/get_firmware_link.py ufs "<original_prompt>" "<custom_name>"
```

Example:

```bash
python3 /Volumes/SSD1T/SharedFiles/hermes_workspace/scripts/get_firmware_link.py ufs "download SQ27M WUZI UFS firmware" "WUZI"
```

---

## Step 5: Return Result

Return the Python script output to the user exactly as-is.

Do not rewrite it.
Do not summarize it.
Do not translate it.
Do not add explanation.
Do not add comments.
Do not add links.
Do not add follow-up questions.
Do not add markdown formatting unless it already exists in the script output.

---

## Pitfalls & Known Issues

### Model name extraction requires "SQ" prefix
The script's `extract_internal_name()` uses regex `sq[a-zA-Z0-9]*`. If the user says "65B" or "29M" without the "SQ" prefix, extraction fails with `Please enter an internal name(e.g., SQ53ST)`. **Always ensure the prompt contains the full SQ model name** (e.g., "SQ65B" not just "65B"). If user omits "SQ", prepend it before passing to the script.

### Script returned wrong model's firmware
When requesting SQ65F (latest overseas financial), the script returned SQ65B firmware link instead. This may indicate a fallback/lookup bug in `resolve_os_folder()`. If the returned firmware filename doesn't match the requested model, **verify by listing the remote directory directly via SFTP** before presenting the link to the user.

### SFTP listing can timeout
Remote SFTP directory listings may timeout. Use `background=true` with longer timeouts for SFTP commands.

## Failure Handling

If the Python script fails, return only the script error output exactly as-is.

Do not retry, except when the script output contains `Permission denied`. You should only retry one more time!
Do not run another command.
Do not inspect files.
Do not inspect SFTP.
Do not run fallback tools.
Do not explain the failure unless the script output already explains it.

End the task immediately after returning the script output or script error output.

---

## Known Pitfalls

### Model shorthand (missing SQ prefix)
Users frequently say "65B" or "65F" instead of "SQ65B" or "SQ65F". The script's regex `sq[a-zA-Z0-9]*` requires the `SQ` prefix. Always check and prepend `SQ` if the user's shorthand is missing it.

### Script returned wrong model's firmware
The script may return another model's firmware (e.g., SQ65B link for SQ65F request). If the returned filename doesn't match the requested model, report this discrepancy to the user. Do NOT try to manually query SFTP — the script is the single source of truth per the hard rules.

### Background processes and SSHPASS
The Python script sets SSHPASS internally via `os.environ`. If you ever need to run `sshpass` commands directly, the background terminal session won't inherit SSHPASS from the shell environment — the script's internal handling is the reliable path.
