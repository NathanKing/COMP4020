import java.awt.*;

class States
{
	static int main(String args[])
	{

	
		return 0;
	}

	/* WARNING: There's likely a horrible bug when switching between resize and swipe states
		Basically, there will be a huge jump if the user removes the initial finger before
		the secondary finger.
	*/	
	
	enum FingerStates
	{
		IDLE,			// No user input
		FINGERDOWN,		// Has a finger been detected
		SWIPE,			// User is moving
		RESIZE,			// User is resizing / moving
		HOVER,			// User is selecting an object
		SELECT
	}
	
	public static final int SWIPE_DISTANCE =   10;	// How many pixels do we have to move to initiate a move
	public static final int HOVER_TIMEOUT  =  300;	// How many milliseconds before detecting hover
	public static final int SELECT_TIMEOUT = 1000;	// How long without moving before item should be selected
	public static final int SELECT_MOVE    =   10;  // How many pixels does it take to reset the select timeout
	
	// Used by other functions
	static Point screen_offset;
	static double zoom;
	
	// Used only in inputMachine
	static FingerStates fs; 
	static double oldDistance;
	static long oldTimestamp;
	static long stateStart;		// How long have we been in the current mode
	static Point old;			// Old position
	static FingerStates inputMachine(Point one, Point two)
	{
		double distance;
		long timestamp = System.currentTimeMillis();
		
		// If at any time the user removes both fingers, jump back to waiting
		if (one == null)
			fs = FingerStates.IDLE;
		
		switch (fs)
		{
			case IDLE:
				// First finger down
				if (one != null)
				{
					stateStart = System.currentTimeMillis();
					fs = FingerStates.FINGERDOWN;
				}
				
				break;
			
			case FINGERDOWN:
				distance = one.distance(old);
			
				// User moved their finger, so they want to swipe the screen
				if (distance > SWIPE_DISTANCE)
				{
					// Move to swipe mode
					fs = FingerStates.SWIPE;
					
					// Track how far the finger moved
					Point change = new Point(old.x - one.x, old.y - one.y);	// TODO: Test me!
					screen_offset.translate(change.x, change.y);
					break;
				}
				
				// Found a second finger, so they want to resize
				if (two != null)
				{
					fs = FingerStates.RESIZE;
					oldDistance = 0;
					break;
				}
					
				// Finger was left on the screen without moving, so the user wants
				// to hover/select something
				if (System.currentTimeMillis() > stateStart + HOVER_TIMEOUT)
				{
					stateStart = System.currentTimeMillis();
					fs = FingerStates.HOVER;
				}
				
				break;
				
			// Two fingers, expecting resizing but can still move the screen
			case RESIZE:
				// Revert back to swipe if we lost the second finger
				if (two == null)
					fs = FingerStates.SWIPE;
				
				// We have a decent sample
				if (oldDistance != 0)
				{
					// Offset our zoom by the distance between the two fingers
					zoom += oldDistance - one.distance(two);					
				}
				
				oldDistance = one.distance(two);
				
			// One finger. Just moving
			case SWIPE:
				// Upgrade to resize if a second finger was found
				if (two != null)
				{
					fs = FingerStates.RESIZE;
					oldDistance = 0;
				}
			
				// Track how far the finger moved and apply it to our canvas offset
				screen_offset.translate(old.x - one.x, old.y - one.y); // TODO: Test me!
			
				break;
				
			// One finger, used to explode targets
			case HOVER:
				// If user moved, consider the hover state reset
				if (old.distance(one) > SELECT_MOVE)
					stateStart = System.currentTimeMillis();
				
				// If the user hovers without moving, consider something selected
				if (System.currentTimeMillis() > stateStart + SELECT_MOVE)
					fs = FingerStates.SELECT;
				
				break;
				
			case SELECT:
				// We have nothing to do in here but wait for fingers to be removed
				break;
		}
		
		
		oldTimestamp = timestamp;
		old = one;
		return fs;
	}
}
