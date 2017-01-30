package thecave.matrixcontrol.device;


import thecave.matrixcontrol.device.commands.*;
import thecave.matrixcontrol.sbridge.SerialCommand;
import thecave.matrixcontrol.sbridge.SerialThread;


public class Device {
    private final SerialThread thread;
    private DeviceListener listener;


    private String name;
    private String version;
    private int brightness;
    private int maxImages;
    private int maxAnimations;
    private int maxTextLen;
    private DeviceMode mode;
    private int itemIndex = -1;

    private String text;
    private int textSpeed;
    private DeviceImage[] images;
    private DeviceAnimation[] animations;


    public Device(SerialThread xthread, final DeviceListener xlistener) {
        this.thread = xthread;
        this.listener = xlistener;
        CommandResolverManager cr = new CommandResolverManager();
        thread.setListener(cr);

        cr.add(InfoCommand.class, new CommandResolver<InfoCommand>() {
            @Override
            public void resolve(InfoCommand cmd) {
                checkErrors(cmd);
                name = cmd.getName();
                version = cmd.getVersion();
                if (listener != null) listener.loadFinished(Device.this);
            }
        });
        cr.add(LimitsCommand.class, new CommandResolver<LimitsCommand>() {
            @Override
            public void resolve(LimitsCommand cmd) {
                checkErrors(cmd);
                maxImages = cmd.getMaxImages();
                maxAnimations = cmd.getMaxAnimations();
                maxTextLen = cmd.getMaxTextLength();

                images = new DeviceImage[maxImages];
                animations = new DeviceAnimation[maxAnimations];

                loadAll();
            }
        });
        cr.add(GetModeCommand.class, new CommandResolver<GetModeCommand>() {
            @Override
            public void resolve(GetModeCommand cmd) {
                checkErrors(cmd);
                boolean change = false;
                if (mode != cmd.getDeviceMode()) {
                    change = true;
                    mode = cmd.getDeviceMode();
                }
                if (itemIndex != cmd.getItemIndex()) {
                    change = true;
                    itemIndex = cmd.getItemIndex();
                }
                if (change && listener != null) listener.paramsChanged(Device.this);
            }
        });
        cr.add(SetModeCommand.class, new CommandResolver<SetModeCommand>() {
            @Override
            public void resolve(SetModeCommand cmd) {
                checkErrors(cmd);
                boolean change = false;
                if (mode != cmd.getDeviceMode()) {
                    change = true;
                    mode = cmd.getDeviceMode();
                }
                if (itemIndex != cmd.getItemIndex()) {
                    change = true;
                    itemIndex = cmd.getItemIndex();
                }
                if (change && listener != null) listener.paramsChanged(Device.this);
            }
        });
        cr.add(BrightnessCommand.class, new CommandResolver<BrightnessCommand>() {
            @Override
            public void resolve(BrightnessCommand cmd) {
                checkErrors(cmd);
                brightness = cmd.getBrightness();
                if (listener != null) listener.paramsChanged(Device.this);
            }
        });
        cr.add(TextCommand.class, new CommandResolver<TextCommand>() {
            @Override
            public void resolve(TextCommand cmd) {
                checkErrors(cmd);
                text = cmd.getText();
                textSpeed = cmd.getSpeed();

                if (listener != null) listener.paramsChanged(Device.this);
            }
        });
        cr.add(ImageCommand.class, new CommandResolver<ImageCommand>() {
            @Override
            public void resolve(ImageCommand cmd) {
                checkErrors(cmd);
                if (images == null) return;

                images[cmd.getImageIndex()] = cmd.getImage();
                if (listener != null) listener.imageLoaded(Device.this, cmd.getImageIndex());
            }
        });
        cr.add(AnimationCommand.class, new CommandResolver<AnimationCommand>() {
            @Override
            public void resolve(AnimationCommand cmd) {
                checkErrors(cmd);
                if (animations == null) return;

                animations[cmd.getAnimIndex()] = cmd.getAnimation();
                if (listener != null) listener.animationLoaded(Device.this, cmd.getAnimIndex());
            }
        });
        cr.add(RevertCommand.class, new CommandResolver<RevertCommand>() {
            @Override
            public void resolve(RevertCommand cmd) {
                checkErrors(cmd);
                loadAll();
            }
        });
        cr.add(SaveCommand.class, new CommandResolver<SaveCommand>() {
            @Override
            public void resolve(SaveCommand cmd) {
                checkErrors(cmd);
                if (listener != null) listener.saveFinished(Device.this);
            }
        });
    }

    private void checkErrors(AbstractCommand cmd) {
        if (cmd.getStatus() == SerialCommand.Status.ERROR) {
            if (listener != null) listener.commandFailed(this, cmd.getResponse());
            throw new RuntimeException("Failed command");
        }
        if (cmd.getStatus() == SerialCommand.Status.TIMEOUT) {
            if (listener != null) listener.commandFailed(this, "Command timed out");
            throw new RuntimeException("Timeout");
        }
    }

    public void start() {
        thread.addCommand(new LimitsCommand());
    }

    public void stop() {
        thread.stopRunning();
    }

    private void loadAll() {
        thread.addCommand(new BrightnessCommand());
        thread.addCommand(new TextCommand());
        thread.addCommand(new GetModeCommand());
        for (int i = 0; i < maxImages; i++) {
            thread.addCommand(new ImageCommand(i));
        }
        for (int i = 0; i < maxAnimations; i++) {
            thread.addCommand(new AnimationCommand(i));
        }
        thread.addCommand(new InfoCommand());
    }

    public DeviceAnimation[] getAnimations() {
        return animations;
    }

    public DeviceImage[] getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public int getBrightness() {
        return brightness;
    }

    public DeviceMode getMode() {
        return mode;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public String getText() {
        return text;
    }

    public int getTextSpeed() {
        return textSpeed;
    }

    public void setBrightness(int bri) {
        thread.addCommand(new BrightnessCommand(bri));
    }

    public void setMode(DeviceMode mode) {
        thread.addCommand(new SetModeCommand(mode));
    }

    public void setMode(DeviceMode mode, int index) {
        thread.addCommand(new SetModeCommand(mode, index));
    }

    public void setText(String text, int speed) {
        if (text.length() > maxTextLen) throw new IllegalArgumentException("Text too long");
        thread.addCommand(new TextCommand(text, speed));
    }

    public void setImage(int imageId, DeviceImage image) {
        if (imageId < 0 || imageId >= maxImages) throw new IllegalArgumentException("Image id invalid");
        thread.addCommand(new ImageCommand(imageId, image));
    }

    public void setAnimation(int animId, DeviceAnimation animation) {
        if (animId < 0 || animId >= maxAnimations) throw new IllegalArgumentException("Anim id invalid");
        thread.addCommand(new AnimationCommand(animId, animation));
    }

    public int getMaxImages() {
        return maxImages;
    }

    public int getMaxAnimations() {
        return maxAnimations;
    }

    public int getMaxTextLen() {
        return maxTextLen;
    }

    public void save() {
        thread.addCommand(new SaveCommand());
    }

    public void revert() {
        thread.addCommand(new RevertCommand());
    }
}
