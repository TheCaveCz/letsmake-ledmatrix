// unused
void buttonShortPress() {

}

void buttonLongPress() {

}


void setup() {
  commSetup();
  buttonSetup();
  displaySetup();
  textSetup();
  timeSetup();
  eepromSetup();

  Serial.println("+ready");
}



void loop() {
  commCheck();
  buttonCheck();

  timeLoop();
}


