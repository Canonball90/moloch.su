package net.spartanb312.base.utils;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static net.spartanb312.base.BaseCenter.mc;

public class RotationUtil {

    public static float[] getRotations(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0;
        double difZ = to.z - from.z;
        double dist = MathHelper.sqrt((difX * difX + difZ * difZ));
        return new float[]{normalizeAngle((float) Math.toDegrees(Math.atan2(difZ, difX)) - 90.0f), normalizeAngle((float) Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static float[] getRotationsBlock(BlockPos block, EnumFacing face, boolean Legit) {
        double x = block.getX() + 0.5 - mc.player.posX +  (double) face.getXOffset()/2;
        double z = block.getZ() + 0.5 - mc.player.posZ +  (double) face.getZOffset()/2;
        double y = (block.getY() + 0.5);

        if (Legit)
            y += 0.5;

        double d1 = mc.player.posY + mc.player.getEyeHeight() - y;
        double d3 = MathHelper.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);

        if (yaw < 0.0F) {
            yaw += 360f;
        }
        return new float[]{yaw, pitch};
    }

    public static float calcNormalizedAngleDiff(float angle1, float angle2) {
        float angleDiff = angle1 - angle2;
        if (Math.abs(angleDiff) <= 180) return angleDiff;
        return angleDiff + (angleDiff > 0 ? -360.0f : 360.0f);
    }

    public static float normalizeAngle(float angle) {
        angle %= 360.0f;
        angle += 360.0f * (angle >= 180.0f ? -1 :
                        angle < -180.0f ? 1 : 0);
        return angle;
    }
}
