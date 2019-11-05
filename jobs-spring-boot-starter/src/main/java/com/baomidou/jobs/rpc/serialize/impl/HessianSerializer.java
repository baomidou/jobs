package com.baomidou.jobs.rpc.serialize.impl;

import com.baomidou.jobs.exception.JobsRpcException;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * hessian serializer
 *
 * @author jobob
 * @since 2019-11-01
 */
public class HessianSerializer implements IJobsRpcSerializer {

  @Override
  public <T> byte[] serialize(T obj) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
    try {
      hessian2Output.writeObject(obj);
      // 必须先关闭，才能转成二进制数组
      hessian2Output.close();
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      throw new JobsRpcException(e);
    } finally {
      try {
        byteArrayOutputStream.close();
      } catch (IOException e) {
        throw new JobsRpcException(e);
      }
    }
  }

  @Override
  public <T> T deserialize(byte[] data, Class<T> clazz) {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
    Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
    try {
      return (T) hessian2Input.readObject(clazz);
    } catch (IOException e) {
      throw new JobsRpcException(e);
    } finally {
      try {
        hessian2Input.close();
        byteArrayInputStream.close();
      } catch (IOException e) {
        throw new JobsRpcException(e);
      }
    }
  }
}
