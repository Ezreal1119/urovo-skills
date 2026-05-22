---
name: apk-sign
description: APP sign | APK sign | APP签名 | 签名APK | 签下名 | sign this
---

# APK Sign Skill

## Purpose

Sign a local APK using the browser-based APK signature script, upload the signed APK to Cloudflare R2, and return the public download link.

---

## When To Use

Use this skill when the user asks to sign an APK/APP and provides an explicit local APK path.

Typical trigger words:

- sign APK
- APK signing
- sign this APK
- APP签名
- APK签名
- 签名
- 签下名

---

## Input Rule

The input prompt must contain a local APK path.

Example:

```json
{
  "user_prompt": "App 签名",
  "quoted_file_path": "/Volumes/SSD1T/SharedFiles/hermes_workspace/AQ.apk"
}
```

Always use the exact provided APK path.

Do NOT:

- scan the folder
- guess the latest APK
- reconstruct the path
- search for APK files manually

---

## Execution Rule

Run only this command:

```bash
node /Users/patrickxu/.hermes/skills/apk-sign/scripts/login_signature_system.js "$apk"
```

Where `$apk` is the exact APK path provided by the user.

Example:

```bash
node /Users/patrickxu/.hermes/skills/apk-sign/scripts/login_signature_system.js "/Volumes/SSD1T/SharedFiles/hermes_workspace/AQ.apk"
```

Do NOT run any other command.

---

## What The Script Does

The script will automatically:

1. Validate the APK path
2. Check that the file exists
3. Check that the file ends with `.apk`
4. Generate the signed APK in the same folder

Example:

```txt
Input:
AQ.apk

Output:
AQ_signed.apk
```

5. Open the browser-based signing system
6. Upload the original APK
7. Download the signed APK
8. Upload the signed APK to Cloudflare R2
9. Print the final download URL

Expected final output:

```txt
Signed APK uploaded successfully.
DOWNLOAD_URL=https://temp.patrick-shenzhen.org/apk/AQ_signed.apk
```

---

## Success Rule

If the command succeeds, extract the URL from the line starting with:

```txt
DOWNLOAD_URL=
```

Then report the URL to the user.

Example response:

```txt
Signed APK uploaded successfully.

Download link:
https://temp.patrick-shenzhen.org/apk/AQ_signed.apk
```

---

## Failure Rule

If anything fails:

- report the error message from the command output
- do not retry
- do not debug
- do not run additional commands
- end the task immediately

---

## Important Notes

- Only execute the specified `node` command.
- Do not revise the script.
- Do not run `ls`, `find`, `pwd`, `test`, `rclone`, or any diagnostic command.
- Do not run upload commands manually.
- Do not search for APK files automatically.
- Do not guess filenames.
- Only use the APK path explicitly provided in the prompt.
- The script handles signing, verification, upload, and URL generation internally.
- If the script fails, stop immediately and report the error.