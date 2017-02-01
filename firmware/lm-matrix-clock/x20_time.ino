
uint32_t timeCurrent;
uint32_t timeLastTick;
uint16_t timeTreshold;

void timeSetup() {
  timeTreshold = 1000;
  timeSet(0);
}

void timeLoop() {
  uint32_t now = millis();
  if (now - timeLastTick >= timeTreshold) {
    timeCurrent++;
    if (timeCurrent >= 86400l) {
      timeCurrent = 0;
    }
    timeLastTick = now;
  }
}

void timeSet(uint32_t newTime) {
  timeCurrent = newTime;
  timeLastTick = millis();
}

void timeToArray(uint32_t t, uint8_t *result) {
  result[5] = t % 10;
  t /= 10;
  result[4] = t % 6;
  t /= 6;
  result[3] = t % 10;
  t /= 10;
  result[2] = t % 6;
  t /= 6;
  result[1] = t % 10;
  t /= 10;
  result[0] = t;
}

uint32_t timeFromArray(uint8_t *a) {
  uint32_t result = 0;

  uint8_t v = a[0] * 10 + a[1];
  if (v >= 24) return 0;
  result += v * 3600l;

  v = a[2] * 10 + a[3];
  if (v >= 60) return 0;
  result += v * 60l;

  v = a[4] * 10 + a[5];
  if (v >= 60) return 0;
  result += v;

  return result;
}

