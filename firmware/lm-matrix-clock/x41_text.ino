#define MAX_TEXT_LEN 6
#define ANIMATION_FRAME_TIME 10

typedef struct {
  uint8_t len;
  char buffer[MAX_TEXT_LEN];
  uint8_t delayFrames;
} TextInfo;

TextInfo textInfo;
uint8_t textPos;
uint8_t textDisplayBufferPos;
uint32_t textLastFrameTime;



void textSetup() {
  memset(textInfo.buffer, 0, sizeof(textInfo.buffer));
  textInfo.buffer[2] = 10;
  textInfo.buffer[5] = 11;
  textInfo.len = MAX_TEXT_LEN;
  textInfo.delayFrames = 10;
}

void textEnter() {
  textDisplayBufferPos = displayStart = 0;
  textPos = 0;
  textRefresh();
  textShowNext();
  textShowNext();
  displayRefresh();
  textLastFrameTime = millis();
}

void textLoop() {
  uint32_t now = millis();
  if (now - textLastFrameTime < textInfo.delayFrames * ANIMATION_FRAME_TIME) return;
  textLastFrameTime = now;

  if ((displayStart + 8) % 16 == textDisplayBufferPos) {
    textShowNext();
  }
  displayStart = (displayStart + 1) % 16;
  displayRefresh();
}

void textRefresh() {
  uint8_t a[6];
  timeToArray(timeCurrent, a);

  textInfo.buffer[0] = a[0];
  textInfo.buffer[1] = a[1];
  textInfo.buffer[3] = a[2];
  textInfo.buffer[4] = a[3];
}

uint8_t textDrawChar(char code, uint8_t pos) {
  Character ch;
  memcpy_P(&ch, &textChars[code - 0], sizeof(Character));
  for (uint8_t i = 0; i < ch.cols; i++) {
    displayBuffer[pos] = ch.data[i];
    pos = (pos + 1) % 16;
  }
  displayBuffer[pos] = 0;
  pos = (pos + 1) % 16;
  return pos;
}

void textShowNext() {
  textDisplayBufferPos = textDrawChar(textInfo.buffer[textPos++], textDisplayBufferPos);
  if (textPos >= textInfo.len) {
    textPos = 0;
    textRefresh();
  }
}

