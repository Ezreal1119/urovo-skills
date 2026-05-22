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
const customerKeyword = process.argv[3];

info("============================================================");
info("login_debug_switch_system.js started");
info(`Log file: ${getLogPath()}`);
info(`argv: ${process.argv.join(" ")}`);

if (!sn || !customerKeyword) {
  writeLog("ERROR", "Missing required arguments: sn or customerKeyword");

  console.error(
    "Usage: node login_debug_switch_system.js <sn> <customer_keyword>",
  );
  console.error(
    'Example: node login_debug_switch_system.js "8019231421412" "PowerStore"',
  );
  process.exit(1);
}

info(`SN: ${sn}`);
info(`Customer keyword: ${customerKeyword}`);

console.log("SN:", sn);
console.log("Customer keyword:", customerKeyword);

(async () => {
  log("Launch browser");

  const browser = await chromium.launch({ headless: true });

  log("Create new page");

  const page = await browser.newPage();

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

  log("Click 添加设备 button");

  await page
    .locator("button", {
      hasText: "添加设备",
    })
    .click();

  // ------------------------------------ //

  log("Locate 新增 modal");

  const modal = page.locator(".ant-modal").filter({
    hasText: "新增",
  });

  log(`Fill SN: ${sn}`);

  await modal.locator("#deviceno").fill(sn);

  const customerSelect = modal
    .locator(".ant-form-item")
    .filter({ hasText: "客户名称" })
    .locator(".ant-select");

  const customerFormItem = modal
    .locator(".ant-form-item")
    .filter({ hasText: "客户名称" });

  log("Locate customer form item box");

  const itemBox = await customerFormItem.boundingBox();

  if (!itemBox) {
    throw new Error("customer form item not found");
  }

  info(`Customer form item box: ${JSON.stringify(itemBox)}`);

  // 直接点这一行右侧输入框区域
  log("Click customer select input area");

  await page.mouse.click(
    itemBox.x + itemBox.width * 0.65,
    itemBox.y + itemBox.height / 2,
  );

  await page.waitForTimeout(500);

  // 直接键盘输入，别找 input
  log(`Type customer keyword: ${customerKeyword}`);

  await page.keyboard.type(customerKeyword, { delay: 80 });

  await page.waitForTimeout(1000);

  log("Press Enter to select customer");

  await page.keyboard.press("Enter");

  await page.waitForTimeout(500);

  // 让 Select 失焦，关闭下拉
  log("Press Tab to blur customer select");

  await page.keyboard.press("Tab");
  await page.waitForTimeout(300);

  const selectedText = await customerSelect.textContent();

  info(`selected: ${selectedText}`);

  console.log("selected:", selectedText);

  // ---------------------------------------- //

  log("Read selected customer from modal");

  const selectedCustomer = await page.evaluate(() => {
    const modals = Array.from(document.querySelectorAll(".ant-modal"));
    const modal = modals.find((m) => m.innerText.includes("新增"));
    if (!modal) return "";

    const items = Array.from(modal.querySelectorAll(".ant-form-item"));
    const customerItem = items.find((item) =>
      item.innerText.includes("客户名称"),
    );
    if (!customerItem) return "";

    const selected = customerItem.querySelector(
      ".ant-select-selection-selected-value",
    );
    if (selected) return selected.textContent.trim();

    const item = customerItem.querySelector(".ant-select-selection-item");
    if (item) return item.textContent.trim();

    return "";
  });

  info(`selected customer: ${selectedCustomer}`);

  console.log("selected customer:", selectedCustomer);

  if (!selectedCustomer || selectedCustomer.includes("请选择客户名称")) {
    throw new Error(
      `Filtered customer failed: no customer was selected for keyword "${customerKeyword}"`,
    );
  }

  // 点击确定
  log("Click confirm button");

  await modal
    .locator("button")
    .filter({ hasText: /确\s*定/ })
    .click({ force: true });

  const toast = page.locator(".ant-message-notice-content").last();

  log("Wait for toast");

  await toast.waitFor({
    state: "visible",
    timeout: 5000,
  });

  const toastText = ((await toast.textContent()) || "").trim();

  info(`toast: ${toastText}`);

  console.log("toast:", toastText);

  if (toastText.includes("添加成功")) {
    info("Add device success");

    console.log("Add device success");

    log("Close browser after add success");

    await browser.close();

    info("login_debug_switch_system.js finished successfully");
    info("============================================================");

    return;
  }

  throw new Error(`Add device failed: ${toastText}`);
})().catch((err) => {
  errorLog(err);
  throw err;
});
