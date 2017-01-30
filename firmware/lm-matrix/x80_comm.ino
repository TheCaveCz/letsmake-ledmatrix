#define COMM_BUF_SIZE 70

bool commOverflow;
uint8_t commBuffer[COMM_BUF_SIZE];
uint8_t commBufPos;

void commSetup() {
  Serial.begin(115200);
}

void commCheck() {
  int d = Serial.read();
  if (d == '\n') {
    if (commOverflow) {
      commOverflow = false;
      commBufPos = 0;
      Serial.println(F("-command too long"));
    } else if (commBufPos) {
      commBuffer[commBufPos] = 0;
      commExec();
      commBufPos = 0;
    }
  } else if (d >= 0 && d != '\r') {
    commBuffer[commBufPos++] = d;
    if (commBufPos >= sizeof(commBuffer)) {
      commBufPos = 0;
      commOverflow = true;
    }
  }
}

int commParseHex(const uint8_t addr, const uint8_t maxVal = 255) {
  uint8_t r1 = commParseHexDigitUnchecked(addr);
  uint8_t r2 = commParseHexDigitUnchecked(addr + 1);
  if (r1 == 255 || r2 == 255) {
    Serial.println(F("-invalid number"));
    return -1;
  }
  r1 = (r1 << 4) | r2;
  if (r1 > maxVal) {
    Serial.println(F("-number too high"));
    return -1;
  }
  return r1;
}

bool commParseHexArray(const uint8_t addr, uint8_t * buf, const uint8_t len, const uint8_t maxVal = 255) {
  int d;
  for (uint8_t i = 0; i < len; i++) {
    d = commParseHex(addr + i * 2, maxVal);
    if (d == -1) return false;
    buf[i] = d;
  }
  return true;
}

uint8_t commParseHexDigitUnchecked(const uint8_t addr) {
  uint8_t val = commBuffer[addr];
  if (val >= '0' && val <= '9') return val - '0';
  if (val >= 'A' && val <= 'F') return val - 'A' + 10;
  if (val >= 'a' && val <= 'f') return val - 'a' + 10;
  return 255;
}

uint8_t commParseHexDigit(const uint8_t addr, const uint8_t maxVal) {
  uint8_t r = commParseHexDigitUnchecked(addr);
  if (r == 255) {
    Serial.println(F("-invalid number"));
  }
  if (r > maxVal) {
    Serial.println(F("-number too high"));
  }
  return r;
}

void commPrintHex(const uint8_t b) {
  if (b < 16) Serial.write('0');
  Serial.print(b, HEX);
}

