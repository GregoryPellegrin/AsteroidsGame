/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Client;

import Entity.Entity;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client implements Runnable
{
	public static final String HOST_NAME = "127.0.0.1";
	public static final int BYTE_SIZE = 5000;
	public static final int PORT = 2345;
	
	private List <Entity> entities;
	private Object entity;
	
	public Client (Entity entity)
	{
		this.entities = new ArrayList <> ();
		this.entity = entity;
	}
	
	public List <Entity> update (Entity entity)
	{
		this.entity = entity;
		
		return this.entities;
	}
	
	@Override
	public void run ()
	{
		while (true)
		{
			try
			{
				DatagramSocket client = new DatagramSocket ();
				InetAddress adresse = InetAddress.getByName(Client.HOST_NAME);
				ByteArrayOutputStream objectByteSendToServeur = new ByteArrayOutputStream (Client.BYTE_SIZE);
				ObjectOutputStream objectStreamSendToServeur = new ObjectOutputStream (new BufferedOutputStream (objectByteSendToServeur));
				
				objectStreamSendToServeur.writeObject(this.entity);
				objectStreamSendToServeur.flush();
				
				byte [] bufferSendToServeur = objectByteSendToServeur.toByteArray();
				DatagramPacket paquetSendToServeur = new DatagramPacket
				(
					bufferSendToServeur,
					bufferSendToServeur.length,
					adresse,
					Client.PORT
				);
				
				paquetSendToServeur.setData(bufferSendToServeur);
				client.send(paquetSendToServeur);
				
				objectStreamSendToServeur.close();
				
				byte [] bufferGetFromServeur = new byte [Client.BYTE_SIZE];
				DatagramPacket paquetGetFromServeur = new DatagramPacket
				(
					bufferGetFromServeur,
					bufferGetFromServeur.length
				);
				
				client.receive(paquetGetFromServeur);
				
				ByteArrayInputStream objectByteGetFromServeur = new ByteArrayInputStream (bufferGetFromServeur);
				ObjectInputStream objectStreamGetFromServeur = new ObjectInputStream (new BufferedInputStream (objectByteGetFromServeur));
				
				this.entities = (List <Entity>) objectStreamGetFromServeur.readObject();
				
				objectStreamGetFromServeur.close();
				paquetGetFromServeur.setLength(bufferGetFromServeur.length);
				
				//println("[CLIENT] Nombres d'Entity recu " + entities.size());
				
				/*try
				{
					Thread.sleep(this.sleep);
				}
				catch (InterruptedException e)
				{
					System.out.println("[CLIENT] InterruptedException : " + e.getMessage());
					System.out.println(Arrays.toString(e.getStackTrace()));
				}*/
			} 
			catch (SocketException e)
			{
				System.out.println("[CLIENT] SocketException : " + e.getMessage());
				System.out.println(Arrays.toString(e.getStackTrace()));
			}
			catch (UnknownHostException e)
			{
				System.out.println("[CLIENT] UnknownHostException : " + e.getMessage());
				System.out.println(Arrays.toString(e.getStackTrace()));
			}
			catch (ClassNotFoundException e)
			{
				System.out.println("[CLIENT] ClassNotFoundException : " + e.getMessage());
				System.out.println(Arrays.toString(e.getStackTrace()));
			}
			catch (NotSerializableException e)
			{
				System.out.println("[CLIENT] NotSerializableException : " + e.getMessage());
				System.out.println(Arrays.toString(e.getStackTrace()));
			}
			catch (IOException e)
			{
				System.out.println("[CLIENT] IOException : " + e.getMessage());
				System.out.println(Arrays.toString(e.getStackTrace()));
			}
		}
	}
	
	public static synchronized void println (String chaine)
	{
		System.out.println(chaine);
	}
}