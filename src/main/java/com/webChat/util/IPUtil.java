package com.webChat.util;

import com.webChat.result.CodeEnum;
import eu.bitwalker.useragentutils.UserAgent;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author liuhua
 */
public class IPUtil {

    private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);
    private final static String localIp = "127.0.0.1";

    private static Searcher searcher = null;

    /**
     * 在服务启动时加载 ip2region.db 到内存中
     *      解决打包jar后找不到 ip2region.db 的问题
     */
    static {
        try {
            InputStream ris = IPUtil.class.getResourceAsStream("ip2region.xdb");
            byte[] cBuff = FileCopyUtils.copyToByteArray(ris);
            searcher = Searcher.newWithBuffer(cBuff);
            //注意：不能使用文件类型，打成jar包后，会找不到文件
            logger.debug("缓存成功！！！！");
        } catch (IOException e) {
            logger.error("解析ip地址失败,无法创建搜索器: {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取用户ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        try {
            // 以下两个获取在k8s中，将真实的客户端IP，放到了x-Original-Forwarded-For。而将WAF的回源地址放到了 x-Forwarded-For了。
            ipAddress = request.getHeader("X-Original-Forwarded-For");
            if (ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("X-Forwarded-For");
            }
            //获取nginx等代理的ip
            if (ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("x-forwarded-for");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            // 2.如果没有转发的ip，则取当前通信的请求端的ip(兼容k8s集群获取ip)
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                // 如果是127.0.0.1，则取本地真实ip
                if (localIp.equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                        ipAddress = inet.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            logger.error("解析请求IP失败", e);
            ipAddress = "";
        }
        return "0:0:0:0:0:0:0:1".equals(ipAddress) ? localIp : ipAddress;
    }


    /**
     * 获取访问设备os
     *
     * @param request
     * @return
     */
    public static String getUserAgentOs(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        return userAgent.getOperatingSystem().getName();
    }

    /**
     * 获取访问设备浏览器信息
     *
     * @param request
     * @return
     */
    public static String getUserAgentBrowser(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        return userAgent.getBrowser().getName();
    }

    /**
     * 获取城市信息
     *
     * @param ipAddress
     * @return
     */
    public static String getCityInfo(String ipAddress) {
        String cityInfo = "";
        try {
            return searcher.search(ipAddress);
        } catch (Exception e) {
            logger.error("搜索:{} 失败: {}", ipAddress, e);
        }
        return "";
    }

    /**
     * 根据ip2region解析ip地址
     *
     * @param ip
     * @return
     */
    public static String getIp2region(String ip) {

        if (searcher == null) {
            logger.error("Error: DbSearcher is null");
            return "";
        }
        try {
            String ipInfo = searcher.search(ip);
            if (!StringUtils.isEmpty(ipInfo)) {
                ipInfo = ipInfo.replace("|0", "");
                ipInfo = ipInfo.replace("0|", "");
            }
            return ipInfo;
        } catch (Exception e) {
            logger.error(CodeEnum.IP_IP2REGION_FAIL.getMsg(), e);
        }
        return "";
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        }
        return localIp;
    }

    /**
     * 获取主机名
     *
     * @return
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        return "未知";
    }

    /**
     * 获取登录地址 ip2region解析ip地址 + 城市信息
     *
     * @return
     */
    public static String getLoginLocation(String ipAddress, String ip) {
        String cityInfo = getCityInfo(ipAddress);
        String ip2region = getIp2region(ip);
        String loginLocation = (ip2region != ""? ip2region: "") + (ip2region != ""? ",": "") + (cityInfo != ""? cityInfo: "");
        return loginLocation;
    }
}


