#define MAX_ANIMATIONS 32
#define ANIMATION_FRAME_TIME 10
#define ANIMATION_NO_FRAME 0xff
#define TRANS_NONE 1
#define TRANS_SLIDE 2
#define TRANS_EMPTY_ANIM 3


typedef struct {
  uint8_t delayFrames;
  uint8_t reserved: 2;
  bool loop: 1;
  uint8_t transition: 2;
  uint8_t framesMax: 3; // maximum frame index
  uint8_t frames[8];
} Animation;

Animation animations[MAX_ANIMATIONS];
Animation *animationCurrent;
uint8_t animationCurrentId;
bool animationFinished;
uint8_t animationFrame;
uint32_t animationLastFrameTime;
bool animationBufferSecondPage;

const Animation defaultAnimations[] PROGMEM = {
  {10, 0, 1, TRANS_NONE, 5, {5, 6, 7, 8, 7, 6}},
  {80, 0, 1, TRANS_NONE, 1, {0, 1}},
  {10, 0, 1, TRANS_SLIDE, 1, {4, 4}},
};


void animationSetup() {
  memset(animations, 0xff, sizeof(animations)); // for eeprom write
  memcpy_P(animations, defaultAnimations, sizeof(defaultAnimations));
  animationCurrentId = 0;
}

void animationEnter() {
  animationPlay(animationCurrentId);
}

void animationLoop() {
  if (animationCurrent == NULL) return;

  uint32_t now = millis();
  if (now - animationLastFrameTime < animationCurrent->delayFrames * ANIMATION_FRAME_TIME) return;
  animationLastFrameTime = now;

  if (animationFinished) return;

  if (animationCurrent->transition == TRANS_SLIDE) {
    displayStart = (displayStart + 1) % 16;
    if (displayStart == 0 || displayStart == 8) {
      animationAdvanceFrame();
    }
  } else {
    displayStart = (displayStart + 8) % 16;
    animationAdvanceFrame();
  }
  displayRefresh();
}

void animationButton() {
  while (1) {
    animationCurrentId++;
    if (animationCurrentId >= MAX_ANIMATIONS) {
      animationCurrentId = 0;
    }
    if (animations[animationCurrentId].transition != TRANS_EMPTY_ANIM || animationCurrentId == 0) {
      animationPlay(animationCurrentId);
      return;
    }
  }
}

void animationPlay(uint8_t animId) {
  animationCurrent = &animations[animId];
  animationFrame = 0;
  animationLastFrameTime = millis();
  animationBufferSecondPage = false;
  animationFinished = false;
  displayStart = 0;

  animationUpdateFrame();
  displayRefresh();
}

void animationUpdateFrame() {
  imageCopyToBuffer(animationCurrent->frames[animationFrame], animationBufferSecondPage);
  imageCopyToBuffer(animationCurrent->frames[animationGetNextFrame()], !animationBufferSecondPage);
}

void animationAdvanceFrame() {
  animationBufferSecondPage = !animationBufferSecondPage;
  animationFrame = animationGetNextFrame();
  animationUpdateFrame();
}

uint8_t animationGetNextFrame() {
  if (animationFrame != animationCurrent->framesMax) {
    // not at the end, can do +1
    return animationFrame + 1;
  } else if (animationCurrent->loop) {
    // at the end and animation loops, start over
    return 0;
  } else {
    // at the end without loop, keep the frame and stop the anim
    animationFinished = true;
    return animationFrame;
  }
}

