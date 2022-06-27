package de.cheaterpaul.autoelytraflight;

import net.minecraft.world.phys.Vec3;

public class GraphDataPoint {
    public Vec3 realPosition;
    public double horizontalDelta;
    public double velocity;
    public boolean pullUp;
    public boolean pullDown;

    public GraphDataPoint(Vec3 realPosition) {
        this.realPosition = realPosition;
    }

    public GraphDataPoint(Vec3 realPosition, Vec3 previousPosition) {
        this.realPosition = realPosition;

        Vec3 delta = new Vec3(realPosition.x - previousPosition.x, 0, realPosition.z - previousPosition.z);
        this.horizontalDelta = delta.length();

    }
}