void commExec() {
  char firstChar = commBuffer[0];
  if (commBufPos == 1 && firstChar == '?') {
    Serial.println(F("+TheCave-LedMatrix/1.0"));

  } else if (commBufPos == 1 && firstChar == 'W') {
    eepromSave();
    Serial.println(F("+OK"));

  } else if (commBufPos == 1 && firstChar == 'X') {
    eepromLoad();
    Serial.println(F("+OK"));

  } else if (commBufPos == 1 && firstChar == 'L') {
    Serial.write('+');
    commPrintHex(MAX_IMAGES);
    commPrintHex(MAX_ANIMATIONS);
    commPrintHex(MAX_TEXT_LEN);
    Serial.println();

  } else if (commBufPos == 1 && firstChar == 'T') {
    commSendText();

  } else if (commBufPos >= 3 && firstChar == 'T') {
    int textSpeed = commParseHex(1);
    if (textSpeed == -1) return;
    textShow((char*)&commBuffer[3], textSpeed, false);
    if (modeCurrent == MODE_TEXT) textEnter();
    commSendText();

  } else if (commBufPos == 1 && firstChar == 'B') {
    commSendBrightness();

  } else if (commBufPos == 2 && firstChar == 'B') {
    uint8_t b = commParseHexDigit(1, 15);
    if (b == 255) return;
    displaySetBrightness(b);
    commSendBrightness();

  } else if (commBufPos == 1 && firstChar == 'M') {
    commSendMode();

  } else if (commBufPos == 4 && firstChar == 'M') {
    uint8_t newMode = commParseHexDigit(1, MODE_COUNT - 1);
    if (newMode == 255) return;
    int newCurrent = commParseHex(2);
    if (newCurrent == -1) return;
    if (newMode == MODE_IMAGES && newCurrent < MAX_IMAGES) {
      imageCurrent = newCurrent;
    } else if (newMode == MODE_ANIMATIONS && newCurrent < MAX_ANIMATIONS) {
      animationCurrentId = newCurrent;
    }
    modeSet(newMode);
    commSendMode();

  } else if (commBufPos == 3 && firstChar == 'I') {
    int imgId = commParseHex(1, MAX_IMAGES - 1);
    if (imgId == -1) return;
    commSendImage(imgId);

  } else if (commBufPos == 20 && firstChar == 'I') {
    int imgId = commParseHex(1, MAX_IMAGES - 1);
    if (imgId == -1) return;

    Image img;
    img.reserved = 0;
    img.ignored = commBuffer[3] == '0' ? false : true;
    if (!commParseHexArray(4, img.data, sizeof(img.data))) return;

    memcpy(&images[imgId], &img, sizeof(img));
    if (modeCurrent == MODE_IMAGES && imageCurrent == imgId) {
      imageShow(imgId);
    }
    commSendImage(imgId);


  } else if (commBufPos == 3 && firstChar == 'A') {
    int animId = commParseHex(1, MAX_ANIMATIONS - 1);
    if (animId == -1) return;
    commSendAnimation(animId);

  } else if (commBufPos == 24 && firstChar == 'A') {
    int animId = commParseHex(1, MAX_ANIMATIONS - 1);
    if (animId == -1) return;

    Animation anim;
    anim.reserved  = 0;

    int d;
    d = commParseHexDigit(3, TRANS_EMPTY_ANIM);
    if (d == 255) return;
    if (d == 0) d = TRANS_NONE;
    anim.transition = d;

    d = commParseHex(4);
    if (d == -1) return;
    anim.delayFrames = d;

    anim.loop = commBuffer[6] == '0' ? false : true;

    d = commParseHexDigit(7, 7);
    if (d == 255) return;
    anim.framesMax = d;

    if (!commParseHexArray(8, anim.frames, sizeof(anim.frames), MAX_IMAGES - 1)) return;

    memcpy(&animations[animId], &anim, sizeof(anim));
    if (modeCurrent == MODE_ANIMATIONS && animationCurrentId == animId) {
      animationPlay(animId);
    }
    commSendAnimation(animId);


  } else {
    Serial.println(F("-unknown command"));
  }
}

void commSendImage(const uint8_t imgId) {
  Serial.write('+');
  commPrintHex(imgId);
  Serial.write(images[imgId].ignored ? '1' : '0');
  for (uint8_t i = 0; i < 8; i++) {
    commPrintHex(images[imgId].data[i]);
  }
  Serial.println();
}

void commSendText() {
  Serial.write('+');
  commPrintHex(textInfo.delayFrames);
  for (uint8_t i = 0; i < textInfo.len; i++) {
    Serial.write(textInfo.buffer[i]);
  }
  Serial.println();
}

void commSendAnimation(const uint8_t animId) {
  Serial.write('+');
  commPrintHex(animId);
  Serial.write(animations[animId].transition + '0');
  commPrintHex(animations[animId].delayFrames);
  Serial.write(animations[animId].loop ? '1' : '0');
  Serial.write(animations[animId].framesMax + '0');
  for (uint8_t i = 0; i < 8; i++) {
    commPrintHex(animations[animId].frames[i]);
  }
  Serial.println();
}

void commSendBrightness() {
  Serial.write('+');
  Serial.println(displayBrightness, HEX);
}

void commSendMode() {
  Serial.write('+');
  Serial.print(modeCurrent, HEX);
  if (modeCurrent == MODE_IMAGES) {
    commPrintHex(imageCurrent);
  } else if (modeCurrent == MODE_ANIMATIONS) {
    commPrintHex(animationCurrentId);
  } else {
    commPrintHex(0);
  }
  Serial.println();
}

