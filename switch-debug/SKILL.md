---
name: switch-debug
description: 切机 | 切DEBUG | 开发者模式 | Switch Debug | Switch Developer | PCTool
---

# Switch Debug Skill

## Purpose

Add device SNs into the PCTool debug/developer switch system, so the device can be switched to Debug Mode by PCTool.

This skill is used when the user wants to enable a device SN for Debug Mode / Developer Mode switching.

---

## When To Use

Use this skill when the user asks to add an SN for debug/developer mode switching.

Typical trigger words:

- Switch Debug
- switch debug mode
- switch developer mode
- enable debug mode
- debug switch
- 切机
- 切 debug
- 切debug
- 切 developer 模式
- 切developer模式
- 加入 debug
- 加入开发者模式

---

## Input Rule

The user prompt must contain:

1. SN
2. Customer name or customer keyword

SN is usually a string of digits or letters.

Examples:

```txt
80152546054401
26SQ68GO051201
```

Customer name can be any literal text provided by the user.

Examples:

```txt
001
TEST001
PowerStore
Retail
客户001
```

The customer value should be extracted literally from the prompt.

Do NOT invent the customer name.

If the SN or customer name is missing, stop and ask the user to provide the missing value.

---

## Main Command

Use this command to add the SN:

`node /Users/patrickxu/.hermes/skills/switch-debug/scripts/login_debug_switch_system.js <sn> "<customer_name>"`

Example:

`node /Users/patrickxu/.hermes/skills/switch-debug/scripts/login_debug_switch_system.js 801625447436 "001"`

Always quote the customer name using double quotes.

--- 

## Delete Command

If the SN already exists, delete it first using:

`node /Users/patrickxu/.hermes/skills/switch-debug/scripts/login_debug_switch_system_delete_sn.js <sn>`

Example:

`node /Users/patrickxu/.hermes/skills/switch-debug/scripts/login_debug_switch_system_delete_sn.js 801625447436`

Expected delete success message:

`Delete device success`

---

## Execution Steps

### Step 1 — Extract SN And Customer Name

Extract:

```txt
sn=<SN from user prompt>
customer_name=<customer name or keyword from user prompt>
```

Example user prompt:

```txt
801625447436加入切机 001
```

Extract:

```txt
sn=801625447436
customer_name=001
```

---

### Step 2 — Add SN To PCTool Debug Switch System

Run:

`node /Users/patrickxu/.hermes/skills/switch-debug/scripts/login_debug_switch_system.js <sn> "<customer_name>"`

Example:

`node /Users/patrickxu/.hermes/skills/switch-debug/scripts/login_debug_switch_system.js 801625447436 "001"`

---

## Result Handling

### Case 1 — Captcha Failed

If output contains:

```txt
captcha failed
```

Then re-run the same add command.

Retry up to 5 times in total.

If it still fails after 5 attempts, report:

```txt
Failed: captcha failed 5 times in a row.
```

Then end the task.

---

### Case 2 — Customer Not Found

If output contains:

```txt
Error: Filtered customer failed: no customer was selected for keyword
```

Then report to the user:

```txt
Failed: customer name does not exist or cannot be selected.
Customer keyword: <customer_name>
```

Do not retry.

End the task.

---

### Case 3 — SN Already Exists

If output contains:

```txt
Error: Add device failed: 设备SN已经存在，请重新输入
```

Then run the delete command:

`node /Users/patrickxu/.hermes/skills/switch-debug/scripts/login_debug_switch_system_delete_sn.js <sn>`

Expected success output:

```txt
Delete device success
```

If delete fails, retry the delete command up to 5 times in total.

If delete still fails after 5 attempts, report the delete failure to the user and end the task.

After delete succeeds, re-run the add command:

`node /Users/patrickxu/.hermes/skills/switch-debug/scripts/login_debug_switch_system.js <sn> "<customer_name>"`

Again, follow all result handling rules.

---

### Case 4 — Add Success

If output contains:

```txt
Add device success
```

Then report success to the user:

```txt
Success: SN has been added to the PCTool debug switch system.
SN: <sn>
Customer: <customer_name>
```

Then end the task.

---

### Case 5 — Unknown Error

If the command returns any error other than the known cases above, report the exact error output to the user and end the task.

Do not guess.

---

## Retry Rules

### Add Command Retry

Retry the add command only when:

```txt
captcha failed
```

Maximum retries:

```txt
5 times in total
```

### Delete Command Retry

Retry the delete command only when deletion fails.

Maximum retries:
```txt
5 times in total
```

### Do Not Retry

```txt
Error: Filtered customer failed: no customer was selected for keyword
```

Because this means the customer keyword is invalid or not found.

---

## Full Flow Summary

```txt
1. Extract SN and customer name from user prompt.
2. Run add command.
3. If captcha failed, retry add command up to 5 times.
4. If customer not found, report customer error and stop.
5. If SN already exists:
   5.1 Run delete command.
   5.2 Retry delete up to 5 times if needed.
   5.3 After delete succeeds, run add command again.
6. If add success, report success.
7. For unknown errors, report the error and stop.
```

---

### Important Notes

* Always use the exact SN provided by the user.
* Always use the exact customer name or customer keyword provided by the user.
* Always quote the customer name in the shell command.
* Do not invent customer names.
* Do not change the SN format.
* Do not manually open the website.
* Use only the provided Node.js scripts.
* Stop clearly when customer selection fails.
* Stop clearly when retry limit is exceeded.
* Report the exact error when an unknown error occurs.
