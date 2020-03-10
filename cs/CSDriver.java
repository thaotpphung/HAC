package cs;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class CSDriver
{
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
