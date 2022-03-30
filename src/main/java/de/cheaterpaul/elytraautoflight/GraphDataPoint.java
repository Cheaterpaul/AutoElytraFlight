package de.cheaterpaul.elytraautoflight;
import net.minecraft.util.math.vector.Vector3d;

public class GraphDataPoint {
    public Vector3d realPosition;
    public double horizontalDelta;
    public double velocity;
    public boolean pullUp;
    public boolean pullDown;

    public GraphDataPoint(Vector3d realPosition) {
        this.realPosition = realPosition;
    }

    public GraphDataPoint(Vector3d realPosition, Vector3d previousPosition) {
        this.realPosition = realPosition;

        Vector3d delta = new Vector3d(realPosition.x - previousPosition.x, 0, realPosition.z - previousPosition.z);
        this.horizontalDelta = delta.length();

    }
}
