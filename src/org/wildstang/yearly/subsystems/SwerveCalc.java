package org.wildstang.yearly.subsystems;


public class SwerveCalc
{
   double leftX;
   double leftY;
   double rightX;
   double magnitude;
   double rotMag;
   double targetAngle;
   double encodeAngle;
   final double DEADBAND = 0.0;

   public static void main(String[] args)
   {
      // TODO Auto-generated method stub

      SwerveCalc calc = new SwerveCalc();

      calc.doTest();

   }

   public void doTest()
   {
      
      
      System.out.println("leftX,leftY,rightX,targetAngle,magnitude,rotMag");
      for (leftX = -1.0; leftX <= 1.0; leftX += 0.25)
      {
         for (leftY = -1.0; leftY <= 1.0; leftY += 0.25)
         {
            update();
            System.out.println(String.format("%1.2f", leftX) + ", " + String.format("%1.2f", leftY) +", " + String.format("%1.2f", getAngle(leftX, leftY) * (180/Math.PI)));
         }
      }

   }

   
   public void update()
   {
      if (leftX == 0 && leftY == 0)
      { // if no controller input
         magnitude = 0;
         rotMag = 0;
      }
      else
      {
         targetAngle = getAngle(leftX, leftY);
         rotMag = getRotMag(encodeAngle, targetAngle);
         double unscaledMagnitude = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2)); // here we get the raw magnitude from Pythagorean Theorem
         if (Math.abs(leftX) >= Math.abs(leftY))
         {
            // This little algorithm should scale it by dividing the current raw magnitude by the maximum raw magnitude.
            magnitude = unscaledMagnitude * Math.sin(targetAngle);
         }
         else
         {
            magnitude = unscaledMagnitude * Math.cos(targetAngle);
         }

      }

   }

   private double getRotMag(double actual, double desired)
   {
      double oppositeDesired = limitAngle(desired + 180); // get the opposite of
                                                          // the desired angle
      double rotateMag;
      double dummyDesired; // These dummy variables fix the cases where the
                           // desired angle is in between 270 and 359.9
      double dummyActual;
      if (desired >= 270 && (actual > 0 && actual < 90))
      {
         dummyDesired = desired - 90;
         dummyActual = actual - 90;
      }
      else
      {
         dummyDesired = desired;
         dummyActual = actual;
      }
      if (actual >= desired - DEADBAND && actual <= desired + DEADBAND)
      {
         rotateMag = 0;
      }
      else if ((Math.abs(actual - desired) < Math.abs(actual - oppositeDesired))
            && // if actual is closer to desired
            (limitAngle(dummyActual) > limitAngle(dummyDesired) && desired < 270.0))
      { // and the actual is to the left of desired
         rotateMag = -1.0;
      }
      else
      {
         rotateMag = 1.0;
      }
      if (!(Math.abs(actual - desired) > Math.abs(actual - oppositeDesired)))
      { // going to the opposite of desired angle (run motors in reverse)
         magnitude *= -1;
      }
      return rotateMag;
   }

   private static double getAngle(double x, double y) {
	   double angle = 0;
	   if (y > 0) {
		   angle = limitAngle(Math.atan(x/y));
	   } else if(y < 0){
		   angle = limitAngle(Math.PI + Math.atan(x/y));
	   } 
   	    else if( y == 0 && x >= 0){
   		   angle = (Math.PI/2);
   	   } else {
   		   angle = (Math.PI * 1.5);
   	   }

	   return angle;
   }


   private static double limitAngle(double oldAngle)
   {
      double newAngle = oldAngle;
      while (newAngle >= 360)
      {
         newAngle -= 360;
      }
      while (newAngle < 0)
      {
         newAngle += 360;
      }

      return newAngle;
   }

}