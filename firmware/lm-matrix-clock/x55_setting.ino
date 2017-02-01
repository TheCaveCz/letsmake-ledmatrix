
uint8_t settingDigit;
uint8_t settingValues[6];

void settingEnter1() {
  settingEnter(0);
}

void settingEnter2() {
  settingEnter(1);
}

void settingEnter3() {
  settingEnter(2);
}

void settingEnter4() {
  settingEnter(3);
}

void settingEnter(uint8_t digit) {
  if (digit == 0) {
    timeToArray(timeCurrent, settingValues);
    settingValues[4] = 0;
    settingValues[5] = 0;
  }
  settingDigit = digit;
  displayStart = 0;
  settingRefresh();
}

void settingExit() {
  uint32_t t = timeFromArray(settingValues);
  if (t != 0)
    timeCurrent = t;
}

void settingButton() {
  uint8_t v = settingValues[settingDigit] + 1;
  switch (settingDigit) {
    case 0: if (v > 2) v = 0; break;
    case 1: if (v > (settingValues[0] == 2 ? 3 : 9)) v = 0; break;
    case 2: if (v > 5) v = 0; break;
    case 3: if (v > 9) v = 0; break;
  }
  settingValues[settingDigit] = v;
  settingRefresh();
}

void settingLoop() {
  settingRefresh();
}

void settingRefresh() {
  memcpy_P(displayBuffer, textChars[settingValues[settingDigit]].data, 7);
  displayBuffer[7] = (millis() % 600) < 300 ? 0b00100000 >> settingDigit : 0;
  displayRefresh();
}

