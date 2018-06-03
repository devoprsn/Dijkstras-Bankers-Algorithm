import java.util.ArrayList;
import java.util.Random;

public class Program
{

	final static int NUM_PROCS = 6; // How many concurrent processes
	final static int TOTAL_RESOURCES = 30; // Total resources in the system
	final static int MAX_PROC_RESOURCES = 13; // Highest amount of resources any process could need
	final static int ITERATIONS = 30; // How long to run the program
	static Random rand = new Random();
	
	public static void main(String[] args)
	{
		
		// The list of processes:
		ArrayList<Proc> processes = new ArrayList<Proc>();
		for (int i = 0; i < NUM_PROCS; i++)
			processes.add(new Proc(MAX_PROC_RESOURCES - rand.nextInt(3))); // Initialize to a new Proc, with some small range for its max
		
		int totalHeldResources = 0;
		// Run the simulation:
		for (int i = 0; i < ITERATIONS; i++)
		{
			// loop through the processes and for each one get its request
			for (int j = 0; j < processes.size(); j++)
			{
				// Get the request
				int currRequest = processes.get(j).resourceRequest(TOTAL_RESOURCES - totalHeldResources);				
				
				// just ignore processes that don't ask for resources
				if (currRequest == 0) {
					System.out.println("Process " + j + " requested " + currRequest);
					continue;
				}
								
				// Here you have to enter code to determine whether or not the request can be granted,
				// and then grant the request if possible. Remember to give output to the console 
				// this indicates what the request is, and whether or not its granted.
				if(currRequest < 0)
				{
					totalHeldResources -= processes.get(j).getMaxResources();
					System.out.println("Process " + j + " finished, returned " + processes.get(j).getMaxResources());
				}
				else if((currRequest + processes.get(j).getHeldResources()) == processes.get(j).getMaxResources())					
				{
					//if the request # of process and # of held processes == max processes, check if the max is
					//greater than the claimed # of resources of at least one other process
					boolean grant = false;
					for(int x = 0; x < processes.size(); x++)
					{
						if(x == j) continue;
						if(processes.get(j).getMaxResources() > (processes.get(x).getMaxResources() - 
								processes.get(x).getHeldResources())) 
						{
							grant = true;
							break;
						}
					}
					
					if(grant) {
						processes.get(j).addResources(currRequest);
						totalHeldResources += currRequest;
						System.out.println("Process " + j + " requested " + currRequest + ", granted");
					}
					else {
						System.out.println("Process " + j + " requested " + currRequest + ", denied");
					}
				}
				else {
					// check if available resources after granting the request is  greater than or equal to the number
					// of claimed resources of at least one process
					boolean grant = false;
					for(int y = 0; y < processes.size(); y++)
					{
						if(((TOTAL_RESOURCES - totalHeldResources) - currRequest) >= (processes.get(y).getMaxResources() 
								- processes.get(y).getHeldResources()))
						{
							//check if the max of the process is greater than the claimed # of resources of at least 
							//one other process
							for(int x = 0; x < processes.size(); x++)
							{								
								if(x == j) continue;
								if(processes.get(j).getMaxResources() > (processes.get(x).getMaxResources() - 
										processes.get(x).getHeldResources())) 
								{
									grant = true;
									break;
								}
							}
						}
					}
					
					if(grant) {
						processes.get(j).addResources(currRequest);
						totalHeldResources += currRequest;
						System.out.println("Process " + j + " requested " + currRequest + ", granted");
					}
					else {
						System.out.println("Process " + j + " requested " + currRequest + ", denied");
					}
				}
				

				// At the end of each iteration, give a summary of the current status:
				System.out.println("\n***** STATUS *****");
				System.out.println("Total Available: " + (TOTAL_RESOURCES - totalHeldResources));
				for (int k = 0; k < processes.size(); k++)
					System.out.println("Process " + k + " holds: " + processes.get(k).getHeldResources() + ", max: " +
							processes.get(k).getMaxResources() + ", claim: " + 
							(processes.get(k).getMaxResources() - processes.get(k).getHeldResources()));
				System.out.println("***** STATUS *****\n");
				
			}
		}

	}

}
