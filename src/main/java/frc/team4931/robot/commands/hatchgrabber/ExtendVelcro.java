package frc.team4931.robot.commands.hatchgrabber;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4931.robot.subsystems.*;
import frc.team4931.robot.Robot;

public class ExtendVelcro extends Command {
    
    public ExtendVelcro() {
        requires(Robot.getHatchGrabber());
    }

    @Override
    protected void initialize() {
        Robot.getHatchGrabber().extendVelcro();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}