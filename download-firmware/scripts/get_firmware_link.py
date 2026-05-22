import subprocess
import shlex
import os
import sys
import re
import posixpath
import fcntl
from contextlib import contextmanager
from datetime import datetime

WORKSPACE = "/Volumes/SSD1T/SharedFiles/hermes_workspace"

SFTP_HOST = "urovo-duanxiongbao@120.76.195.35"

SFTP_OPTS = [
    "-oKexAlgorithms=+diffie-hellman-group1-sha1",
    "-oHostKeyAlgorithms=+ssh-rsa",
    "-oPubkeyAcceptedAlgorithms=+ssh-rsa",
]

os.environ["SSHPASS"] = "Udxb*24081914"

LOG_DIR = "/Users/patrickxu/logs/download-firmware"
SCRIPT_NAME = "download_firmware"
DEBUG_CONSOLE = False


def get_date_string() -> str:
    return datetime.now().strftime("%Y-%m-%d")


def get_timestamp() -> str:
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")


def get_log_path() -> str:
    return os.path.join(LOG_DIR, f"{SCRIPT_NAME}_{get_date_string()}.log")


def ensure_log_dir() -> None:
    os.makedirs(LOG_DIR, exist_ok=True)


def write_log(level: str, message: str) -> None:
    ensure_log_dir()

    line = f"[{get_timestamp()}] [{level}] {message}\n"

    with open(get_log_path(), "a", encoding="utf-8") as file:
        file.write(line)


def log(action: str) -> None:
    message = f"[ACTION] {action}"

    if DEBUG_CONSOLE:
        print(message)

    write_log("ACTION", action)


def info(message: str) -> None:
    if DEBUG_CONSOLE:
        print(message)

    write_log("INFO", message)


def error_log(err) -> None:
    message = str(err)

    write_log("ERROR", message)


def sftp_global_lock_path() -> str:
    return os.path.join(WORKSPACE, ".sftp_global.lock")


def run_sftp(commands: list[str], timeout: int = 60) -> str:
    script = "\n".join(commands + ["bye"])

    cmd = [
        "bash",
        "-lc",
        "sshpass -e sftp "
        + " ".join(shlex.quote(opt) for opt in SFTP_OPTS)
        + f" {shlex.quote(SFTP_HOST)} <<'EOF'\n"
        + script
        + "\nEOF",
    ]

    info("Run SFTP commands:")
    for command in commands:
        info(f"  {command}")
    info(f"SFTP timeout: {timeout}")

    with file_lock(sftp_global_lock_path()):
        log("SFTP global lock acquired")
        res = subprocess.run(
            cmd,
            capture_output=True,
            text=True,
            timeout=timeout,
            env=os.environ.copy(),
        )
        log("SFTP command finished")

    if res.stdout:
        write_log("INFO", f"SFTP stdout:\n{res.stdout.strip()}")

    if res.stderr:
        write_log("INFO", f"SFTP stderr:\n{res.stderr.strip()}")

    if res.returncode != 0:
        write_log("ERROR", f"SFTP failed with return code {res.returncode}")
        raise RuntimeError(res.stderr or res.stdout)

    return res.stdout + res.stderr


def run_shell(command: list[str], timeout: int = 1800) -> str:
    info(f"Run shell command: {' '.join(command)}")
    info(f"Shell timeout: {timeout}")

    res = subprocess.run(
        command,
        capture_output=True,
        text=True,
        timeout=timeout,
    )

    output = res.stdout + res.stderr

    if output:
        write_log("INFO", f"Shell output:\n{output.strip()}")

    if res.returncode != 0:
        write_log("ERROR", f"Shell command failed with return code {res.returncode}")
        raise RuntimeError(output)

    return output


def sftp_ls(path: str) -> str:
    info(f"List SFTP folder: {path}")

    return run_sftp([
        f'cd "{path}"',
        "ls -l",
    ])


# ------------------------------------- #

