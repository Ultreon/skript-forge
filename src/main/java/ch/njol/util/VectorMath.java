/**
 *   This file is part of Skript.
 * <p>
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * <p>
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * <p>
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.util;

import org.bukkit.util.Vec3;

/**
 * @author bi0qaw
 */
public class VectorMath {

	public static final double PI = Math.PI;
	public static final double HALF_PI = PI / 2;
	public static final double DEG_TO_RAD = PI / 180;
	public static final double RAD_TO_DEG =  180 / PI;

	public static Vec3 fromSphericalCoordinates(double radius, double theta, double phi) {
		double r = Math.abs(radius);
		double t = theta * DEG_TO_RAD;
		double p = phi * DEG_TO_RAD;
		double sinp = Math.sin(p);
		double x = r * sinp * Math.cos(t);
		double y = r * Math.cos(p);
		double z = r * sinp * Math.sin(t);
		return new Vec3(x, y, z);
	}

	public static Vec3 fromCylindricalCoordinates(double radius, double phi, double height) {
		double r = Math.abs(radius);
		double p = phi * DEG_TO_RAD;
		double x = r * Math.cos(p);
		double z = r * Math.sin(p);
		return new Vec3(x, height, z);

	}

	public static Vec3 fromYawAndPitch(float yaw, float pitch) {
		double y = Math.sin(pitch * DEG_TO_RAD);
		double div = Math.cos(pitch * DEG_TO_RAD);
		double x = Math.cos(yaw * DEG_TO_RAD);
		double z = Math.sin(yaw * DEG_TO_RAD);
		x *= div;
		z *= div;
		return new Vec3(x,y,z);
	}

	public static float getYaw(Vec3 vector) {
		if (((Double) vector.getX()).equals((double) 0) && ((Double) vector.getZ()).equals((double) 0)){
			return 0;
		}
		return (float) (Math.atan2(vector.getZ(), vector.getX()) * RAD_TO_DEG);
	}

	public static float getPitch(Vec3 vector) {
		double xy = Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
		return (float) (Math.atan(vector.getY() / xy) * RAD_TO_DEG);
	}

	public static Vec3 setYaw(Vec3 vector, float yaw) {
		vector = fromYawAndPitch(yaw, getPitch(vector));
		return vector;
	}

	public static Vec3 setPitch(Vec3 vector, float pitch) {
		vector = fromYawAndPitch(getYaw(vector), pitch);
		return vector;
	}

	public static Vec3 rotX(Vec3 vector, double angle) {
		double sin = Math.sin(angle * DEG_TO_RAD);
		double cos = Math.cos(angle * DEG_TO_RAD);
		Vec3 vy = new Vec3(0, cos, -sin);
		Vec3 vz = new Vec3(0, sin, cos);
		Vec3 clone = vector.clone();
		vector.setY(clone.dot(vy));
		vector.setZ(clone.dot(vz));
		return vector;
	}

	public static Vec3 rotY(Vec3 vector, double angle) {
		double sin = Math.sin(angle * DEG_TO_RAD);
		double cos = Math.cos(angle * DEG_TO_RAD);
		Vec3 vx = new Vec3(cos, 0, sin);
		Vec3 vz = new Vec3(-sin, 0, cos);
		Vec3 clone = vector.clone();
		vector.setX(clone.dot(vx));
		vector.setZ(clone.dot(vz));
		return vector;
	}

	public static Vec3 rotZ(Vec3 vector, double angle) {
		double sin = Math.sin(angle * DEG_TO_RAD);
		double cos = Math.cos(angle * DEG_TO_RAD);
		Vec3 vx = new Vec3(cos, -sin, 0);
		Vec3 vy = new Vec3(sin, cos, 0);
		Vec3 clone = vector.clone();
		vector.setX(clone.dot(vx));
		vector.setY(clone.dot(vy));
		return vector;
	}

	public static Vec3 rot(Vec3 vector, Vec3 axis, double angle) {
		double sin = Math.sin(angle * DEG_TO_RAD);
		double cos = Math.cos(angle * DEG_TO_RAD);
		Vec3 a = axis.clone().normalize();
		double ax = a.getX();
		double ay = a.getY();
		double az = a.getZ();
		Vec3 rotx = new Vec3(cos+ax*ax*(1-cos), ax*ay*(1-cos)-az*sin, ax*az*(1-cos)+ay*sin);
		Vec3 roty = new Vec3(ay*ax*(1-cos)+az*sin, cos+ay*ay*(1-cos), ay*az*(1-cos)-ax*sin);
		Vec3 rotz = new Vec3(az*ax*(1-cos)-ay*sin, az*ay*(1-cos)+ax*sin, cos+az*az*(1-cos));
		double x = rotx.dot(vector);
		double y = roty.dot(vector);
		double z = rotz.dot(vector);
		vector.setX(x).setY(y).setZ(z);
		return vector;
	}

	public static float notchYaw(float yaw){
		float y = yaw - 90;
		if (y < -180){
			y += 360;
		}
		return y;
	}

	public static float notchPitch(float pitch){
		return -pitch;
	}

	public static float fromNotchYaw(float notchYaw){
		float y = notchYaw + 90;
		if (y > 180){
			y -= 360;
		}
		return y;
	}

	public static float fromNotchPitch(float notchPitch){
		return -notchPitch;
	}

	public static float skriptYaw(float yaw){
		float y = yaw - 90;
		if (y < 0){
			y += 360;
		}
		return y;
	}

	public static float skriptPitch(float pitch){
		return -pitch;
	}

	public static float fromSkriptYaw(float yaw){
		float y = yaw + 90;
		if (y > 360){
			y -= 360;
		}
		return y;
	}

	public static float fromSkriptPitch(float pitch){
		return -pitch;
	}

	public static float wrapAngleDeg(float angle) {
		angle %= 360f;
		if (angle <= -180) {
			return angle + 360;
		} else if (angle > 180) {
			return angle - 360;
		} else {
			return angle;
		}
	}

	/**
	 * Copies vector components of {@code vector2} into {@code vector1}.
	 */
	public static void copyVector(Vec3 vector1, Vec3 vector2) {
		vector1.setX(vector2.getX()).setY(vector2.getY()).setZ(vector2.getZ());
	}

}
