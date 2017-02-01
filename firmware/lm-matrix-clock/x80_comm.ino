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
    r = 255;
    Serial.println(F("-number too high"));
  }
  return r;
}

void commExec() {
  char firstChar = commBuffer[0];
  if (commBufPos == 1 && firstChar == '?') {
    Serial.println(F("+TheCave-LedMatrix-Clock/1.0"));

  } else if (commBufPos == 1 && firstChar == 'B') {
    commSendBrightness();

  } else if (commBufPos == 2 && firstChar == 'B') {
    uint8_t b = commParseHexDigit(1, 15);
    if (b == 255) return;
    displaySetBrightness(b);
    commSendBrightness();

  } else if (commBufPos == 1 && firstChar == 'C') {
    commSendClockMode();

  } else if (commBufPos == 2 && firstChar == 'C') {
    uint8_t newMode = commParseHexDigit(1, CLOCK_MODE_COUNT - 1);
    if (newMode == 255) return;
    clockMode = newMode;
    clockEnter();
    commSendClockMode();

  } else if (commBufPos == 1 && firstChar == 'D') {
    commSendTreshold();

  } else if (commBufPos == 4 && firstChar == 'D') {
    uint16_t a = commParseHexDigit(1, 15);
    if (a == 255) return;
    uint8_t b = commParseHexDigit(2, 15);
    if (b == 255) return;
    uint8_t c = commParseHexDigit(3, 15);
    if (c == 255) return;

    timeTreshold = a << 8 | b << 4 | c;
    commSendTreshold();

  } else if (commBufPos == 1 && firstChar == 'M') {
    commSendMode();

  } else if (commBufPos == 2 && firstChar == 'M') {
    uint8_t newMode = commParseHexDigit(1, MODE_COUNT - 1);
    if (newMode == 255) return;
    modeSet(newMode);
    commSendMode();

  } else if (commBufPos == 1 && firstChar == 'S') {
    commPrintTime();

  } else if (commBufPos == 7 && firstChar == 'S') {
    uint8_t inTime[6];
    for (uint8_t i = 0; i < 6; i++) {
      inTime[i] = commParseHexDigit(i + 1, 9);
      if (inTime[i] == 255) return;
    }
    uint32_t newTime = timeFromArray(inTime);
    if (newTime == 0) {
      Serial.println(F("-invalid time"));
      return;
    }
    timeSet(newTime);
    commPrintTime();

  } else if (commBufPos == 1 && firstChar == 'W') {
    eepromSave();
    Serial.println(F("+OK"));

  } else {
    Serial.println(F("-unknown command"));
  }
}

void commPrintTime() {
  Serial.write('+');
  uint8_t t[6];
  timeToArray(timeCurrent, t);
  for (uint8_t i = 0; i < 6; i++)
    Serial.write('0' + t[i]);
  Serial.println();
}

void commSendBrightness() {
  Serial.write('+');
  Serial.println(displayBrightness, HEX);
}

void commSendMode() {
  Serial.write('+');
  Serial.println(modeCurrent, HEX);
}

void commSendClockMode() {
  Serial.write('+');
  Serial.println(clockMode, HEX);
}

void commSendTreshold() {
  Serial.write('+');
  if (timeTreshold < 256) Serial.write('0');
  if (timeTreshold < 16) Serial.write('0');
  Serial.println(timeTreshold, HEX);
}

