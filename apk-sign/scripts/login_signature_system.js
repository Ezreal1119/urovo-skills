const { chromium } = require("playwright");
const path = require("path");
const fs = require("fs");
const { execFile } = require("child_process");

const LOG_DIR = "/Users/patrickxu/logs/apk-sign";
const SCRIPT_NAME = "login_signature_system";
const DEBUG_CONSOLE = false;

function getDateString() {
  const now = new Date();

  const yyyy = now.getFullYear();
  const mm = String(now.getMonth() + 1).padStart(2, "0");
  const dd = String(now.getDate()).padStart(2, "0");

  return `${yyyy}-${mm}-${dd}`;
}

function getTimestamp() {
  const now = new Date();

  const yyyy = now.getFullYear();
  const mm = String(now.getMonth() + 1).padStart(2, "0");
  const dd = String(now.getDate()).padStart(2, "0");
  const hh = String(now.getHours()).padStart(2, "0");
  const mi = String(now.getMinutes()).padStart(2, "0");
  const ss = String(now.getSeconds()).padStart(2, "0");

  return `${yyyy}-${mm}-${dd} ${hh}:${mi}:${ss}`;
}

function getLogPath() {
  return path.join(LOG_DIR, `${SCRIPT_NAME}_${getDateString()}.log`);
}

function ensureLogDir() {
  if (!fs.existsSync(LOG_DIR)) {
    fs.mkdirSync(LOG_DIR, {
      recursive: true,
    });
  }
}

function writeLog(level, message) {
  ensureLogDir();

  const line = `[${getTimestamp()}] [${level}] ${message}\n`;

  fs.appendFileSync(getLogPath(), line, "utf8");
}

function log(action) {
  const message = `[ACTION] ${action}`;

  if (DEBUG_CONSOLE) {
    console.log(message);
  }

  writeLog("ACTION", action);
}

function info(message) {
  if (DEBUG_CONSOLE) {
    console.log(message);
  }

  writeLog("INFO", message);
}

function errorLog(err) {
  const message = err && err.stack ? err.stack : String(err);

  console.error(err);
  writeLog("ERROR", message);
}

function getSignedApkPath(apkPath) {
  const parsed = path.parse(apkPath);

  return path.join(parsed.dir, `${parsed.name}_signed${parsed.ext}`);
}

function runCommand(command, args, timeout = 1800 * 1000) {
  return new Promise((resolve, reject) => {
    const child = execFile(command, args, {
      shell: false,
      timeout,
    });

    let stderr = "";

    child.stdout.on("data", (data) => {
      const text = data.toString().trimEnd();

      if (text) {
        if (DEBUG_CONSOLE) {
          console.log(text);
        }

        writeLog("INFO", text);
      }
    });

    child.stderr.on("data", (data) => {
      const text = data.toString().trimEnd();

      if (text) {
        stderr += `${text}\n`;

        if (DEBUG_CONSOLE) {
          console.error(text);
        }

        writeLog("ERROR", text);
      }
    });

    child.on("error", (err) => {
      reject(err);
    });

    child.on("close", (code) => {
      if (code !== 0) {
        reject(
          new Error(
            `${command} exited with code ${code}${stderr ? `\n${stderr}` : ""}`,
          ),
        );
        return;
      }

      resolve();
    });
  });
}

function buildPublicUrl(fileName) {
  const encoded =
    fileName.split("__").length - 1 >= 2
      ? fileName.replaceAll("__", "%5F%5F")
      : fileName;

  return `https://temp.patrick-shenzhen.org/apk/${encoded}`;
}

async function uploadToR2(localPath, timeout = 1800 * 1000) {
  if (!fs.existsSync(localPath)) {
    throw new Error(`Upload failed: local file not found: ${localPath}`);
  }

  const apkFileName = path.basename(localPath);

  const command = "rclone";
  const args = [
    "copy",
    localPath,
    "r2:firmware/apk/",
    "--s3-no-check-bucket",
    "-P",
  ];

  let lastError = "";

  for (let attempt = 1; attempt <= 3; attempt++) {
    try {
      log(`Upload signed APK to Cloudflare R2, attempt ${attempt}`);

      await runCommand(command, args, timeout);

      const downloadUrl = buildPublicUrl(apkFileName);

      info(`Upload success: ${downloadUrl}`);

      return downloadUrl;
    } catch (err) {
      lastError = err && err.stack ? err.stack : String(err);
      writeLog("ERROR", `Upload attempt ${attempt} failed:\n${lastError}`);
    }
  }

  throw new Error(`Upload failed after 3 attempts:\n${lastError}`);
}

async function findUploadFrame(page) {
  for (const frame of page.frames()) {
    const fileInputCount = await frame
      .locator('input[type="file"]')
      .count()
      .catch(() => 0);

    info(`Frame URL: ${frame.url()}`);
    info(`file input count: ${fileInputCount}`);

    if (fileInputCount > 0) {
      return frame;
    }
  }

  return null;
}

