---
name: get-tool-link
description: 获取链接 | get link | 给我链接 | 链接 | Link | 生成链接 | generate link | 连接 | 下载 | 下载链接
---

# Get Tool Link Skill

---

## Purpose

This SKILL helps to get the link of the tool based on user's prompt using a Python script.

---

## Work flow

1. **Identify the tool asked**:

The user's prompt should contains only one tool name.
You should reject and end the task if it's not the case.
Then, you should extract the name of the tool.

Example:

```txt
给我PCTool链接
```

You should identify the name of the tool as "PCTool"

2. **Run the script with the tool name you got from last step**:

Command:

```bash
python3 /Users/patrickxu/.hermes/skills/get-tool-link/scripts/get_tool_link.py "<tool_name>"
```

Example:

```bash
python /Users/patrickxu/.hermes/skills/get-tool-link/scripts/get_tool_link.py "PCTool"
```

3. **Return exactly the same output as you got from the script**

---

## Notes

- Do not run any other command other than the one in the script.

