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

void commPrintDec(const uint8_t b) {
  if (b < 10) Serial.write('0');
  Serial.print(b);
}

uint8_t commParseDecDigit(const uint8_t addr) {
  uint8_t val = commBuffer[addr];
  if (val >= '0' && val <= '9') return val - '0';
  Serial.println(F("-invalid digit"));
  return 255;
}


void commExec() {
  char firstChar = commBuffer[0];
  if (commBufPos == 1 && firstChar == '?') {
    Serial.println(F("+TheCave-LedMatrix-Clock/1.0"));

  } else if (commBufPos == 1 && firstChar == 'S') {
    commPrintTime();

  } else if (commBufPos == 7 && firstChar == 'S') {
    uint8_t a = commParseDecDigit(1);
    if (a == 255) return;
    uint8_t b = commParseDecDigit(2);
    if (b == 255) return;
    uint8_t c = commParseDecDigit(3);
    if (c == 255) return;
    uint8_t d = commParseDecDigit(4);
    if (d == 255) return;
    uint8_t e = commParseDecDigit(5);
    if (e == 255) return;
    uint8_t f = commParseDecDigit(6);
    if (f == 255) return;

    a = a * 10 + b;
    c = c * 10 + d;
    e = e * 10 + f;
    if (a >= 24 || c >= 60 || e >= 60) {
      Serial.println(F("-invalid time"));
      return;
    }

    updateTime(3600l * a + 60l * c + e);

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
  uint32_t t = currentTime;
  commPrintDec(t / 3600);
  t %= 3600;
  commPrintDec(t / 60);
  t %= 60;
  commPrintDec(t);
  Serial.println();
}

