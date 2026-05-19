---
name: pos-sdk-helper
description: Use this skill whenever the user asks a question about Urovo SDK integration, POS SDK APIs, EMV kernel APIs, SDK demo usage, or related SDK troubleshooting. This skill searches the local SDK API documentation and demo project to answer SDK-related questions.
---

# SDK Helper

When the user asks an SDK-related question, search the local SDK documentation files and the demo project to find relevant API information and code examples, then compose an answer for the user.

## Available Resources

| Resource | Path | Description |
|---|---|---|
| USDK Documentation | `references/usdk.md` | Low-level device SDK APIs (Device Manager, Beeper, LED, Printer, Scanner, Pinpad, etc.) |
| EMV Kernel Documentation | `references/emv_sdk.md` | EMV Kernel SDK APIs, callbacks, enums, and FAQ entries |
| General SDK Documentation | `references/general_sdk.md` | High-level POS SDK APIs covering all device modules with usage examples |
| Kiosk / Lock Task Mode | `references/kiosk_lock_task.md` | DeviceManager Lock Task Mode APIs (setLockTaskMode, setLockTaskModePassword) — NOT in official docs, discovered from demo code |
| Demo Project | `assets/UrovoPosSdkDemo/` | Android demo app with working code examples for nearly all SDK APIs |

Each `.md` file is organized as a series of `###` sections. Each section describes one API, including:
- **Purpose**: what the API does
- **Signature/prototype**: the Java method signature
- **Parameters**: input parameters and types
- **Return value**: what the method returns
- **Usage notes**: important details about usage
- **Simplified example**: a concise code snippet (present in `references/general_sdk.md`)
- **Demo reference**: path to the corresponding demo Java file in `UrovoPosSdkDemo/`
- **Package/class path**: the fully qualified class for the API

## Workflow

1. **Understand the user's question**: identify which SDK module(s) the question relates to (e.g., EMV, Printer, Scanner, Beeper, Device Manager, Pinpad, etc.).

2. **Search documentation files**: use `grep` to search for relevant APIs in one or more of:
   - `usdk.md` — low-level device SDK APIs
   - `emv_sdk.md` — EMV kernel APIs
   - `general_sdk.md` — general POS SDK APIs (start here for most questions, as it covers a broad range of modules with usage examples)

   The `grep` tool supports full regex. Search for the API name, class name, or relevant keywords from the user's question.

   Example searches:
   ```
   grep pattern="startBeep" include="*.md"
   grep pattern="startKernel" include="*.md"
   grep pattern="Printer" include="*.md"
   grep pattern="getDeviceId\|getIMEI" include="*.md"
   ```

3. **Read the matching API sections**: once you locate the relevant `###` section(s), use `read` with an appropriate offset to get the full API description, including parameters, return value, usage notes, and demo references.

4. **Find demo code**: if the API section has a `Demo reference` field, read the referenced Java file in `assets/UrovoPosSdkDemo/` to extract relevant code examples. The demo files are under:
   ```
   assets/UrovoPosSdkDemo/app/src/main/java/com/urovo/sdk/
   ```
   Common areas:
   - `emv/` — EMV kernel examples (e.g., `EmvActivityNew.java`, `TestEmv.java`)
   - `view/` — UI demo activities for each module (e.g., `BeeperActivity.java`, `PrintActivity.java`, `ScanActivity.java`, `PinpadActivity.java`, `LedActivity.java`)
   - `utils/` — utility classes used by the demos
   - `pollingcard/` — card polling examples
   - `model/` — data models (e.g., `Pinpad.java`, `Translations.java`)

5. **Compose the answer**: synthesize the documentation and demo code into a clear, concise answer. Include:
   - Which SDK module / class provides the relevant API
   - The method signature
   - Key parameters (name, type, meaning)
   - Return value
   - A minimal code example (either from the docs or adapted from demo code)
   - Important usage notes or caveats
   - Reference to the specific demo file for further study

   If the user's question spans multiple APIs, provide a coherent answer covering all relevant APIs.

## Failure Handling

If no relevant information is found in any documentation file or demo project, tell the user:

```
I could not find information about [topic] in the SDK documentation or demo project. Please check the official Urovo SDK documentation for further details.
```

**NOTE**: Some APIs (e.g., `setLockTaskMode`, `setLockTaskModePassword`) exist in the demo code but are NOT documented in the SDK reference files. Always search the demo source code (`assets/UrovoPosSdkDemo/`) when documentation searches come up empty — the demo is often the most complete source of truth for available APIs. See `references/kiosk_lock_task.md` for an example of APIs discovered this way.

## Constraints

- Search all three SDK `.md` files in `/references/` for relevant information — do not assume the answer is only in one file.
- When demo code is available, always check it to validate API signatures and usage patterns.
- Do not fabricate API signatures or parameters. Only provide information found in the documentation files.
- If the documentation mentions a specific version requirement or device model limitation, include that in the answer.
