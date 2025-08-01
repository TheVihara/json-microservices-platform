package net.unnamed.service.gui.api.item;

import java.util.LinkedList;

public interface AnimatedItem extends Item {
    Item getCurrentFrame();

    LinkedList<Item> getFrames();

    default void nextFrame() {
        LinkedList<Item> frames = getFrames();
        if (frames.isEmpty()) {
            return;
        }

        if (getCurrentFrame() == null) {
            setCurrentFrame(frames.getFirst());
            return;
        }

        int currentIndex = frames.indexOf(getCurrentFrame());
        int nextIndex = (currentIndex + 1) % frames.size();
        setCurrentFrame(frames.get(nextIndex));
    }

    void setCurrentFrame(Item currentFrame);

    int getInterval();
}
