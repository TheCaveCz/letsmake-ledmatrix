// unused
void buttonShortPress() {

}

void buttonLongPress() {

}

uint32_t lastTick;

void setup() {

  commSetup();
  buttonSetup();
  displaySetup();
  textSetup();

  lastTick = millis();
  updateText();
  eepromSetup();

  Serial.println("+ready");
}

void updateTime(uint32_t newTime) {
  currentTime = newTime;
  updateText();
  lastTick = millis();
}

void updateText() {
  uint32_t t = currentTime;

  //20:14:22
  //01234567

  textInfo.buffer[7] = '0' + (t % 10);
  t /= 10;
  textInfo.buffer[6] = '0' + (t % 6);
  t /= 6;

  textInfo.buffer[4] = '0' + (t % 10);
  t /= 10;
  textInfo.buffer[3] = '0' + (t % 6);
  t /= 6;

  textInfo.buffer[1] = '0' + (t % 10);
  t /= 10;
  textInfo.buffer[0] = '0' + t;
}

void loop() {
  commCheck();
  buttonCheck();

  uint32_t now = millis();
  if (now - lastTick >= 1000) {
    currentTime++;
    if (currentTime >= 86400l) {
      currentTime = 0;
    }
    updateText();
    lastTick = now;
  }

  textLoop();
}


