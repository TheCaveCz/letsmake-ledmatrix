#define MODE_CLOCK 0
#define MODE_SETTING_1 1
#define MODE_SETTING_2 2
#define MODE_SETTING_3 3
#define MODE_SETTING_4 4
#define MODE_COUNT 5

typedef struct {
  void (*loopCallback)();
  void (*buttonCallback)();
  void (*enterCallback)();
  void (*exitCallback)();
} Mode;

Mode modes[MODE_COUNT] = {
  {&clockLoop, &clockButton, &clockEnter, NULL},
  {&settingLoop, &settingButton, &settingEnter1, NULL},
  {&settingLoop, &settingButton, &settingEnter2, NULL},
  {&settingLoop, &settingButton, &settingEnter3, NULL},
  {&settingLoop, &settingButton, &settingEnter4, &settingExit}
};

uint8_t modeCurrent;


void modeSetup() {
  modeCurrent = 0;
  modeSet(0);
}

void modeSet(uint8_t mode) {
  if (modes[modeCurrent].exitCallback != NULL) {
    modes[modeCurrent].exitCallback();
  }
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
  if (modes[modeCurrent].exitCallback != NULL) {
    modes[modeCurrent].exitCallback();
  }
  modeCurrent++;
  if (modeCurrent >= MODE_COUNT) {
    modeCurrent = 0;
  }
  if (modes[modeCurrent].enterCallback != NULL) {
    modes[modeCurrent].enterCallback();
  }
}

