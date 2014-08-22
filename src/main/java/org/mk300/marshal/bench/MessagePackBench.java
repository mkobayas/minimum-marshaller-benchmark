package org.mk300.marshal.bench;

import java.util.concurrent.TimeUnit;

import org.msgpack.MessagePack;
import org.msgpack.packer.BufferPacker;
import org.msgpack.unpacker.BufferUnpacker;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import data.media.MediaContent;

@Warmup(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class MessagePackBench {

	private static MessagePack msgpack;
	private static final byte[] binary;
	
	static{
		msgpack = new MessagePack();

		try {
			BufferPacker packer = msgpack.createBufferPacker();
			packer.write(Util.getObject());
			binary = packer.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("", e);
		}
	}

	private static byte[] getBinary() {
		byte[] clone = new byte[binary.length];
		System.arraycopy(binary, 0, clone, 0, binary.length);
		return clone;
	}

	@Benchmark
	public void t1_marshalling() throws Exception {
		BufferPacker packer = msgpack.createBufferPacker(binary.length+1);
		packer.write(Util.getObject());
		packer.toByteArray();
	}

	@Benchmark
	public void t2_unmarshalling() throws Exception {
		BufferUnpacker unpacker = msgpack.createBufferUnpacker();
		unpacker.wrap(getBinary());
		unpacker.read(MediaContent.class);
	}

	@Benchmark
	public void t3_mix() throws Exception {
		Object o = Util.getObject();

		BufferPacker packer = msgpack.createBufferPacker(binary.length+1);
		BufferUnpacker unpacker = msgpack.createBufferUnpacker();

		packer.write(o);
		byte[] b = packer.toByteArray();

		unpacker.wrap(b);
		unpacker.read(MediaContent.class);
	}
}
