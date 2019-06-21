package xxl.job.web.monitor;

import com.xxl.job.core.enums.RegistryConfig;
import lombok.extern.slf4j.Slf4j;
import xxl.job.web.entity.XxlJobGroup;
import xxl.job.web.entity.XxlJobRegistry;
import xxl.job.web.spring.XxlJobHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * job registry instance
 *
 * @author xuxueli 2016-10-02 19:10:24
 */
@Slf4j
public class XxlJobRegistryMonitor {
    private Thread registryThread;
    private volatile boolean toStop = false;

    private static XxlJobRegistryMonitor instance = new XxlJobRegistryMonitor();
    public static XxlJobRegistryMonitor getInstance(){
        return instance;
    }

    public void start() {
        registryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!toStop) {
                    try {
                        // auto registry group
                        List<XxlJobGroup> groupList = XxlJobHelper.getJobGroupService().listByAddressType(0);
                        if (groupList != null && !groupList.isEmpty()) {

                            // remove dead address (admin/executor)
                            XxlJobHelper.getJobRegistryService().removeTimeOut(RegistryConfig.DEAD_TIMEOUT);

                            // fresh online address (admin/executor)
                            HashMap<String, List<String>> appAddressMap = new HashMap<>(16);
                            List<XxlJobRegistry> list = XxlJobHelper.getJobRegistryService().listTimeout(RegistryConfig.DEAD_TIMEOUT);
                            if (list != null) {
                                for (XxlJobRegistry item : list) {
                                    if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
                                        String appName = item.getRegistryKey();
                                        List<String> registryList = appAddressMap.get(appName);
                                        if (registryList == null) {
                                            registryList = new ArrayList<String>();
                                        }

                                        if (!registryList.contains(item.getRegistryValue())) {
                                            registryList.add(item.getRegistryValue());
                                        }
                                        appAddressMap.put(appName, registryList);
                                    }
                                }
                            }

                            // fresh group address
                            for (XxlJobGroup group : groupList) {
                                List<String> registryList = appAddressMap.get(group.getAppName());
                                String addressListStr = null;
                                if (registryList != null && !registryList.isEmpty()) {
                                    Collections.sort(registryList);
                                    addressListStr = "";
                                    for (String item : registryList) {
                                        addressListStr += item + ",";
                                    }
                                    addressListStr = addressListStr.substring(0, addressListStr.length() - 1);
                                }
                                group.setAddressList(addressListStr);
                                XxlJobHelper.getJobGroupService().updateById(group);
                            }
                        }
                    } catch (Exception e) {
                        if (!toStop) {
                            log.error(">>>>>>>>>>> xxl-job, job registry monitor monitor error:{}", e);
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                    } catch (InterruptedException e) {
                        if (!toStop) {
                            log.error(">>>>>>>>>>> xxl-job, job registry monitor monitor error:{}", e);
                        }
                    }
                }
                log.info(">>>>>>>>>>> xxl-job, job registry monitor monitor stop");
            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("xxl-job, admin XxlJobRegistryMonitor");
        registryThread.start();
    }

    public void toStop() {
        toStop = true;
        // interrupt and wait
        registryThread.interrupt();
        try {
            registryThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
