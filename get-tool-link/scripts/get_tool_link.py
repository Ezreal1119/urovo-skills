import subprocess
import sys
import shutil
import time


R2_TOOLS_PATH = "r2:firmware/tools/"
PUBLIC_BASE_URL = "https://temp.patrick-shenzhen.org/tools"


# Add extra related links here.
# Key should be lowercase.
# Each tool supports only one link type label, but can contain multiple links.
#
# Example:
# EXTRA_LINKS = {
#     "pctool": {
#         "label": "Video Tutorial",
#         "links": [
#             "https://www.youtube.com/watch?v=xxxx",
#             "https://www.youtube.com/watch?v=yyyy",
#         ],
#     },
# }
EXTRA_LINKS = {
    "pctool": {
      "label": "Video Tutorial",
      "links": [
          "https://www.youtube.com/watch?v=k6IvQ-Uh7jc",
      ],
    },
    "adb": {
      "label": "Web ADB",
      "links": [
          "https://urovo.patrick-shenzhen.org/tools/adb",
      ],
    },
    "ums": {
      "label": "Video Tutorial & Apps",
      "links": [
          "UMS Overview: https://www.youtube.com/watch?v=DtrkYRCYGdQ",
          "Group Management: https://www.youtube.com/watch?v=NibOV3mtuZ8",
          "App Management: https://www.youtube.com/watch?v=xLEUUJlfJdw",
          "Remote Management: https://www.youtube.com/watch?v=UtHbkkDFMsk",
          "Account Management: https://www.youtube.com/watch?v=nkPxqLHZeBM",
          "[App]UMS Agent: https://cdn.patrick-shenzhen.org/urovo/mdm/ums/UMS_2.10.6.20251126_aurovo_release_I9000_V3_20251202153732_signed.apk",
          "[App]UMS AppMarket: https://cdn.patrick-shenzhen.org/urovo/mdm/ums/AppMarket_1.3.6.20240910_20240910091844_release_I9000_V2_signed.apk",
          "[App]UTMS Agent: https://cdn.patrick-shenzhen.org/urovo/mdm/utms/UTMS_2.0.11.20250307_aurovo_release_20250307171427_I9000_V2.apk",
          "[App]UTMS AppMarket: https://cdn.patrick-shenzhen.org/urovo/mdm/utms/Appmarket_UTMS_v19_2.0.6.20250117_I9000_V2_20250117102454_signed.apk"
      ],
    },
}


def error(message: str) -> None:
    print(message)
    sys.exit(1)


def run_rclone_lsf(timeout: int = 300, retries: int = 3) -> list[str]:
    if shutil.which("rclone") is None:
        error("rclone is not installed or not found in PATH")

    command = [
        "rclone",
        "lsf",
        R2_TOOLS_PATH,
        "--s3-no-check-bucket",
    ]

    last_error = ""

    for attempt in range(1, retries + 1):
        try:
            res = subprocess.run(
                command,
                capture_output=True,
                text=True,
                timeout=timeout,
            )

            output = (res.stdout + res.stderr).strip()

            if res.returncode != 0:
                raise RuntimeError(output or "rclone lsf failed")

            files = [
                line.strip()
                for line in res.stdout.splitlines()
                if line.strip() and not line.strip().endswith("/")
            ]

            return files

        except subprocess.TimeoutExpired:
            last_error = f"rclone lsf timed out after {timeout} seconds"
        except Exception as exc:
            last_error = str(exc).strip()

        if attempt < retries:
            time.sleep(2)

    error(f"Failed to list R2 tools after {retries} attempts:\n{last_error}")


def fuzzy_match_by_order(query: str, filename: str) -> bool:
    """
    Match rule:
    Every character in query must appear in filename in the same order.
    Case-insensitive.

    Example:
    query: pctool
    filename: PC_tool.zip
    result: True
    """
    q = query.lower().strip()
    f = filename.lower()

    if not q:
        return False

    index = 0

    for char in f:
        if index < len(q) and char == q[index]:
            index += 1

    return index == len(q)


def build_public_url(filename: str) -> str:
    # Only replace double underscores to avoid WhatsApp markdown parsing.
    # Keep single underscores unchanged.
    safe_filename = filename.replace("__", "%5F%5F")
    return f"{PUBLIC_BASE_URL}/{safe_filename}"


def normalize_tool_key(value: str) -> str:
    return "".join(ch for ch in value.lower().strip() if ch.isalnum())


def find_extra_links(tool_name: str) -> dict[str, list[str]] | None:
    normalized_tool_name = tool_name.lower().strip()
    compact_tool_name = normalize_tool_key(tool_name)

    # Exact key match first.
    if normalized_tool_name in EXTRA_LINKS:
        item = EXTRA_LINKS[normalized_tool_name]
    else:
        item = None

        # Fuzzy key match fallback.
        # Example: user enters "pc tool", key is "pctool".
        for key, value in EXTRA_LINKS.items():
            compact_key = normalize_tool_key(key)

            if compact_tool_name == compact_key:
                item = value
                break

    if not item:
        return None

    label = str(item.get("label", "")).strip()
    links = item.get("links", [])

    cleaned_links = [
        str(link).strip()
        for link in links
        if str(link).strip()
    ]

    if not label or not cleaned_links:
        return None

    return {
        "label": label,
        "links": cleaned_links,
    }


def main() -> None:
    if len(sys.argv) < 2:
        error('Usage: python3 get_tool_link.py "<tool_name>"')

    tool_name = " ".join(sys.argv[1:]).strip()

    if not tool_name:
        error('Usage: python3 get_tool_link.py "<tool_name>"')

    files = run_rclone_lsf()

    if not files:
        error("No files found in R2 tools folder")

    matched_files = [
        filename
        for filename in files
        if fuzzy_match_by_order(tool_name, filename)
    ]

    if not matched_files:
        error(f"No matching tool found for: {tool_name}")

    links = [build_public_url(filename) for filename in matched_files]

    output_blocks = links

    extra = find_extra_links(tool_name)
    if extra:
        extra_block = extra["label"] + ":\n" + "\n".join(extra["links"])
        output_blocks.append(extra_block)

    print("Related links:")
    print()
    print("\n\n".join(output_blocks))


if __name__ == "__main__":
    try:
        main()
    except Exception as exc:
        print(str(exc).strip())
        sys.exit(1)