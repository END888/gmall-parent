package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2021000121625132";

	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCAsW6trEW4slkejHYcK3oW3PxLdpDFhDVaT75JmqHFbWiPbuUW5R+GL2d9vjIjfpq8maRwEZuybix9OCN0vRvWBaV3oDS7qtW82bVp9fdT9OwKKs0LGARFdpXNca5NbW7FFbXhoBegbBT+G6leppHLsSGx29EUb2kl2u/rcPzDAmj1BXZnWs3jtYEpuxeZ8NTJliN28ZRKg062bTrZmZLOAUrHd91BlnjEgTUf1KK4iKq5vt+sKd3sCbT79U2XmGxeC404Q0wBL5vrXllgOrEtmjJ794jHbAWYqhVJ7dKYDDthdfaq4qCmQr7m0sXyVtkPCUf1biwiK2mnLRx2wyedAgMBAAECggEALalG4N4v+sFk937U5VfTwMr1xXyzDzHCkGGc3Za88UTJhaLOK9pt3flx2d6jAY8JLgPBoXXN7XhD1sYVZnU2rDBirlgsfa22moQQZyE7eRF8snrPN7F0yoSx0CwWsSgz1j/GweBAoc3XgPWlmYy2TDR0BRw+Sebhwum0mLAFLtdqMCxTF5aHn6YWwYe3i91qcd5on++gQT81JYDZUxGw8hSvciE+LcGwzUcyubeB1Y03cfQjgsZ4RntzCfO/X8OaqaUWE9GvX/XtBXhjdKFxjl9bqOg1Nbybio54nmWuXCHJa2kAz3ghnlAYL2ASPQPMhQefn9xkM08ADd3aStT9gQKBgQC7ppXEJwqpZT7wjjZ+xb2OR7YZYK+5SfHDsPr5hJAppgfY1Je3RiGDYjYyI11TMPRsJg5I50smpbTKWL19TD2DhosRJcw62cu46uzeh1BeMDjE9LT8jhDll2TlJ1w2RuFzrJ4+E4ie7YiPQqZFYEF6SaDQiqEsnZmUUcPkas6eIQKBgQCvkV4Ocb/GXLvUxTz1Wx69VsOkEhfMN9SNck5/sJ+LfFqeYmof+2+wnrkmI9JlU9Law7HwHyhg+R2DCQuyFxJxAKKxluvGWW9viuGREzBXauQvcZ33y/3DSFXarpqsiS5UfditK1nGSc+500TZHiTPduvEf8ufC99E3BEylBrB/QKBgB5s2iedtOrWUagrRsazRRLGqc3fkV5XkO2LyoWeasl19BvCDH9YzaJB0Eu81Cri6x6F5RcI+XRgtbmBPegM7oRe0jmLiVuZaKCo5pMGGY/L/chhbN7uIes36tIE2byrLW7V9CpYfHRYSyX/RnpFKxiqy3pEn/Fd9ELKJxrn/mxBAoGAdWtco35yC0GEIfZUIahyc6EusXGkVuHUF1GmrIG9ArYFiT46ye8IsPBG1fpJQZmBQOx2+ihsgga6NSP8nXwOymLpbKqKP5sbC1ewKq3Ru/VybUSoqMzWiB+9w8bZDaGlyb5tBswie5psAlUJC8i8R8C6SC5MSa/QACbKnbAuASUCgYEAhpJNKPw3BKs7uh4E8Fq3LWtT/DShUxG3kx9onREPwHisSwsuT6yKdcFXBpr+CeqRzBxuUTRy+qx5HWsqnMejdmLEskw9MKr2ZWRuiSDRGiFhwg1cIcsjAvjSFA3etwH64CSNkfdoEQpliFtUjSrWO/hF+VwDGHDr7/QNw1dyNQE=";

	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。

    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6WuZ0wMVkS5rabOB71OhU6f86ZVwxhdymtG46A0Fp/lplcjzIrLNM6Seh1WyaFtSqFxhs81WbyCjfG3xaC+MlezbN2VY45lI/i9Nv3IIf6NZ5EV/sE23lCIJz4RfEBtyRoMeTwhdadpb3tP1dC0V3x3edGLcR42+zdBWRwzhOM5sEWFyNHAMtTdQ6emaZd2RXf0uEx3v68udkcZDwxYRfN1ifAyxcwDp6FpCFK8p3LM7M2Wye4UTyuQ8y6yA0pEnDvg89WueEbDrLm3IEGJ/3AL63T3cPpSwKvhkFW8iTTBc/wsG80+UKb5hZbhfwI6FboUlanKGr9IcBSNP4/3EFwIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://localhost:8080/alipay_demo_war_exploded/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";

	// 字符编码格式
	public static String charset = "utf-8";

	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

