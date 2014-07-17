package org.mk300.marshal.bench;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.mk300.marshal.minimum.MinimumMarshaller;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

@Warmup(iterations=1, time=5, timeUnit=TimeUnit.SECONDS)
@Measurement(iterations=3, time=5, timeUnit=TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class MinimumMarshallerBench {

	private static final byte[] binary;
	static {
		try {
			binary = MinimumMarshaller.marshal(Util.getObject());
		} catch (IOException e) {
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
		MinimumMarshaller.marshal(Util.getObject(), 512);
	}

	@Benchmark
	public void t2_unmarshalling() throws Exception {
		MinimumMarshaller.unmarshal(getBinary());
	}
	
	@Benchmark
	public void t3_mix() throws Exception {
		Object o = Util.getObject();
		byte[] b = MinimumMarshaller.marshal(o);
		MinimumMarshaller.unmarshal(b);
	}
}
