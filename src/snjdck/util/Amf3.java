package snjdck.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;
import flex.messaging.io.amf.Amf3Output;

final public class Amf3
{
	private final SerializationContext context;
	
	public Amf3()
	{
		context = new SerializationContext();
	}
	
	public byte[] encode(Object object)
	{
		Amf3Output output = new Amf3Output(context);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		output.setOutputStream(stream);
		
		try{
			output.writeObject(object);
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
		return stream.toByteArray();
	}
	
	public Object decode(byte[] buffer)
	{
		Amf3Input input = new Amf3Input(context);
		
		ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
		input.setInputStream(stream);
		
		try{
			return input.readObject();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
		}
		
		return null;
	}
}