async function clickUploadFileFallback(page, signatureFrame) {
  log("Fallback: click Upload file with stronger methods");

  const uploadBtnReal = signatureFrame
    .locator(".l-toolbar-item", {
      hasText: "Upload file",
    })
    .first();

  await uploadBtnReal.waitFor({
    state: "visible",
    timeout: 15000,
  });

  await uploadBtnReal.scrollIntoViewIfNeeded();

  const box = await uploadBtnReal.boundingBox();

  if (!box) {
    throw new Error("Upload file button bounding box not found");
  }

  info(
    `Upload file button box: x=${box.x}, y=${box.y}, width=${box.width}, height=${box.height}`,
  );

  // Method 1: normal click
  await uploadBtnReal.click({
    force: true,
  });

  await page.waitForTimeout(1500);

  // Method 2: mouse coordinate click
  await page.mouse.click(box.x + box.width / 2, box.y + box.height / 2);

  await page.waitForTimeout(1500);

  // Method 3: dispatch DOM mouse events
  await uploadBtnReal.evaluate((el) => {
    el.dispatchEvent(new MouseEvent("mouseover", { bubbles: true }));
    el.dispatchEvent(new MouseEvent("mousedown", { bubbles: true }));
    el.dispatchEvent(new MouseEvent("mouseup", { bubbles: true }));
    el.dispatchEvent(new MouseEvent("click", { bubbles: true }));
  });

  await page.waitForTimeout(3000);
}