@contextmanager
def file_lock(lock_path: str):
    os.makedirs(os.path.dirname(lock_path), exist_ok=True)

    info(f"Acquire file lock: {lock_path}")

    with open(lock_path, "w") as lock_file:
        fcntl.flock(lock_file, fcntl.LOCK_EX)
        try:
            yield
        finally:
            fcntl.flock(lock_file, fcntl.LOCK_UN)
            info(f"Release file lock: {lock_path}")


def format_size(size_bytes: int) -> str:
    units = ["B", "KB", "MB", "GB", "TB"]
    size = float(size_bytes)

    for unit in units:
        if size < 1024 or unit == units[-1]:
            if unit == "B":
                return f"{int(size)}{unit}"
            return f"{size:.2f}{unit}"
        size /= 1024


def prepare_firmware(firmware_path: str, size: int) -> None:
    info(f"Prepare firmware: {firmware_path}")
    info(f"Firmware size: {size}")

    firmware_file_name = posixpath.basename(firmware_path)
    lock_path = os.path.join(WORKSPACE, firmware_file_name + ".lock")

    info(f"Firmware file name: {firmware_file_name}")
    info(f"Firmware lock path: {lock_path}")

    with file_lock(lock_path):
        local_path = sftp_get(firmware_path, size)
        info(f"Local firmware path ready: {local_path}")

        download_link = upload_to_r2(local_path)
        info(f"Firmware uploaded link: {download_link}")

    print(f"Firmware Download link: (Size: {format_size(size)})")
    print(download_link)
    print()
    print("Firmware upgrade instruction:")
    print("https://cdn.patrick-shenzhen.org/urovo/manuals/How_to_upgrade_firmware-OS_UFS_SE.zip")


# ------------------------------------- #
        
def extract_internal_name(prompt: str) -> str | None:
    info(f"Extract internal name from prompt: {prompt}")

    k388_match = re.search(r"k388pro", prompt, re.IGNORECASE)
    if k388_match:
        info("Internal name matched: K388PRO")
        return "K388PRO"

    match = re.search(r"sq[a-zA-Z0-9]*", prompt, re.IGNORECASE)
    if not match:
        info("No internal name matched")
        return None

    internal_name = match.group(0).upper()
    info(f"Internal name matched: {internal_name}")

    return internal_name


def is_multiple_tasks(prompt: str) -> bool:
    matches = re.findall(r"sq[a-zA-Z0-9]*", prompt, re.IGNORECASE)
    info(f"Multiple task detection matches: {matches}")

    return len(matches) >= 2


# ------------------------------------- #

def normalize_key(text: str) -> str:
    return text.strip().lower()


def prompt_contains(prompt: str, keyword: str) -> bool:
    return keyword.lower() in prompt.lower()


def error(message: str) -> None:
    write_log("ERROR", message)
    print(message)
    sys.exit(1)


def select_by_firmware_type(options: dict[str, str], firmware_type: str) -> str:
    key = firmware_type.lower()

    info(f"Select firmware type: {firmware_type}")
    info(f"Available firmware options: {options}")

    if key in options:
        selected = options[key]
        info(f"Selected firmware path by key '{key}': {selected}")
        return selected

    if "overseas" in options:
        selected = options["overseas"]
        info(f"Selected overseas firmware path fallback: {selected}")
        return selected

    error(f"No {firmware_type.upper()} firmware path for this model")


def parse_sftp_ls_entries(output: str) -> list[dict[str, str | int]]:
    entries = []

    for line in output.splitlines():
        line = line.strip()

        if not line:
            continue

        if line.startswith("sftp>"):
            continue

        if line.startswith("Connected to"):
            continue

        # Typical ls -l:
        # -rw-r--r--    1 user group 123456 Jan 01 00:00 filename.zip
        # drwxr-xr-x    2 user group   4096 Jan 01 00:00 folder name
        if not (line.startswith("-") or line.startswith("d")):
            continue

        parts = line.split(maxsplit=8)
        if len(parts) < 9:
            continue

        file_type = parts[0][0]
        size_text = parts[4]
        name = parts[8]

        try:
            size = int(size_text)
        except ValueError:
            size = 0

        entries.append({
            "type": file_type,
            "size": size,
            "name": name,
            "raw": line,
        })

    info(f"Parsed SFTP entries count: {len(entries)}")

    return entries


