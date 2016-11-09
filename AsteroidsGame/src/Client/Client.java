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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable
{
	public static final String SERVEUR_NAME = "127.0.0.1";
	public static final int SERVEUR_PORT = 2345;
	
	public String CLIENT_ADRESSE;
	
	private static final int BYTE_SIZE = 5000;
	
	private final Lock entitiesLock;
	private final Lock entityLock;
	
	private ArrayList <Entity> entities;
	private Object entity;
	
	public Client (Entity entity)
	{
		this.entities = new ArrayList <> ();
		this.entity = entity;
		
		this.entitiesLock = new ReentrantLock ();
		this.entityLock = new ReentrantLock ();
		
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
		this.entitiesLock.lock();
		
		return this.entities;
	}
	
	public void addEntitiesTerminated ()
	{
		this.entitiesLock.unlock();
		
		this.entities.clear();
	}
	
	public void update (Entity entity)
	{
		this.entityLock.lock();
		try
		{
			this.entity = entity;
		}
		finally
		{
			this.entityLock.unlock();
		}
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
				
				if (this.entityLock.tryLock())
				{
					try
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
					finally
					{
						this.entityLock.unlock();
					}
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
				
				if (this.entitiesLock.tryLock())
				{
					try
					{
						this.entities.clear();
						this.entities.addAll((ArrayList <Entity>) objectStreamGetFromServeur.readObject());
					}
					finally
					{
						this.entitiesLock.unlock();
					}
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