#define CLOCK_MODE_TEXT 0
#define CLOCK_MODE_MATRIX 1
#define CLOCK_MODE_MATRIX_HARD 2

#define CLOCK_MODE_COUNT 3

uint8_t clockMode;

void clockEnter() {
  if (clockMode == CLOCK_MODE_TEXT) {
    textEnter();
  } else {
    matrixEnter(clockMode == CLOCK_MODE_MATRIX_HARD);
  }
}

void clockLoop() {
  if (clockMode == CLOCK_MODE_TEXT) {
    textLoop();
  } else {
    matrixLoop();
  }
}

void clockButton() {
  clockMode++;
  if (clockMode >= CLOCK_MODE_COUNT)
    clockMode = 0;

  clockEnter();
}

