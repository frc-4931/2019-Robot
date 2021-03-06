package frc.team4931.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4931.robot.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HatchGrabber extends Subsystem {

    private DoubleSolenoid pneumaticDispenser;
    private DoubleSolenoid pneumaticVelcro;
    private DoubleSolenoid pneumaticPivot;

    public HatchGrabber() {

        pneumaticDispenser = new DoubleSolenoid(RobotMap.COMPRESSOR, 
                RobotMap.DISPENSER_EXTEND, RobotMap.DISPENSER_RETRACT);
        pneumaticDispenser.set(Value.kReverse);

        pneumaticVelcro = new DoubleSolenoid(RobotMap.COMPRESSOR, 
                RobotMap.VELCRO_EXTEND, RobotMap.VELCRO_RETRACT);
        pneumaticVelcro.set(Value.kReverse);

        pneumaticPivot = new DoubleSolenoid(RobotMap.COMPRESSOR,
                RobotMap.PIVOT_PISTON_EXTEND,
                RobotMap.PIVOT_PISTON_RETRACT);
        pneumaticPivot.set(Value.kReverse);

    }

    protected void initDefaultCommand() {
    }

    public void extendHatchGrabber() {
        pneumaticDispenser.set(Value.kForward);
    }

    public void resetHatchGrabber() {
        pneumaticDispenser.set(Value.kReverse);
    }

    public void extendVelcro() {
        pneumaticVelcro.set(Value.kForward);
    }

    public void retractVelcro() {
        pneumaticVelcro.set(Value.kReverse);
    }

    public void changeVelcroState() {
        if(pneumaticVelcro.get() == Value.kReverse){
            extendVelcro();
        } else {
            retractVelcro();
        }
    }

    public void pivotDown() {
        pneumaticPivot.set(Value.kForward);
    }

    public void pivotUp() {
        pneumaticPivot.set(Value.kReverse);
    }

    public void pressureTest() {
        pneumaticDispenser.set(Value.kOff);
        pneumaticPivot.set(Value.kOff);
        pneumaticVelcro.set(Value.kOff);
    }

    public void log() {
        SmartDashboard.putBoolean("Velcro Extended", 
                pneumaticVelcro.get() == Value.kForward ? true : false);
        SmartDashboard.putBoolean("Pivot Exdended", 
                pneumaticPivot.get() == Value.kForward ? true : false);
    }

}