// Handling short and long button press

// how many ms to trigger long press
#define BUTTON_LONG_TRESHOLD 700

// time in millis when there was first button press detection
// 0xffffffff has special meaning - long button event was already fired
uint32_t buttonDownTime;

// forward declarations of handler functions
void buttonShortPress();
void buttonLongPress();

void buttonSetup() {
  // just setting some defaults
  buttonDownTime = 0;
  pinMode(PIN_BUTTON, INPUT_PULLUP);
}

// called in every loop
void buttonCheck() {
  bool pressed = digitalRead(PIN_BUTTON) == LOW;
  // time since button was pressed
  uint32_t delta = buttonDownTime == 0 || buttonDownTime == 0xfffffffful ? 0 : millis() - buttonDownTime;

  if (pressed && buttonDownTime == 0) {
    // button pressed, but time of press wasn't recorded. set it and bail
    buttonDownTime = millis();
    
  } else if (pressed && delta > BUTTON_LONG_TRESHOLD) {
    // button is still pressed and time exceeded long press treshold - fire long press event and mark it
    buttonLongPress();
    buttonDownTime = 0xfffffffful;
    
  } else if (!pressed) {
    // button is not pressed - so if time is good for short press, fire the event and reset time
    if (delta <= BUTTON_LONG_TRESHOLD && delta > 10) {
      buttonShortPress();
    }
    buttonDownTime = 0;
  }
}

