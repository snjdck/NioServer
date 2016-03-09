package snjdck.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;
import flex.messaging.io.amf.Amf3Output;

final public class Amf
{
	static private final SerializationContext context = new SerializationContext();
	static private final Amf3Output output = new Amf3Output(context);
	static private final Amf3Input  input  = new Amf3Input (context);
	
	static public byte[] Encode(Object object)
	{
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
	
	static public Object Decode(byte[] buffer)
	{
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