package com.baomidou.jobs.rpc.serialize.impl;

import com.baomidou.jobs.exception.JobsRpcException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.baomidou.jobs.rpc.serialize.Serializer;

import java.io.IOException;

/**
 * Jackson serializer
 *
 * 		1、obj need private and set/get；
 * 		2、do not support inner class；
 *
 * @author xuxueli 2015-9-25 18:02:56
 */
public class JacksonSerializer extends Serializer {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    
    /** bean、array、List、Map --> json 
     * @param <T>*/
    @Override
	public <T> byte[] serialize(T obj) {
		try {
			return objectMapper.writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			throw new JobsRpcException(e);
		}
	}

    /** string --> bean、Map、List(array) */
    @Override
	public <T> Object deserialize(byte[] bytes, Class<T> clazz)  {
		try {
			return objectMapper.readValue(bytes, clazz);
		} catch (JsonParseException e) {
			throw new JobsRpcException(e);
		} catch (JsonMappingException e) {
			throw new JobsRpcException(e);
		} catch (IOException e) {
			throw new JobsRpcException(e);
		}
	}

}