def extract_six_digit_version(filename: str) -> int:
    matches = re.findall(r"\d{6}", filename)
    if not matches:
        return 0
    return max(int(item) for item in matches)


def find_latest_zip_in_sftp_folder(remote_folder: str) -> tuple[str, int]:
    info(f"Find latest zip in SFTP folder: {remote_folder}")

    output = sftp_ls(remote_folder)
    entries = parse_sftp_ls_entries(output)

    zip_files = [
        item for item in entries
        if item["type"] == "-" and str(item["name"]).lower().endswith(".zip")
    ]

    info(f"Zip firmware count: {len(zip_files)}")

    if not zip_files:
        error(f"No zip firmware found in {remote_folder}")

    selected = max(
        zip_files,
        key=lambda item: (
            extract_six_digit_version(str(item["name"])),
            int(item["size"]),
            str(item["name"]),
        ),
    )

    filename = str(selected["name"])
    size = int(selected["size"])

    firmware_path = f"{remote_folder.rstrip('/')}/{filename}"

    info(f"Selected latest firmware: {firmware_path}")
    info(f"Selected latest firmware size: {size}")

    return firmware_path, size


def sftp_quote(value: str) -> str:
    return '"' + value.replace("\\", "\\\\").replace('"', '\\"') + '"'


def sftp_get(remote_path: str, expected_size: int, timeout: int = 1200) -> str:
    info(f"Start SFTP get: {remote_path}")
    info(f"Expected size: {expected_size}")

    firmware_file_name = posixpath.basename(remote_path)
    remote_dir = posixpath.dirname(remote_path)

    final_path = os.path.join(WORKSPACE, firmware_file_name)
    temp_path = final_path + ".part"

    info(f"Remote dir: {remote_dir}")
    info(f"Firmware file name: {firmware_file_name}")
    info(f"Final local path: {final_path}")
    info(f"Temp local path: {temp_path}")

    os.makedirs(WORKSPACE, exist_ok=True)

    # 1. Final file cache check
    if os.path.exists(final_path):
        local_size = os.path.getsize(final_path)

        info(f"Existing final file found: {final_path}")
        info(f"Existing final file size: {local_size}")

        if local_size == expected_size:
            info("Existing final file size matches expected size, use cache")
            return final_path

        info("Existing final file size mismatch, remove final file")
        os.remove(final_path)

    # 2. Remove stale temp file
    if os.path.exists(temp_path):
        info(f"Remove stale temp file: {temp_path}")
        os.remove(temp_path)

    # 3. Download to .part first
    try:
        log("Download firmware to temp file")
        run_sftp(
            [
                f"cd {sftp_quote(remote_dir)}",
                f"get {sftp_quote(firmware_file_name)} {sftp_quote(temp_path)}",
            ],
            timeout=timeout,
        )
    except Exception as exc:
        write_log("ERROR", f"SFTP download failed: {exc}")
        if os.path.exists(temp_path):
            info(f"Remove temp file after download failure: {temp_path}")
            os.remove(temp_path)
        raise

    # 4. Verify temp file
    if not os.path.exists(temp_path):
        error(f"Download failed: temp file not found: {temp_path}")

    temp_size = os.path.getsize(temp_path)
    info(f"Downloaded temp file size: {temp_size}")

    if temp_size != expected_size:
        info("Downloaded temp file size mismatch, remove temp file")
        os.remove(temp_path)
        error(
            f"Download failed: file size mismatch. "
            f"expected={expected_size}, actual={temp_size}"
        )

    # 5. Atomic replace
    info(f"Atomic replace temp file to final file: {final_path}")
    os.replace(temp_path, final_path)

    info(f"SFTP get completed: {final_path}")

    return final_path


