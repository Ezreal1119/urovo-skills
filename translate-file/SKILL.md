---
name: translate-file
description: PDF translate | docx translate | translate document | file translate | translation | 文档翻译 | PDF翻译 | docx翻译 | 翻译一下
---

# Translate File

When the user asks to translate a document from Chinese to English:

## Input Rule

The input prompt must contain a local document path.

Example:

```json
{
  "user_prompt": "帮我翻译一下成英文",
  "quoted_file_path": "/Volumes/SSD1T/SharedFiles/hermes_workspace/product.docx"
}
```

Always use the exact provided document path.

Do NOT:

- scan the folder
- guess the latest path
- reconstruct the path
- search for files manually

## Workflow

1. **Identify the task**: if user asks to do any kind of translation other that translating from Chinese into English. Reject immediately and end the task.

2. **Translate the document name**: translate the name of the document into English-only, don't modify the suffix of the file. The translation will be implemented using the <rename_path> parameter in the script below. Make sure to also replace all the "_" with "-". If the name doesn't have any meaningful name, just give it a string of random strings.

3. **Identify the file path**: get it from the prompt as described above.

4. **Determine the output directory**: Use `/Volumes/SSD1T/SharedFiles/hermes_workspace/output_translated` as the output directory.

5. **Run the translation command**:

```bash
source ~/.hermes/skills/translate-file/api.env && \
~/Python/wordflux/.venv/bin/python ~/Python/wordflux/translate_documents.py \
  "<file_path>" \
  --rename-to <rename_path> \
  --source-lang Chinese \
  --target-lang English \
  --model deepseek-v4-pro \
  --api-key $DEEPSEEK_API \
  --base-url https://api.deepseek.com \
  --max-chunk-size 1000 \
  --max-concurrent 10 \
  --output-dir /Volumes/SSD1T/SharedFiles/hermes_workspace/output_translated \
  --upload-r2
```

Replace `<file_path>` with the actual path to the document.

4. **Return the R2 link**: The command will output a link like:
   ```
   Link: https://temp.patrick-shenzhen.org/firmware/<filename>
   ```
   Give this link to the user as the download URL for the translated file.

## Notes

- The script supports PDF, `.doc`, and `.docx` files.
- The translated file is automatically uploaded to R2 cloud storage.
- If the translation fails, report the error output to the user.
- Do not run any other command other than the one in the script.