async function main() {
  const url = "https://sign.urovo.com:8083/SDBConsole/Login.action";
  const username = "xialiangliang";
  const password = "e376i9g";

  const apkPath = process.argv[2];

  info("============================================================");
  info(`${SCRIPT_NAME} started`);
  info(`Log file: ${getLogPath()}`);

  if (apkPath) {
    info(`APK path: ${apkPath}`);
    info(`APK file name: ${path.basename(apkPath)}`);
  }

  if (!apkPath) {
    throw new Error("Usage: node login_signature_system.js <apk_path>");
  }

  if (!fs.existsSync(apkPath)) {
    throw new Error(`APK file not found: ${apkPath}`);
  }

  if (path.extname(apkPath).toLowerCase() !== ".apk") {
    throw new Error(`File is not an APK: ${apkPath}`);
  }

  const outputDir = path.dirname(path.resolve(apkPath));
  const savePath = getSignedApkPath(apkPath);
  const finalFileName = path.basename(savePath);

  info(`Output directory: ${outputDir}`);
  info(`Signed APK path: ${savePath}`);
  info(`Signed APK file name: ${finalFileName}`);

  if (fs.existsSync(savePath)) {
    try {
      info(
        `Old signed APK already exists, delete it before new signing: ${savePath}`,
      );
      fs.unlinkSync(savePath);
    } catch (err) {
      throw new Error(
        `Failed to delete existing signed APK: ${savePath}\n${
          err && err.stack ? err.stack : String(err)
        }`,
      );
    }
  }

  log("Launch browser");

  const browser = await chromium.launch({
    headless: true,
  });

  log("Create browser context");

  const context = await browser.newContext({
    acceptDownloads: true,
  });

  log("Create new page");

  const page = await context.newPage();

  log(`Open login page: ${url}`);

  await page.goto(url, {
    waitUntil: "domcontentloaded",
  });

  log("Fill username");

  await page
    .locator('input[name="username"], input[type="text"]')
    .first()
    .fill(username);

  log("Fill password");

  await page
    .locator('input[name="password"], input[type="password"]')
    .first()
    .fill(password);

  log("Click login");

  await page
    .getByRole("button", {
      name: /login/i,
    })
    .click();

  log("Wait for network idle after login");

  await page
    .waitForLoadState("networkidle", {
      timeout: 15000,
    })
    .catch(() => {
      info("Network idle wait timed out after login, continue");
    });

  log("Open File signature management");

  await page
    .getByText("File signature management", {
      exact: true,
    })
    .click();

  log("Wait after opening File signature management");

  await page.waitForTimeout(8000);

  log("Find signature management frame");

  const signatureFrame = page
    .frames()
    .find((frame) => frame.url().includes("signedManage_init.action"));

  if (!signatureFrame) {
    throw new Error("Signature management frame not found");
  }

  info(`Signature frame URL: ${signatureFrame.url()}`);

  const uploadBtn = signatureFrame.locator(".l-toolbar-item", {
    hasText: "Upload file",
  });

  log("Wait for Upload file button");

  await uploadBtn.waitFor({
    state: "visible",
    timeout: 15000,
  });

  log("Click upload file");

  await page.waitForTimeout(5000);

  // First try: normal click
  await uploadBtn.first().click({
    force: true,
  });

  await page.waitForTimeout(5000);

  let uploadFrame = null;

  info("Current frames after normal Upload file click:");

  uploadFrame = await findUploadFrame(page);

  // Fallback: if normal click did not open upload input
  if (!uploadFrame) {
    log("Upload input not found after normal click, start fallback click");

    await clickUploadFileFallback(page, signatureFrame);

    info("Current frames after fallback Upload file click:");

    uploadFrame = await findUploadFrame(page);
  }

  if (!uploadFrame) {
    const screenshotPath =
      "/Users/patrickxu/Desktop/upload_input_not_found.png";

    log(`Upload input not found, save screenshot: ${screenshotPath}`);

    await page.screenshot({
      path: screenshotPath,
      fullPage: true,
    });

    throw new Error(
      "Upload input not found. Screenshot saved: /Users/patrickxu/Desktop/upload_input_not_found.png",
    );
  }

  info(`Upload frame URL: ${uploadFrame.url()}`);

  log(`Select APK: ${path.basename(apkPath)}`);

  await uploadFrame
    .locator('input[type="file"]')
    .first()
    .setInputFiles(apkPath);

  log("Wait after selecting APK");

  await page.waitForTimeout(5000);

  let buttonFrame = null;

  log("Find upload confirm button");

  for (const frame of page.frames()) {
    const count = await frame
      .locator(".l-dialog-btn-inner", {
        hasText: "Upload",
      })
      .count()
      .catch(() => 0);

    if (count > 0) {
      buttonFrame = frame;
      break;
    }
  }

  if (!buttonFrame) {
    throw new Error("Upload button not found");
  }

  info(`Upload button frame URL: ${buttonFrame.url()}`);

  log("Click upload confirm");

  await buttonFrame
    .locator(".l-dialog-btn-inner", {
      hasText: "Upload",
    })
    .click({
      force: true,
    });

  log("Wait for upload completed");

  await uploadFrame
    .getByText("Uploaded successfully", {
      exact: true,
    })
    .waitFor({
      state: "visible",
      timeout: 600000,
    });

  log("Upload completed");

  let closeFrame = null;

  log("Find close button");

  for (const frame of page.frames()) {
    const count = await frame
      .locator(".l-dialog-btn-inner", {
        hasText: "Close",
      })
      .count()
      .catch(() => 0);

    if (count > 0) {
      closeFrame = frame;
      break;
    }
  }

  if (!closeFrame) {
    throw new Error("Close button not found");
  }

  info(`Close button frame URL: ${closeFrame.url()}`);

  log("Click close dialog");

  await closeFrame
    .locator(".l-dialog-btn-inner", {
      hasText: "Close",
    })
    .click({
      force: true,
    });

  log("Wait after closing dialog");

  await page.waitForTimeout(5000);

  log("Prepare download listener");

  const downloadPromise = page.waitForEvent("download");

  log("Click signature download");

  await signatureFrame
    .locator('input[value="Signature file download"]')
    .first()
    .click({
      force: true,
    });

  log("Wait for download event");

  const download = await downloadPromise;

  const suggestedName = download.suggestedFilename();

  info(`Suggested download file name: ${suggestedName}`);
  info(`Final download file name: ${finalFileName}`);

  log(`Save downloaded file: ${savePath}`);

  await download.saveAs(savePath);

  info(`Downloaded: ${savePath}`);

  if (!fs.existsSync(savePath)) {
    throw new Error(`Signed APK was not found after download: ${savePath}`);
  }

  const downloadUrl = await uploadToR2(savePath);

  log(`Delete local APK files after upload`);

  for (const filePath of [savePath, apkPath]) {
    try {
      if (fs.existsSync(filePath)) {
        fs.unlinkSync(filePath);
        info(`Deleted local APK file: ${filePath}`);
      } else {
        info(`Local APK file already missing, skip delete: ${filePath}`);
      }
    } catch (err) {
      writeLog(
        "ERROR",
        `Failed to delete local APK file after upload: ${filePath}\n${
          err && err.stack ? err.stack : String(err)
        }`,
      );
    }
  }

  console.log("Signed APK uploaded successfully.");
  console.log(`DOWNLOAD_URL=${downloadUrl}`);

  writeLog("INFO", "Signed APK uploaded successfully.");
  writeLog("INFO", `Download link: ${downloadUrl}`);
  writeLog("INFO", `DOWNLOAD_URL=${downloadUrl}`);

  log("Close browser context");

  await context.close();

  log("Close browser");

  await browser.close();

  info("Browser closed");
  writeLog("INFO", "Browser closed");
  writeLog("INFO", `${SCRIPT_NAME} finished successfully`);
  info("============================================================");

  process.exit(0);
}

main().catch((err) => {
  errorLog(err);
  process.exit(1);
});