def upload_to_r2(local_path: str, timeout: int = 1800) -> str:
    info(f"Upload to R2: {local_path}")

    if not os.path.exists(local_path):
        error(f"Upload failed: local file not found: {local_path}")

    firmware_file_name = os.path.basename(local_path)
    info(f"Upload firmware file name: {firmware_file_name}")

    command = [
        "rclone",
        "copy",
        local_path,
        "r2:firmware/",
        "--s3-no-check-bucket",
        "-P",
    ]

    last_error = ""

    def build_public_url(file_name: str) -> str:
        if file_name.count("__") >= 2:
            encoded = file_name.replace("__", "%5F%5F")
        else:
            encoded = file_name

        return f"https://temp.patrick-shenzhen.org/{encoded}"

    for attempt in range(1, 4):
        try:
            log(f"Upload firmware to Cloudflare R2, attempt {attempt}")
            run_shell(command, timeout=timeout)
            download_url = build_public_url(firmware_file_name)
            info(f"Upload success: {download_url}")
            return download_url
        except Exception as exc:
            last_error = str(exc)
            write_log("ERROR", f"Upload attempt {attempt} failed:\n{last_error}")

    error(f"Upload failed after 3 attempts:\n{last_error}")


def resolve_os_folder(prompt: str, internal_name: str, firmware_type: str) -> str:
    key = internal_name.upper()
    is_go = prompt_contains(prompt, "go")

    info(f"Resolve OS folder: internal_name={internal_name}, firmware_type={firmware_type}, is_go={is_go}")

    os_path_map: dict[str, dict[str, str]] = {
        "SQ28W": {
            "fi": "/os/i2000/SQ28W/标准化版本",
            "in": "/os/i2000/SQ28W/行业版版本",
        },
        "SQ65A": {
            "fi": "/os/i5000/SQ65A/标准化",
        },
        "SQ65B": {
            "fi": "/os/i5300/SQ65B/标准化版本",
            "in": "/os/i5300/SQ65B/行业版",
        },
        "SQ65F": {
            "fi": "/os/i5300L/SQ65F/海外标准化",
            "in": "/os/i5300L/SQ65F/行业版",
        },
        "SQ27M": {
            "fi": "/os/i9000s/SQ27M/海外标准化版本",
            "in": "/os/i9000s/SQ27M/海外行业版版本",
        },
        "SQ29G": {
            "fi": "/os/i9100/SQ29G/二合一标准化版本",
            "in": "/os/i9100/SQ29G/二合一行业版",
        },
        "SQ29M": {
            "fi": "/os/i9100/SQ29M/标准化",
            "in": "/os/i9100/SQ29M/行业版",
        },
        "SQ29WR": {
            "fi": "/os/i9100/SQ29WR/OS/二合一标准化版本",
            "in": "/os/i9100/SQ29WR/OS/二合一行业版",
        },
        "SQ68PN": {
            "in": "/os/i9200/SQ68PN/海外行业版",
        },

        # Overseas/general-only models
        "SQ48": {
            "overseas": "/os/CT48/国内海外二合一通用版本",
        },
        "SQ48C": {
            "overseas": "/os/CT48C/海外通用",
        },
        "SQ58": {
            "overseas": "/os/CT58/国内海外二合一通用版本",
        },
        "SQ58C": {
            "overseas": "/os/CT58C/海外通用",
        },
        "SQ58CU": {
            "overseas": "/os/CT58C A14/二合一通用版本",
        },
        "SQ58S": {
            "overseas": "/os/CT58S A14/国内海外二合一通用",
        },
        "SQ45T": {
            "overseas": "/os/DT40/SQ45T/海外通用版本",
        },
        "SQ53": {
            "overseas": "/os/DT50/SQ53/海外通用/OS包",
        },
        "SQ53ST": {
            "overseas": "/os/DT50/SQ53ST/海外通用（SQ53UR)",
        },
        "SQ53X": {
            "overseas": "/os/DT50/SQ53X/海外通用",
        },
        "SQ610": {
            "overseas": "/os/DT610/SQ610/海外通用",
        },
        "SQ630": {
            "overseas": "/os/DT630/海外通用",
        },
        "SQ66": {
            "overseas": "/os/DT66/SQ66/国内海外二合一通用版本",
        },
        "K388PRO": {
            "overseas": "/os/K388Pro/海外通用",
        },
        "SQ81": {
            "overseas": "/os/P8100/SQ81/OS/海外通用",
        },
        "SQ81A": {
            "overseas": "/os/P8100 4G/SQ81A/国内海外二合一通用版本",
        },
        "SQ83": {
            "overseas": "/os/P8100P/SQ83/OS包/海外通用",
        },
        "SQ83A": {
            "overseas": "/os/P8100P 4G/国内海外二合一通用版本",
        },
        "SQ83S": {
            "overseas": "/os/P8100P 5G/国内海外二合一通用版本",
        },
        "SQ47": {
            "overseas": "/os/RT40/SQ47/OS/海外通用",
        },
        "SQ47S": {
            "overseas": "/os/RT40S/国内海外二合一通用版本",
        },
        "SQ46S": {
            "overseas": "/os/U2S/国内海外二合一通用版本",
        },
    }

    if key == "SQ29MB":
        if is_go:
            options = {
                "fi": "/os/i9100/SQ29MB(A13 go)/标准化",
                "in": "/os/i9100/SQ29MB(A13 go)/海外行业版",
            }
        else:
            options = {
                "fi": "/os/i9100/SQ29MB/海外标准化版本",
                "in": "/os/i9100/SQ29MB/海外行业版本",
            }

        return select_by_firmware_type(options, firmware_type)

    if key == "SQ29MR":
        if is_go:
            options = {
                "fi": "/os/i9100/SQ29MR(A13 go)/海外标准化",
            }
        else:
            options = {
                "fi": "/os/i9100/SQ29MR/海外标准化",
                "in": "/os/i9100/SQ29MR/海外行业版",
            }

        return select_by_firmware_type(options, firmware_type)

    if key == "SQ68":
        if is_go:
            options = {
                "fi": "/os/i9200/SQ68A13GO/标准化",
                "in": "/os/i9200/SQ68A13GO/行业版",
            }
        else:
            options = {
                "fi": "/os/i9200/SQ68/标准化版本",
                "in": "/os/i9200/SQ68/行业版本/带GMS",
            }

        return select_by_firmware_type(options, firmware_type)

    options = os_path_map.get(key)
    if not options:
        error(f"Unsupported internal name for OS firmware: {internal_name}")

    return select_by_firmware_type(options, firmware_type)


