package com.smart.smartparking.common;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baidu.aip.ocr.AipOcr;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;


/**
 * 百度智能AI
 */
@Slf4j
@Component
@Configuration
@EnableConfigurationProperties({BaiDuProperties.class})
public class BaiDuUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaiDuUtils.class);

    private BaiDuProperties baiDu;

    public BaiDuUtils(BaiDuProperties baiDu) {
        this.baiDu = baiDu;
    }

    private AipOcr client;

    @PostConstruct
    public void init() {
        try {
            client = new AipOcr("31714713", "OwknN4y0b2l2Hqdx5HGE4VmT", "DURsb6aRG9T1RO0VqkNGD5iuLyVx1x0r");
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);
        } catch (Exception e) {
            LOGGER.error("百度智能AI初始化失败,{}", e.getMessage());
        }
    }

    /**
     * 参数为本地图片路径
     */
    public String plateLicense(String image) {
        try {
            HashMap<String, String> options = new HashMap<>();
            /**
             * 是否检测多张车牌，默认为false
             * 当置为true的时候可以对一张图片内的多张车牌进行识别
             */
            options.put("detect_direction", "true");//是否检测图片朝向，并自动进行校正
            options.put("detect_language", "true");//检测识别出来的文字所属的语言类型的

            SslUtils.ignoreSsl();
            JSONObject res = client.basicAccurateGeneral(image, options);
            Object result = res.get("words_result");
            JSONArray array = JSON.parseArray(result.toString());

            com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
            String number = "";
            for (int i=0;i<array.size();i++){
                object = JSON.parseObject(array.get(i).toString());
                number += object.get("words");
            }
            return number.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

    }

    public static void main(String[] args)  {
        try {
            AipOcr client = new AipOcr("31714713", "OwknN4y0b2l2Hqdx5HGE4VmT", "DURsb6aRG9T1RO0VqkNGD5iuLyVx1x0r");
          //  AipOcr  client = new AipOcr("31047334", "D3sAulGIClgEujiKZfbBHWUl", "GmL3dyh2BPa8wD8kYxaomYkufmzkCCqo");

            //设置网络连接参数
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);


            HashMap<String, String> options = new HashMap<>();
            String image = "C:\\Users\\Administrator\\Desktop\\暂存\\车牌4.jpg";
            /**
             * 是否检测多张车牌，默认为false
             * 当置为true的时候可以对一张图片内的多张车牌进行识别
             */

            // 传入可选参数调用接口
            options.put("detect_direction", "true");
            options.put("detect_language", "true");
            SslUtils.ignoreSsl();
            log.info(image);
            // 参数为本地图片路径
            JSONObject res = client.basicAccurateGeneral(image,options);
            log.info(res.toString());

            Object result = res.get("words_result");

            JSONArray array = JSON.parseArray(result.toString());
            com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();

            String number = "";

            for (int i=0;i<array.size();i++){
                object = JSON.parseObject(array.get(i).toString());
                number += object.get("words");
            }

            System.out.println("车牌号:"+number);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
