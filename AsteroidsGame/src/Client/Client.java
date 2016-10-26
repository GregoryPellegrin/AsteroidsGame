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

public class Client implements Runnable
{
	public static final String SERVEUR_NAME = "127.0.0.1";
	public static final int SERVEUR_PORT = 2345;
	
	public String CLIENT_ADRESSE;
	
	private static final int BYTE_SIZE = 5000;
	
	private ArrayList <Entity> entities;
	private Object entity;
	
	private boolean entityIsLocked;
	private boolean entitiesIsLocked;
	
	public Client (Entity entity)
	{
		this.entities = new ArrayList <> ();
		this.entity = entity;
		
		this.entityIsLocked = false;
		this.entitiesIsLocked = false;
		
		try
		{
			InetAddress IP = InetAddress.getLocalHost();
			this.CLIENT_ADRESSE = IP.getHostAddress();
		}
		catch (UnknownHostException e)
		{
			System.out.println("[CLIENT] UnknownHostException : " + e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
	}
	
	public ArrayList <Entity> getEntities ()
	{
		this.entitiesIsLocked = true;
		
		return this.entities;
	}
	
	public void addEntitiesTerminated ()
	{
		this.entitiesIsLocked = true;
		
		this.entities.clear();
		
		this.entitiesIsLocked = false;
	}
	
	public void update (Entity entity)
	{
		this.entityIsLocked = true;
		
		this.entity = entity;
		
		this.entityIsLocked = false;
	}
	
	@Override
	public void run ()
	{
		while (true)
		{
			try
			{
				DatagramSocket client = new DatagramSocket ();
				InetAddress adresse = InetAddress.getByName(Client.SERVEUR_NAME);
				ByteArrayOutputStream objectByteSendToServeur = new ByteArrayOutputStream (Client.BYTE_SIZE);
				ObjectOutputStream objectStreamSendToServeur = new ObjectOutputStream (new BufferedOutputStream (objectByteSendToServeur));
				
				if (! this.entityIsLocked)
				{
					objectStreamSendToServeur.writeObject(this.entity);
					objectStreamSendToServeur.flush();

					byte [] bufferSendToServeur = objectByteSendToServeur.toByteArray();
					DatagramPacket paquetSendToServeur = new DatagramPacket
					(
						bufferSendToServeur,
						bufferSendToServeur.length,
						adresse,
						Client.SERVEUR_PORT
					);

					paquetSendToServeur.setData(bufferSendToServeur);
					client.send(paquetSendToServeur);
				}
				
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
				
				if (! this.entitiesIsLocked)
				{
					this.entities.clear();
					this.entities.addAll((ArrayList <Entity>) objectStreamGetFromServeur.readObject());
				}
				
				objectStreamGetFromServeur.close();
				paquetGetFromServeur.setLength(bufferGetFromServeur.length);
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