def handle_os(prompt: str, internal_name: str, firmware_type: str) -> None:
    info(f"Handle OS firmware: prompt={prompt}, internal_name={internal_name}, firmware_type={firmware_type}")

    folder = resolve_os_folder(prompt, internal_name, firmware_type)
    info(f"Resolved OS folder: {folder}")

    firmware_path, size = find_latest_zip_in_sftp_folder(folder)

    prepare_firmware(firmware_path, size)


# ------------------------------------- #

def handle_se(prompt: str, internal_name: str) -> None:
    info(f"Handle SE firmware: prompt={prompt}, internal_name={internal_name}")

    error("SE NOT SUPPORTED NOW")


# ------------------------------------- #

def resolve_base_folder(internal_name: str) -> str:
    key = internal_name.upper()

    info(f"Resolve base folder: {internal_name}")

    base_path_map = {
        "SQ28W": "/os/i2000/SQ28W",
        "SQ65A": "/os/i5000/SQ65A",
        "SQ65B": "/os/i5300/SQ65B",
        "SQ65F": "/os/i5300L/SQ65F",
        "SQ27M": "/os/i9000s/SQ27M",
        "SQ29G": "/os/i9100/SQ29G",
        "SQ29M": "/os/i9100/SQ29M",
        "SQ29MB": "/os/i9100/SQ29MB",
        "SQ29MR": "/os/i9100/SQ29MR",
        "SQ29WR": "/os/i9100/SQ29WR",
        "SQ68": "/os/i9200/SQ68",
        "SQ68PN": "/os/i9200/SQ68PN",

        "SQ48": "/os/CT48",
        "SQ48C": "/os/CT48C",
        "SQ58": "/os/CT58",
        "SQ58C": "/os/CT58C",
        "SQ58CU": "/os/CT58C A14",
        "SQ58S": "/os/CT58S A14",
        "SQ45T": "/os/DT40/SQ45T",
        "SQ53": "/os/DT50/SQ53",
        "SQ53ST": "/os/DT50/SQ53ST",
        "SQ53X": "/os/DT50/SQ53X",
        "SQ610": "/os/DT610/SQ610",
        "SQ630": "/os/DT630",
        "SQ66": "/os/DT66/SQ66",
        "K388PRO": "/os/K388Pro",
        "SQ81": "/os/P8100/SQ81",
        "SQ81A": "/os/P8100 4G/SQ81A",
        "SQ83": "/os/P8100P/SQ83",
        "SQ83A": "/os/P8100P 4G",
        "SQ83S": "/os/P8100P 5G",
        "SQ47": "/os/RT40/SQ47",
        "SQ47S": "/os/RT40S",
        "SQ46S": "/os/U2S",
    }

    base_folder = base_path_map.get(key)

    if not base_folder:
        error(f"Unsupported internal name for UFS firmware: {internal_name}")

    info(f"Resolved base folder: {base_folder}")

    return base_folder


