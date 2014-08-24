package org.mk300.marshal.bench;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;

@Warmup(iterations=1, time=5, timeUnit=TimeUnit.SECONDS)
@Measurement(iterations=3, time=5, timeUnit=TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class KryoBench {

	private static final byte[] binary;
	static {
		Kryo kryo = getKryo();
		Output output = new Output(256, -1);
		kryo.writeClassAndObject(output, Util.getObject());
		binary = output.toBytes();
	}
	
	private static byte[] getBinary() {
		byte[] clone = new byte[binary.length];
		System.arraycopy(binary, 0, clone, 0, binary.length);
		return clone;
	}

	private static Kryo getKryo() {
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.register(ArrayList.class);
		kryo.register(MediaContent.class);
		kryo.register(Media.Player.class);
		kryo.register(Media.class);
		kryo.register(Image.Size.class);
		kryo.register(Image.class);
		return kryo;
	}
	
	@Benchmark
	public void t1_marshalling() throws Exception {
		Kryo kryo = getKryo();
		Output output = new Output(binary.length+1, -1);
		kryo.writeClassAndObject(output, Util.getObject());
		output.toBytes();
	}

	@Benchmark
	public void t2_unmarshalling() throws Exception {
		Kryo kryo = getKryo();
		Input input = new Input(getBinary());
		kryo.readClassAndObject(input);
	}

	@Benchmark
	public void t3_mix() throws Exception {
		Object o = Util.getObject();

		Kryo kryo = getKryo();
		
		Output output = new Output(binary.length+1, -1);
		
		kryo.writeClassAndObject(output, o);
		byte[] b = output.toBytes();

		Input input = new Input(b);
		kryo.readClassAndObject(input);
	}
}
