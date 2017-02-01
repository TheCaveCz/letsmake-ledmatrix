uint32_t matrixLastTime;
uint8_t matrixHard;


void matrixEnter(bool hard) {
  matrixHard = hard;
  displayStart = 0;
  memset(displayBuffer, 0, sizeof(displayBuffer));
  matrixRefresh();
}

void matrixLoop() {
  if (matrixLastTime != timeCurrent) {
    matrixRefresh();
  }
}

void matrixRefresh() {
  uint8_t t[6];
  timeToArray(timeCurrent, t);

  if (matrixHard) {
    displayBuffer[0] = displayBuffer[1] = (t[0] * 10 + t[1]) << 1;
    displayBuffer[3] = displayBuffer[4] = (t[2] * 10 + t[3]) << 1;
    displayBuffer[6] = displayBuffer[7] = (t[4] * 10 + t[5]) << 1;
  } else {
    displayBuffer[0] = (t[0] << 2) | 0b10000001;
    displayBuffer[1] = (t[1] << 2) | 0b10000001;
    displayBuffer[3] = (t[2] << 2) | 0b10000001;
    displayBuffer[4] = (t[3] << 2) | 0b10000001;
    displayBuffer[6] = (t[4] << 2) | 0b10000001;
    displayBuffer[7] = (t[5] << 2) | 0b10000001;
  }

  displayRefresh();
  matrixLastTime = timeCurrent;
}