def resolve_ufs_base_folder(prompt: str, internal_name: str) -> str:
    key = internal_name.upper()
    is_go = prompt_contains(prompt, "go")

    info(f"Resolve UFS base folder: internal_name={internal_name}, is_go={is_go}")

    if key == "SQ29MB":
        if is_go:
            folder = "/os/i9100/SQ29MB(A13 go)"
            info(f"Resolved UFS base folder: {folder}")
            return folder

        folder = "/os/i9100/SQ29MB"
        info(f"Resolved UFS base folder: {folder}")
        return folder

    if key == "SQ29MR":
        if is_go:
            folder = "/os/i9100/SQ29MR(A13 go)"
            info(f"Resolved UFS base folder: {folder}")
            return folder

        folder = "/os/i9100/SQ29MR"
        info(f"Resolved UFS base folder: {folder}")
        return folder

    if key == "SQ68":
        if is_go:
            folder = "/os/i9200/SQ68A13GO"
            info(f"Resolved UFS base folder: {folder}")
            return folder

        folder = "/os/i9200/SQ68"
        info(f"Resolved UFS base folder: {folder}")
        return folder

    return resolve_base_folder(internal_name)


def find_customer_folder(base_folder: str, customer_name: str) -> str:
    info(f"Find customer folder: base_folder={base_folder}, customer_name={customer_name}")

    output = sftp_ls(base_folder)
    entries = parse_sftp_ls_entries(output)

    folders = [
        str(item["name"])
        for item in entries
        if item["type"] == "d"
    ]

    customer = customer_name.strip().lower()

    matched = [
        folder for folder in folders
        if customer in folder.lower()
    ]

    info(f"Customer folder candidates count: {len(folders)}")
    info(f"Matched customer folders: {matched}")

    if not matched:
        error(f"No UFS folder found for customer '{customer_name}' under {base_folder}")

    if len(matched) == 1:
        info(f"Selected customer folder: {matched[0]}")
        return matched[0]

    # Prefer folders that clearly look like UFS/custom folders
    preferred = [
        folder for folder in matched
        if "ufs" in folder.lower() or "定制" in folder.lower() or "custom" in folder.lower()
    ]

    info(f"Preferred customer folders: {preferred}")

    if len(preferred) == 1:
        info(f"Selected preferred customer folder: {preferred[0]}")
        return preferred[0]

    if preferred:
        # If multiple still match, choose the longest name.
        # Usually the longer folder is more specific, e.g. "标准化版本 WUZI UFS定制包"
        selected = sorted(preferred, key=len, reverse=True)[0]
        info(f"Selected longest preferred customer folder: {selected}")
        return selected

    selected = sorted(matched, key=len, reverse=True)[0]
    info(f"Selected longest matched customer folder: {selected}")

    return selected


