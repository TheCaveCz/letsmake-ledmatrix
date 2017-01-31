#define MODE_TEXT 0
#define MODE_MATRIX 1
#define MODE_MATRIX_HARD 2
#define MODE_COUNT 3

typedef struct {
  void (*loopCallback)();
  void (*buttonCallback)();
  void (*enterCallback)();
} Mode;

Mode modes[MODE_COUNT] = {
  {&textLoop, &textEnter, &textEnter},
  {&matrixLoop, NULL, &matrixEnterEasy},
  {&matrixLoop, NULL, &matrixEnterHard},
};

uint8_t modeCurrent;


void modeSetup() {
  modeSet(0);
}

void modeSet(uint8_t mode) {
  modeCurrent = mode >= MODE_COUNT ? 0 : mode;
  if (modes[modeCurrent].enterCallback != NULL) {
    modes[modeCurrent].enterCallback();
  }
}

void modeLoop() {
  if (modes[modeCurrent].loopCallback != NULL) {
    modes[modeCurrent].loopCallback();
  }
}

void buttonShortPress() {
  if (modes[modeCurrent].buttonCallback != NULL) {
    modes[modeCurrent].buttonCallback();
  }
}

void buttonLongPress() {
  modeCurrent++;
  if (modeCurrent >= MODE_COUNT) {
    modeCurrent = 0;
  }
  if (modes[modeCurrent].enterCallback != NULL) {
    modes[modeCurrent].enterCallback();
  }
}

