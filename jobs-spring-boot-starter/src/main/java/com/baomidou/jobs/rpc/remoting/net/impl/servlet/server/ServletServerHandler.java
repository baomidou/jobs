package com.baomidou.jobs.rpc.remoting.net.impl.servlet.server;

import com.baomidou.jobs.exception.JobsRpcException;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcResponse;
import com.baomidou.jobs.rpc.remoting.provider.JobsRpcProviderFactory;
import com.baomidou.jobs.service.JobsHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * servlet
 *
 * @author xuxueli 2015-11-24 22:25:15
 */
@Slf4j
public class ServletServerHandler {
    private JobsRpcProviderFactory jobsRpcProviderFactory;

    public ServletServerHandler(JobsRpcProviderFactory xxlRpcProviderFactory) {
        this.jobsRpcProviderFactory = xxlRpcProviderFactory;
    }

    /**
     * handle servlet request
     */
    public void handle(String target, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if ("/services".equals(target)) {
            // services mapping

            StringBuffer stringBuffer = new StringBuffer("<ui>");
            for (String serviceKey : jobsRpcProviderFactory.getServiceData().keySet()) {
                stringBuffer.append("<li>").append(serviceKey).append(": ").append(jobsRpcProviderFactory.getServiceData().get(serviceKey)).append("</li>");
            }
            stringBuffer.append("</ui>");

            writeResponse(response, stringBuffer.toString().getBytes());
            return;
        } else {    // default remoting mapping

            // request parse
            JobsRpcRequest xxlRpcRequest = null;
            try {

                xxlRpcRequest = parseRequest(request);
            } catch (Exception e) {
                writeResponse(response, JobsHelper.getErrorInfo(e).getBytes());
                return;
            }

            // invoke
            JobsRpcResponse xxlRpcResponse = jobsRpcProviderFactory.invokeService(xxlRpcRequest);

            // response-serialize + response-write
            byte[] responseBytes = jobsRpcProviderFactory.getSerializer().serialize(xxlRpcResponse);
            writeResponse(response, responseBytes);
        }

    }

    /**
     * write response
     */
    private void writeResponse(HttpServletResponse response, byte[] responseBytes) throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        OutputStream out = response.getOutputStream();
        out.write(responseBytes);
        out.flush();
    }

    /**
     * parse request
     */
    private JobsRpcRequest parseRequest(HttpServletRequest request) throws Exception {
        // deserialize request
        byte[] requestBytes = readBytes(request);
        if (requestBytes == null || requestBytes.length == 0) {
            throw new JobsRpcException("Jobs rpc request data is empty.");
        }
        JobsRpcRequest rpcXxlRpcRequest = (JobsRpcRequest) jobsRpcProviderFactory.getSerializer().deserialize(requestBytes, JobsRpcRequest.class);
        return rpcXxlRpcRequest;
    }

    /**
     * read bytes from http request
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static final byte[] readBytes(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int contentLen = request.getContentLength();
        InputStream is = request.getInputStream();
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return new byte[]{};
    }
}
