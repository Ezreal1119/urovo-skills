---
name: download_firmware
version: 2.1.0
description: Use this skill when the user wants to download firmware, upload firmware, get a firmware download link, 获取固件, 下载固件, 上传固件, 获取固件链接, 固件链接, firmware link, firmware upload, firmware download.
category: download_firmware
---

# Download Firmware Skill

## Purpose

This skill is only used for firmware download/upload/link tasks.

The Python script handles all firmware resolving, downloading, verification, uploading, retry, and final output.

Hermes only classifies the request and executes exactly one allowed Python command.

---

## Hard Rules

This is a tool-first skill.

If the request is valid and complete, the first response MUST be a terminal command.

Do NOT explain the workflow.
Do NOT inspect SFTP manually.
Do NOT run `sftp`.
Do NOT run `rclone`.
Do NOT run `curl`.
Do NOT run `ls`, `pwd`, `cat`, `grep`, `find`, `node`, or any other command.
Do NOT access secrets.
Do NOT use `/tmp`.
Do NOT retry with another command after failure, except when the script output contains `Permission denied`.
If retry is allowed, retry the exact same Python command only. Do not modify the command. Do not run any other command.
Do NOT attempt fallback methods.
Do NOT manually answer the firmware request.

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

---

## Step 1: Validate Task Type

Only continue if the user is asking for one of these tasks:

```text
download firmware
upload firmware
get firmware link
firmware link
下载固件
上传固件
固件链接
获取固件链接
帮我找固件
```

If the user is not asking for a firmware download/upload/link task, return only:

```text
This is not a firmware download or upload task.
```

Then end the task.

---

## Step 2: Validate Model / Internal Name

The prompt must contain either:

- an internal name starting with `SQ`, such as `SQ29M`, `SQ81A`, `SQ53ST`
- or `K388Pro`

Matching is case-insensitive.

If the prompt does not contain `SQ` or `K388Pro`, return only:

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

## Step 3: Classify Firmware Mode

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

## Failure Handling

If the Python script fails, return only the script error output exactly as-is.

Do not retry, except when the script output contains `Permission denied`. You should only retry one more time!
Do not run another command.
Do not inspect files.
Do not inspect SFTP.
Do not run fallback tools.
Do not explain the failure unless the script output already explains it.

End the task immediately after returning the script output or script error output.
