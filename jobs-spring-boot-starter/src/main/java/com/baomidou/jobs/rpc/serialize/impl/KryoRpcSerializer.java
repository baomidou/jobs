package com.baomidou.jobs.rpc.serialize.impl;

import com.baomidou.jobs.exception.JobsRpcException;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * kryo serializer
 *
 * @author jobob
 * @since 2019-11-01
 */
public class KryoRpcSerializer implements IJobsRpcSerializer {

    private final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Output output = new Output(os);
        try {
            kryoLocal.get().writeObject(output, obj);
            output.flush();

            byte[] result = os.toByteArray();
            return result;
        } catch (Exception e) {
            throw new JobsRpcException(e);
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                throw new JobsRpcException(e);
            }
            try {
                os.close();
            } catch (IOException e) {
                throw new JobsRpcException(e);
            }
        }
    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        Input input = new Input(is);
        try {
            Object result = kryoLocal.get().readObject(input, clazz);
            return result;
        } catch (Exception e) {
            throw new JobsRpcException(e);
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                throw new JobsRpcException(e);
            }
            try {
                is.close();
            } catch (IOException e) {
                throw new JobsRpcException(e);
            }
        }
    }
}
