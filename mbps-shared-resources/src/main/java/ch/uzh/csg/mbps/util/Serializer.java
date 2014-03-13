package ch.uzh.csg.mbps.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

	public static <T> byte[] serialize(T signedObject) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(signedObject);
		return baos.toByteArray();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] bytes) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (T) ois.readObject();
	}
}
