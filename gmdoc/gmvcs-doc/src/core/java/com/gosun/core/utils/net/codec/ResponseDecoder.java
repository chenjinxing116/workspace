package com.gosun.core.utils.net.codec;

import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gosun.core.utils.StringUtils;
import com.gosun.core.utils.net.Constants;
import com.gosun.core.utils.net.dto.BaseReceivedMessage;

/***
 * @ClassName: ResponseDecoder
 * @Description: 网络协议解码工具类
 * @author houyx
 * @date 2012-05-16
 * 
 */
public class ResponseDecoder extends CumulativeProtocolDecoder {
	
	private static final Logger log = LoggerFactory.getLogger(ResponseDecoder.class);
	private static int contentTypeLength = 0;
	private boolean isRequestMessage = true;
	
	/**
	 * 解码码工具类重写doDecode方法来解码协议数据
	 * @return boolean
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
	    in.mark();
        in.order(ByteOrder.LITTLE_ENDIAN);
        int contentLength;
	    
		if(!this.isValidHead(in)) return false;
		
		contentLength = this.getContentLength(in);
		if(this.checkPackageAllReceived(in,contentLength)) return false;
		byte[] data = new byte[contentLength];
		try {
			in.get(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BaseReceivedMessage messgae = new BaseReceivedMessage();
		messgae.setRequestMessage(this.isRequestMessage);
		messgae.setData(data);
		out.write(messgae);
		return true;
	}
	
	/**
	 * 解码码工具类：判断协议头是否接受完全和包头有问题的时候自动寻找到下一个包的开始地址
	 * @param in
	 * @return boolean
	 * @throws CharacterCodingException 
	 */
	private boolean isValidHead(IoBuffer in){
		try {
		  String inStr = in.getString(Constants.decoder);
		  if(StringUtils.isBlank(inStr)||inStr.indexOf("GSSmp")<0){
				return false;
			}else{
				boolean matched = false;
				String headArray[] = inStr.split("\r\n");
				if(headArray.length<3)
				{
					in.skip(in.remaining());
					return false;
				}
				if((headArray[0]).equalsIgnoreCase(Constants.gsCommonReponseHead))
				{
					contentTypeLength = contentTypeLength(headArray[1]);
					if(contentTypeLength == 0)
					{
						in.skip(in.remaining());
						return false;
					}
					in.position(61+contentTypeLength);
					isRequestMessage = false;
					matched = true;
				}
				if(!matched){
					if((headArray[0]).equalsIgnoreCase(Constants.gsCommonRequestHead))
					{
						contentTypeLength = contentTypeLength(headArray[1]);
						if(contentTypeLength == 0)
						{
							in.skip(in.remaining());
							return false;
						}
						in.position(60+contentTypeLength);
						isRequestMessage = true;
						matched = true;
					}
				}
				
				if(matched){
					while(in.remaining() >= 4){
						String str = in.getString(1,Constants.decoder);
						if(str.equals("\r")){
							if(in.getString(1,Constants.decoder).equals("\n")&&in.getString(1,Constants.decoder).equals("\r")&&in.getString(1,Constants.decoder).equals("\n")){
								return true;
							}else{
								in.position(in.position() - 1);
								continue;
							}
						}
			        }
				}
				
				in.skip(in.remaining());
		        return false;
			}
		} catch (CharacterCodingException e1) {
			in.skip(in.remaining());
	        return false;
		}
	}
	
	private int getContentLength(IoBuffer in){
		int length = 0;
		
		int position = in.position();
		int headBeforePackageSize = this.isRequestMessage ? (60+contentTypeLength) : (61+contentTypeLength);
		
		int contentPositon = position-4-headBeforePackageSize;//减去固定头部和尾部的\r\n
		in.position(headBeforePackageSize);
		try {
			String contentLength = in.getString(contentPositon, Constants.decoder);
			length = Integer.parseInt(contentLength);
			in.position(position);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}//内容长度
		
		return length;
	}
	
	/**
	 * 解码码工具类：判断协议是否接受完全
	 * @param in
	 * @param head
	 * @return boolean
	 */
	private boolean checkPackageAllReceived(IoBuffer in,int contentLength){
		if(in.remaining() < contentLength){
			in.reset();
			log.debug("数据包长度不完整，等待下次接收,数据长度为:{} ",contentLength);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取消息类型的长度
	 * @param gsCommonHead
	 * @return
	 */
	private int contentTypeLength(String gsCommonHead)
	{
		int i = 0;
		String type = gsCommonHead.substring(gsCommonHead.indexOf("/")+1,gsCommonHead.length());
		if(!StringUtils.isBlank(type))
		{
			if (type.equals(Constants.MESSAGE_TYPE_BINARY)
					|| type.equals(Constants.MESSAGE_TYPE_JSON)
					|| type.equals(Constants.MESSAGE_TYPE_XML))
			{
				i = type.length();
			}
		}
		return i;
	}
}
