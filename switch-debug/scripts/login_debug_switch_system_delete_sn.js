const { chromium } = require("playwright");
const path = require("path");
const fs = require("fs");
const { execSync } = require("child_process");

const LOG_DIR = "/Users/patrickxu/logs/switch-debug";
const SCRIPT_NAME = "switch_debug";
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
  try {
    ensureLogDir();

    const line = `[${getTimestamp()}] [${level}] ${message}\n`;

    fs.appendFileSync(getLogPath(), line, "utf8");
  } catch (_) {}
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

  writeLog("ERROR", message);
}

const sn = process.argv[2];

info("============================================================");
info("login_debug_switch_system_delete_sn.js started");
info(`Log file: ${getLogPath()}`);
info(`argv: ${process.argv.join(" ")}`);

if (!sn) {
  writeLog("ERROR", "Missing required argument: sn");

  console.error("Usage: node login_debug_switch_system_delete_sn.js <sn>");
  console.error(
    'Example: node login_debug_switch_system_delete_sn.js "801625490277436"',
  );
  process.exit(1);
}

info(`SN: ${sn}`);

console.log("SN:", sn);

(async () => {
  log("Launch browser");

  const browser = await chromium.launch({ headless: true });

  log("Create new page");

  const page = await browser.newPage();

  page.on("dialog", async (dialog) => {
    info(`Native dialog detected: ${dialog.type()} ${dialog.message()}`);

    console.log("Native dialog detected:", dialog.type(), dialog.message());

    await dialog.accept();

    info("Native dialog accepted");

    console.log("Native dialog accepted");
  });

  // ----------1. Enter account/password/OTP---------- //

  log("Open login page");

  await page.goto("http://activate.urovo.com:8118/user/login", {
    waitUntil: "domcontentloaded",
  });

  log("Fill username");

  await page.locator("#username").fill("PatrickXu");

  log("Fill password");

  await page.locator('input[type="password"]').fill("PatrickXu123");

  const captchaPath = "/Volumes/SSD1T/SharedFiles/hermes_workspace/captcha.png";

  log("Locate captcha input box");

  const inputBox = await page.locator("#inputCode").boundingBox();

  if (!inputBox) {
    throw new Error("inputCode not found");
  }

  info(`Captcha input box: ${JSON.stringify(inputBox)}`);

  log(`Save captcha screenshot: ${captchaPath}`);

  await page.screenshot({
    path: captchaPath,
    clip: {
      x: inputBox.x + inputBox.width + 8,
      y: inputBox.y - 2,
      width: 120,
      height: inputBox.height + 4,
    },
  });

  info(`Captcha screenshot saved: ${captchaPath}`);

  console.log("Captcha screenshot saved:", captchaPath);

  log("Run tesseract OCR for captcha");

  const code = execSync(
    `tesseract "${captchaPath}" stdout -c tessedit_char_whitelist=0123456789 --psm 7`,
  )
    .toString()
    .replace(/\D/g, "")
    .trim();

  info(`Captcha code: ${code}`);

  console.log("Captcha code:", code);

  log("Fill captcha code");

  await page.locator("#inputCode").fill(code);

  // 不用 Playwright click，避免它傻等
  log("Click login submit button by page.evaluate");

  await page.evaluate(() => {
    const button = document.querySelector('button[type="submit"]');

    if (!button) {
      throw new Error("Login submit button not found");
    }

    button.click();
  });

  // 最多等 2 秒
  log("Wait 2 seconds after login submit");

  await page.waitForTimeout(2000);

  // 2 秒后检查是否登录成功
  log("Check login result");

  const loginSuccess = await page
    .locator("li.ant-menu-submenu")
    .filter({ hasText: "设备管理" })
    .isVisible()
    .catch(() => false);

  info(`Login success flag: ${loginSuccess}`);

  if (!loginSuccess) {
    writeLog("ERROR", "captcha failed");

    console.log("captcha failed");

    log("Close browser after captcha failed");

    await browser.close();
    process.exit(1);
  }

  info("Login success");

  console.log("Login success");

  // ----------2. Trigger the dialog---------- //

  log("Open 设备管理 menu");

  await page
    .locator("li.ant-menu-submenu")
    .filter({
      hasText: "设备管理",
    })
    .locator(".ant-menu-submenu-title")
    .click();

  log("Open 设备管理 page");

  await page
    .locator('a[href="/device/manage"] span', {
      hasText: "设备管理",
    })
    .click();

  // ----------3. Search device by SN---------- //

  await page.waitForTimeout(1000);

  // 输入设备编号
  log(`Fill device SN search input: ${sn}`);

  await page
    .locator(".table-page-search-wrapper")
    .locator('input[placeholder="请输入设备编号"]')
    .fill(sn);

  await page.waitForTimeout(8000);

  // 点击查询按钮
  log("Click search button");

  await page
    .locator(".table-page-search-wrapper")
    .locator("button")
    .filter({ hasText: "查询" })
    .click();

  await page.waitForTimeout(1000);

  info(`Search completed: ${sn}`);

  console.log(`Search completed: ${sn}`);

  // ----------4. Delete device---------- //

  await page.waitForTimeout(1000);

  log("Try direct delete button");

  const directDeleteClicked = await page.evaluate(() => {
    const links = Array.from(document.querySelectorAll("a"));

    const deleteLink = links.find(
      (a) => a.innerText && a.innerText.trim() === "删除",
    );

    if (!deleteLink) {
      return false;
    }

    deleteLink.click();
    return true;
  });

  info(`Direct delete clicked: ${directDeleteClicked}`);

  if (directDeleteClicked) {
    console.log("Direct delete button clicked");
  } else {
    console.log("Direct delete button not found, using batch delete");

    info("Direct delete button not found, using batch delete");

    // 1. 勾选搜索结果中的设备行
    log("Select target row checkbox");

    const rowSelected = await page.evaluate((sn) => {
      const rows = Array.from(document.querySelectorAll("tr"));

      const targetRow = rows.find((row) => {
        return row.innerText && row.innerText.includes(sn);
      });

      if (!targetRow) {
        return false;
      }

      const checkbox =
        targetRow.querySelector('input[type="checkbox"]') ||
        targetRow.querySelector(".ant-checkbox-input");

      if (!checkbox) {
        return false;
      }

      checkbox.click();
      return true;
    }, sn);

    info(`Target row selected flag: ${rowSelected}`);

    if (!rowSelected) {
      throw new Error(
        `Target device row not found or checkbox not found: ${sn}`,
      );
    }

    console.log("Target row selected");

    await page.waitForTimeout(500);

    // 2. 点击“批量操作”
    log("Locate batch operation button");

    const batchButtonBox = await page
      .locator("button")
      .filter({ hasText: "批量操作" })
      .boundingBox();

    if (!batchButtonBox) {
      throw new Error("Batch operation button not found");
    }

    info(`Batch button box: ${JSON.stringify(batchButtonBox)}`);

    log("Click batch operation button");

    await page.mouse.click(
      batchButtonBox.x + batchButtonBox.width / 2,
      batchButtonBox.y + batchButtonBox.height / 2,
    );

    console.log("Batch operation clicked");

    await page.waitForTimeout(1000);

    // 3. 点击下拉菜单里的“删除”：直接点击菜单项中心
    await page.waitForTimeout(1000);

    log("Locate batch delete menu item");

    const deleteMenuItem = page
      .locator(".ant-dropdown-menu-item")
      .filter({ hasText: "删除" })
      .last();

    await deleteMenuItem.waitFor({
      state: "visible",
      timeout: 5000,
    });

    const deleteMenuBox = await deleteMenuItem.boundingBox();

    if (!deleteMenuBox) {
      throw new Error("Batch delete menu item box not found");
    }

    info(`Delete menu item box: ${JSON.stringify(deleteMenuBox)}`);

    console.log("Delete menu item box:", deleteMenuBox);

    log("Move mouse to batch delete menu item");

    await page.mouse.move(
      deleteMenuBox.x + deleteMenuBox.width / 2,
      deleteMenuBox.y + deleteMenuBox.height / 2,
    );

    await page.waitForTimeout(300);

    log("Click batch delete menu item");

    await page.mouse.click(
      deleteMenuBox.x + deleteMenuBox.width / 2,
      deleteMenuBox.y + deleteMenuBox.height / 2,
    );

    console.log("Batch delete clicked by menu item coordinate");

    await page.waitForTimeout(1000);

    log("Save screenshot after batch delete click");

    await page.screenshot({
      path: "/Volumes/SSD1T/SharedFiles/hermes_workspace/after_batch_delete_click.png",
      fullPage: true,
    });

    info(
      "Screenshot after delete click saved: /Volumes/SSD1T/SharedFiles/hermes_workspace/after_batch_delete_click.png",
    );

    console.log(
      "Screenshot after delete click saved: /Volumes/SSD1T/SharedFiles/hermes_workspace/after_batch_delete_click.png",
    );
  }

  // 4. 等待确认弹窗 / 气泡确认框出现，然后点击确定
  // 4. 确认删除：兼容 Ant 弹窗 / 原生 confirm
  await page.waitForTimeout(1000);

  log("Find delete confirm button");

  const confirmButton = page
    .locator(".ant-popover button, .ant-modal button, button")
    .filter({ hasText: /确\s*定|确\s*认|是/ })
    .last();

  const confirmVisible = await confirmButton
    .isVisible({
      timeout: 3000,
    })
    .catch(() => false);

  info(`Confirm button visible: ${confirmVisible}`);

  if (confirmVisible) {
    log("Click delete confirm button");

    await confirmButton.click({ force: true });

    console.log("Delete confirmed by Ant confirm button");
  } else {
    log("Delete confirm button missing, save screenshot");

    await page.screenshot({
      path: "/Volumes/SSD1T/SharedFiles/hermes_workspace/delete_confirm_missing.png",
      fullPage: true,
    });

    info(
      "No Ant confirm button found. It may have been handled by native dialog. Screenshot saved: /Volumes/SSD1T/SharedFiles/hermes_workspace/delete_confirm_missing.png",
    );

    console.log(
      "No Ant confirm button found. It may have been handled by native dialog. Screenshot saved: /Volumes/SSD1T/SharedFiles/hermes_workspace/delete_confirm_missing.png",
    );
  }

  // ----------5. Check result toast---------- //

  const toast = page.locator(".ant-message-notice-content").last();

  log("Wait for delete result toast");

  await toast.waitFor({
    state: "visible",
    timeout: 5000,
  });

  const toastText = ((await toast.textContent()) || "").trim();

  info(`toast: ${toastText}`);

  console.log("toast:", toastText);

  if (toastText.includes("删除成功")) {
    info("Delete device success");

    console.log("Delete device success");

    log("Close browser after delete success");

    await browser.close();

    info("login_debug_switch_system_delete_sn.js finished successfully");
    info("============================================================");

    return;
  }

  throw new Error(`Delete device failed: ${toastText}`);
})().catch((err) => {
  errorLog(err);
  throw err;
});
