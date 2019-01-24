package frc.team4931.robot.commands.lineup;

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4931.robot.Robot;
import frc.team4931.robot.commands.utilities.RotateToDegree;
import frc.team4931.robot.sensors.Pigeon;
import frc.team4931.robot.subsystems.Drivetrain;

public class LineupWithTarget extends Command {
  private static final String DISTANCE_KEY = "Vision Distance";
  private static final String OFFSET_KEY = "Vision Offset";
  private static final double ANGLE_CORRECTION = 0.5; // AKA Max speed
  private static final double OFFSET_CORRECTION = 0.5; // AKA speed per foot
  private static final double DISTANCE_CORRECTION = 0.3; // AKA speed per foot
  private static final double MAX_SPEED = 0.3;
  private Drivetrain drivetrain;
  private Pigeon pigeon;
  private boolean finished;
  private int targetAngle;
  private boolean useCurrentAngle = false;

  public LineupWithTarget() {
    drivetrain = Robot.getDrivetrain();
    useCurrentAngle = true;
    requires(drivetrain);
    setInterruptible(true);
  }

  public LineupWithTarget(Angles angle) {
    drivetrain = Robot.getDrivetrain();
    targetAngle = angle.getAngle();
    requires(drivetrain);
    setInterruptible(true);
  }

  private double range(double value) {
    return Math.min(Math.max(value, -1), 1);
  }

  private int getClosestAngle(double angle) {
    var values = Angles.values();
    var low = -180;
    var high = 180;

    for(var i : values) {
      var ang = i.getAngle();

      if (ang <= angle && ang >= low)
        low = ang;

      else if (ang >= angle && ang <= high)
        high = ang;
    }

    var diff_low = Math.abs(angle - low);
    var diff_high = Math.abs(high - angle);

    return (diff_high < diff_low) ? high : low;
  }

  @Override
  protected void initialize() {
    pigeon = Robot.getPigeon();
    finished = false;

    if (useCurrentAngle)
      targetAngle = getClosestAngle(pigeon.getAngle());
  }

  @Override
  protected void execute() {
    // Distance and Offset are in feet
    var curDistance = SmartDashboard.getNumber(DISTANCE_KEY, -1);
    var curOffset = SmartDashboard.getNumber(OFFSET_KEY, -1);
    var curAngle = pigeon.getAngle();

    // Calculate how far from the nearest preset angle in degrees
    var deltaTarget = curAngle - targetAngle; //Positive if robot is in a clockwise rotation

    // Calculate correction values
    var angleCorrection = -deltaTarget / 30 * ANGLE_CORRECTION;
    var offsetCorrection = Math.pow(Math.max(1 - Math.abs(angleCorrection), 0), 2) * curOffset * OFFSET_CORRECTION;
    var distanceCorrection = Math.pow(Math.max(1 - Math.abs(offsetCorrection), 0), 2) * curDistance * DISTANCE_CORRECTION;

    if (Math.abs(deltaTarget) < 3 && Math.abs(curOffset) < 0.15 && Math.abs(curDistance) < 0.15)
      finished = true;

    angleCorrection = range(angleCorrection) * MAX_SPEED;
    offsetCorrection = range(offsetCorrection) * MAX_SPEED;
    distanceCorrection = range(distanceCorrection) * MAX_SPEED;

    drivetrain.driveCartesian(distanceCorrection, offsetCorrection, angleCorrection);
  }

  @Override
  protected void end() {
    drivetrain.stop();
  }

  @Override
  protected boolean isFinished() {
    return finished;
  }
}