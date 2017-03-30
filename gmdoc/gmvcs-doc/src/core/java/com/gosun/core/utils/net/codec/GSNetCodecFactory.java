package com.gosun.core.utils.net.codec;


import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/***
 * @ClassName: GSNetCodecFactory
 * @Description: 网络库协议解码工厂
 * @author houyx
 * @date 2015-10-16
 * 
 */
public class GSNetCodecFactory implements ProtocolCodecFactory {
	private ProtocolDecoder decoder;
	private ProtocolEncoder encoder;
	
	public GSNetCodecFactory(boolean server) {
		decoder = new ResponseDecoder();
		encoder = new RequestEncoder();
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

}