def handle_ufs(prompt: str, internal_name: str, customer_name: str) -> None:
    info(f"Handle UFS firmware: prompt={prompt}, internal_name={internal_name}, customer_name={customer_name}")

    base_folder = resolve_ufs_base_folder(prompt, internal_name)
    info(f"Resolved UFS base folder: {base_folder}")

    customer_folder_name = find_customer_folder(base_folder, customer_name)
    info(f"Resolved customer folder name: {customer_folder_name}")

    target_folder = f"{base_folder.rstrip('/')}/{customer_folder_name}"
    info(f"Resolved target folder: {target_folder}")

    firmware_path, size = find_latest_zip_in_sftp_folder(target_folder)

    prepare_firmware(firmware_path, size)


def main() -> None:
    info("============================================================")
    info(f"{SCRIPT_NAME} started")
    info(f"Log file: {get_log_path()}")
    info(f"argv: {sys.argv}")

    if len(sys.argv) < 3:
        info("Argument count too small")
        print("Usage:")
        print('  python3 fw.py os "<prompt>" "<firmware_type>"')
        print('  python3 fw.py ufs "<prompt>" "<customer_name>"')
        print('  python3 fw.py se "<prompt>"')
        sys.exit(1)

    mode = sys.argv[1].strip().lower()
    prompt = sys.argv[2].strip()

    info(f"Mode: {mode}")
    info(f"Prompt: {prompt}")

    if mode not in {"os", "se", "ufs"}:
        write_log("ERROR", f"Invalid mode: {mode}")
        print("Invalid mode. Use one of: os, se, ufs")
        sys.exit(1)

    if is_multiple_tasks(prompt):
        write_log("ERROR", "Multi-task detected")
        print("Multi-task detected")
        sys.exit(1)

    internal_name = extract_internal_name(prompt)
    if not internal_name:
        write_log("ERROR", "Internal name not found")
        print("Please enter an internal name(e.g., SQ53ST)")
        sys.exit(1)

    firmware_type = ""
    customer_name = ""

    if mode == "os":
        if len(sys.argv) < 4:
            write_log("ERROR", "Missing firmware_type for OS firmware")
            print("Please enter firmware_type for OS firmware")
            sys.exit(1)

        firmware_type = sys.argv[3].strip().lower()
        info(f"Firmware type: {firmware_type}")

        if not firmware_type:
            write_log("ERROR", "Empty firmware_type for OS firmware")
            print("Please enter firmware_type for OS firmware")
            sys.exit(1)

    elif mode == "ufs":
        if len(sys.argv) < 4:
            write_log("ERROR", "Missing customer name for UFS firmware")
            print('Usage: python3 fw.py ufs "<prompt>" "<customer_name>"')
            sys.exit(1)

        customer_name = sys.argv[3].strip()
        info(f"Customer name: {customer_name}")

        if not customer_name:
            write_log("ERROR", "Empty customer name for UFS firmware")
            print("Please enter customer name for UFS firmware")
            sys.exit(1)

    elif mode == "se":
        pass

    # print("mode:", mode)
    # print("internal_name:", internal_name)
    # print("firmware_type:", firmware_type or "-")
    # print("customer_name:", customer_name or "-")

    if mode == "os":
        handle_os(prompt, internal_name, firmware_type)
    elif mode == "se":
        handle_se(prompt, internal_name)
    elif mode == "ufs":
        handle_ufs(prompt, internal_name, customer_name)

    info(f"{SCRIPT_NAME} finished successfully")
    info("============================================================")


if __name__ == "__main__":
    try:
        main()
    except Exception as exc:
        error_log(exc)
        print(str(exc).strip())
        sys.exit(1)