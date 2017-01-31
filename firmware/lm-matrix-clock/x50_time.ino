
uint32_t timeCurrent = 0;
uint32_t timeLastTick = 0;


void timeSetup() {
  timeLastTick = millis();
  timeUpdate();
}

void timeLoop() {
  uint32_t now = millis();
  if (now - timeLastTick < 1000) return;

  timeCurrent++;
  if (timeCurrent >= 86400l) {
    timeCurrent = 0;
  }
  timeUpdate();
  timeLastTick = now;
  
  textLoop();
}

void timeSet(uint32_t newTime) {
  timeCurrent = newTime;
  timeUpdate();
  timeLastTick = millis();
}

void timeUpdate() {
  uint32_t t = timeCurrent;

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
