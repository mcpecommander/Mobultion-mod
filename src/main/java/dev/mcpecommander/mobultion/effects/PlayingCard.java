package dev.mcpecommander.mobultion.effects;

import java.awt.*;

/* McpeCommander created on 19/07/2021 inside the package - dev.mcpecommander.mobultion.effects */
public class PlayingCard {
    //This class is used to have a reference to each play card symbol being displayed.

    final int posX, posY;
    Color color;
    final float uv;

    public PlayingCard(int posX, int posY, Color color, float uv) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.uv = uv;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Color getColor() {
        return color;
    }

    public float getUv() {
        return uv;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
