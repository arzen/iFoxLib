/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.arzen.iFoxLib.pay.ali;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	// 合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088111960071433";

	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "1123499344@qq.com";

	// 商户私钥，自助生成
	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKVaFevBU6oT6zvq5VzkNTCO2ktuWeAKnIcebSZXxMu91lsvUubvD0n73dlNZrZbCaHBh0qyxhPiYXxUJAjtmL6sJuR97Fny5ATCMh9gQsthCEHndb+bd0IUiyk0/KUbfo3lZdL8YYaQleIVhUdNj7ULsBKXE6DEyBRUxtGrWYx3AgMBAAECgYA+8xrhVAp793VYrEaBpMezIs9sZCiHKMMKlcTMEiHzxuPydwPCs0RI0EBXWCZASkxSbpEJGSJbs5WVdZr6YyoA4EFfUJ7AouOv+dNQywlzdVdME5SeYfnNZkgveCJUfS+axkjuCWZBCbf2fEqpJFvrYvcyprUxfAM8lp2xjijS8QJBANh/gDAqwPKi8oP6uEacQGzqDUUwUn2/g/1Y57Yt30CXS+z3TbXxRK2emF/5X92nsFQNV/07EpInhJ51Sqt1kSUCQQDDhZPZ34ulPXNfxo0Z/zWJcR3YtNXZcT0YL4Ix5Zs84+8GiF+yn1lCpTOgO+C3Xsa8FS2+2NH8pkKTOX7VbbprAkEA14RVP53P5bqAYRG9T81L9SApha7M75Eu+4z9XUXc8JesZYDqQyyiiZNPbHr7C9Ram9GMvfGIUyYzxupMItMuYQJAQr2/KXkQbxhZ4NCi68PdH4x4Vrfr46yu4SmedK/e3NslhYJZwFx6mLBRvhmSbKWyixNjqSjUDT0nrp/Ktsoc/wJBAIgsIZNfus+EeAifs/DEcZ8hrsU+r0KuXfF/1yWkxFCBwwTU32sJUsk5ibSZEGODy5jDuFBjcwiQjnhEXLbA6go=";
	
	//公钥
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
