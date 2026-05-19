---
name: apk-sign
description: APP sign | APK sign | APP签名 | 签名APK | 签下名 | sign this
---

# APK Sign Skill

## Purpose

Sign an APK using the local browser-based APK signature script, then upload the signed APK to Cloudflare R2.

---

## When To Use

Use this skill when:

- The user asks to sign an APK/APP

---

## Input Rule

The input prompt must contain a local APK path.

Example:

```json
{
  "user_prompt": "App 签名",
  "quoted_file_path": "/Volumes/SSD1T/SharedFiles/hermes_workspace/xxx.apk"
}
```

Always use the exact provided path.

Do NOT:

- scan the folder
- guess the latest APK
- reconstruct the path
- search for APK files manually

---

## Output Rule

The signed APK must be generated as a new file.

Example:

Input:

```txt
/Volumes/SSD1T/SharedFiles/hermes_workspace/AQ.apk
```

Output:

```txt
/Volumes/SSD1T/SharedFiles/hermes_workspace/AQ_signed.apk
```

Never overwrite the original APK.

---

## Execution Steps

### Step 1 — Extract APK Path

Extract the APK local path from the prompt.

Example:

```txt
/Volumes/SSD1T/SharedFiles/hermes_workspace/AQ.apk
```

Verify:

- the file exists
- the path ends with `.apk`

If not valid, stop and report the error.

---

### Step 2 — Generate Signed APK Path

Generate the signed APK path by adding `_signed` before `.apk`.

Example:

```bash
apk="/Volumes/SSD1T/SharedFiles/hermes_workspace/AQ.apk"

signed_file_name="$(basename "${apk%.apk}_signed.apk")"
signed_apk="$(dirname "$apk")/$signed_file_name"
```

Result:

```txt
/Volumes/SSD1T/SharedFiles/hermes_workspace/AQ_signed.apk
```

---

### Step 3 — Run APK Signing Script

Run:

```bash
node /Users/patrickxu/.hermes/skills/apk-sign/scripts/login_signature_system.js "$apk" "$signed_file_name"
```

Expected flow:

- open browser
- login
- upload APK
- sign APK
- download signed APK
- close browser

Expected output example:

```txt
[ACTION] Fill username
[ACTION] Fill password
[ACTION] Click login
[ACTION] Open File signature management
[ACTION] Click upload file
[ACTION] Select APK: AQ.apk
[ACTION] Click upload confirm
[ACTION] Upload completed
[ACTION] Click close dialog
[ACTION] Click signature download
Downloaded: /Volumes/SSD1T/SharedFiles/hermes_workspace/AQ_signed.apk
Browser closed
```

---

### Step 4 — Verify Signed APK Exists

Verify:

```bash
test -f "$signed_apk"
```

If the signed APK does not exist:

- stop execution
- report signing failure

---

### Step 5 — Upload Signed APK To Cloudflare R2

Run:

```bash
rclone copy "$signed_apk" r2:firmware/apk/ --s3-no-check-bucket -P
```

Expected result:

- signed APK uploaded successfully
- upload progress displayed

---

### Step 6 — Report the download link to the user

Generate the download URL using the signed APK file name only.

```bash
signed_file_name="$(basename "$signed_apk")"
download_url="https://temp.patrick-shenzhen.org/apk/$signed_file_name"
```

Report:

```text
Signed APK uploaded successfully.

Download link:
<download_url>
```

---

## Full Command Template

```bash
apk="/Volumes/SSD1T/SharedFiles/hermes_workspace/AQ.apk"

signed_file_name="$(basename "${apk%.apk}_signed.apk")"
signed_apk="$(dirname "$apk")/$signed_file_name"

node ~/browser-agent/login_signature_system.js "$apk" "$signed_file_name"

test -f "$signed_apk"

rclone copy "$signed_apk" r2:firmware/apk/ --s3-no-check-bucket -P

download_url="https://temp.patrick-shenzhen.org/apk/$signed_file_name"

echo "$download_url"
```

---

## Important Notes

- You MUST NOT revise the script. End the task if the signing process failed.
- Stop the end immediately if anything goes wrong. Don't try to debug by yourself.
- Always quote file paths using double quotes
- Never overwrite the original APK
- Always generate a new `_signed.apk`
- Stop immediately if signing fails
- Stop immediately if the signed APK file does not exist
- If upload fails, report the local signed APK path
- Do not attempt to search for APK files automatically
- Do not guess filenames
- Only use the APK path explicitly provided in the prompt