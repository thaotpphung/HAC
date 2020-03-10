package cs;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class CSDriver
{
	/**
	 * a testing driver class
	 * it has nothing to do with sending data to / receiving data from other hosts yet
	 * the sole purpose of this driver class to have each host an updated list of the participating hosts
	 * so that this list can be used when the host is dead and comes alive again later
	 * upon running this code, be sure you change the hard-coded directory to the one where
	 * your test.txt file is located
	 */
	public static void main(String[] args) throws Exception
	{
		File file = new File("C:\\Users\\josua\\Documents\\Truman\\Spring 2020\\CS470 - Computer Networks"
				+ "\\Projects\\Project 1\\bin\\cs\\test.txt");
		
		Scanner s = new Scanner(file);
		String inputIP;
		ArrayList<PeerInfo> peerList = new ArrayList<PeerInfo>();
		
		while (s.hasNextLine())
		{		
			inputIP = s.nextLine();
			s.nextLine();
			
			peerList.add(new PeerInfo(inputIP, System.currentTimeMillis()));
		}
		
		System.out.println("completed reading");
		
		FileWriter writer = new FileWriter("C:\\Users\\josua\\Documents\\Truman\\Spring 2020\\CS470 - Computer Networks"
				+ "\\Projects\\Project 1\\bin\\cs\\test.txt");
		BufferedWriter buffer = new BufferedWriter(writer);
		
		for (int index = 0; index < peerList.size(); index++)
		{
			buffer.write(peerList.get(index).getIPAddress());
			buffer.newLine();
			if (peerList.get(index).getStatus())
			{
				buffer.write("true");
			}
			else
			{
				buffer.write("false");
			}
			buffer.newLine();
		}
		
		System.out.println("completed updating");
		
		buffer.close();
		
		s.close();
	}
}
