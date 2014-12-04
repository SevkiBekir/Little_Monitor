import java.io.*;



public class denemeB   {

	
	public static String OSname() {
	    return System.getProperty("os.name").toLowerCase();
	}
	
	public static boolean isWindows() {
		 
		return (OSname().indexOf("win") >= 0);
 
	}
 
	public static boolean isMac() {
 
		return (OSname().indexOf("mac") >= 0);
 
	}
 
	public static boolean isUnix() {
 
		return (OSname().indexOf("nix") >= 0 || OSname().indexOf("nux") >= 0 || OSname().indexOf("aix") > 0 );
 
	}
	
	public static int fProcess(String osName) throws IOException 
	{
		String command="";
		Runtime runtime = Runtime.getRuntime();
		Process p;
		if(isWindows())
		{
			//command="tasklist /svc | findstr \"svchost.exe\"";
			command="\\system32\\"+"tasklist.exe";
			p = runtime.exec(System.getenv("windir")+command);
		}
		else if(isMac()==true || isUnix()==true)
		{
			  command="ps -e";
			  p = runtime.exec(command);
		}
		else
		{
			return 0;
		}
	   
	   String line;
      
       BufferedReader input=new BufferedReader(new InputStreamReader(p.getInputStream()));
       int count=0;
       while ((line = input.readLine()) != null) 
       {
           //System.out.println(line);
    	   count++;
       }
			
		return count;
		
	}
	static int toGB(long x)
	{
		x=x/(1024*1024*1024);
		return (int) x;
	}
    public static void main(String []args) throws IOException {
    	
       
       
       try {
    	   Runtime runtime = Runtime.getRuntime();
           double maxMemory = runtime.maxMemory();
           //double allocatedMemory = runtime.totalMemory();
           double freeMemory = runtime.freeMemory();
           
           String fileSystemRoot="";
           int totalSpace=0, freeSpace=0, usingSpace=0;
           
           File[] roots = File.listRoots();
           int say=roots.length;
           int i=0;
           int[][] fileArr=new int[say][3];
           String[] fileString=new String[say];
           for (File root : roots) 
           {
        	   fileString[i]=fileSystemRoot=root.getAbsolutePath();
        	   fileArr[i][0]=toGB(root.getTotalSpace()); //totalSpace
               fileArr[i][1]=toGB(root.getFreeSpace()); //freeSpace
               fileArr[i][2]=toGB(root.getTotalSpace()-root.getUsableSpace()); //usingSpace
               i++;
             
           }

          
           
	       PrintWriter file = new PrintWriter("Result_"+System.getProperty("os.name")+".xml", "UTF-8");
	       file.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	       file.println("<memory>");
	       file.println("\t <maxMemory>");
	       file.printf("\t %f\n",maxMemory);
	       file.println("\t </maxMemory>");
	       file.println("\t <freeMemory>");
	       file.printf("\t %f\n",freeMemory);
	       file.println("\t </freeMemory>");
	       file.println("</memory>");
	       file.println("<runningProcess>");
	       file.printf("%d\n",fProcess(OSname()));
	       file.println("</runningProcess>");
	       file.println("<disc>");
	       
	       for(i=0;i<say;i++)
	       { 
	    	   file.println("\t<fileSystemRoot>");
		       file.printf("\t  %s\n",fileString[i]);
		       file.println("\t</fileSystemRoot>");
		       file.println("\t\t <totalSpace>");
		       file.printf("\t\t %d GB\n",fileArr[i][0]);
		       file.println("\t\t </totalSpace>");
		       file.println("\t\t <usingSpace>");
		       file.printf("\t\t %d GB\n",fileArr[i][2]);
		       file.println("\t\t </usingSpace>");
		       file.println("\t\t <freeSpace>");
		       file.printf("\t\t%d GB\n",fileArr[i][1]);
		       file.println("\t\t</freeSpace>");   
	       }
	       
	       
	       file.println("</disc>");
	       file.close();
       }
       catch(Exception err) 
       {
    	      err.printStackTrace();
       }
       
       
       
       
       
       
    }
}
