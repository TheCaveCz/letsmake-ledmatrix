package thecave.matrixcontrol.device;


public class DeviceAnimation {
    public static final int MAX_FRAMES = 8;
    public static final int UNUSED_FRAME_ID = -1;


    public enum Transition {
        NONE('1', "Change frames"), SLIDE('2', "Slide (right->left)"), UNUSED('3', "Ignored animation");

        private final char value;
        private final String description;

        Transition(char value, String description) {
            this.value = value;
            this.description = description;
        }

        public char getValue() {
            return value;
        }

        public static Transition fromValue(char c) {
            for (Transition t : values()) {
                if (t.value == c) {
                    return t;
                }
            }
            throw new IllegalArgumentException("Invalid transition value " + c);
        }

        @Override
        public String toString() {
            return description;
        }
    }

    private int delayFrames;
    private boolean loop;
    private Transition transition;
    private int[] frames;

    public DeviceAnimation() {
        setDefaultFrames();
    }

    private void setDefaultFrames() {
        setFrames(new int[]{0, UNUSED_FRAME_ID, UNUSED_FRAME_ID, UNUSED_FRAME_ID,
                UNUSED_FRAME_ID, UNUSED_FRAME_ID, UNUSED_FRAME_ID, UNUSED_FRAME_ID});
    }

    public void copyFrom(DeviceAnimation anim) {
        delayFrames = anim.delayFrames;
        loop = anim.loop;
        transition = anim.transition;
        System.arraycopy(anim.frames, 0, frames, 0, MAX_FRAMES);
    }

    public int getDelayFrames() {
        return delayFrames;
    }

    public void setDelayFrames(int delayFrames) {
        this.delayFrames = delayFrames;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    public int[] getFrames() {
        return frames;
    }

    public int getFramesCount() {
        int cnt = 0;
        for (int f : frames) {
            if (f == UNUSED_FRAME_ID) break;
            cnt++;
        }
        return cnt;
    }

    public int[] getFilteredFrames() {
        int[] result = new int[MAX_FRAMES];
        for (int i = 0; i < MAX_FRAMES; i++) {
            result[i] = frames[i] == UNUSED_FRAME_ID ? 0 : frames[i];
        }
        return result;
    }

    public void setFilteredFrames(int[] f, int cnt) {
        if (f.length != MAX_FRAMES) throw new IllegalArgumentException("Invalid element count");
        if (cnt < 1 || cnt > MAX_FRAMES) throw new IllegalArgumentException("Invalid size");

        // special case - when read from uninitialized eeprom, frames will be max length and all values will be 255
        // detect this and correct it
        if (cnt == 8) {
            boolean all255 = true;
            for (int x : f) {
                if (x != 255) {
                    all255 = false;
                    break;
                }
            }
            if (all255) {
                setDefaultFrames();
                return;
            }
        }

        for (int i = 0; i < MAX_FRAMES; i++) {
            frames[i] = i < cnt ? f[i] : UNUSED_FRAME_ID;
        }
    }

    public void setFrames(int[] frames) {
        if (frames.length != MAX_FRAMES) throw new IllegalArgumentException("Invalid anim frames len");
        if (frames[0] == UNUSED_FRAME_ID) throw new IllegalArgumentException("Must have at least one frame");

        this.frames = frames;
    }

    public void setFrameAt(int id, int frame) {
        if (id == 0 && frame == UNUSED_FRAME_ID) throw new IllegalArgumentException("Must have at least one frame");

        if (id >= 0 && id < frames.length) {
            this.frames[id] = frame;
        }
    }

    public int getFrameAt(int id) {
        if (id >= 0 && id < frames.length)
            return frames[id];
        else
            return UNUSED_FRAME_ID;
    }
}
